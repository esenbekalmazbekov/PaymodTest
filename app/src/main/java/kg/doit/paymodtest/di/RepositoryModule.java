package kg.doit.paymodtest.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import kg.doit.data.network.AppApi;
import kg.doit.data.repo_impl.AppRepositoryImpl;
import kg.doit.domain.repository.AppRepository;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Singleton
    @Provides
    public AppRepository provideAppRepository(
            AppApi api
    ) {
        return new AppRepositoryImpl(api);
    }
}