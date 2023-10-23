package org.lxp.powermock.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lxp.powermock.PowerMockHelper;
import org.lxp.powermock.RealPowerMockService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.lxp.powermock.PowerMockIgnoreHelper.POWER_MOCK_IGNORE_MANAGEMENT;
import static org.lxp.powermock.PowerMockIgnoreHelper.POWER_MOCK_IGNORE_SSL;

/**
 * https://blog.csdn.net/lqadam/article/details/78939161
 * Reimport mockito-core before running this test as mockito-core is conflict with mockito-inline
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PowerMockHelper.class})
@PowerMockIgnore({POWER_MOCK_IGNORE_MANAGEMENT, POWER_MOCK_IGNORE_SSL})
@Ignore
public class RealPowerMockServiceImplTest {
    private RealPowerMockService realPowerMockService;

    @Before
    public void setUp() {
        realPowerMockService = new RealPowerMockServiceImpl();
    }

    @Test
    public void testExecute() {
        Assert.assertEquals("Super Liaa", realPowerMockService.execute("aa"));

        PowerMockito.mockStatic(PowerMockHelper.class);
        PowerMockito.when(PowerMockHelper.getName()).thenReturn("bb");
        Assert.assertEquals("bbaa", realPowerMockService.execute("aa"));
    }

}
