package mobile_computing.delifast.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import mobile_computing.delifast.R;

public class AuthenticationActivity extends AppCompatActivity {

    TabLayout authTable;
    ViewPager2 authViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        initFragments();
    }

    protected void initFragments() {
        authTable = findViewById(R.id.authTable);
        authViewpager = findViewById(R.id.authViewpager);
        authViewpager.setAdapter(new TabAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(authTable, authViewpager, new TabConfigurationStrategy());
        tabLayoutMediator.attach();


    }
}