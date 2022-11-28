package kg.doit.paymodtest.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kg.doit.domain.dto.AppDto;
import kg.doit.domain.mapper.AppListMapper;
import kg.doit.domain.mapper.AppMapper;
import kg.doit.domain.model.App;
import kg.doit.domain.model.AppStatus;
import kg.doit.domain.repository.AppRepository;
import kg.doit.paymodtest.MainActivity;
import kg.doit.paymodtest.R;
import kg.doit.paymodtest.utils.Constants;

@AndroidEntryPoint
public class UpdateService extends Service {
    private static boolean isRunning = false;

    @Inject
    AppRepository repository;
    @Inject
    AppListMapper mapper;

    private UUID workId;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Disposable disposable = repository.fetchApps()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        dto -> {
                            ArrayList<App> newList = new ArrayList<>();
                            for (App app: mapper.mapToDomain(dto)) {
                                if (app.getStatus() == AppStatus.NEED_TO_UPDATE) {
                                    newList.add(app);
                                }
                            }
                            if (!newList.isEmpty()) {
                                makeNotification(newList);
                            }
                        },
                        Throwable::printStackTrace
                    );
            compositeDisposable.add(disposable);
        }
    };

    private void makeNotification(List<App> app) {
        PendingIntent pendingIntent = createPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(getString(R.string.updates))
                .setContentText(createContent(app))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ripple_submit)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.channel_name);
            String descriptionText = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.default_notification_channel_id),
                    name,
                    importance);
            channel.setDescription(descriptionText);

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat.from(this).notify(1, builder.build());
    }

    private String createContent(@NonNull List<App> apps) {
        StringBuilder builder = new StringBuilder();
        for (App app : apps) {
            builder.append(app.getTitle()).append(" ").append(app.getVersion()).append("\n");
        }
        return builder.toString();
    }

    private PendingIntent createPendingIntent() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            return PendingIntent.getActivity(this, 0, notificationIntent, 0);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String channelId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel("my_service", "My Background Service");
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            channelId = "";
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.Monitoring_app_update))
                .setSmallIcon(R.drawable.btn_shape_installed)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        registerReceiver(receiver, new IntentFilter(Constants.UPDATE_TRIGGER_BROADCAST_FILTER));

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(UpdateWorker.class, 1, TimeUnit.DAYS)
                .build();

        workId = request.getId();
        WorkManager.getInstance(this).enqueue(request);
    }

    @Override
    public void onDestroy() {
        WorkManager.getInstance(this).cancelWorkById(workId);
        unregisterReceiver(receiver);
        compositeDisposable.dispose();

        super.onDestroy();
    }

    public static boolean isRunning() {
        return isRunning;
    }
}
