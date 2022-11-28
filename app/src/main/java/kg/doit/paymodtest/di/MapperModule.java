package kg.doit.paymodtest.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import kg.doit.domain.mapper.AppListMapper;
import kg.doit.domain.mapper.AppMapper;
import kg.doit.paymodtest.MainApp;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class MapperModule {

    @Singleton
    @Provides
    public AppListMapper provideAllAppMapper(
            AppMapper appMapper
    ) {
        return new AppListMapper(appMapper);
    }

    @Singleton
    @Provides
    public AppMapper provideAppMapper(Context context) {
        return new AppMapper(((MainApp) context).findStatus);
    }
}