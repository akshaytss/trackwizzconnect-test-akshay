package app.trackwizz.connect.config;

import app.trackwizz.connect.constant.common.EndpointsConstants;
import app.trackwizz.connect.constant.common.FilterConstants;
import app.trackwizz.connect.filter.ServiceFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

@Configuration
public class CommonFilterConfig {
    @Bean
    public FilterRegistrationBean<ServiceFilter> serviceFilterFilterRegistrationBean(Environment environment) {
        FilterRegistrationBean<ServiceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setName(FilterConstants.SERVICE_FILTER);
        registrationBean.addUrlPatterns(EndpointsConstants.INFO);
        registrationBean.setFilter(new ServiceFilter(environment));
        return registrationBean;
    }
}


