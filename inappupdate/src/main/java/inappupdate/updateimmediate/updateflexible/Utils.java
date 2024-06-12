package inappupdate.updateimmediate.updateflexible;

import android.content.Context;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

public class Utils {
    private static boolean result = false;
    public static boolean isUpdateAvailable(Context context){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                result = true;
            }
        });
        return result;
    }
}
