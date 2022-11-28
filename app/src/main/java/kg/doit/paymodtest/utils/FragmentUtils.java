package kg.doit.paymodtest.utils;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class FragmentUtils {
    public static ActivityResultLauncher<String> requestPermission (Fragment fragment, PermissionListener canceled, PermissionListener succeed) {
        return fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) { succeed.change(); }
            else { canceled.change(); }
        });
    }

    public static boolean hasReadStoragePermission(Fragment fragment) {
        return ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasWriteStoragePermission(Fragment fragment) {
        return ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public interface PermissionListener {
        void change();
    }
}
