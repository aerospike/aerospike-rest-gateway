//package com.aerospike.restclient.util.converters;
//
//import com.aerospike.client.Value;
//import com.aerospike.client.cdt.CTX;
//import com.aerospike.client.cdt.ListOrder;
//import com.aerospike.client.cdt.MapOrder;
//import com.aerospike.restclient.util.RestClientErrors;
//import org.apache.commons.lang3.NotImplementedException;
//
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//public class CTXConverter {
//
//    public static final String CTX_LIST_INDEX_KEY = "list_by_index";
//    public static final String CTX_LIST_INDEX_CREATE_KEY = "list_index_create";
//    public static final String CTX_LIST_RANK_KEY = "list_by_rank";
//    public static final String CTX_LIST_VALUE_KEY = "list_by_value";
//    public static final String CTX_MAP_INDEX_KEY = "map_by_index";
//    public static final String CTX_MAP_RANK_KEY = "map_by_rank";
//    public static final String CTX_MAP_KEY_KEY = "map_by_key";
//    public static final String CTX_MAP_KEY_CREATE_KEY = "map_key_create";
//    public static final String CTX_MAP_VALUE_KEY = "map_by_value";
//
//    private static final List<String> CTX_KEYS = Arrays.asList(
//            CTX_LIST_INDEX_KEY,
//            CTX_LIST_INDEX_CREATE_KEY,
//            CTX_LIST_RANK_KEY,
//            CTX_LIST_VALUE_KEY,
//            CTX_MAP_INDEX_KEY,
//            CTX_MAP_RANK_KEY,
//            CTX_MAP_KEY_KEY,
//            CTX_MAP_KEY_CREATE_KEY,
//            CTX_MAP_VALUE_KEY
//    );
//
//    private static final Pattern doublePattern = Pattern.compile("-?\\d+\\.{1}\\d+");
//    private static final Pattern intPattern = Pattern.compile("-?\\d+");
//    private static final Pattern strPattern = Pattern.compile("(?:'{1}(.*?)'{1})|(?:\"(.*?)\")");
//    private static final Pattern boolPattern = Pattern.compile("(?:[tT]{1}[Rr]{1}[Uu]{1}[Ee]{1})|(?:[Ff]{1}[Aa]{1}[Ll]{1}[Ss]{1}[Ee]{1})");
//    private static final Pattern base64Pattern = Pattern.compile("(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})");
//    private static final String double_ = "double";
//    private static final String int_ = "int";
//    private static final String str_ = "str";
//    private static final String bool_ = "bool";
//    private static final String base64Bytes_ = "base64Bytes";
//    private static final String[] patternNames = new String[]{double_, int_, str_, bool_, base64Bytes_};
//    private static final Pattern particlePatternWithNames = Pattern.compile(String.format("(?:(?P<%s>%s)|(?P<%s>%s)|(?P<%s>%s)|(?P<%s>%s)|(?P<%s>%s))", double_, doublePattern, int_, intPattern, str_, strPattern, bool_, boolPattern, base64Bytes_, base64Pattern));
//
//    public static CTX convertStringToCTX(String ctxItem) {
//        ctxItem = ctxItem.trim();
//
//        if (ctxItem.endsWith(")")) {
//            ctxItem = ctxItem.substring(0, ctxItem.length() - 2);
//        }
//
//        String[] parsedCDT = ctxItem.split("\\(", 2);
//
//        if (parsedCDT.length != 2) {
//            throw new RestClientErrors.InvalidCTXError("Unable to parse ctx item " + ctxItem);
//        }
//
//        String item = parsedCDT[0];
//        String argsStr = parsedCDT[1];
//
//        switch (item) {
//            case CTX_LIST_INDEX_KEY:
//                return CTX.listIndex(getIntFromStr(argsStr));
//            case CTX_LIST_INDEX_CREATE_KEY:
//                List<String> lArgs = splitCTXArgs(argsStr, 3);
//                return CTX.listIndexCreate(getIntFromStr(lArgs.get(0)),
//                        getListOrderFromString(lArgs.get(1)), getBoolFromStr(lArgs.get(2)));
//            case CTX_LIST_RANK_KEY:
//                return CTX.listRank(getIntFromStr(argsStr));
//            case CTX_LIST_VALUE_KEY:
//                return CTX.listValue(getValueFromStr(argsStr));
//            case CTX_MAP_INDEX_KEY:
//                return CTX.mapIndex(getIntFromStr(argsStr));
//            case CTX_MAP_KEY_KEY:
//                return CTX.mapKey(getValueFromStr(argsStr));
//            case CTX_MAP_KEY_CREATE_KEY:
//                List<String> mArgs = splitCTXArgs(argsStr, 2);
//                MapOrder mapOrder = getMapOrder(mArgs.get(1));
//                return CTX.mapKeyCreate(getValueFromStr(mArgs.get(0)), mapOrder);
//            case CTX_MAP_RANK_KEY:
//                return CTX.mapRank(getIntFromStr(argsStr));
//            case CTX_MAP_VALUE_KEY:
//                return CTX.mapValue(getValueFromStr(argsStr));
//        }
//
//        throw new RestClientErrors.InvalidCTXError(String.format("Unable to parse ctx item \"%s\"", ctxItem));
//    }
//
//    static private List<String> splitCTXArgs(String argsStr, int maxSplit) {
//        StringBuilder reveresedArgsStr = new StringBuilder(argsStr);
//        List<String> arg = Arrays.asList(reveresedArgsStr.reverse().toString().split(",", maxSplit));
//        Collections.reverse(arg);
//        return arg;
//
////        List<String> result = new ArrayList<>();
////        StringBuilder nextArg = new StringBuilder();
////        boolean lastArgFoundAndIsString = false;
////
////        for (int i = argsStr.length() - 1; i >= 0; i--) {
////            char c = argsStr.charAt(i);
////
////            if (!lastArgFoundAndIsString) {
////                if (c == ',') {
////                    result.add(0, nextArg.reverse().toString());
////                    nextArg = new StringBuilder();
////                    continue;
////                } else if (c == '\"' || c == '\'') {
////                    lastArgFoundAndIsString = true;
////                    // Still want to add arg for string parsing later.
////                }
////            }
////
////            nextArg.append(c);
////        }
////
////        result.add(nextArg.reverse().toString());
////
////        return result;
//    }
//
//    static public int getIntFromStr(String value) {
//        try {
//            return Integer.parseInt(value);
//        } catch (NumberFormatException e) {
//            throw new RestClientErrors.InvalidCTXError(String.format("Unable to parse integer \"%s\"", value));
//        }
//    }
//
//    static public double getDoubleFromStr(String value) {
//        try {
//            return Double.parseDouble(value);
//        } catch (NumberFormatException e) {
//            throw new RestClientErrors.InvalidCTXError(String.format("Unable to parse double \"%s\"", value));
//        }
//    }
//
//    static public boolean getBoolFromStr(String value) {
//        try {
//            return Boolean.parseBoolean(value);
//        } catch (NumberFormatException e) {
//            throw new RestClientErrors.InvalidCTXError(String.format("Unable to parse bool \"%s\"", value));
//        }
//    }
//
//    static public byte[] getBytesFromStr(String value) {
//        try {
//            return value.getBytes(StandardCharsets.UTF_8);
//        } catch (NumberFormatException e) {
//            throw new RestClientErrors.InvalidCTXError(String.format("Unable to parse bool from string \"%s\"", value));
//        }
//    }
//
//    static public Value getValueFromStr(String value) {
//        Matcher m = particlePatternWithNames.matcher(value);
//
//        for (String name : patternNames) {
//            String match = m.group(name);
//
//            if (match != null) {
//                switch(name) {
//                    case double_:
//                        return Value.get(getDoubleFromStr(match));
//                    case int_:
//                        return Value.get(getIntFromStr(match));
//                    case str_:
//                        return Value.get(match);
//                    case bool_:
//                        return Value.get(getBoolFromStr(match));
//                    case base64Bytes_:
//                        return Value.get(getBytesFromStr(match));
//                    default:
//                        throw new NotImplementedException(String.format("type name %s has not been implemented in parser", name));
//                }
//            }
//        }
//
//        throw new RestClientErrors.InvalidCTXError(String.format("Unable to parse Value from string \"%s\""));
//    }
//
//    static ListOrder getListOrderFromString(String value) {
//        try {
//            return ListOrder.valueOf(value);
//        } catch (IllegalArgumentException e) {
//            throw new RestClientErrors.InvalidCTXError("Invalid List order: " + value);
//        }
//    }
//
//    static MapOrder getMapOrder(String value) {
//        try {
//            return MapOrder.valueOf(value);
//        } catch (IllegalArgumentException e) {
//            throw new RestClientErrors.InvalidCTXError("Invalid Map order: " + value);
//        }
//    }
//
//}
