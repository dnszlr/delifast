package mobile_computing.delifast.authentication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import mobile_computing.delifast.R;
import mobile_computing.delifast.others.DelifastConstants;

public class AuthenticationActivity extends AppCompatActivity {

    TabLayout authTable;
    ViewPager2 authViewpager;

    private FirebaseAuth mAuth;

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