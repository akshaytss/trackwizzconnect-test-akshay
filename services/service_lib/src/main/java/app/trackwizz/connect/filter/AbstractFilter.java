package app.trackwizz.connect.filter;

import app.trackwizz.connect.constant.JwtConstants;
import app.trackwizz.connect.util.JsonUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.Filter;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractFilter implements Filter {

    private final Set<String> excludedUrlSet = new HashSet<>();
    private final Set<String> excludedUrlPatternSet = new HashSet<>();

    /**
     * initialize the ExcludedUrl Set from File which is stored in resource
     */
    protected void initializeExcludeUrls(String filterKey) {
        try (InputStream inputStream = new ClassPathResource(JwtConstants.JWT_FILTER_EXCLUDED_URL_JSON_FILE).getInputStream()) {
            String[] excludedUrls = JsonUtil
                    .parseObjectToEntity(JsonUtil
                            .readJsonFromResource(inputStream, filterKey), String[].class);
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            for (String url : excludedUrls) {
                if (antPathMatcher.isPattern(url)) excludedUrlPatternSet.add(url);
                else excludedUrlSet.add(url);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * return true if the given request servlet path is excluded
     *
     * @param servletPath
     */
    protected boolean checkIfExcludedPath(String servletPath) {
        if (excludedUrlSet.contains(servletPath)) return true;
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String pattern : excludedUrlPatternSet) {
            if (antPathMatcher.match(pattern, servletPath)) return true;
        }
        return false;
    }

    /**
     * initialize the excluded url in which the api is not annotated with given annotation
     *
     * @param handlerMethodsMap
     */
    protected void initializeJWTExcludeEndpoints(Map<RequestMappingInfo, HandlerMethod> handlerMethodsMap,
                                                 Class<? extends Annotation> annotationClass) {
        handlerMethodsMap
                .keySet()
                .forEach(requestMappingInfo -> {
                    HandlerMethod handlerMethod = handlerMethodsMap.get(requestMappingInfo);
                    if (!handlerMethod.getMethod().isAnnotationPresent(annotationClass)) {
                        String url = String.valueOf(requestMappingInfo.getPathPatternsCondition().getPatterns().iterator().next());
                        excludedUrlSet.add(url);
                    }
                });
    }
}
