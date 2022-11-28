package kg.doit.paymodtest.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import kg.doit.paymodtest.R;
import kg.doit.paymodtest.ui.detail.DetailFragment;

public class DialogUtils {
    @SuppressLint("InflateParams")
    public static void showErrorDialog(
        Fragment fragment,
        String text,
        OnOkClicked onOkClicked
    ) {
        Dialog dialog = new Dialog(fragment.requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = dialog.getLayoutInflater().inflate(R.layout.dialog_error, null);

        TextView textView = view.findViewById(R.id.tv_title);
        textView.setText(text);

        AppCompatButton btn = view.findViewById(R.id.btn_submit);
        btn.setOnClickListener(view1 -> {
            onOkClicked.clicked();
            dialog.dismiss();
        });

        dialog.setContentView(view);

        dialog.setCancelable(true);
        Window wd = dialog.getWindow();
        wd.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wd.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        wd.setGravity(Gravity.CENTER);
        wd.setBackgroundDrawable(new ColorDrawable(0));

        dialog.show();
    }

    public static void openPermissionSettings(Fragment fragment) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + fragment.requireActivity().getPackageName())
        );
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        fragment.startActivity(intent);
    }

    public interface OnOkClicked {
        void clicked();
    }
}
