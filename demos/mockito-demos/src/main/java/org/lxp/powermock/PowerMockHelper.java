package org.lxp.powermock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PowerMockHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PowerMockHelper.class);

    public static String getFullName(String firstName, String lastName) {
        String fullName = String.format("%s %s", firstName, lastName);
        LOGGER.info("Full name is {}", fullName);
        return fullName;
    }
}
