package mobilecomputing.delifast.interaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import mobilecomputing.delifast.authentication.AuthenticationViewModel;
import mobilecomputing.delifast.interaction.delivery.DeliveryFragment;
import mobilecomputing.delifast.interaction.notification.NotificationFragment;
import mobilecomputing.delifast.interaction.profile.ProfileFragment;
import mobilecomputing.delifast.R;
import mobilecomputing.delifast.interaction.order.ParentOrderFragment;
import mobilecomputing.delifast.interaction.profile.ProfileViewModel;

public class DelifastActivity extends AppCompatActivity {

    private BottomNavigationView btmNavi;

    private int savedNaviState;

    private DelifastViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delifast);
        initView();

        viewModel = new ViewModelProvider(this).get(DelifastViewModel.class);

        if(savedInstanceState==null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLayout, new ParentOrderFragment())
                    .commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener naviListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.page_1:
                    //selectedFragment = new ParentOrderFragment();
                    selectedFragment = viewModel.getOrderFragment();
                    break;

                case R.id.page_2:
                    selectedFragment = viewModel.getDeliveryFragment();
                    break;

                case R.id.page_3:
                    selectedFragment = viewModel.getNotificationFragment();
                    break;

                case R.id.page_4:
                    selectedFragment = viewModel.getProfileFragment();
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
        //naviListener.onNavigationItemSelected(btmNavi.getMenu().getItem(0));
    }


}