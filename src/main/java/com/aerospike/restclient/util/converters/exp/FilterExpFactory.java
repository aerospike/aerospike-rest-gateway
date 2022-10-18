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

import com.aerospike.client.cdt.ListReturnType;
import com.aerospike.client.cdt.MapReturnType;
import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.ListExp;
import com.aerospike.client.exp.MapExp;
import com.aerospike.client.query.RegexFlag;
import com.google.common.base.Preconditions;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

public class FilterExpFactory extends BaseExpFactory {

    public Exp getLogicalExp(String name, Exp[] exps) {
        final Operator.Logic op = Operator.Logic.fromString(name);
        Preconditions.checkNotNull(op, "getLogicalExp operator fromString");
        switch (op) {
            case AND:
                return Exp.and(exps);
            case OR:
                return Exp.or(exps);
            default:
                throw new InvalidParameterException(String.format("Invalid logical operator name: %s", name));
        }
    }

    public Exp getUnaryLogicalExp(String name, Exp exp) {
        final Operator.LogicUnary op = Operator.LogicUnary.fromString(name);
        Preconditions.checkNotNull(op, "getUnaryLogicalExp operator fromString");
        switch (op) {
            case NOT:
                return Exp.not(exp);
            default:
                throw new InvalidParameterException(String.format("Invalid unary logical operator name: %s", name));
        }
    }

    public Exp getCompareExp(String c1, String op, String c2) {
        Exp exp;
        Optional<Integer> intVal = parseInt(c2);
        if (intVal.isPresent()) {
            exp = getSimpleExp(op, Exp.bin(c1, Exp.Type.INT), Exp.val(intVal.get()));
        } else {
            exp = getSimpleExp(op, Exp.bin(c1, Exp.Type.STRING), Exp.val(stripQuotes(c2)));
        }
        return exp;
    }

    public Exp getSimpleExp(String op, Exp left, Exp right) {
        final Operator.Simple operator = Operator.Simple.fromString(op);
        Preconditions.checkNotNull(operator, "getSimpleExp operator fromString");
        Exp exp = null;
        switch (operator) {
            case EQUAL:
                exp = Exp.eq(left, right);
                break;
            case UNEQUAL:
                exp = Exp.ne(left, right);
                break;
            case GREATER:
                exp = Exp.gt(left, right);
                break;
            case GREATEREQ:
                exp = Exp.ge(left, right);
                break;
            case LESS:
                exp = Exp.lt(left, right);
                break;
            case LESSEQ:
                exp = Exp.le(left, right);
                break;
        }
        return exp;
    }

    public Exp getListExp(String op, Exp left, Exp right) {
        final Operator.Simple operator = Operator.Simple.fromString(op);
        Preconditions.checkNotNull(operator, "getListExp operator fromString");
        Exp exp;
        switch (operator) {
            case EQUAL:
            case UNEQUAL:
                exp = ListExp.getByValue(ListReturnType.COUNT, left, right);
                break;
            case GREATEREQ:
                exp = ListExp.getByValueRange(ListReturnType.COUNT, left, null, right);
                break;
            case LESS:
                exp = ListExp.getByValueRange(ListReturnType.COUNT, null, left, right);
                break;
            default:
                throw new InvalidParameterException(String.format("Unsupported getListExp logical operator: %s", op));
        }
        return exp;
    }

    public Exp getMapExpKey(String op, Exp left, Exp right) {
        final Operator.Simple operator = Operator.Simple.fromString(op);
        Preconditions.checkNotNull(operator, "getMapExpKey operator fromString");
        Exp exp;
        switch (operator) {
            case EQUAL:
            case UNEQUAL:
                exp = MapExp.getByKey(MapReturnType.COUNT, Exp.Type.INT, left, right);
                break;
            case GREATEREQ:
                exp = MapExp.getByKeyRange(MapReturnType.COUNT, left, null, right);
                break;
            case LESS:
                exp = MapExp.getByKeyRange(MapReturnType.COUNT, null, left, right);
                break;
            default:
                throw new InvalidParameterException(String.format("Unsupported getMapExpKey logical operator: %s", op));
        }
        return exp;
    }

