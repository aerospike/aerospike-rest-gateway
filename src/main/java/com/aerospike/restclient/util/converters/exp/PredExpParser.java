/*
 * Copyright 2020 Aerospike, Inc.
 *
 * Portions may be licensed to Aerospike, Inc. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.aerospike.restclient.util.converters.exp;

import com.aerospike.client.query.PredExp;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PredExpParser extends BaseExpressionParser<List<PredExp>>
        implements ExpressionParser<PredExp[]> {

    private final PredExpFactory predExpFactory = new PredExpFactory();

    /**
     * Parses a logical expression to an array of predicate expression filters.
     *
     * @param expression the string representation of a logical expression in the infix form
     *                   with a special filters syntax support.
     * @return the {@link PredExp} array for the specified logical expression.
     */
    @Override
    public PredExp[] parse(String expression) {
        List<String> tokens = parseExpressionString(expression);
        Stack<NestedBlock> stack = new Stack<>();
        stack.push(new NestedBlock());

        for (int i = 0; i < tokens.size(); i++) {
            String t = tokens.get(i).trim();
            if (t.equals("(")) {
                stack.push(new NestedBlock());
            } else if (t.equals(")")) {
                NestedBlock block = stack.pop();
                List<PredExp> allBlockPreds = readAllFilters(block);
                Preconditions.checkState(forAllEqual(block.logicOperators),
                        "Require equal logic operations on the same level");
                if (!block.logicOperators.isEmpty())
                    allBlockPreds.add(predExpFactory.getLogicalExp(block.logicOperators.pop(), block.filters.size()));
                if (!stack.peek().unaryLogicOperators.isEmpty())
                    allBlockPreds.add(predExpFactory.getUnaryLogicalExp(stack.peek().unaryLogicOperators.pop()));
                stack.peek().filters.push(allBlockPreds);
            } else if (Operator.isSimple(t)) {
                stack.peek().simpleOperators.push(t);
            } else if (Operator.isLogic(t)) {
                stack.peek().logicOperators.push(t);
            } else if (Operator.isLogicUnary(t)) {
                stack.peek().unaryLogicOperators.push(t);
            } else if (Operator.isSpecial(t)) {
                // read special operation
                StringBuilder buff = new StringBuilder();
                do {
                    buff.append(tokens.get(++i));
                } while (!tokens.get(i).equals(")"));
                List<PredExp> special = predExpFactory.getSpecialExp(t, buff.toString());
                if (!stack.peek().unaryLogicOperators.isEmpty())
                    special.add(predExpFactory.getUnaryLogicalExp(stack.peek().unaryLogicOperators.pop()));
                stack.peek().filters.push(special);
            } else {
                // is operand
                if (stack.peek().simpleOperators.size() > 0) {
                    if (stack.peek().operands.size() == 1) {
                        List<PredExp> predList = predExpFactory.getCompareExp(
                                stack.peek().operands.pop(),
                                stack.peek().simpleOperators.pop(),
                                t
                        );
                        if (stack.peek().unaryLogicOperators.size() > 0)
                            predList.add(predExpFactory.getUnaryLogicalExp(stack.peek().unaryLogicOperators.pop()));
                        stack.peek().filters.push(predList);
                    }
                } else {
                    stack.peek().operands.push(t);
                }
            }
        }

        NestedBlock block = stack.pop();
        List<PredExp> predExpList = readAllFilters(block);
        Preconditions.checkState(forAllEqual(block.logicOperators),
                "Require equal logic operations on the same level");
        if (!block.logicOperators.isEmpty())
            predExpList.add(predExpFactory.getLogicalExp(block.logicOperators.pop(), block.filters.size()));

        return predExpList.toArray(new PredExp[0]);
    }

    private List<PredExp> readAllFilters(NestedBlock block) {
        List<PredExp> list = new ArrayList<>();
        for (List<PredExp> f : block.filters) {
            list.addAll(f);
        }
        return list;
    }
}
