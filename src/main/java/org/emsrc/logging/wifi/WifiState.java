package org.emsrc.logging.wifi;

/**
 * COPIED FROM PHONESTUDY
 */
public enum WifiState {
    DISABLED("DISABLED"),
    ENABLED_DISCONNECTED("ENABLED_DISCONNECTED"),
    ENABLED_CONNECTED("ENABLED_CONNECTED"),
    UNKNOWN("UNKNOWN");

    private final String name;

    WifiState(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }

    public static WifiState fromString(String text) {
        if (text != null) {
            for (WifiState w : WifiState.values()) {
                if (text.equalsIgnoreCase(w.name)) {
                    return w;
                }
            }
        }
        return null;
    }

}

