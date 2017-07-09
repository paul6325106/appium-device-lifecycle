package paul6325106.automation.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

class AppiumDriverLocalServiceTask implements Callable<Void> {

    private final Logger LOG = LoggerFactory.getLogger(AppiumDriverLocalServiceTask.class);

    private final static long DELAY = Duration.ofSeconds(30).toMillis();

    private final AppiumDriverLocalService service;
    private final AtomicBoolean isDeviceConnected;

    AppiumDriverLocalServiceTask(final AppiumDriverLocalService service, final AtomicBoolean isDeviceConnected) {
        this.service = service;
        this.isDeviceConnected = isDeviceConnected;
    }

    @Override
    public Void call() throws Exception {
        // TODO somewhat concerned about service start/stop blocking on interrupt

        try {
            while (!Thread.currentThread().isInterrupted()) {

                // query once per iteration
                final boolean isDeviceConnected = this.isDeviceConnected.get();
                final boolean isServiceRunning = service.isRunning();

                // appium has its own internal lock for starting/stopping
                if (isServiceRunning && !isDeviceConnected) {
                    LOG.info("Stopping Appium node {}", service.getUrl());
                    service.stop();
                } else if (!isServiceRunning && isDeviceConnected) {
                    LOG.info("Starting Appium node {}", service.getUrl());
                    service.start();
                }

                Thread.sleep(DELAY);
            }
        } catch (final InterruptedException ignored) {
        } catch (final Exception e) {
            LOG.error("Error occurred during Appium service management", e);
            throw e;
        } finally {
            if (service.isRunning()) {
                service.stop();
            }
        }

        return null;
    }
}
