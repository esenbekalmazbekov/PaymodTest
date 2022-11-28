package kg.doit.paymodtest.usecase;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import kg.doit.domain.dto.AppDto;
import kg.doit.domain.mapper.AppListMapper;
import kg.doit.domain.mapper.BaseMapper;
import kg.doit.domain.model.App;
import kg.doit.domain.repository.AppRepository;

public class GetAllAppsUseCase extends SingleUseCase<List<AppDto>, List<App>, SingleUseCase.EmptyParams> {
    private final AppRepository repository;
    private final AppListMapper listMapper;

    public GetAllAppsUseCase(AppRepository repository, AppListMapper listMapper) {
        this.repository = repository;
        this.listMapper = listMapper;
    }

    public void executeDomain(
        Consumer<List<App>> onSuccess,
        Consumer<String> onError,
        Action onFinished
    ) {
        execute(onSuccess, onError, onFinished, listMapper, EmptyParams.getInstance());
    }

    @Override
    protected Single<List<AppDto>> buildUseCaseSingle(EmptyParams params) {
        return repository.fetchApps();
    }
}
