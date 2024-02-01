package app.trackwizz.connect.constant;

public final class JwtConstants {
    public static final String JWT_TOKEN_ISSUER = "TrackWizzConnect";
    public static final String JWT_CLAIM_COMPANY_ID = "cpi";
    public static final String JWT_CLAIM_SCOPES = "scp";
    public static final String JWT_CLAIM_ISSUING_ENVIRONMENT = "i-env";
    public static final String TOKEN_SCOPES_DELIMITER = "|";
    public static final String JWT_FILTER_KEY = "JwtAuthenticationFilter";
    public static final String JWT_FILTER_EXCLUDED_URL_JSON_FILE = "/filter/excluded-url.json";


    private JwtConstants() {
    }
}
