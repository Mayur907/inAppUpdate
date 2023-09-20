package inappupdate.updateimmediate.updateflexible;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;

public class InAppUpdate extends AppCompatActivity {
    private static final int RC_APP_UPDATE_IMMEDIATE = 123456;
    private static final int RC_APP_UPDATE_FLEXIBLE = 654321;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private Type type;
    private int layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.e("update", "onCreate ");
        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (extras.containsKey("key")))
            type = (Type) extras.getSerializable("key");

//        if ((extras != null) && (extras.containsKey("layout")))
//            layout = extras.getInt("layout");
//        else
//            layout = R.layout.custom_dialog_layout;

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
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                removeInstallStateUpdateListener();
            } else {
                Toast.makeText(getApplicationContext(), "InstallStateUpdatedListener: state: " + state.installStatus(),
                        Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        };
        appUpdateManager.registerListener(installStateUpdatedListener);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlowFlexible(appUpdateInfo);
//                Log.e("update", " UPDATE_AVAILABLE ");
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
//                Log.e("update", " DOWNLOADED ");
            } else {
//                Log.e("update", " else fail ");
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
//            Log.e("update", " addOnSuccessListener ");
        });

        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
//                Log.e("update", " addOnFailureListener ");
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
//                Log.e("update", " addOnFailureListener ");
            }
        });
    }

    private void startUpdateFlowFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, RC_APP_UPDATE_FLEXIBLE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void popupSnackBarForCompleteUpdate() {
        /*custom dialog*/
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CommonDialogStyle);
//        View view = LayoutInflater.from(this).inflate(layout, null);
//        builder.setView(view);
//
//        Dialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.show();
//
//        TextView negText = dialog.findViewById(R.id.txt_neg);
//        TextView posText = dialog.findViewById(R.id.txt_pos);
//
//        negText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                dialog.dismiss();
//            }
//        });
//
//        posText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (appUpdateManager != null) {
//                    appUpdateManager.completeUpdate();
//                }
//            }
//        });

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
                finish();
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        /*show snack bar*/
//        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!", Snackbar.LENGTH_INDEFINITE)
//                .setAction("Install", view -> {
//                    if (appUpdateManager != null) {
//                        appUpdateManager.completeUpdate();
//                    }
//                })
//                .setActionTextColor(getResources().getColor(android.R.color.white))
//                .show();
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (type == Type.FLEXIBLE) {
            removeInstallStateUpdateListener();
        }
    }

    @Override
    public void onBackPressed() {
        if (type == Type.FLEXIBLE) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra("from", RC_APP_UPDATE_IMMEDIATE);
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
        }
    }

    /*immediate update*/
    public void immediateUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                Log.e("update", " UPDATE_AVAILABLE ");
                startUpdateFlowImmediate(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                Log.e("update", " DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS ");
                startUpdateFlowImmediate(appUpdateInfo);
            } else {
//                Log.e("update", " else fail ");
                Intent intent = new Intent();
                intent.putExtra("update", true);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
//            Log.e("update", " addOnSuccessListener ");
        });

        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
//                Log.e("update", " addOnFailureListener ");
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void startUpdateFlowImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, RC_APP_UPDATE_IMMEDIATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
//            Log.e("update", " error :: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("update", "inapp requestCode :: " + requestCode);
//        Log.e("update", "inapp resultCode :: " + resultCode);
        if (requestCode == RC_APP_UPDATE_IMMEDIATE || requestCode == RC_APP_UPDATE_FLEXIBLE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
//                Log.e("update", " cancel ");
                Intent intent = new Intent();
                intent.putExtra("from", requestCode);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
//                Log.e("update", " ok ");
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
//                Log.e("update", " else ");
                checkType(type);
            }
        }
    }
}
