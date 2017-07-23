package paul6325106.device;

import java.io.IOException;
import java.util.List;

public interface DeviceDetector {

    /**
     * Gets list of ids for currently connected devices.
     * @return list of device ids.
     * @throws IOException if unable to read connection status of devices.
     */
    List<String> getConnectedDeviceIds() throws IOException;

}
