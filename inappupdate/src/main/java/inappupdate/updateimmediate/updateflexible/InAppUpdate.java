package inappupdate.updateimmediate.updateflexible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (extras.containsKey("key")))
            type = (Type) extras.getSerializable("key");

        appUpdateManager = AppUpdateManagerFactory.create(this);
        checkType(type);
    }

    private void checkType(Type type) {
        if (type == Type.IMMEDIATE) {
            immediateUpdate();
        } else {
            flexibleUpdate();
        }
    }

    /*flexible update*/
    public void flexibleUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlowFlexible(appUpdateInfo);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("inApp", "FLEXIBLE error Exception :: " + e.getMessage());
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
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
    new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // handle callback
            int resultCode = result.getResultCode();
            if (resultCode != RESULT_OK) {
                Log.e("inApp", "Update canceled by user! Result Code: " + resultCode);
            } else {
                Log.e("inApp", "Update success! Result Code: " + resultCode);
            }
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    });

    private void popupSnackBarForCompleteUpdate() {
        /*android default dialog*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("New app is ready, Do you want to install?");
        builder.setCancelable(false);
        builder.setPositiveButton("Install", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (appUpdateManager != null) {
                    appUpdateManager.completeUpdate();
                }
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                dialogInterface.dismiss();
            }
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
                startUpdateFlowImmediate(appUpdateInfo);
            } else {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("inApp", "IMMEDIATE error Exception :: " + e.getMessage());
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
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
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // handle callback
                    int resultCode = result.getResultCode();
                    Intent intent = new Intent();
                    if (resultCode != RESULT_OK) {
                        Log.e("inApp", "Update canceled by user! Result Code: " + resultCode);
                        intent.putExtra("isImmediate", true);
                    } else {
                        Log.e("inApp", "Update success! Result Code: " + resultCode);
                    }
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });

}