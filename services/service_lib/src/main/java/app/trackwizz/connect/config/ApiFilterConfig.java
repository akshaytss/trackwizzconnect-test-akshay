package app.trackwizz.connect.config;

import app.trackwizz.connect.constant.common.EndpointsConstants;
import app.trackwizz.connect.constant.common.FilterConstants;
import app.trackwizz.connect.filter.JwtAuthorizationFilter;
import app.trackwizz.connect.helper.httpcontext.IHttpContextHelper;
import app.trackwizz.connect.service.token.ITokenService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.annotation.WebFilter;

@Configuration
@WebFilter(EndpointsConstants.API)
public class ApiFilterConfig {
    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> jwtAuthorizationFilterFilterRegistrationBean(ITokenService ITokenService, IHttpContextHelper IHttpContextHelper, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        FilterRegistrationBean<JwtAuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(10);
        registrationBean.setName(FilterConstants.JWT_FILTER);
        registrationBean.addUrlPatterns(EndpointsConstants.ALL);
        registrationBean.setFilter(new JwtAuthorizationFilter(ITokenService, IHttpContextHelper, requestMappingHandlerMapping));
        return registrationBean;
    }
}