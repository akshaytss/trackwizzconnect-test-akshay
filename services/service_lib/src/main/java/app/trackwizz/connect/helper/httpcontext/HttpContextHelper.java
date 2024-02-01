package app.trackwizz.connect.helper.httpcontext;

import app.trackwizz.connect.constant.common.HttpConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@RequestScope
public class HttpContextHelper implements IHttpContextHelper {
    @Override
    public String getToken() {
        HttpServletRequest currentHttpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String requestTokenHeader = currentHttpServletRequest.getHeader(HttpConstants.AUTHORIZATION);
        if (StringUtils.isBlank(requestTokenHeader)) {
            String requestTokenAttribute = (String) currentHttpServletRequest.getAttribute(HttpConstants.AUTHORIZATION);
            if (StringUtils.isBlank(requestTokenAttribute))
                return null;
            return requestTokenAttribute;
        }
        return requestTokenHeader.substring(HttpConstants.BEARER.length());
    }

    @Override
    public String getCallerIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
            // For localhost, try to determine the real IP from X-Forwarded-For header
            String forwardedFor = request.getHeader("X-Forwarded-For");
            if (forwardedFor != null && !forwardedFor.isEmpty()) {
                remoteAddr = forwardedFor.split(",")[0]; // Take first IP from comma-separated list
            }
        }
        return remoteAddr;
    }

    @Override
    public String getRequestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
