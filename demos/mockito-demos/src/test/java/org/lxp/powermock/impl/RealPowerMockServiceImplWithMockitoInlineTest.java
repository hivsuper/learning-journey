package org.lxp.powermock.impl;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lxp.powermock.PowerMockHelper;
import org.lxp.powermock.RealPowerMockService;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.eq;


/**
 * https://frontbackend.com/java/how-to-mock-static-methods-with-mockito
 */
public class RealPowerMockServiceImplWithMockitoInlineTest {
    private RealPowerMockService realPowerMockService;

    @BeforeEach
    public void setUp() {
        realPowerMockService = new RealPowerMockServiceImpl();
    }

    @Test
    public void testExecute() {
        MatcherAssert.assertThat(realPowerMockService.execute("first", "last"), Matchers.is("Hello, first last!"));
        try (MockedStatic<PowerMockHelper> theMock = Mockito.mockStatic(PowerMockHelper.class)) {
            theMock.when(() -> PowerMockHelper.getFullName("first", "last")).thenReturn("bb");

            MatcherAssert.assertThat(PowerMockHelper.getFullName("first", "last"), Matchers.is("bb"));

            MatcherAssert.assertThat(realPowerMockService.execute("first", "last"), Matchers.is("Hello, bb!"));
            theMock.verify(() -> PowerMockHelper.getFullName(eq("first"), eq("last")), Mockito.times(2));
        }
        MatcherAssert.assertThat(realPowerMockService.execute("first", "last"), Matchers.is("Hello, first last!"));
    }

}
