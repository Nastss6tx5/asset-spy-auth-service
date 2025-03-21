package asset.spy.auth.service.util;

public class UserAgentUtil {

    public static String determineUserAgent(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "unknown device";
        }

        if (userAgent.contains("Mobile")) {
            if (userAgent.contains("Android")) {
                return "mobile-android";
            }
            if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
                return "mobile-ios";
            }
            return "mobile-other";
        }
        return "web-browser";
    }
}
