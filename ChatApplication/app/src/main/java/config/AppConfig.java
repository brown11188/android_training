package config;

/**
 * Created by enclaveit on 1/22/16.
 */
public class AppConfig {

    //GCM API caller
    public static final String GCM_API = "https://android.googleapis.com/gcm/send";

    //Sender id
    public static final String SENDER_ID = "396598309792";

    //API key
    public static final String API_KEY = "AIzaSyCzf6uVgBl9D2XDDpL3oIwvgvSPF-gvasA";

    //Registration id
    public static final String REGISTRATION_ID = "f3fZJ1yIfjo:APA91bGGBgNPekEj12c84u9xjI9nuAXD9b0hJ-ddz179ix9V9PnXjoM3vVKVr-ggUcmNQckU0FgE10m-WW19HplX3jhRWTNOSYSzJ2OJ_W01HKpcrmkdKg9FZQGTP0_pFCHrD9mwkF2w";

    //Expires time: 4 weeks
    public static final long GCM_TIME_TO_LIVE = 60L * 60L * 24L * 7L * 4L;
}
