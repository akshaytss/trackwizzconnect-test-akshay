package app.trackwizz.connect.service.token;

import app.trackwizz.connect.exception.TokenException;
import app.trackwizz.connect.model.JwtToken;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.List;

public interface ITokenService {
    /**
     * @param companyId
     * @param requestScopes
     * @return
     */
    JwtToken createToken(Integer companyId, List<String> requestScopes);

    /**
     * @param token
     * @return
     * @throws Exception
     */
    List<String> verifyTokenAndReturnScopes(String token) throws Exception;

    /**
     * @param token
     * @return
     */
    List<String> getListOfScopesFromToken(String token);

    /**
     * @param decodedJWT
     * @return
     */
    boolean isTokenExpired(DecodedJWT decodedJWT);

    /**
     * @param decodedJWT
     * @param key
     * @return
     */
    Integer getClaimAsInt(DecodedJWT decodedJWT, String key);

    /**
     * @param decodedJWT
     * @param key
     * @return
     */
    List<String> getClaimAsListOfString(DecodedJWT decodedJWT, String key);

    /**
     * @param token
     * @return
     */
    DecodedJWT verifyToken(String token) throws TokenException;
}
