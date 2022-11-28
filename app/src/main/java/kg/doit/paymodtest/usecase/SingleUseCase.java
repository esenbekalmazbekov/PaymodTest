package kg.doit.paymodtest.usecase;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kg.doit.domain.mapper.BaseMapper;
import kg.doit.domain.usecase.UseCase;

public abstract class SingleUseCase<T,Domain, P extends SingleUseCase.Params> extends UseCase {
    protected abstract Single<T> buildUseCaseSingle(P params);

    protected void execute(
        Consumer<Domain> onSuccess,
        Consumer<String> onError,
        Action onFinished,
        BaseMapper<Domain, T> mapper,
        P params
    ) {
        disposeLast();
        lastDisposable = buildUseCaseSingle(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished)
                .subscribe(
                    t -> onSuccess.accept(mapper.mapToDomain(t)),
                    throwable -> onError.accept(throwable.getMessage())
                );
        compositeDisposable.add(lastDisposable);
    }

    protected abstract static class Params {
    }

    protected static class EmptyParams extends Params {
        private static EmptyParams instance;
        private EmptyParams() {

        }
        public static EmptyParams getInstance() {
            if (instance == null) {
                instance = new EmptyParams();
            }
            return instance;
        }
    }
}