package com.bbva.exchange.util;

public final class AppConstants {

    private AppConstants() {
        // Previene instanciación
    }

    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    // JWT Secret Key (mover a config externa en producción)
    public static final String JWT_SECRET_KEY = "mySuperSecretKeyThatIsAtLeast32CharsLong!";

    // JWT Issuer
    public static final String JWT_ISSUER = "bbva-exchange-api";

    // Mensajes comunes
    public static final String ACCESS_DENIED_ADMIN = "Access denied: Admin role required";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";

    // Otros valores constantes que vayas necesitando
}