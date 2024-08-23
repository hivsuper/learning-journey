package com.lxp.tool.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JsonHelperTest {
    private TestJsonVo testJsonVo;

    @BeforeEach
    public void setUp() {
        testJsonVo = new TestJsonVo();
        testJsonVo.setPropertyString("a");
        testJsonVo.setPropertyBoolean(false);
        testJsonVo.setPropertyInt(1);
        testJsonVo.setPropertyDouble(0.56D);
    }

    @Test
    public void testConvertValue() throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("propertyString", testJsonVo.getPropertyString());
        map.put("propertyBoolean", testJsonVo.isPropertyBoolean());
        map.put("propertyInt", testJsonVo.getPropertyInt());
        map.put("propertyDouble", testJsonVo.getPropertyDouble());
        assertEquals(testJsonVo.toString(), JsonHelper.convertValue(TestJsonVo.class, map).toString());
    }

    @Test
    public void testToString() {
        assertEquals("{\"propertyString\":\"a\",\"propertyBoolean\":false,\"propertyInt\":1,\"propertyDouble\":0.56}",
                JsonHelper.toString(testJsonVo));
    }

    @Test
    public void shouldReturnNullWhenPassString() {
        assertNull(JsonHelper.toObject(TestJsonVo.class, ""));
    }

    @Test
    public void shouldReturnObjectWhenPassString() {
        assertEquals(testJsonVo.toString(), JsonHelper.toObject(TestJsonVo.class,
                        "{\"propertyString\":\"a\",\"propertyBoolean\":false,\"propertyInt\":1,\"propertyDouble\":0.56}")
                .toString());
    }

    @Test
    public void shouldReturnTypeObjectWhenPassString() {
        TypeReference<List<TestJsonVo>> reference = new TypeReference<>() {
        };
        assertEquals(Collections.singletonList(testJsonVo).toString(), JsonHelper.toObject(reference,
                        "[{\"propertyString\":\"a\",\"propertyBoolean\":false,\"propertyInt\":1,\"propertyDouble\":0.56}]")
                .toString());
    }

    @Test
    public void shouldReturnObjectWhenPassInputStream() throws IOException {
        try (InputStream inputStream = JsonHelperTest.class.getResourceAsStream("/JsonHepler.json")) {
            assertEquals(testJsonVo.toString(), JsonHelper.toObject(TestJsonVo.class, inputStream).toString());
        }
    }

    @Test
    public void shouldReturnTypeObjectWhenPassInputStream() throws IOException, URISyntaxException {
        TypeReference<List<TestJsonVo>> reference = new TypeReference<>() {
        };
        String content = String.format("[%s]", Files.lines(Paths.get(JsonHelperTest.class.getResource("/JsonHepler.json").toURI()), StandardCharsets.UTF_8).collect(Collectors.joining()));
        try (InputStream inputStream = new ByteArrayInputStream(content.getBytes())) {
            assertEquals(Collections.singletonList(testJsonVo).toString(), JsonHelper.toObject(reference, inputStream).toString());
        }
    }

    @Test
    public void shouldPropertyDoubleWhenPassDouble() throws IOException, URISyntaxException {
        assertEquals(testJsonVo.toString(), JsonHelper.toObject(TestJsonVo.class,
                        "{\"propertyString\":\"a\",\"propertyBoolean\":false,\"propertyInt\":1,\"propertyDouble\":0.56}")
                .toString());
    }

    @Test
    public void shouldPropertyDoubleWhenPassFloat() throws IOException, URISyntaxException {
        assertEquals(testJsonVo.toString(), JsonHelper.toObject(TestJsonVo.class,
                        "{\"propertyString\":\"a\",\"propertyBoolean\":false,\"propertyInt\":1,\"propertyFloat\":0.56}")
                .toString());
    }

    @Test
    public void shouldGeneratePropertyDouble() {
        assertEquals("{\"propertyString\":\"a\",\"propertyBoolean\":false,\"propertyInt\":1,\"propertyDouble\":0.56}", JsonHelper.toString(testJsonVo));
    }

    private static class TestJsonVo {
        private String propertyString;
        private boolean propertyBoolean;
        private int propertyInt;
        @JsonProperty("propertyDouble")
        @JsonAlias("propertyFloat")
        private double propertyDouble;

        public String getPropertyString() {
            return propertyString;
        }

        public void setPropertyString(String propertyString) {
            this.propertyString = propertyString;
        }

        public boolean isPropertyBoolean() {
            return propertyBoolean;
        }

        public void setPropertyBoolean(boolean propertyBoolean) {
            this.propertyBoolean = propertyBoolean;
        }

        public int getPropertyInt() {
            return propertyInt;
        }

        public void setPropertyInt(int propertyInt) {
            this.propertyInt = propertyInt;
        }

        public double getPropertyDouble() {
            return propertyDouble;
        }

        public void setPropertyDouble(double propertyDouble) {
            this.propertyDouble = propertyDouble;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("TestJsonVo [propertyString=").append(getPropertyString()).append(", propertyBoolean=")
                    .append(isPropertyBoolean()).append(", propertyInt=").append(getPropertyInt())
                    .append(", propertyDouble=").append(getPropertyDouble()).append("]");
            return builder.toString();
        }
    }
}
