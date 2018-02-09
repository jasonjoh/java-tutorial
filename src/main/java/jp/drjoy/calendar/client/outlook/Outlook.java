package jp.drjoy.calendar.client.outlook;

import java.util.UUID;
import jp.drjoy.calendar.client.outlook.auth.AuthHelper;

public class Outlook {
    public String getAuthUrl() {
        UUID state = UUID.randomUUID();
        UUID nonce = UUID.randomUUID();
        return AuthHelper.getLoginUrl(state, nonce);
    }
}
