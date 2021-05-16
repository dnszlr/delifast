package mobile_computing.delifast.authentication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {
    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new LoginFragment();
            case 1: return new RegisterFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
