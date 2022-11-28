package kg.doit.paymodtest.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.components.SingletonComponent;
import kg.doit.domain.mapper.AppListMapper;
import kg.doit.domain.mapper.AppMapper;
import kg.doit.domain.repository.AppRepository;
import kg.doit.paymodtest.usecase.GetAllAppsUseCase;
import kg.doit.paymodtest.usecase.GetDetailAppUseCase;

@Module
@InstallIn(ViewModelComponent.class)
public class UseCaseModule {

    @Provides
    public GetAllAppsUseCase provideGetAllAppsUseCase(
        AppRepository repository,
        AppListMapper appListMapper
    ) {
        return new GetAllAppsUseCase(repository, appListMapper);
    }

    @Provides
    public GetDetailAppUseCase provideGetDetailAppUseCase(
            AppRepository repository,
            AppMapper appMapper
    ) {
        return new GetDetailAppUseCase(repository, appMapper);
    }
}