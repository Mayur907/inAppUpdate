package inappupdate.updateimmediate.updateflexible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

public class InAppUpdate extends AppCompatActivity {
    private AppUpdateManager appUpdateManager;
    private Type type;
    private String updateType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (extras.containsKey("key")))
            type = (Type) extras.getSerializable("key");

        appUpdateManager = AppUpdateManagerFactory.create(this);

        if (type == Type.IMMEDIATE) {
            updateType = "immediate";
            immediateUpdate();
        } else {
            updateType = "flexible";
            flexibleUpdate();
        }
    }

    /*flexible update*/
    public void flexibleUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                Log.e("inApp", "FLEXIBLE UPDATE_AVAILABLE ");
                startUpdateFlowFlexible(appUpdateInfo);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                Log.e("inApp", "FLEXIBLE DOWNLOADED ");
                popupSnackBarForCompleteUpdate();
            } else {
                Log.e("inApp", "FLEXIBLE else ");
                backToSplash(Activity.RESULT_CANCELED);
            }
        });

        appUpdateInfoTask.addOnFailureListener(e -> {
            Log.e("inApp", "FLEXIBLE error Exception :: " + e.getMessage());
            backToSplash(Activity.RESULT_CANCELED);
        });
    }

    private void startUpdateFlowFlexible(AppUpdateInfo appUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                flexibleActivityResult,
                AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build());
    }


    ActivityResultLauncher<IntentSenderRequest> flexibleActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                // handle callback
                int resultCode = result.getResultCode();
                if (resultCode == RESULT_OK) {
                    Log.e("inApp", "FLEXIBLE Update success! Result Code: " + resultCode);
                } else {
                    Log.e("inApp", "FLEXIBLE Update canceled by user! Result Code: " + resultCode);
                }
                backToSplash(resultCode);
            });

    private void popupSnackBarForCompleteUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("New app is ready, Do you want to install?");
        builder.setCancelable(false);
        builder.setPositiveButton("Install", (dialogInterface, i) -> {
            if (appUpdateManager != null) {
                Log.e("inApp", "FLEXIBLE positive button pressed ");
                appUpdateManager.completeUpdate();
            }
        });
        builder.setNegativeButton("Later", (dialogInterface, i) -> {
            Log.e("inApp", "FLEXIBLE negative button pressed ");
            dialogInterface.dismiss();
            backToSplash(Activity.RESULT_CANCELED);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*immediate update*/
    public void immediateUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                Log.e("inApp", "IMMEDIATE UPDATE_AVAILABLE ");
                startUpdateFlowImmediate(appUpdateInfo);
            } else {
                Log.e("inApp", "IMMEDIATE else ");
                backToSplash(Activity.RESULT_CANCELED);
            }
        });

        appUpdateInfoTask.addOnFailureListener(e -> {
            Log.e("inApp", "IMMEDIATE error Exception :: " + e.getMessage());
            backToSplash(Activity.RESULT_CANCELED);
        });
    }

    private void startUpdateFlowImmediate(AppUpdateInfo appUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                immediateActivityResult,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
    }

    ActivityResultLauncher<IntentSenderRequest> immediateActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                // handle callback
                int resultCode = result.getResultCode();
                if (resultCode == RESULT_OK) {
                    Log.e("inApp", "IMMEDIATE Update success! Result Code: " + resultCode);
                } else {
                    Log.e("inApp", "IMMEDIATE Update canceled by user! Result Code: " + resultCode);
                }
                backToSplash(resultCode);
            });

    private void backToSplash(int resultCode){
        Intent intent = new Intent();
        intent.putExtra("from", updateType);
        setResult(resultCode, intent);
        finish();
    }
}