package kg.doit.domain.usecase;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class UseCase {
    protected Disposable lastDisposable;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void disposeLast() {
        if (lastDisposable != null) {
            if (!lastDisposable.isDisposed()) {
                lastDisposable.isDisposed();
            }
        }
    }

    public void dispose() {
        compositeDisposable.clear();
    }
}