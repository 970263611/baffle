package com.dahuaboke.model;

/**
 * @author dahua
 * @time 2023/7/18 10:08
 */
public enum BaffleMode {

    FILE,
    SERVICE,
    ONLY_FILE,
    ONLY_SERVICE;

    public static BaffleMode getBaffleMode(String mode) {
        if (mode == null) {
            return FILE;
        }
        for (int i = 0; i < values().length; i++) {
            if (values()[i].name().equals(mode.toUpperCase())) {
                return values()[i];
            }
        }
        return FILE;
    }
}