    public Exp getMapExpValue(String op, Exp left, Exp right) {
        final Operator.Simple operator = Operator.Simple.fromString(op);
        Preconditions.checkNotNull(operator, "getMapExpValue operator fromString");
        Exp exp;
        switch (operator) {
            case EQUAL:
            case UNEQUAL:
                exp = MapExp.getByValue(MapReturnType.COUNT, left, right);
                break;
            case GREATEREQ:
                exp = MapExp.getByValueRange(MapReturnType.COUNT, left, null, right);
                break;
            case LESS:
                exp = MapExp.getByValueRange(MapReturnType.COUNT, null, left, right);
                break;
            default:
                throw new InvalidParameterException(
                        String.format("Unsupported getMapExpValue logical operator: %s", op));
        }
        return exp;
    }

    public Exp getSpecialExp(String name, String params) {
        final Operator.Special op = Operator.Special.valueOf(name);
        switch (op) {
            case LAST_UPDATE:
                return getLastUpdateExpr(params);
            case VOID_TIME:
                return getVoidTimeExpr(params);
            case DIGEST_MODULO:
                return getDigestModuloExpr(params);
            case STRING_REGEX:
                return getStringRegexExpr(params);
            case LIST_ITERATE_OR:
                return getListIterateOrExpr(params);
            case LIST_ITERATE_AND:
                return getListIterateAndExpr(params);
            case MAPKEY_ITERATE_OR:
                return getMapKeyIterateOrExpr(params);
            case MAPVAL_ITERATE_OR:
                return getMapValIterateOrExpr(params);
            case MAPKEY_ITERATE_AND:
                return getMapKeyIterateAndExpr(params);
            case MAPVAL_ITERATE_AND:
                return getMapValIterateAndExpr(params);
            default:
                throw new InvalidParameterException(String.format("Invalid Special Expression name: %s", name));
        }
    }

