package paul6325106.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paul6325106.device.AdbDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AppiumNodeManager {

    private final Logger LOG = LoggerFactory.getLogger(AppiumNodeManager.class);

    private final List<AppiumProperties> appiumPropertiesList;
    private final Lock lock;

    private ExecutorService executorService;
    private boolean isStarted;

    public AppiumNodeManager(final List<AppiumProperties> appiumPropertiesList) {
        this.appiumPropertiesList = appiumPropertiesList;
        this.isStarted = false;
        this.lock = new ReentrantLock(true);
    }

    private List<Callable<Void>> getTasks(final List<AppiumProperties> appiumPropertiesList) {
        final AppiumDriverLocalServiceFactory factory = new AppiumDriverLocalServiceFactory();

        final Map<String, AtomicBoolean> deviceConnectedMap = new HashMap<>();

        final List<Callable<Void>> tasks = new ArrayList<>();

        for (final AppiumProperties appiumProperties : appiumPropertiesList) {
            final AppiumDriverLocalService service = factory.buildAndroidService(appiumProperties);

            final AtomicBoolean isDeviceConnected = new AtomicBoolean(false);

            deviceConnectedMap.put(appiumProperties.getDeviceId(), isDeviceConnected);

            tasks.add(new AppiumDriverLocalServiceTask(service, isDeviceConnected));
        }

        // TODO enabling/disabling adb and xcode detectors
        tasks.add(new DeviceDetectorTask(new AdbDetector(), deviceConnectedMap));

        return tasks;
    }

    public void start() {
        lock.lock();
        try {
            if (isStarted) {
                return;
            }

            LOG.info("Starting Appium node manager");

            final List<Callable<Void>> tasks = getTasks(appiumPropertiesList);

            executorService = Executors.newFixedThreadPool(tasks.size());

            for (final Callable<Void> task : tasks) {
                executorService.submit(task);
            }

            isStarted = true;
        } finally {
            lock.unlock();
        }
    }

    public boolean isRunning() {
        lock.lock();
        try {
            // TODO any checking of threads status?
            return isStarted && !executorService.isShutdown();
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        lock.lock();
        try {
            if (isStarted) {
                LOG.info("Stopping Appium node manager");
                executorService.shutdownNow();
            }
        } finally {
            isStarted = false;
            lock.unlock();
        }
    }

}
