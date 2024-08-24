package com.lxp.tool.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BigDecimalHelperTest {
    private final double num_1 = 1.05;
    private final double num_2 = 0.5;

    @ParameterizedTest
    @MethodSource("provideDoublesForAdd")
    public void testAdd(double num_1, double num_2, double expected) {
        assertThat(BigDecimalHelper.add(num_1, num_2)).isEqualTo(expected);
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

    @ParameterizedTest
    @MethodSource("provideDoublesForEquals")
    public void equals(BigDecimal num_1, BigDecimal num_2, boolean expected) {
        assertThat(BigDecimalHelper.equals(num_1, num_2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDoublesForCompare")
    public void compare(BigDecimal num_1, BigDecimal num_2, int expected) {
        assertThat(BigDecimalHelper.compare(num_1, num_2)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDoublesForAdd() {
        return Stream.of(
                Arguments.of(1.05, 0.5, 1.55),
                Arguments.of(-1.5, 0.5, -1),
                Arguments.of(-0.5, 0.5, 0)
        );
    }

    private static Stream<Arguments> provideDoublesForEquals() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(110, 2), new BigDecimal(1.1), false),
                Arguments.of(BigDecimal.valueOf(110, 2), new BigDecimal("1.10"), true),
                Arguments.of(BigDecimal.valueOf(1.10), new BigDecimal("1.1"), true)
        );
    }

    private static Stream<Arguments> provideDoublesForCompare() {
        return Stream.of(
                // Greater Than
                Arguments.of(BigDecimal.valueOf(111, 2), BigDecimal.valueOf(1.1), 1),
                Arguments.of(BigDecimal.valueOf(111, 2), new BigDecimal("1.10"), 1),
                Arguments.of(BigDecimal.valueOf(1.11), new BigDecimal("1.1"), 1),
                // Equals
                Arguments.of(BigDecimal.valueOf(110, 2), BigDecimal.valueOf(1.1), 0),
                Arguments.of(BigDecimal.valueOf(110, 2), new BigDecimal("1.10"), 0),
                Arguments.of(BigDecimal.valueOf(1.10), new BigDecimal("1.1"), 0),
                // Less Than
                Arguments.of(BigDecimal.valueOf(109, 2), BigDecimal.valueOf(1.1), -1),
                Arguments.of(BigDecimal.valueOf(109, 2), new BigDecimal("1.10"), -1),
                Arguments.of(BigDecimal.valueOf(1.09), new BigDecimal("1.1"), -1)
        );
    }
}