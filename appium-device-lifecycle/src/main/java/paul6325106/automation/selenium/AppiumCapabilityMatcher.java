package paul6325106.automation.selenium;

import org.openqa.grid.internal.utils.DefaultCapabilityMatcher;

import java.util.Map;

public class AppiumCapabilityMatcher extends DefaultCapabilityMatcher {

    @Override
    public boolean matches(final Map<String, Object> nodeCapability, final Map<String, Object> requestedCapability) {
        // TODO
        return true;
    }
}
