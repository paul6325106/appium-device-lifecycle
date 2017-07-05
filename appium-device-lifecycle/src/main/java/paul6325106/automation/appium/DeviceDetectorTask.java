package paul6325106.automation.appium;

import paul6325106.automation.device.DeviceDetector;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

class DeviceDetectorTask implements Callable<Void> {

    private final static long DELAY = Duration.ofSeconds(30).toMillis();

    private final DeviceDetector deviceDetector;
    private final HashMap<String, AtomicBoolean> deviceConnectedMap;
    private final AtomicBoolean isStopRequested;

    DeviceDetectorTask(final DeviceDetector deviceDetector, final Map<String, AtomicBoolean> deviceConnectedMap,
            final AtomicBoolean isStopRequested) {

        this.deviceDetector = deviceDetector;
        this.deviceConnectedMap = new HashMap<>(deviceConnectedMap);
        this.isStopRequested = isStopRequested;
    }

    @Override
    public Void call() throws Exception {
        try {
            while (!isStopRequested.get()) {
                final List<String> connectedDeviceIds = deviceDetector.getConnectedDeviceIds();

                for (final Map.Entry<String, AtomicBoolean> entry : deviceConnectedMap.entrySet()) {
                    final String deviceId = entry.getKey();
                    final boolean isDeviceConnected = connectedDeviceIds.contains(deviceId);
                    entry.getValue().set(isDeviceConnected);
                }

                Thread.sleep(DELAY);
            }
        } catch (final Exception e) {
            isStopRequested.set(true);
            throw e;
        }

        return null;
    }
}
