package com.aerospike.restclient.domain.geojsonmodels;

import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.IASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class GeoJSONTest {

    private final IASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonGeoJSONMapper(),
                new MsgPackGeoJSONMapper()
        };
    }

    public GeoJSONTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testEmptyMapDoesNotMapToGeoJSON() {
        Map<String, Object> geoMap = new HashMap<>();

        try {
            mapper.bytesToObject(mapper.objectToBytes(geoMap));
            Assert.fail("Should have not mapped to RestClientCTX");
        } catch (Exception e) {
            // Success
        }
    }

    @Test
    public void testMapsToPoint() {
        Map<String, Object> geoMap = new HashMap<>();
        geoMap.put("type", AerospikeAPIConstants.GeoJSON.Types.POINT);
        geoMap.put("coordinates", new double[]{1.2, 2.6});

        try {
            Point restGeo = (Point) mapper.bytesToObject(mapper.objectToBytes(geoMap));
            Assert.assertEquals(new LngLat(1.2, 2.6), restGeo.coordinates);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to GeoJSON %s", e));
        }
    }

    @Test
    public void testMapsToPolygon() {
        Map<String, Object> geoMap = new HashMap<>();
        geoMap.put("type", AerospikeAPIConstants.GeoJSON.Types.POLYGON);
        geoMap.put("coordinates", new double[][]{new double[]{1.2, 2.6}, new double[]{3, 4}});
        List<LngLat> expectedCoord = new ArrayList<>();
        expectedCoord.add(new LngLat(1.2, 2.6));
        expectedCoord.add(new LngLat(3, 4));

        try {
            Polygon restGeo = (Polygon) mapper.bytesToObject(mapper.objectToBytes(geoMap));
            Assert.assertEquals(expectedCoord, restGeo.coordinates);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to GeoJSON %s", e));
        }
    }

    @Test
    public void testMapsToAeroCircle() {
        Map<String, Object> geoMap = new HashMap<>();
        geoMap.put("type", AerospikeAPIConstants.GeoJSON.Types.AERO_CIRCLE);
        geoMap.put("coordinates", new Object[]{new double[]{1.2, 2.6}, 3});

        try {
            AeroCircle restGeo = (AeroCircle) mapper.bytesToObject(mapper.objectToBytes(geoMap));
            Assert.assertEquals(new LngLatRad(1.2, 2.6, 3), restGeo.coordinates);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to GeoJSON %s", e));
        }
    }

}

class JsonGeoJSONMapper extends ASTestMapper {

    public JsonGeoJSONMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), GeoJSON.class);
    }
}

class MsgPackGeoJSONMapper extends ASTestMapper {

    public MsgPackGeoJSONMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), GeoJSON.class);
    }
}
