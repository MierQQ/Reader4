package nsu.mier.reader.repository;

import okhttp3.OkHttpClient;

public class SingletonOkHttpClient {
    private static OkHttpClient client;
    public static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }
    private SingletonOkHttpClient() {}
}
