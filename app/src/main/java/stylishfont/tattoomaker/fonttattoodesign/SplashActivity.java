package stylishfont.tattoomaker.fonttattoodesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        intent.putExtra("key", Type.FLEXIBLE); /* Type.IMMEDIATE, Type.FLEXIBLE */
        inAppActivity.launch(intent);
    }

    private void createTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(COUNTER_TIME * 1000, 1000) {
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


    ActivityResultLauncher<Intent> inAppActivity = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // handle callback
            Intent data = result.getData();
            if (data != null && data.getBooleanExtra("isImmediate", false)) {
                /* if isImmediate is true so app close */
                finish();
            } else {
                createTimer();
            }
        }
    });
}
