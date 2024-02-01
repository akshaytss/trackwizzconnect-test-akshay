package app.trackwizz.connect.service.token;

import app.trackwizz.connect.constant.AppSettingConstants;
import app.trackwizz.connect.constant.JwtConstants;
import app.trackwizz.connect.constant.common.HttpConstants;
import app.trackwizz.connect.exception.TokenException;
import app.trackwizz.connect.model.JwtToken;
import app.trackwizz.connect.util.KeyLoader;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class TokenService implements ITokenService {
    private static final PrivateKey PRIVATE_KEY = KeyLoader.loadPrivateKey();
    private static final PublicKey PUBLIC_KEY = KeyLoader.loadPublicKey();
    private final long accessTokenValidity;
    private final String ISSUING_ENV;

    public TokenService(Environment environment) {
        this.ISSUING_ENV = environment.getRequiredProperty(AppSettingConstants.TWC_ENVIRONMENT_NAME);
        this.accessTokenValidity = Long.parseLong(environment.getRequiredProperty(AppSettingConstants.TWC_JWT_TOKEN_EXPIRY));
    }

    @Override
    public JwtToken createToken(Integer companyId, List<String> requestScopes) {
        Instant tokenCreationTime = Instant.now();
        Instant tokenExpiryTime = tokenCreationTime.plus(Duration.ofMinutes(accessTokenValidity));

        // Signing with private key
        Algorithm algorithm = Algorithm.ECDSA256((ECPublicKey) PUBLIC_KEY, (ECPrivateKey) PRIVATE_KEY);

        String token = JWT.create()
                .withClaim(JwtConstants.JWT_CLAIM_COMPANY_ID, companyId)
                .withClaim(JwtConstants.JWT_CLAIM_SCOPES, requestScopes)
                .withClaim(JwtConstants.JWT_CLAIM_ISSUING_ENVIRONMENT, ISSUING_ENV)
                .withIssuer(JwtConstants.JWT_TOKEN_ISSUER)
                .withIssuedAt(tokenCreationTime)
                .withExpiresAt(tokenExpiryTime)
                .sign(algorithm);
        return new JwtToken(token, companyId, requestScopes, tokenCreationTime, tokenExpiryTime, ISSUING_ENV);
    }

    @Override
    public List<String> verifyTokenAndReturnScopes(String token) throws TokenException {
        try {
            // verifies token signature, matches iss, check if token is expired
            DecodedJWT decodedJWT = verifyToken(token);
            return getClaimAsListOfString(decodedJWT, JwtConstants.JWT_CLAIM_SCOPES);
        } catch (Exception e) {
            throw new TokenException(e.getMessage());
        }
    }

    @Override
    public List<String> getListOfScopesFromToken(String token) {
        return getClaimAsListOfString(JWT.decode(token), JwtConstants.JWT_CLAIM_SCOPES);
    }

    @Override
    public boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date());
    }

    @Override
    public Integer getClaimAsInt(DecodedJWT decodedJWT, String key) {
        return decodedJWT.getClaim(key).asInt();
    }

    @Override
    public List<String> getClaimAsListOfString(DecodedJWT decodedJWT, String key) {
        return decodedJWT.getClaim(key).asList(String.class);
    }

    @Override
    public DecodedJWT verifyToken(String token) throws TokenException {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);

            // verify with public key
            Algorithm algorithm = Algorithm.ECDSA256((ECPublicKey) PUBLIC_KEY, null);

            // verifies token signature, iss, exp & i-env
            return JWT.require(algorithm)
                    .withIssuer(JwtConstants.JWT_TOKEN_ISSUER)
                    .withClaim(JwtConstants.JWT_CLAIM_ISSUING_ENVIRONMENT, ISSUING_ENV)
                    .build()
                    .verify(decodedJWT);
        } catch (JWTDecodeException e) {
            throw new TokenException("Failed to decode token");
        } catch (TokenExpiredException e) {
            throw new TokenException("Token Expired at: " + e.getExpiredOn());
        } catch (IncorrectClaimException e) {
            if (e.getClaimName().equals(JwtConstants.JWT_CLAIM_ISSUING_ENVIRONMENT)) {
                throw new TokenException("Token issued by: " + e.getClaimValue() + " env cannot be used on \"" + ISSUING_ENV + "\" env");
            }
            throw new TokenException("Invalid token claim. ClaimName: " + e.getClaimName() + " ClaimValue: " + e.getClaimValue() + " was not expected");
        } catch (JWTVerificationException e) {
            throw new TokenException("Failed to verify token signature");
        }
    }
}
