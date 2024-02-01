package app.trackwizz.connect.filter;

import app.trackwizz.connect.annotation.Authorize;
import app.trackwizz.connect.constant.AppSettingConstants;
import app.trackwizz.connect.constant.JwtConstants;
import app.trackwizz.connect.exception.TokenException;
import app.trackwizz.connect.helper.httpcontext.IHttpContextHelper;
import app.trackwizz.connect.service.token.ITokenService;
import app.trackwizz.connect.util.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthorizationFilter extends AbstractFilter {
    private final ITokenService ITokenService;
    private final IHttpContextHelper IHttpContextHelper;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public JwtAuthorizationFilter(ITokenService ITokenService, IHttpContextHelper IHttpContextHelper, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.ITokenService = ITokenService;
        this.IHttpContextHelper = IHttpContextHelper;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        initializeExcludeUrls(JwtConstants.JWT_FILTER_KEY);
        initializeJWTExcludeEndpoints(requestMappingHandlerMapping.getHandlerMethods(), Authorize.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Map<String, Object> errorDetails = new HashMap<>();

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            if (checkIfExcludedPath(httpServletRequest.getServletPath())) {
                filterChain.doFilter(request, response);
                return;
            }

            // verify if header has token
            String accessToken = IHttpContextHelper.getToken();
            if (!StringUtils.hasText(accessToken)) {
                throw new TokenException("Empty token");
            }

            // verify token
            ITokenService.verifyToken(accessToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            errorDetails.put("message: ", "Authentication Error");
            errorDetails.put("details: ", e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(JsonUtil.convertEntityToJson(errorDetails));
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
