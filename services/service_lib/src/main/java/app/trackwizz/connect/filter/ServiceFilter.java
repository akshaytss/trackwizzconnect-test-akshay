package app.trackwizz.connect.filter;

import app.trackwizz.connect.constant.AppSettingConstants;
import app.trackwizz.connect.constant.common.EndpointsConstants;
import org.springframework.core.env.Environment;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;


public class ServiceFilter implements Filter {
    private final Environment environment;

    public ServiceFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // Check if the request is for the root URL
        if (request instanceof HttpServletRequest httpRequest) {
            if (httpRequest.getRequestURI().equals(httpRequest.getContextPath() + EndpointsConstants.INFO)) {
                // Return the service name directly
                PrintWriter out = response.getWriter();
                out.println("Environment : " + environment.getProperty(AppSettingConstants.TWC_ENVIRONMENT_NAME));
                out.println("Service : " + environment.getProperty(AppSettingConstants.SERVICE_NAME) + " - " + environment.getProperty(AppSettingConstants.SERVICE_VERSION));
                return; // Stop the filter chain
            }
        }

        // Continue the request/response chain for other URLs
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
