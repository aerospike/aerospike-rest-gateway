/*
 * Copyright 2022 Aerospike, Inc.
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

import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.Expression;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Stack;

public class FilterExpParser extends BaseExpressionParser<Exp> implements ExpressionParser<Expression> {

    private final FilterExpFactory filterExpFactory = new FilterExpFactory();

    /**
     * Parses a logical expression string to an Aerospike Filter Expression.
     *
     * @param expression the string representation of a logical expression in the infix form
     *                   with a special filters syntax support.
     * @return the {@link Expression} for the specified logical expression.
     */
    @Override
    public Expression parse(String expression) {
        List<String> tokens = parseExpressionString(expression);
        Stack<NestedBlock> stack = new Stack<>();
        stack.push(new NestedBlock());

        for (int i = 0; i < tokens.size(); i++) {
            String t = tokens.get(i).trim();
            if (t.equals("(")) {
                stack.push(new NestedBlock());
            } else if (t.equals(")")) {
                NestedBlock block = stack.pop();
                Exp exp = block.filters.get(0);
                if (!block.logicOperators.isEmpty()) {
                    Preconditions.checkState(forAllEqual(block.logicOperators),
                            "Require equal logic operations on the same level");
                    exp = filterExpFactory.getLogicalExp(block.logicOperators.pop(), block.filters.toArray(new Exp[0]));
                }
                if (!stack.peek().unaryLogicOperators.isEmpty())
                    exp = filterExpFactory.getUnaryLogicalExp(stack.peek().unaryLogicOperators.pop(), exp);
                stack.peek().filters.push(exp);
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
                Exp special = filterExpFactory.getSpecialExp(t, buff.toString());
                if (!stack.peek().unaryLogicOperators.isEmpty())
                    special = filterExpFactory.getUnaryLogicalExp(stack.peek().unaryLogicOperators.pop(), special);
                stack.peek().filters.push(special);
            } else {
                // is operand
                if (stack.peek().simpleOperators.size() > 0) {
                    if (stack.peek().operands.size() == 1) {
                        Exp exp = filterExpFactory.getCompareExp(stack.peek().operands.pop(),
                                stack.peek().simpleOperators.pop(), t);
                        if (stack.peek().unaryLogicOperators.size() > 0)
                            exp = filterExpFactory.getUnaryLogicalExp(stack.peek().unaryLogicOperators.pop(), exp);
                        stack.peek().filters.push(exp);
                    }
                } else {
                    stack.peek().operands.push(t);
                }
            }
        }

        NestedBlock block = stack.pop();
        Exp exp = block.filters.get(0);
        if (!block.logicOperators.isEmpty()) {
            Preconditions.checkState(forAllEqual(block.logicOperators),
                    "Require equal logic operations on the same level");
            exp = filterExpFactory.getLogicalExp(block.logicOperators.peek(), block.filters.toArray(new Exp[0]));
        }

        return Exp.build(exp);
    }
}
