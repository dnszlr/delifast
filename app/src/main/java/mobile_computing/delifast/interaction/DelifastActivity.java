package mobile_computing.delifast.interaction;

import androidx.annotation.FractionRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import mobile_computing.delifast.R;
import mobile_computing.delifast.authentication.AuthenticationActivity;
import mobile_computing.delifast.interaction.delivery.DeliveryFragment;
import mobile_computing.delifast.interaction.notification.NotificationFragment;
import mobile_computing.delifast.interaction.order.OrderFragment;
import mobile_computing.delifast.interaction.profile.ProfileFragment;

public class DelifastActivity extends AppCompatActivity {

    private BottomNavigationView btmNavi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delifast);

        initView();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener naviListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.page_1:
                    selectedFragment = new OrderFragment();
                    break;

                case R.id.page_2:
                    selectedFragment = new DeliveryFragment();
                    break;

                case R.id.page_3:
                    selectedFragment = new NotificationFragment();
                    break;

                case R.id.page_4:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLayout, selectedFragment)
                    .commit();

            return true;
        }
    };

    public void initView() {
        btmNavi = findViewById(R.id.bottomNavigationView);
        btmNavi.setOnNavigationItemSelectedListener(naviListener);
    }


}