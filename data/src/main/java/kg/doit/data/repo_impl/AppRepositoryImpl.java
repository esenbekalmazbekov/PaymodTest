package kg.doit.data.repo_impl;

import java.util.List;

import io.reactivex.Single;
import kg.doit.domain.dto.AppDto;
import kg.doit.data.network.AppApi;
import kg.doit.domain.repository.AppRepository;

public class AppRepositoryImpl implements AppRepository {
    private final AppApi api;

    public AppRepositoryImpl(AppApi api) {
        this.api = api;
    }

    @Override
    public Single<List<AppDto>> fetchApps() {
        return api.fetchApps();
    }

    @Override
    public Single<AppDto> fetchApp(String type) {
        return api.fetchAppDetail(type);
    }
}
