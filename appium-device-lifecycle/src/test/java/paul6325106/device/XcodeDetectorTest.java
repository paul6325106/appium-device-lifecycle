package paul6325106.device;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class XcodeDetectorTest {

    private XcodeDetector detector;

    @Before
    public void setUp() throws Exception {
        detector = new XcodeDetector();
    }

    // TODO testXcodeUnavailable

    @Test
    public void testXcodeWithoutDevices() throws Exception {
        final String filename = "/paul6325106/device/xcode_empty.txt";
        final InputStream resourceAsStream = this.getClass().getResourceAsStream(filename);

        final List<String> connectedDeviceIds = detector.getConnectedDeviceIds(resourceAsStream);
        Assert.assertTrue(connectedDeviceIds.isEmpty());
    }

    @Test
    public void testXcodeWithDevices() throws Exception {
        final String filename = "/paul6325106/device/xcode_multiple.txt";
        final InputStream resourceAsStream = this.getClass().getResourceAsStream(filename);

        final List<String> connectedDeviceIds = detector.getConnectedDeviceIds(resourceAsStream);
        Assert.assertEquals(3, connectedDeviceIds.size());
        Assert.assertTrue(connectedDeviceIds.contains("F0607F9E-2422-37VC-C65R-6578113205G4"));
        Assert.assertTrue(connectedDeviceIds.contains("f19f8lllw1548v109a0v12aw7mb98910684q032a"));
        Assert.assertTrue(connectedDeviceIds.contains("0989FB00-AS32-4C32-7Y5H-D149DV016553"));
    }

}
