package mobilecomputing.delifast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;

import mobilecomputing.delifast.others.DelifastConstants;
import mobilecomputing.delifast.R;
import mobilecomputing.delifast.authentication.AuthenticationActivity;

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