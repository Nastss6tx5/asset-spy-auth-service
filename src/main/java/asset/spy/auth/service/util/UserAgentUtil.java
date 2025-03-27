package asset.spy.auth.service.util;

public class UserAgentUtil {

    public static String determineUserAgent(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "unknown device";
        }
        String lowerAgent = userAgent.toLowerCase();
        if (lowerAgent.contains("android")) {
            return "mobile-android";
        }
        if (lowerAgent.contains("iphone") || lowerAgent.contains("ipad")) {
            return "mobile-ios";
        }
        if (lowerAgent.contains("mobile")) {
            return "mobile-other";
        }
        return "web-browser";
    }
}
