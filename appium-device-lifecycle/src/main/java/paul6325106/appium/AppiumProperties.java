package paul6325106.appium;

public class AppiumProperties {

    private final String deviceId;
    private final String nodeConfig;
    private final int port;
    private final int bootstrapPort;
    private final int chromeDriverPort;
    private final boolean sessionOverride;

    public AppiumProperties(final String deviceId, final String nodeConfig, final int port, final int bootstrapPort,
            final int chromeDriverPort, final boolean sessionOverride) {

        this.deviceId = deviceId;
        this.nodeConfig = nodeConfig;
        this.port = port;
        this.bootstrapPort = bootstrapPort;
        this.chromeDriverPort = chromeDriverPort;
        this.sessionOverride = sessionOverride;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getNodeConfig() {
        return nodeConfig;
    }

    public int getPort() {
        return port;
    }

    public int getBootstrapPort() {
        return bootstrapPort;
    }

    public int getChromeDriverPort() {
        return chromeDriverPort;
    }

    public boolean isSessionOverride() {
        return sessionOverride;
    }
}