    /**
     * Create record last update time predicate expressed in seconds since 1970-01-01 epoch as 64 bit integer.
     * Example:
     * <pre>
     * LAST_UPDATE(>=, 1577880000)
     */
    private Exp getLastUpdateExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 2, "getLastUpdateExpr invalid format");
        return getSimpleExp(p.get(0), Exp.lastUpdate(), Exp.val(Long.parseLong(p.get(1)) * 1000000000));
    }

    /**
     * Create record expiration time predicate expressed in seconds since 1970-01-01 epoch as 64 bit integer.
     * Example:
     * <pre>
     * VOID_TIME(>=, 1577880000)
     */
    private Exp getVoidTimeExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 2, "getVoidTimeExpr invalid format");
        return getSimpleExp(p.get(0), Exp.voidTime(), Exp.val(Long.parseLong(p.get(1)) * 1000000000));
    }

    /**
     * Create a digest modulo record metadata value predicate expression.
     * The digest modulo expression assumes the value of 4 bytes of the
     * record's key digest modulo it's argument.
     * <p>
     * Example, the following instruction
     * DIGEST_MODULO(3, ==, 1)
     * selects records that have digest(key) % 3 == 1)
     */
    private Exp getDigestModuloExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getDigestModuloExpr invalid format");
        return getSimpleExp(p.get(1), Exp.digestModulo(Integer.parseInt(p.get(0))),
                Exp.val(Integer.parseInt(p.get(2))));
    }

    /**
     * Create regular expression string operation predicate.
     * Example:
     * <pre>
     * STRING_REGEX(str, [0-9]*)
     */
    private Exp getStringRegexExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 2, "getStringRegexExpr invalid format");
        return Exp.regexCompare(stripQuotes(p.get(1)), RegexFlag.NONE, Exp.stringBin(p.get(0)));
    }

    /**
     * Create list predicate where expression matches for any list item.
     * Example:
     * <pre>
     * Find records where any list item v = "hello" in list bin x.
     * LIST_ITERATE_OR(x, ==, hello)
     */
    private Exp getListIterateOrExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getListIterateOrExpr invalid format");
        Exp exp;
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            exp = getListExp(p.get(1), Exp.val(ival.get()), Exp.listBin(p.get(0)));
        } else {
            exp = getListExp(p.get(1), Exp.val(p.get(2)), Exp.listBin(p.get(0)));
        }
        if (Operator.Simple.fromString(p.get(1)) == Operator.Simple.UNEQUAL) {
            return Exp.lt(exp, ListExp.size(Exp.listBin(p.get(0))));
        }
        return Exp.gt(exp, Exp.val(0));
    }

    /**
     * Create list predicate where expression matches for all list items.
     * Example:
     * <pre>
     * Find records where all list elements v != "goodbye" in list bin x.
     * LIST_ITERATE_AND(x, !=, goodbye)
     */
    private Exp getListIterateAndExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getListIterateAndExpr invalid format");
        Exp exp;
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            exp = getListExp(p.get(1), Exp.val(ival.get()), Exp.listBin(p.get(0)));
        } else {
            exp = getListExp(p.get(1), Exp.val(p.get(2)), Exp.listBin(p.get(0)));
        }
        if (Operator.Simple.fromString(p.get(1)) == Operator.Simple.UNEQUAL) {
            return Exp.eq(exp, Exp.val(0));
        }
        return Exp.eq(exp, ListExp.size(Exp.listBin(p.get(0))));
    }

    /**
     * Create map predicate where expression matches for any map key.
     * Example:
     * <pre>
     * Find records where any map key k = 7 in map bin m.
     * MAPKEY_ITERATE_OR(m, ==, 7)
     */
    private Exp getMapKeyIterateOrExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getMapKeyIterateOrExpr invalid format");
        Exp exp;
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            exp = getMapExpKey(p.get(1), Exp.val(ival.get()), Exp.mapBin(p.get(0)));
        } else {
            exp = getMapExpKey(p.get(1), Exp.val(p.get(2)), Exp.mapBin(p.get(0)));
        }
        if (Operator.Simple.fromString(p.get(1)) == Operator.Simple.UNEQUAL) {
            return Exp.lt(exp, MapExp.size(Exp.mapBin(p.get(0))));
        }
        return Exp.gt(exp, Exp.val(0));
    }

    /**
     * Create map predicate where expression matches for any map value.
     * <pre>
     * Find records where any map value v >= 100 in map bin m.
     * MAPVAL_ITERATE_OR(m, >=, 100)
     */
    private Exp getMapValIterateOrExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getMapValIterateOrExpr invalid format");
        Exp exp;
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            exp = getMapExpValue(p.get(1), Exp.val(ival.get()), Exp.mapBin(p.get(0)));
        } else {
            exp = getMapExpValue(p.get(1), Exp.val(p.get(2)), Exp.mapBin(p.get(0)));
        }
        if (Operator.Simple.fromString(p.get(1)) == Operator.Simple.UNEQUAL) {
            return Exp.lt(exp, MapExp.size(Exp.mapBin(p.get(0))));
        }
        return Exp.gt(exp, Exp.val(0));
    }

    /**
     * Create map key predicate where expression matches for all map keys.
     * Example:
     * <pre>
     * Find records where all map keys k < 5 in map bin m.
     * MAPKEY_ITERATE_AND(m, <, 5)
     */
    private Exp getMapKeyIterateAndExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getMapKeyIterateAndExpr invalid format");
        Exp exp;
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            exp = getMapExpKey(p.get(1), Exp.val(ival.get()), Exp.mapBin(p.get(0)));
        } else {
            exp = getMapExpKey(p.get(1), Exp.val(p.get(2)), Exp.mapBin(p.get(0)));
        }
        if (Operator.Simple.fromString(p.get(1)) == Operator.Simple.UNEQUAL) {
            return Exp.eq(exp, Exp.val(0));
        }
        return Exp.eq(exp, MapExp.size(Exp.mapBin(p.get(0))));
    }

    /**
     * Create map predicate where expression matches for all map values.
     * Example:
     * <pre>
     * Find records where all map values v >= 500 in map bin m.
     * MAPVAL_ITERATE_AND(m, >=, 500)
     */
    private Exp getMapValIterateAndExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getMapValIterateAndExpr invalid format");
        Exp exp;
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            exp = getMapExpValue(p.get(1), Exp.val(ival.get()), Exp.mapBin(p.get(0)));
        } else {
            exp = getMapExpValue(p.get(1), Exp.val(p.get(2)), Exp.mapBin(p.get(0)));
        }
        if (Operator.Simple.fromString(p.get(1)) == Operator.Simple.UNEQUAL) {
            return Exp.eq(exp, Exp.val(0));
        }
        return Exp.eq(exp, MapExp.size(Exp.mapBin(p.get(0))));
    }

}
