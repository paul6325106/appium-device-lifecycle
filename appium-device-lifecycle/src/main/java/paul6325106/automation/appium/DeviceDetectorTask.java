package paul6325106.automation.appium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paul6325106.automation.device.DeviceDetector;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

class DeviceDetectorTask implements Callable<Void> {

    private final Logger LOG = LoggerFactory.getLogger(DeviceDetectorTask.class);

    private final static long DELAY = Duration.ofSeconds(30).toMillis();

    private final DeviceDetector deviceDetector;
    private final HashMap<String, AtomicBoolean> deviceConnectedMap;

    DeviceDetectorTask(final DeviceDetector deviceDetector, final Map<String, AtomicBoolean> deviceConnectedMap) {
        this.deviceDetector = deviceDetector;
        this.deviceConnectedMap = new HashMap<>(deviceConnectedMap);
    }

    @Override
    public Void call() throws Exception {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                final List<String> connectedDeviceIds = deviceDetector.getConnectedDeviceIds();

                for (final Map.Entry<String, AtomicBoolean> entry : deviceConnectedMap.entrySet()) {
                    final String deviceId = entry.getKey();
                    final boolean isDeviceConnected = connectedDeviceIds.contains(deviceId);
                    entry.getValue().set(isDeviceConnected);
                }

                Thread.sleep(DELAY);
            }
        } catch (final InterruptedException ignored) {
        } catch (final Exception e) {
            LOG.error("Error occurred during device detection", e);
            throw e;
        }

        return null;
    }
}
