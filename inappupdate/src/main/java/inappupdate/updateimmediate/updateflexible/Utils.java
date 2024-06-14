package inappupdate.updateimmediate.updateflexible;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {
    public interface UpdateStatusCallback {
        void onUpdateStatusReceived(boolean status);
    }
    public static void updateAvailable(Context context, UpdateStatusCallback callback) {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            boolean status = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE || appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED;
            callback.onUpdateStatusReceived(status);
        });

        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onUpdateStatusReceived(false);
            }
        });

        appUpdateInfoTask.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                callback.onUpdateStatusReceived(false);
            }
        });
    }
}
