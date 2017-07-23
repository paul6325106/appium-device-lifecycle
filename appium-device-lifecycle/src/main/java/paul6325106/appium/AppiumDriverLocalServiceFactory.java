package paul6325106.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import static io.appium.java_client.service.local.flags.AndroidServerFlag.*;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.*;

class AppiumDriverLocalServiceFactory {

    AppiumDriverLocalService buildAndroidService(final AppiumProperties appiumProperties) {
        final AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withArgument(LOG_LEVEL, "warn")
                .withArgument(CONFIGURATION_FILE, appiumProperties.getNodeConfig())
                .usingPort(appiumProperties.getPort())
                .withArgument(BOOTSTRAP_PORT_NUMBER, String.valueOf(appiumProperties.getBootstrapPort()))
                .withArgument(CHROME_DRIVER_PORT, String.valueOf(appiumProperties.getChromeDriverPort()));

        if (appiumProperties.isSessionOverride()) {
            builder.withArgument(SESSION_OVERRIDE);
        }

        return builder.build();
    }

}
