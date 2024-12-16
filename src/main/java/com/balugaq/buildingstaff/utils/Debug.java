package com.balugaq.buildingstaff.utils;

import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;

public class Debug {
    public static void stackTraceManually() {
        try {
            throw new Error();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void log(Object... object) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : object) {
            if (obj == null) {
                sb.append("null");
            } else {
                sb.append(obj.toString());
            }
        }

        log(sb.toString());
    }

    public static void log(Object object) {
        log(object.toString());
    }

    public static void log(String message) {
        BuildingStaffPlugin.getInstance().getLogger().info(message);
    }
}
