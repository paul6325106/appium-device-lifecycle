package paul6325106.automation.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdbDetector implements DeviceDetector {

    private final static String COMMAND = "adb devices";

    @Override
    public List<String> getConnectedDeviceIds() throws IOException {
        final List<String> deviceIds = new ArrayList<>();

        final Pattern pattern = Pattern.compile("^([^\\s]+)\\s+device$");

        final Process process = Runtime.getRuntime().exec(COMMAND);
        try {
            process.waitFor();
        } catch (final InterruptedException e) {
            throw new IOException("Interrupted while waiting for output from adb devices");
        }

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                final Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    deviceIds.add(matcher.group(1));
                }
            }
        }

        return deviceIds;
    }
}
