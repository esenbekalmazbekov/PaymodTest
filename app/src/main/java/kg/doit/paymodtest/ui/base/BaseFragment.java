package kg.doit.paymodtest.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

import java.util.Objects;

import kg.doit.paymodtest.utils.DialogUtils;

public abstract class BaseFragment <VB extends ViewBinding> extends Fragment {
    private @Nullable VB _binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        _binding = inflateBinding(inflater);

        return _binding.getRoot();
    }

    protected abstract VB inflateBinding(
        LayoutInflater inflater
    );

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    protected abstract void init();

    protected @NonNull VB getBinding() {
        return Objects.requireNonNull(_binding);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }

    protected NavController findNavController() {
        return NavHostFragment.Companion.findNavController(this);
    }

    public String showErrorDialog(String message) {
        DialogUtils.showErrorDialog(this, message,() -> {});
        return message;
    }
}
