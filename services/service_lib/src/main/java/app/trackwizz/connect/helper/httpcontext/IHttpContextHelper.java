package app.trackwizz.connect.helper.httpcontext;

import javax.servlet.http.HttpServletRequest;

public interface IHttpContextHelper {

    /**
     * @return
     */
    String getToken();

    /**
     * @param request
     * @return
     */
    String getCallerIp(HttpServletRequest request);

    /**
     * @param request
     * @return
     */
    String getRequestUri(HttpServletRequest request);
}
