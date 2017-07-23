package paul6325106.device;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AdbDetectorTest {

    private AdbDetector detector;

    @Before
    public void setUp() throws Exception {
        detector = new AdbDetector();
    }

    @Test
    public void testAdbUnavailable() throws Exception {
        final String filename = "/paul6325106/device/adb_unavailable.txt";
        final InputStream resourceAsStream = this.getClass().getResourceAsStream(filename);

        try {
            detector.getConnectedDeviceIds(resourceAsStream);
        } catch (final IOException e) {
            Assert.assertTrue(e.getMessage().contains("Unable to execute adb devices, got header: "));
        }
    }

    @Test
    public void testAdbWithoutDevices() throws Exception {
        final String filename = "/paul6325106/device/adb_empty.txt";
        final InputStream resourceAsStream = this.getClass().getResourceAsStream(filename);

        final List<String> connectedDeviceIds = detector.getConnectedDeviceIds(resourceAsStream);
        Assert.assertTrue(connectedDeviceIds.isEmpty());
    }

    @Test
    public void testAdbWithDevices() throws Exception {
        final String filename = "/paul6325106/device/adb_multiple.txt";
        final InputStream resourceAsStream = this.getClass().getResourceAsStream(filename);

        final List<String> connectedDeviceIds = detector.getConnectedDeviceIds(resourceAsStream);
        Assert.assertEquals(3, connectedDeviceIds.size());
        Assert.assertTrue(connectedDeviceIds.contains("qwertyu9"));
        Assert.assertTrue(connectedDeviceIds.contains("asdfghj6"));
        Assert.assertTrue(connectedDeviceIds.contains("zxcvbnm3"));
    }
}
