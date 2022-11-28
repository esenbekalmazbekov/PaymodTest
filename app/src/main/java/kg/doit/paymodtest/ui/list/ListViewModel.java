package kg.doit.paymodtest.ui.list;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kg.doit.domain.state.State;
import kg.doit.paymodtest.usecase.GetAllAppsUseCase;

@HiltViewModel
public class ListViewModel extends ViewModel {

    private final GetAllAppsUseCase useCase;
    final MutableLiveData<State> state = new MutableLiveData<>();

    @Inject
    public ListViewModel(GetAllAppsUseCase getAllAppsUseCase) {
        this.useCase = getAllAppsUseCase;

        getAllList();
    }

     public void getAllList() {
        state.postValue(new State.Loading(true));
        useCase.executeDomain(
                domain -> state.setValue(new State.Success<>(domain)),
                throwable -> state.setValue(new State.Error(throwable)),
                () -> state.setValue(new State.Loading(false))
        );
    }

    @Override
    protected void onCleared() {
        useCase.dispose();
        super.onCleared();
    }
}