package kg.doit.paymodtest.ui.detail;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import kg.doit.domain.model.App;
import kg.doit.domain.state.State;
import kg.doit.paymodtest.MainApp;
import kg.doit.paymodtest.R;
import kg.doit.paymodtest.databinding.FragmentDetailBinding;
import kg.doit.paymodtest.ui.base.BaseFragment;
import kg.doit.paymodtest.utils.Constants;
import kg.doit.paymodtest.utils.DialogUtils;
import kg.doit.paymodtest.utils.FragmentUtils;
import kg.doit.paymodtest.utils.ViewUtils;

@AndroidEntryPoint
public class DetailFragment extends BaseFragment<FragmentDetailBinding> {
    private DetailViewModel viewModel;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            App app;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                app = intent.getSerializableExtra(Constants.DOWNLOADING_BROADCAST_APP_KEY, App.class);
            } else {
                app = (App) intent.getSerializableExtra(Constants.DOWNLOADING_BROADCAST_APP_KEY);
            }
            viewModel.updateByType(app.getType());
        }
    };

    private final ActivityResultLauncher<String> launcher = FragmentUtils.requestPermission(
            this,
            () -> DialogUtils.showErrorDialog(this,
                showErrorDialog(getString(R.string.Need_to_get_storage_permission)),
                () -> DialogUtils.openPermissionSettings(this)
            ),
            () -> getBinding().btnSubmit.performClick()
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        requireActivity().registerReceiver(receiver, new IntentFilter(Constants.DOWNLOADING_BROADCAST_FILTER));
    }

    @Override
    public void onDestroy() {
        requireActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected FragmentDetailBinding inflateBinding(LayoutInflater inflater) {
        return FragmentDetailBinding.inflate(inflater);
    }

    @Override
    protected void init() {
        initObserver();
        initListener();
    }

    private void initListener() {
        getBinding().swipeRefresh.setOnRefreshListener(viewModel::getDetail);
        getBinding().materialToolbar.setNavigationOnClickListener(this::navigateUp);
    }

    private void navigateUp(View view) {
        findNavController().navigateUp();
    }

    private void initObserver() {
        viewModel.state.observe(getViewLifecycleOwner(), state -> {
            if (state instanceof State.Success &&
                    ((State.Success<?>) state).getValue() instanceof App) {
                setData((App) ((State.Success<?>) state).getValue());
            } else if (state instanceof State.Error) {
                showErrorDialog(((State.Error) state).getMessage());
            } else if (state instanceof State.Loading) {
                getBinding().swipeRefresh.setRefreshing(((State.Loading) state).isLoading());
            }
        });
    }

    private void setData(App value) {
        ViewUtils.setGlideImage(getBinding().ivImage, value.getLogo200Link());
        getBinding().materialToolbar.setTitle(value.getTitle());
        getBinding().tvDescription.setText(value.getDescription());
        getBinding().tvVersion.setText(value.getVersion());
        getBinding().tvType.setText(value.getType());
        initBtns(value);
    }

    private void initBtns(App app) {
        AppCompatButton btn = getBinding().btnSubmit;
        AppCompatButton deleteBtn = getBinding().btnDelete;
        switch (app.getStatus()) {
            case ACTUAL : {
                btn.setBackgroundResource(R.drawable.btn_shape_installed);
                btn.setTextColor(ContextCompat.getColor(btn.getContext(), R.color.blue_344d67));
                btn.setText(R.string.Actual);
                btn.setEnabled(false);
                deleteBtn.setOnClickListener(view -> delete(app));
                deleteBtn.setVisibility(View.VISIBLE);
                break;
            }
            case DOWNLOADED : {
                btn.setBackgroundResource(R.drawable.ripple_submit);
                btn.setTextColor(ContextCompat.getColor(btn.getContext(), R.color.white_f3ecb0));
                btn.setText(R.string.Install);
                btn.setEnabled(true);
                btn.setOnClickListener(view -> install(app));
                deleteBtn.setVisibility(View.GONE);
                break;
            }
            case NEED_TO_UPDATE: {
                btn.setBackgroundResource(R.drawable.ripple_btn_need_to_update);
                btn.setTextColor(ContextCompat.getColor(btn.getContext(), R.color.white_f3ecb0));
                btn.setText(R.string.Update);
                btn.setEnabled(true);
                btn.setOnClickListener(view -> update(app));
                deleteBtn.setOnClickListener(view -> delete(app));
                deleteBtn.setVisibility(View.VISIBLE);
                break;
            }
            case NOT_DOWNLOADED: {
                btn.setBackgroundResource(R.drawable.ripple_download);
                btn.setTextColor(ContextCompat.getColor(btn.getContext(), R.color.blue_344d67));
                deleteBtn.setVisibility(View.GONE);
                initBtnByNotDownload(app);
                break;
            }
        }
    }

    private void delete(App app) {
        Context context = requireContext().getApplicationContext();
        if (context instanceof MainApp) {
            MainApp mainContext = (MainApp) context;
            mainContext.delete(app);
        }
    }

    private void update(App app) {
        Context context = requireContext().getApplicationContext();
        if (context instanceof MainApp) {
            MainApp mainContext = (MainApp) context;
            mainContext.update(app);
            initBtnByNotDownload(app);
        }
    }

    private void install(App app) {
        Context context = requireContext().getApplicationContext();
        if (context instanceof MainApp) {
            MainApp mainContext = (MainApp) context;
            mainContext.install(app);
        }
    }

    private void initBtnByNotDownload(App app) {
        Context context = requireContext().getApplicationContext();
        AppCompatButton btn = getBinding().btnSubmit;
        if (context instanceof MainApp) {
            MainApp mainContext = (MainApp) context;
            boolean isDownloading = mainContext.containsDownloading(app);
            btn.setEnabled(!isDownloading);
            if (isDownloading) {
                btn.setText(R.string.Downloading);
            } else {
                btn.setText(R.string.Download);
                btn.setOnClickListener(view -> download(app));
            }
        }
    }

    private void download(App app) {
        if (!FragmentUtils.hasReadStoragePermission(this)) {
            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }
        if (!FragmentUtils.hasWriteStoragePermission(this)) {
            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return;
        }

        Context context = requireContext().getApplicationContext();
        if (context instanceof MainApp) {
            MainApp mainContext = (MainApp) context;
            mainContext.download(app);
        }
        initBtnByNotDownload(app);
    }
}