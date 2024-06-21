package org.lxp.powermock;

public abstract class PowerMockHelper {
    public static String getFullName(String firstName, String lastName) {
        return String.format("%s %s", firstName, lastName);
    }
}
