package kg.doit.paymodtest.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import kg.doit.data.network.AppApi;
import kg.doit.data.retrofit.RetrofitBuilder;
import kg.doit.paymodtest.BuildConfig;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {

    @Singleton
    @Provides
    public Context provideApplication(@ApplicationContext Context app) {
        return app;
    }

    @Singleton
    @Provides
    public RetrofitBuilder provideRetrofitBuilder () {
        return new RetrofitBuilder();
    }

    @Singleton
    @Provides
    public AppApi provideAppApi(
        RetrofitBuilder builder
    ) {
        return builder.getRetrofit(BuildConfig.BASE_URL).create(AppApi.class);
    }

}
