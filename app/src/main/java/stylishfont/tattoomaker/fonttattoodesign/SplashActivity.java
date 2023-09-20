package stylishfont.tattoomaker.fonttattoodesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;

import inappupdate.updateimmediate.updateflexible.InAppUpdate;
import inappupdate.updateimmediate.updateflexible.Type;

public class SplashActivity extends AppCompatActivity {

    private static final long COUNTER_TIME = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startInAppUpdateActivity();
    }

    private void startInAppUpdateActivity() {
        Intent intent = new Intent(this, InAppUpdate.class);
        intent.putExtra("key", Type.IMMEDIATE); /* Type.IMMEDIATE, Type.FLEXIBLE */
        /* set custom dialog with only color modification
        * don't change any other in "custom_dialog_layout" */
//        intent.putExtra("layout", R.layout.custom_dialog_layout);
        startActivityForResult(intent, 123);
    }

    private void createTimer(long seconds) {
        CountDownTimer countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                startMainActivity();
            }
        };
        countDownTimer.start();
    }

    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                createTimer(COUNTER_TIME);
            } else /*resultCode == RESULT_CANCELED*/ {
                if (data != null) {
                    /* check return from immediate or flexible
                     * if return from immediate so app close */
                    if (data.getIntExtra("from", 0) == 123456) {
                        finish();
                    } else if(data.getBooleanExtra("update", false)) {
                        createTimer(COUNTER_TIME);
                    } else {
                        createTimer(COUNTER_TIME);
                    }
                } else {
                    createTimer(COUNTER_TIME);
                }
            }
        }
    }
}
