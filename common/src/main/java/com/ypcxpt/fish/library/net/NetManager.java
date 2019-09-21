package com.ypcxpt.fish.library.net;

import com.ypcxpt.fish.library.BuildConfig;
import com.ypcxpt.fish.library.net.converter.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class NetManager {

    public static final int CONNECT_TIMEOUT = 10;

    public static final int READ_TIMEOUT = 10;

    private Retrofit mRetrofit;

    private String mBaseUrl;

    public static NetManager getInstance() {
        return Singleton.netManager;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        mRetrofit = null;
    }

    /**
     * @param clazz clazz API service class
     * @param <T>
     * @return Target API service instance
     */
    public <T> T createAPIService(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }

    /**
     * @return Retrofit with common configs
     */
    private Retrofit getRetrofit() {
        return mRetrofit == null ? mRetrofit = createRetrofit() : mRetrofit;
    }

    /**
     * @return Retrofit with common configs
     */
    private Retrofit createRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder();
        return builder.baseUrl(mBaseUrl)
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * @return OkHttpClient with common configs
     */
    private OkHttpClient createClient() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        okBuilder.writeTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        addInterceptor(okBuilder);
        return okBuilder.build();
    }

    /**
     * @param okBuilder
     */
    public void addInterceptor(OkHttpClient.Builder okBuilder) {
        if (BuildConfig.BUILD_TYPE != "release") {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(interceptor);
        }
    }

    private NetManager() {
    }

    private static class Singleton {
        private static final NetManager netManager = new NetManager();
    }

}
