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
package com.aerospike.restclient.util.converters.predexp;

import com.aerospike.client.query.PredExp;
import com.aerospike.client.query.RegexFlag;
import com.google.common.base.Preconditions;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class PredExpFactory {

    private PredExpFactory() {
    }

    public static PredExp getLogicalExp(String name, int nexp) {
        final Operator.Logic op = Operator.Logic.fromString(name);
        Preconditions.checkNotNull(op, "getLogicalExp operator fromString");
        switch (op) {
            case AND:
                return PredExp.and(nexp);
            case OR:
                return PredExp.or(nexp);
            default:
                throw new InvalidParameterException(String.format("Invalid logical operator name: %s", name));
        }
    }

    public static PredExp getUnaryLogicalExp(String name) {
        final Operator.LogicUnary op = Operator.LogicUnary.fromString(name);
        Preconditions.checkNotNull(op, "getUnaryLogicalExp operator fromString");
        switch (op) {
            case NOT:
                return PredExp.not();
            default:
                throw new InvalidParameterException(String.format("Invalid unary logical operator name: %s", name));
        }
    }

    public static List<PredExp> getCompareExp(String c1, String op, String c2) {
        List<PredExp> predExpList = new ArrayList<>();
        Optional<Integer> intVal = parseInt(c2);
        if (intVal.isPresent()) {
            predExpList.add(PredExp.integerBin(c1));
            predExpList.add(PredExp.integerValue(intVal.get()));
            predExpList.add(getSimpleExp(op, true));
        } else {
            predExpList.add(PredExp.stringBin(c1));
            predExpList.add(PredExp.stringValue(stripQuotes(c2)));
            predExpList.add(getSimpleExp(op, false));
        }
        return predExpList;
    }

    private static String stripQuotes(String str) {
        if (str.length() >= 2 && str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"') {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    public static PredExp getSimpleExp(String op, boolean isInt) {
        final Operator.Simple operator = Operator.Simple.fromString(op);
        Preconditions.checkNotNull(operator, "getSimpleExp operator fromString");
        PredExp predExp = null;
        switch (operator) {
            case EQUAL:
                predExp = isInt ? PredExp.integerEqual() : PredExp.stringEqual();
                break;
            case UNEQUAL:
                predExp = isInt ? PredExp.integerUnequal() : PredExp.stringUnequal();
                break;
            case GREATER:
                if (isInt)
                    predExp = PredExp.integerGreater();
                break;
            case GREATEREQ:
                if (isInt)
                    predExp = PredExp.integerGreaterEq();
                break;
            case LESS:
                if (isInt)
                    predExp = PredExp.integerLess();
                break;
            case LESSEQ:
                if (isInt)
                    predExp = PredExp.integerLessEq();
                break;
        }
        Preconditions.checkState(predExp != null, String.format("Invalid String operator: %s", op));
        return predExp;
    }

    public static List<PredExp> getSpecialExp(String name, String params) {
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
            case GEOJSON_WITHIN:
                return getGeojsonWithinExpr(params);
            case GEOJSON_CONTAINS:
                return getGeojsonContainsExpr(params);
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
     * // LAST_UPDATE(>=, 1577880000) will be translated to
     * PredExp.recLastUpdate()
     * PredExp.integerValue(1577880000000000000) //in nano
     * PredExp.integerGreaterEq()
     * </pre>
     */
    private static List<PredExp> getLastUpdateExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 2, "getLastUpdateExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        predExpList.add(PredExp.recLastUpdate());
        predExpList.add(PredExp.integerValue(Long.parseLong(p.get(1)) * 1000000000));
        predExpList.add(getSimpleExp(p.get(0), true));
        return predExpList;
    }

    /**
     * Create record expiration time predicate expressed in seconds since 1970-01-01 epoch as 64 bit integer.
     * Example:
     * <pre>
     * // VOID_TIME(>=, 1577880000) will be translated to
     * PredExp.recVoidTime()
     * PredExp.integerValue(1577880000000000000) //in nano
     * PredExp.integerGreaterEq()
     * </pre>
     */
    private static List<PredExp> getVoidTimeExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 2, "getVoidTimeExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        predExpList.add(PredExp.recVoidTime());
        predExpList.add(PredExp.integerValue(Long.parseLong(p.get(1)) * 1000000000));
        predExpList.add(getSimpleExp(p.get(0), true));
        return predExpList;
    }

    /**
     * Create a digest modulo record metadata value predicate expression.
     * The digest modulo expression assumes the value of 4 bytes of the
     * record's key digest modulo it's argument.
     * <p>
     * Example, the following instruction
     * DIGEST_MODULO(3, 1)
     * selects records that have digest(key) % 3 == 1):
     * <pre>
     * PredExp.recDigestModulo(3)
     * PredExp.integerValue(1)
     * PredExp.integerEqual()
     * </pre>
     */
    private static List<PredExp> getDigestModuloExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getDigestModuloExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        predExpList.add(PredExp.recDigestModulo(Integer.parseInt(p.get(0))));
        predExpList.add(PredExp.integerValue(Integer.parseInt(p.get(2))));
        predExpList.add(getSimpleExp(p.get(1), true));
        return predExpList;
    }

    /**
     * Create regular expression string operation predicate.
     * Example:
     * <pre>
     * // STRING_REGEX(str, [0-9]*) will be translated to
     * PredExp.stringBin("str")
     * PredExp.stringValue("[0-9]*")
     * PredExp.stringRegex(RegexFlag.NONE)
     * </pre>
     */
    private static List<PredExp> getStringRegexExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 2, "getStringRegexExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        predExpList.add(PredExp.stringBin(p.get(0)));
        predExpList.add(PredExp.stringValue(stripQuotes(p.get(1))));
        predExpList.add(PredExp.stringRegex(RegexFlag.NONE));
        return predExpList;
    }

    /**
     * Create geospatial json "within" predicate.
     * Example:
     * <pre>
     * // GEOJSON_WITHIN(location, {"type": "AeroCircle", "coordinates": [[-122.0, 37.5], 1000]}) will be translated to
     * PredExp.geoJSONBin("location")
     * PredExp.geoJSONValue("{"type": "AeroCircle", "coordinates": [[-122.0, 37.5], 1000]}")
     * PredExp.geoJSONWithin()
     * </pre>
     */
    private static List<PredExp> getGeojsonWithinExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 2, "getGeojsonWithinExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        predExpList.add(PredExp.geoJSONBin(p.get(0)));
        predExpList.add(PredExp.geoJSONValue(p.get(1)));
        predExpList.add(PredExp.geoJSONWithin());
        return predExpList;
    }

    /**
     * Create geospatial json "contains" predicate.
     */
    private static List<PredExp> getGeojsonContainsExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 2, "getGeojsonContainsExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        predExpList.add(PredExp.geoJSONBin(p.get(0)));
        predExpList.add(PredExp.geoJSONValue(p.get(1)));
        predExpList.add(PredExp.geoJSONContains());
        return predExpList;
    }

    /**
     * Create list predicate where expression matches for any list item.
     * Example:
     * <pre>
     * // Find records where any list item v = "hello" in list bin x.
     * // LIST_ITERATE_OR(x, ==, hello) will be translated to
     * PredExp.stringVar("v")
     * PredExp.stringValue("hello")
     * PredExp.stringEqual()
     * PredExp.listBin("x")
     * PredExp.listIterateOr("v")
     * </pre>
     */
    private static List<PredExp> getListIterateOrExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getListIterateOrExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            predExpList.add(PredExp.integerVar("v"));
            predExpList.add(PredExp.integerValue(ival.get()));
            predExpList.add(getSimpleExp(p.get(1), true));
        } else {
            predExpList.add(PredExp.stringVar("v"));
            predExpList.add(PredExp.stringValue(p.get(2)));
            predExpList.add(getSimpleExp(p.get(1), false));
        }
        predExpList.add(PredExp.listBin(p.get(0)));
        predExpList.add(PredExp.listIterateOr("v"));
        return predExpList;
    }

    /**
     * Create list predicate where expression matches for all list items.
     * Example:
     * <pre>
     * // Find records where all list elements v != "goodbye" in list bin x.
     * // LIST_ITERATE_AND(x, !=, goodbye) will be translated to
     * PredExp.stringVar("v")
     * PredExp.stringValue("goodbye")
     * PredExp.stringUnequal()
     * PredExp.listBin("x")
     * PredExp.listIterateAnd("v")
     * </pre>
     */
    private static List<PredExp> getListIterateAndExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getListIterateAndExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            predExpList.add(PredExp.integerVar("v"));
            predExpList.add(PredExp.integerValue(ival.get()));
            predExpList.add(getSimpleExp(p.get(1), true));
        } else {
            predExpList.add(PredExp.stringVar("v"));
            predExpList.add(PredExp.stringValue(p.get(2)));
            predExpList.add(getSimpleExp(p.get(1), false));
        }
        predExpList.add(PredExp.listBin(p.get(0)));
        predExpList.add(PredExp.listIterateAnd("v"));
        return predExpList;
    }

    /**
     * Create map predicate where expression matches for any map key.
     * Example:
     * <pre>
     * // Find records where any map key k = 7 in map bin m.
     * // MAPKEY_ITERATE_OR(m, ==, 7) will be translated to
     * PredExp.integerVar("k")
     * PredExp.integerValue(7)
     * PredExp.integerEqual()
     * PredExp.mapBin("m")
     * PredExp.mapKeyIterateOr("k")
     * </pre>
     */
    private static List<PredExp> getMapKeyIterateOrExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getMapKeyIterateOrExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            predExpList.add(PredExp.integerVar("k"));
            predExpList.add(PredExp.integerValue(ival.get()));
            predExpList.add(getSimpleExp(p.get(1), true));
        } else {
            predExpList.add(PredExp.stringVar("k"));
            predExpList.add(PredExp.stringValue(p.get(2)));
            predExpList.add(getSimpleExp(p.get(1), false));
        }
        predExpList.add(PredExp.mapBin(p.get(0)));
        predExpList.add(PredExp.mapKeyIterateOr("k"));
        return predExpList;
    }

    /**
     * Create map predicate where expression matches for any map value.
     * <pre>
     * // Find records where any map value v > 100 in map bin m.
     * // MAPVAL_ITERATE_OR(m, ==, 7) will be translated to
     * PredExp.integerVar("v")
     * PredExp.integerValue(100)
     * PredExp.integerGreater()
     * PredExp.mapBin("m")
     * PredExp.mapValIterateOr("v")
     * </pre>
     */
    private static List<PredExp> getMapValIterateOrExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getMapValIterateOrExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            predExpList.add(PredExp.integerVar("v"));
            predExpList.add(PredExp.integerValue(ival.get()));
            predExpList.add(getSimpleExp(p.get(1), true));
        } else {
            predExpList.add(PredExp.stringVar("v"));
            predExpList.add(PredExp.stringValue(p.get(2)));
            predExpList.add(getSimpleExp(p.get(1), false));
        }
        predExpList.add(PredExp.mapBin(p.get(0)));
        predExpList.add(PredExp.mapValIterateOr("v"));
        return predExpList;
    }

    /**
     * Create map key predicate where expression matches for all map keys.
     * Example:
     * <pre>
     * // Find records where all map keys k < 5 in map bin m.
     * // MAPKEY_ITERATE_AND(m, <, 5) will be translated to
     * PredExp.integerVar("k")
     * PredExp.integerValue(5)
     * PredExp.integerLess()
     * PredExp.mapBin("m")
     * PredExp.mapKeyIterateAnd("k")
     * </pre>
     */
    private static List<PredExp> getMapKeyIterateAndExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getMapKeyIterateAndExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            predExpList.add(PredExp.integerVar("k"));
            predExpList.add(PredExp.integerValue(ival.get()));
            predExpList.add(getSimpleExp(p.get(1), true));
        } else {
            predExpList.add(PredExp.stringVar("k"));
            predExpList.add(PredExp.stringValue(p.get(2)));
            predExpList.add(getSimpleExp(p.get(1), false));
        }
        predExpList.add(PredExp.mapBin(p.get(0)));
        predExpList.add(PredExp.mapKeyIterateAnd("k"));
        return predExpList;
    }

    /**
     * Create map predicate where expression matches for all map values.
     * Example:
     * <pre>
     * // Find records where all map values v > 500 in map bin m.
     * // MAPVAL_ITERATE_AND(m, >, 500) will be translated to
     * PredExp.integerVar("v")
     * PredExp.integerValue(500)
     * PredExp.integerGreater()
     * PredExp.mapBin("m")
     * PredExp.mapValIterateAnd("v")
     * </pre>
     */
    private static List<PredExp> getMapValIterateAndExpr(String params) {
        List<String> p = extractParameters(params);
        Preconditions.checkArgument(p.size() == 3, "getMapValIterateAndExpr invalid format");
        List<PredExp> predExpList = new ArrayList<>();
        Optional<Integer> ival = parseInt(p.get(2));
        if (ival.isPresent()) {
            predExpList.add(PredExp.integerVar("v"));
            predExpList.add(PredExp.integerValue(ival.get()));
            predExpList.add(getSimpleExp(p.get(1), true));
        } else {
            predExpList.add(PredExp.stringVar("v"));
            predExpList.add(PredExp.stringValue(p.get(2)));
            predExpList.add(getSimpleExp(p.get(1), false));
        }
        predExpList.add(PredExp.mapBin(p.get(0)));
        predExpList.add(PredExp.mapValIterateAnd("v"));
        return predExpList;
    }

    private static List<String> extractParameters(String params) {
        params = params.replaceAll("[()]", "");
        List<String> p = Arrays.asList(params.split(","));
        return p.stream().map(String::trim).collect(Collectors.toList());
    }

    private static Optional<Integer> parseInt(String s) {
        int i;
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(i);
    }
}
