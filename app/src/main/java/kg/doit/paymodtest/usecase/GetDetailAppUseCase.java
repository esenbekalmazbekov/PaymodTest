package kg.doit.paymodtest.usecase;

import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import kg.doit.domain.dto.AppDto;
import kg.doit.domain.mapper.AppMapper;
import kg.doit.domain.model.App;
import kg.doit.domain.repository.AppRepository;

public class GetDetailAppUseCase extends SingleUseCase<AppDto, App, GetDetailAppUseCase.DetailParams> {
    private final AppRepository repository;
    private final AppMapper mapper;

    public GetDetailAppUseCase(AppRepository repository, AppMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void executeDomain(
        Consumer<App> onSuccess,
        Consumer<String> onError,
        Action onFinished,
        String type
    ) {
        execute(onSuccess, onError, onFinished, mapper, new DetailParams(type));
    }

    @Override
    protected Single<AppDto> buildUseCaseSingle(DetailParams params) {
        return repository.fetchApp(params.type);
    }

    public static class DetailParams extends Params {
        private final String type;

        public DetailParams(String type) {
            this.type = type;
        }
    }
}
