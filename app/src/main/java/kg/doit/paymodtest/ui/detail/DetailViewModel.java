package kg.doit.paymodtest.ui.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kg.doit.domain.state.State;
import kg.doit.paymodtest.usecase.GetDetailAppUseCase;

@HiltViewModel
public class DetailViewModel extends ViewModel {

    private final GetDetailAppUseCase getAllAppsUseCase;
    private final String type;
    final MutableLiveData<State> state = new MutableLiveData<>();

    @Inject
    public DetailViewModel(
            GetDetailAppUseCase getAllAppsUseCase,
            SavedStateHandle savedStateHandle
    ) {
        this.getAllAppsUseCase = getAllAppsUseCase;

        type = savedStateHandle.get("type");
        getDetail();
    }

    public void getDetail() {
        state.setValue(new State.Loading(true));
        getAllAppsUseCase.executeDomain(
            domain -> state.setValue(new State.Success<>(domain)),
            throwable -> state.setValue(new State.Error(throwable)),
            () -> state.setValue(new State.Loading(false)),
            type
        );
    }

    public void updateByType(String type) {
        if (this.type.equals(type)) {
            getDetail();
        }
    }

    @Override
    protected void onCleared() {
        getAllAppsUseCase.dispose();
        super.onCleared();
    }
}