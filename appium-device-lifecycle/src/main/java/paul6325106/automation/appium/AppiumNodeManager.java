package paul6325106.automation.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import paul6325106.automation.device.AdbDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppiumNodeManager {

    public static void main(final String[] args) throws Exception {
        final List<Callable<Void>> tasks = getTasks(getAppiumPropertiesList(args));

        final List<Future<Void>> futures = Executors.newFixedThreadPool(tasks.size()).invokeAll(tasks);

        for (final Future<Void> future : futures) {
            try {
                future.get();
            } catch (final ExecutionException e) {
                if (e.getCause() instanceof IOException) {
                    throw new IOException("Unable to query connection status of devices", e.getCause());
                } else {
                    throw e;
                }
            }
        }
    }

    private static List<AppiumProperties> getAppiumPropertiesList(final String[] args) {
        // TODO
        return null;
    }

    private static List<Callable<Void>> getTasks(final List<AppiumProperties> appiumPropertiesList) {
        final AppiumDriverLocalServiceFactory factory = new AppiumDriverLocalServiceFactory();

        final Map<String, AtomicBoolean> deviceConnectedMap = new HashMap<>();

        final AtomicBoolean isStopRequested = new AtomicBoolean(false);

        final List<Callable<Void>> tasks = new ArrayList<>();

        for (final AppiumProperties appiumProperties : appiumPropertiesList) {
            final AppiumDriverLocalService service = factory.buildAndroidService(appiumProperties);

            final AtomicBoolean isDeviceConnected = new AtomicBoolean(false);

            deviceConnectedMap.put(appiumProperties.getDeviceId(), isDeviceConnected);

            tasks.add(new AppiumDriverLocalServiceTask(service, isDeviceConnected, isStopRequested));
        }

        tasks.add(new DeviceDetectorTask(new AdbDetector(), deviceConnectedMap, isStopRequested));

        return tasks;
    }

}
