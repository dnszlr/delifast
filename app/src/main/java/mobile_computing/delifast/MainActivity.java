package mobile_computing.delifast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;

import mobile_computing.delifast.authentication.AuthenticationActivity;
import mobile_computing.delifast.others.DelifastConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                finish();
                Intent homepage = new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(homepage);
            }
        }, DelifastConstants.TIMEOUT);

    }
}