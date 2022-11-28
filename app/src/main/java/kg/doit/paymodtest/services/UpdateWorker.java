package kg.doit.paymodtest.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import kg.doit.paymodtest.utils.Constants;

public class UpdateWorker extends Worker {

    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("doWork", "doWork: ");
        getApplicationContext().sendBroadcast(new Intent(Constants.UPDATE_TRIGGER_BROADCAST_FILTER));
        return Result.success();
    }
}
