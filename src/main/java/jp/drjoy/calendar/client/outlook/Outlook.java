package jp.drjoy.calendar.client.outlook;

import java.util.UUID;
import jp.drjoy.calendar.client.outlook.auth.AuthHelper;

public class Outlook {

    /**
     * 環境
     *  - dev, stg, production
     */
    private String env;

    /**
     * デバイス
     *  - pc, sp, app
     */
    private String device;

    private UUID state;

    private UUID nonce;

    public Outlook() {
        this.env = "dev";
        this.device = "pc";
        this.state = UUID.randomUUID();
        this.nonce = UUID.randomUUID();
    }

    public Outlook(String env, String device) {
        this();
        this.env = env;
        this.device = device;
    }

    public String getAuthUrl() {
        return AuthHelper.getLoginUrl(env, device, state, nonce);
    }
}
