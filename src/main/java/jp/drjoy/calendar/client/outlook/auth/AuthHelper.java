package jp.drjoy.calendar.client.outlook.auth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

import org.springframework.web.util.UriComponentsBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class AuthHelper {
    private static final String authority = "https://login.microsoftonline.com";
    private static final String authorizeUrl = authority + "/common/oauth2/v2.0/authorize";

    private static String[] scopes = {
            "openid",
            "offline_access",
            "profile",
            "User.Read",
            "Mail.Read",
            "Calendars.Read",
            "Contacts.Read"
    };

    private static String appId = null;
    private static String appPassword = null;
    private static String redirectUrl = null;

    private static String getAppId(String env, String device) {
        if (appId == null) {
            try {
                loadConfig(env, device);
            } catch (Exception e) {
                return null;
            }
        }
        return appId;
    }

    private static String getAppPassword(String env, String device) {
        if (appPassword == null) {
            try {
                loadConfig(env, device);
            } catch (Exception e) {
                return null;
            }
        }
        return appPassword;
    }

    private static String getRedirectUrl(String env, String device) {
        if (redirectUrl == null) {
            try {
                loadConfig(env, device);
            } catch (Exception e) {
                return null;
            }
        }
        return redirectUrl;
    }

    private static String getScopes() {
        StringBuilder sb = new StringBuilder();
        for (String scope : scopes) {
            sb.append(scope + " ");
        }
        return sb.toString().trim();
    }

    private static void loadConfig(String env, String device) throws IOException {
        String authConfigFile = "auth.properties";
        InputStream authConfigStream =
                AuthHelper.class.getClassLoader().getResourceAsStream(authConfigFile);

        if (authConfigStream != null) {
            Properties authProps = new Properties();
            try {
                authProps.load(authConfigStream);
                appId = authProps.getProperty(String.format("%s.%s.appId", env, device));
                appPassword =
                        authProps.getProperty(String.format("%s.%s.appPassword", env, device));
                redirectUrl =
                        authProps.getProperty(String.format("%s.%s.redirectUrl", env, device));
            } finally {
                authConfigStream.close();
            }
        } else {
            throw new FileNotFoundException(
                    "Property file '" + authConfigFile + "' not found in the classpath.");
        }
    }

    public static String getLoginUrl(String env, String device, UUID state, UUID nonce) {

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(authorizeUrl);
        urlBuilder.queryParam("client_id", getAppId(env, device));
        urlBuilder.queryParam("redirect_uri", getRedirectUrl(env, device));
        urlBuilder.queryParam("response_type", "code id_token");
        urlBuilder.queryParam("scope", getScopes());
        urlBuilder.queryParam("state", state);
        urlBuilder.queryParam("nonce", nonce);
        urlBuilder.queryParam("response_mode", "form_post");

        return urlBuilder.toUriString();
    }

    public static TokenResponse getTokenFromAuthCode(String authCode, String tenantId) {
        return AuthHelper.getTokenFromAuthCode(authCode, tenantId, "dev", "pc");
    }

    public static TokenResponse getTokenFromAuthCode(String authCode, String tenantId, String env,
            String device) {
        // Create a logging interceptor to log request and responses
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        // Create and configure the Retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(authority)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        // Generate the token service
        TokenService tokenService = retrofit.create(TokenService.class);

        try {
            return tokenService.getAccessTokenFromAuthCode(tenantId, getAppId(env, device),
                    getAppPassword(env, device),
                    "authorization_code", authCode, getRedirectUrl(env, device)).execute().body();
        } catch (IOException e) {
            TokenResponse error = new TokenResponse();
            error.setError("IOException");
            error.setErrorDescription(e.getMessage());
            return error;
        }
    }

    public static TokenResponse ensureTokens(TokenResponse tokens, String tenantId) {
        return AuthHelper.ensureTokens(tokens, tenantId, "dev", "pc");
    }

    public static TokenResponse ensureTokens(TokenResponse tokens, String tenantId, String env,
            String device) {
        // Are tokens still valid?
        Calendar now = Calendar.getInstance();
        if (now.getTime().after(tokens.getExpirationTime())) {
            // Still valid, return them as-is
            return tokens;
        } else {
            // Expired, refresh the tokens
            // Create a logging interceptor to log request and responses
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor).build();

            // Create and configure the Retrofit object
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(authority)
                    .client(client)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            // Generate the token service
            TokenService tokenService = retrofit.create(TokenService.class);

            try {
                return tokenService.getAccessTokenFromRefreshToken(tenantId, getAppId(env, device),
                        getAppPassword(env, device),
                        "refresh_token", tokens.getRefreshToken(), getRedirectUrl(env, device))
                        .execute()
                        .body();
            } catch (IOException e) {
                TokenResponse error = new TokenResponse();
                error.setError("IOException");
                error.setErrorDescription(e.getMessage());
                return error;
            }
        }
    }
}
