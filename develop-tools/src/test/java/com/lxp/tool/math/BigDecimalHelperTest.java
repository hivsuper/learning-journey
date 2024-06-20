package com.lxp.tool.math;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BigDecimalHelperTest {
    private final double num_1 = 1.05;
    private final double num_2 = 0.5;

    @Test
    public void testAdd() {
        assertThat(BigDecimalHelper.add(num_1, num_2)).isEqualTo(1.55);
    }

    @Test
    public void testSub() {
        assertThat(BigDecimalHelper.sub(num_1, num_2)).isEqualTo(0.55);
    }

    @Test
    public void testMul() {
        assertThat(BigDecimalHelper.mul(num_1, num_2)).isEqualTo(0.525);
    }

    @Test
    public void testDiv() throws IllegalAccessException {
        assertThat(BigDecimalHelper.div(num_1, num_2)).isEqualTo(2.1);
    }

    @Test
    public void testDivWithScale() throws IllegalAccessException {
        assertThat(BigDecimalHelper.div(num_1, num_2, 0)).isEqualTo(2);
    }

    @Test
    public void shouldThrowIllegalAccessExceptionWhenDivWithNegativeScale() {
        assertThatThrownBy(() -> BigDecimalHelper.div(num_1, num_2, -1))
                .isInstanceOf(IllegalAccessException.class).hasMessage("scale can't be less than 0");
    }

    @Test
    public void equals() {
        assertFalse(BigDecimalHelper.equals(BigDecimal.valueOf(110, 2), new BigDecimal(1.1)));
        assertTrue(BigDecimalHelper.equals(BigDecimal.valueOf(110, 2), new BigDecimal("1.10")));
        assertTrue(BigDecimalHelper.equals(BigDecimal.valueOf(1.10), new BigDecimal("1.1")));
    }

    @Test
    public void compare() {
        // Greater Than
        assertThat(BigDecimalHelper.compare(BigDecimal.valueOf(111, 2), BigDecimal.valueOf(1.1))).isGreaterThan(0);
        assertThat(BigDecimalHelper.compare(BigDecimal.valueOf(111, 2), new BigDecimal("1.10"))).isGreaterThan(0);
        assertThat(BigDecimalHelper.compare(BigDecimal.valueOf(1.11), new BigDecimal("1.1"))).isGreaterThan(0);

        // Equals
        assertThat(BigDecimalHelper.compare(BigDecimal.valueOf(110, 2), BigDecimal.valueOf(1.1))).isEqualTo(0);
        assertThat(BigDecimalHelper.compare(BigDecimal.valueOf(110, 2), new BigDecimal("1.10"))).isEqualTo(0);
        assertThat(BigDecimalHelper.compare(BigDecimal.valueOf(1.10), new BigDecimal("1.1"))).isEqualTo(0);

        // Less Than
        assertThat(BigDecimalHelper.compare(BigDecimal.valueOf(109, 2), BigDecimal.valueOf(1.1))).isLessThan(0);
        assertThat(BigDecimalHelper.compare(BigDecimal.valueOf(109, 2), new BigDecimal("1.10"))).isLessThan(0);
        assertThat(BigDecimalHelper.compare(BigDecimal.valueOf(1.09), new BigDecimal("1.1"))).isLessThan(0);
    }
}