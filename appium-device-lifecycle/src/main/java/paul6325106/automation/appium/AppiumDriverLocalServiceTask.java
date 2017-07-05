package paul6325106.automation.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

class AppiumDriverLocalServiceTask implements Callable<Void> {

    private final static long DELAY = Duration.ofSeconds(30).toMillis();

    private final AppiumDriverLocalService service;
    private final AtomicBoolean isDeviceConnected;
    private final AtomicBoolean isStopRequested;

    AppiumDriverLocalServiceTask(final AppiumDriverLocalService service, final AtomicBoolean isDeviceConnected,
            final AtomicBoolean isStopRequested) {

        this.service = service;
        this.isDeviceConnected = isDeviceConnected;
        this.isStopRequested = isStopRequested;
    }

    @Override
    public Void call() throws Exception {
        try {
            while (!isStopRequested.get()) {

                // query once per iteration
                final boolean isDeviceConnected = this.isDeviceConnected.get();
                final boolean isServiceRunning = service.isRunning();

                // appium has its own internal lock for starting/stopping
                if (isServiceRunning && !isDeviceConnected) {
                    service.stop();
                } else if (!isServiceRunning && isDeviceConnected) {
                    service.start();
                }

                Thread.sleep(DELAY);
            }
        } catch (final Exception e) {
            isStopRequested.set(true);
            throw e;
        } finally {
            if (service.isRunning()) {
                service.stop();
            }
        }

        return null;
    }
}
