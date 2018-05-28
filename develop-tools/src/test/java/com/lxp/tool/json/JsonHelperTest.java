package com.lxp.tool.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class JsonHelperTest {
    private TestJsonVo testJsonVo;

    @Before
    public void setUp() {
        testJsonVo = new TestJsonVo();
        testJsonVo.setPropertyString("a");
        testJsonVo.setPropertyBoolean(false);
        testJsonVo.setPropertyInt(1);
        testJsonVo.setPropertyDouble(0.56D);
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
    public void shouldReturnObjectWhenPassInputStream() throws IOException {
        try (InputStream inputStream = JsonHelperTest.class.getResourceAsStream("/JsonHepler.json")) {
            assertEquals(testJsonVo.toString(), JsonHelper.toObject(TestJsonVo.class, inputStream).toString());
        }
    }

    private static class TestJsonVo {
        private String propertyString;
        private boolean propertyBoolean;
        private int propertyInt;
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
