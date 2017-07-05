package paul6325106.automation.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import static io.appium.java_client.service.local.flags.AndroidServerFlag.*;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.*;

public class AppiumDriverLocalServiceFactory {

    public AppiumDriverLocalService buildAndroidService(final AppiumProperties appiumProperties) {
        return new AppiumServiceBuilder()
                .withArgument(CONFIGURATION_FILE, appiumProperties.getNodeConfig())
                .usingPort(appiumProperties.getPort())
                .withArgument(BOOTSTRAP_PORT_NUMBER, String.valueOf(appiumProperties.getBootstrapPort()))
                .withArgument(CHROME_DRIVER_PORT, String.valueOf(appiumProperties.getChromeDriverPort()))
                .withArgument(SESSION_OVERRIDE, String.valueOf(appiumProperties.isSessionOverride()))
                .build();
    }

}
