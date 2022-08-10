package com.aerospike.restclient.util.converters;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.CTX;
import com.aerospike.client.cdt.ListOrder;
import com.aerospike.client.cdt.MapOrder;
import com.aerospike.restclient.util.RestClientErrors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CTXConverter {

    public static final String CTX_LIST_INDEX_KEY = "list_by_index";
    public static final String CTX_LIST_INDEX_CREATE_KEY = "list_index_create";
    public static final String CTX_LIST_RANK_KEY = "list_by_rank";
    public static final String CTX_LIST_VALUE_KEY = "list_by_value";
    public static final String CTX_MAP_INDEX_KEY = "map_by_index";
    public static final String CTX_MAP_RANK_KEY = "map_by_rank";
    public static final String CTX_MAP_KEY_KEY = "map_by_key";
    public static final String CTX_MAP_KEY_CREATE_KEY = "map_key_create";
    public static final String CTX_MAP_VALUE_KEY = "map_by_value";

    private static final List<String> CTX_KEYS = Arrays.asList(
            CTX_LIST_INDEX_KEY,
            CTX_LIST_INDEX_CREATE_KEY,
            CTX_LIST_RANK_KEY,
            CTX_LIST_VALUE_KEY,
            CTX_MAP_INDEX_KEY,
            CTX_MAP_RANK_KEY,
            CTX_MAP_KEY_KEY,
            CTX_MAP_KEY_CREATE_KEY,
            CTX_MAP_VALUE_KEY
    );

    private static CTX[] extractCTX(List<String> ctxItems) {
        List<CTX> rt = new ArrayList<>();
        for (String ctxItem : ctxItems) {
            ctxItem = ctxItem.trim();
            String[] parsedCDT = ctxItem.split("\\(", 2);

            if (parsedCDT.length != 2) {
                throw new RestClientErrors.InvalidCTXError("Unable to parse ctx item " + ctxItem);
            }

            String item = parsedCDT[0];
            String val = parsedCDT[1];

            if (val.endsWith(")")) {
                val = val.substring(0, val.length() - 1);
            }

            switch (item) {
                case CTX_LIST_INDEX_KEY:
                    rt.add(CTX.listIndex(getIntValue(val)));
                    break;
//                case CTX_LIST_INDEX_CREATE_KEY:
//                    ListOrder listOrder = getListOrder(opValues);
//                    rt.add(CTX.listIndexCreate(getIntValue(opValues, item),
//                            listOrder, getBoolValue(opValues, PAD_KEY)));
//                    break;
                case CTX_LIST_RANK_KEY:
                    rt.add(CTX.listRank(getIntValue(val)));
                    break;
                case CTX_LIST_VALUE_KEY:
                    rt.add(CTX.listValue(Value.get(opValues.get(item))));
                    break;
                case CTX_MAP_INDEX_KEY:
                    rt.add(CTX.mapIndex(getIntValue(val)));
                    break;
                case CTX_MAP_KEY_KEY:
                    rt.add(CTX.mapKey(Value.get(opValues.get(item))));
                    break;
//                case CTX_MAP_KEY_CREATE_KEY:
//                    MapOrder mapOrder = getMapOrder(opValues);
//                    rt.add(CTX.mapKeyCreate(Value.get(opValues.get(item)), mapOrder));
//                    break;
                case CTX_MAP_RANK_KEY:
                    rt.add(CTX.mapRank(getIntValue(val)));
                    break;
                case CTX_MAP_VALUE_KEY:
                    rt.add(CTX.mapValue(Value.get(opValues.get(item))));
                    break;
            }
        }
        return rt.isEmpty() ? null : rt.toArray(new CTX[0]);
    }

    static public int getIntValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RestClientErrors.InvalidCTXError(String.format("Unable to parse integer \"%s\"", value));
        }
    }
}
