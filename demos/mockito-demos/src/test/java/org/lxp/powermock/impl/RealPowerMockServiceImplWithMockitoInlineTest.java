package org.lxp.powermock.impl;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.lxp.powermock.PowerMockHelper;
import org.lxp.powermock.RealPowerMockService;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


/**
 * https://frontbackend.com/java/how-to-mock-static-methods-with-mockito
 */
public class RealPowerMockServiceImplWithMockitoInlineTest {
    private RealPowerMockService realPowerMockService;

    @Before
    public void setUp() {
        realPowerMockService = new RealPowerMockServiceImpl();
    }

    @Test
    public void testExecute() {
        MatcherAssert.assertThat(realPowerMockService.execute("aa"), Matchers.is("Super Liaa"));
        try (MockedStatic<PowerMockHelper> theMock = Mockito.mockStatic(PowerMockHelper.class)) {
            theMock.when(PowerMockHelper::getName).thenReturn("bb");

            MatcherAssert.assertThat(PowerMockHelper.getName(), Matchers.is("bb"));

            MatcherAssert.assertThat(realPowerMockService.execute("aa"), Matchers.is("bbaa"));
        }
        MatcherAssert.assertThat(realPowerMockService.execute("aa"), Matchers.is("Super Liaa"));
    }

}
