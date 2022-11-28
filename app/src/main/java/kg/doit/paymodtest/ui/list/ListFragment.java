package kg.doit.paymodtest.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import dagger.hilt.android.AndroidEntryPoint;
import kg.doit.domain.model.App;
import kg.doit.domain.state.State;
import kg.doit.domain.state.State.Success;
import kg.doit.paymodtest.databinding.FragmentListBinding;
import kg.doit.paymodtest.ui.base.BaseFragment;
import kg.doit.paymodtest.ui.list.adapter.ApplicationAdapter;

@AndroidEntryPoint
public class ListFragment extends BaseFragment<FragmentListBinding> {

    private final ApplicationAdapter adapter = new ApplicationAdapter(this::openDetailPage);
    private ListViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
    }

    @Override
    protected FragmentListBinding inflateBinding(LayoutInflater inflater) {
        return FragmentListBinding.inflate(inflater);
    }

    @Override
    protected void init() {
        iniAdapter();
        initListener();
        initObserver();
    }

    private void initListener() {
        getBinding().swipeRefresh.setOnRefreshListener(viewModel::getAllList);
    }

    private void iniAdapter() {
        getBinding().rvApps.setAdapter(adapter);
    }

    private void initObserver() {
        viewModel.state.observe(getViewLifecycleOwner(), listState -> {
            if (listState instanceof Success &&
                    ((Success<?>) listState).getValue() instanceof List) {
                adapter.submitList(((Success<List<App>>) listState).getValue());
            } else if (listState instanceof State.Error) {
                showErrorDialog(((State.Error) listState).getMessage());
            } else if (listState instanceof State.Loading) {
                getBinding().swipeRefresh.setRefreshing(((State.Loading) listState).isLoading());
            }
        });
    }

    private void openDetailPage(String type) {
        findNavController().navigate(
            ListFragmentDirections.actionListFragmentToDetailFragment(type)
        );
    }
}