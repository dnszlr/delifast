package mobilecomputing.delifast.authentication;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TabConfigurationStrategy implements TabLayoutMediator.TabConfigurationStrategy {
    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        switch (position){
            case 0: tab.setText("Login");
            break;
            case 1: tab.setText("Registrieren");
        }

    }
}
