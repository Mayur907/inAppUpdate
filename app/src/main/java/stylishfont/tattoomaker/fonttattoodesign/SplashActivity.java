package stylishfont.tattoomaker.fonttattoodesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import inappupdate.updateimmediate.updateflexible.InAppUpdate;
import inappupdate.updateimmediate.updateflexible.Type;

public class SplashActivity extends AppCompatActivity {

    private static final long COUNTER_TIME = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, InAppUpdate.class);
        intent.putExtra("key", Type.IMMEDIATE);
//        intent.putExtra("key", Type.FLEXIBLE);
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
        Log.e("update", "splash requestCode :: " + requestCode);
        Log.e("update", "splash resultCode :: " + resultCode);
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                createTimer(COUNTER_TIME);
            } else /*resultCode == RESULT_CANCELED*/ {
                if (data != null) {
                    Log.e("update", "splash from :: " + data.getIntExtra("from", 0));
                    /*check return from immediate or flexible
                     * if return from immediate so app close */
                    if (data.getIntExtra("from", 0) == 123456) {
                        finish();
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
