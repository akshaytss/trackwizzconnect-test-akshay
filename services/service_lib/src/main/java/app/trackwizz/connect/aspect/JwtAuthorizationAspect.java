package app.trackwizz.connect.aspect;

import app.trackwizz.connect.annotation.Authorize;
import app.trackwizz.connect.exception.TokenException;
import app.trackwizz.connect.helper.httpcontext.IHttpContextHelper;
import app.trackwizz.connect.service.token.ITokenService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Aspect
public class JwtAuthorizationAspect {
    private final IHttpContextHelper IHttpContextHelper;
    private final ITokenService ITokenService;

    public JwtAuthorizationAspect(IHttpContextHelper IHttpContextHelper, ITokenService ITokenService) {
        this.IHttpContextHelper = IHttpContextHelper;
        this.ITokenService = ITokenService;
    }

    @Before(value = "@annotation(app.trackwizz.connect.annotation.Authorize)")
    public void checkUserAccess(JoinPoint joinPoint) throws TokenException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Authorize authorize = methodSignature.getMethod().getAnnotation(Authorize.class);
        String token = IHttpContextHelper.getToken();
        List<String> scopes = ITokenService.getListOfScopesFromToken(token);
        if (!CollectionUtils.isEmpty(scopes)) {
            if (!scopes.contains(authorize.scope().getName())) {
                throw new TokenException("Invalid scope");
            }
        }
    }
}
