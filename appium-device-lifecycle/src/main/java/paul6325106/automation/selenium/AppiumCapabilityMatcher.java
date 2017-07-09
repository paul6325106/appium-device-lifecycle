package paul6325106.automation.selenium;

import static io.appium.java_client.remote.MobileCapabilityType.DEVICE_NAME;
import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM;

import org.openqa.grid.internal.utils.DefaultCapabilityMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AppiumCapabilityMatcher extends DefaultCapabilityMatcher {

    @Override
    public boolean matches(final Map<String, Object> nodeCapability, final Map<String, Object> requestedCapability) {
        // overriding default behaviour completely

        final List<String> capabilityTypes = Arrays.asList(BROWSER_NAME, PLATFORM, DEVICE_NAME);

        for (final String capabilityType : capabilityTypes) {

            final boolean isNodeDefined = nodeCapability.containsKey(capabilityType);
            final boolean isRequestedDefined = nodeCapability.containsKey(capabilityType);
            final Object nodeValue = nodeCapability.get(capabilityType);
            final Object requestedValue = requestedCapability.get(capabilityType);

            if (!(isNodeDefined && isRequestedDefined && nodeValue.equals(requestedValue))) {
                return false;
            }
        }

        return true;
    }

}
