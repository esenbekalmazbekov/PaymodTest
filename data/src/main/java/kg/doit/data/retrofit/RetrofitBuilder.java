package kg.doit.data.retrofit;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import kg.doit.data.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    public RetrofitBuilder () {}

    public Retrofit getRetrofit(
        String url
    )  {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(getRetrofitClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient getRetrofitClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .hostnameVerifier((s, sslSession) -> {
                    HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
                    return verifier.verify(s, sslSession);
                })
                .addInterceptor((client -> {
                    Request.Builder requestBuilder = client.request().newBuilder();
                    requestBuilder.addHeader("Accept", "application/json");
                    return client.proceed(requestBuilder.build());
                }));
        setTimeOutToOkHttpClient(builder);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        return builder.build();
    }

    private void setTimeOutToOkHttpClient(OkHttpClient.Builder builder) {
        builder.readTimeout(30L, TimeUnit.SECONDS);
        builder.connectTimeout(30L, TimeUnit.SECONDS);
        builder.writeTimeout(30L, TimeUnit.SECONDS);
    }
}
