package paul6325106.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XcodeDetector implements DeviceDetector {

    private final Pattern pattern;

    public XcodeDetector() {
        pattern = Pattern.compile("^.+ \\[([A-Za-z0-9\\-]+)\\](?: \\(Simulator\\))?$");
    }

    @Override
    public List<String> getConnectedDeviceIds() throws IOException {

        final Process process = Runtime.getRuntime().exec("adb devices");
        try {
            process.waitFor();
        } catch (final InterruptedException e) {
            throw new IOException("Interrupted while waiting for output from adb devices");
        }

        return getConnectedDeviceIds(process.getInputStream());
    }

    List<String> getConnectedDeviceIds(final InputStream inputStream) throws IOException {
        final List<String> deviceIds = new ArrayList<>();

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // expecting header for first line
            String line = reader.readLine();

            // TODO detect xcode not installed message
            if (line == null) {
                throw new IOException("Unable to execute xcode devices, got header: " + line);
            }

            while ((line = reader.readLine()) != null) {
                final Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    deviceIds.add(matcher.group(1));
                } else if (line.equals("Known Templates:")) {
                    return deviceIds;
                }
            }
        }

        return deviceIds;
    }

}
