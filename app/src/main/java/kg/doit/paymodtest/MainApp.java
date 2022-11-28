package kg.doit.paymodtest;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;
import kg.doit.domain.dto.AppDto;
import kg.doit.domain.mapper.AppMapper;
import kg.doit.domain.model.App;
import kg.doit.domain.model.AppStatus;
import kg.doit.paymodtest.ui.detail.DetailFragment;
import kg.doit.paymodtest.utils.Constants;

@HiltAndroidApp
public class MainApp extends Application {

    public AppMapper.IAppStatusFind findStatus = MainApp.this::findStatus;

    private final HashMap<Long, App> downloading = new HashMap<>();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            App app = downloading.get(downloadId);
            if (app != null) {
                Intent i = new Intent(Constants.DOWNLOADING_BROADCAST_FILTER);
                i.putExtra(Constants.DOWNLOADING_BROADCAST_APP_KEY, app);
                install(app);
                sendBroadcast(i);
                downloading.remove(downloadId);
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private AppStatus findStatus(AppDto appDto) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(appDto.getType(), 0);
            if (info.versionName.equals(appDto.getVersion())) {
                return AppStatus.ACTUAL;
            } else {
                return AppStatus.NEED_TO_UPDATE;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return findStatusFile(appDto);
        }
    }

    private AppStatus findStatusFile(AppDto appDto) {
        try {
            File downloadFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            boolean check = new File(downloadFile, appDto.getType() + ".apk").exists();

            if (check) {
                return AppStatus.DOWNLOADED;
            }
            return AppStatus.NOT_DOWNLOADED;
        } catch (Exception exception) {
            return AppStatus.NOT_DOWNLOADED;
        }
    }

    public void download(App app) {
        String fileName = app.localFile();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(app.getLink()));
        request.setTitle(fileName);
        request.setDescription("Downloading " + fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setVisibleInDownloadsUi(true);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = manager.enqueue(request);
        downloading.put(downloadId, app);
    }

    public boolean containsDownloading(App app) {
        return downloading.containsValue(app);
    }

    public void install(App app) {
        try {
            File directory = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(directory, app.localFile());

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(
                    FileProvider.getUriForFile(this, getPackageName() + ".provider", file),
                    "application/vnd.android.package-archive"
            );
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(App app) {
        delete(app);
        install(app);
    }

    public void delete(App app) {
        try {
            File directory = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(directory, app.localFile());
            file.deleteOnExit();
            Intent i = new Intent(Constants.DOWNLOADING_BROADCAST_FILTER);
            i.putExtra(Constants.DOWNLOADING_BROADCAST_APP_KEY, app);
            sendBroadcast(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
