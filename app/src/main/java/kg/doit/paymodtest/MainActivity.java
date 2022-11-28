package kg.doit.paymodtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import dagger.hilt.android.AndroidEntryPoint;
import kg.doit.paymodtest.services.UpdateService;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        if (!UpdateService.isRunning()) {
            Intent intent = new Intent(this, UpdateService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }
    }
}