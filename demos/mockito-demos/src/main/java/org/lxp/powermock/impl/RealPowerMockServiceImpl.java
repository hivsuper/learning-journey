package org.lxp.powermock.impl;

import org.lxp.powermock.PowerMockHelper;
import org.lxp.powermock.RealPowerMockService;

public class RealPowerMockServiceImpl implements RealPowerMockService {

    @Override
    public String execute(String firstName, String lastName) {
        return String.format("Hello, %s!", PowerMockHelper.getFullName(firstName, lastName));
    }

}
