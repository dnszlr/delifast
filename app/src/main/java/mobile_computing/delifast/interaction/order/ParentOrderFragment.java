package mobile_computing.delifast.interaction.order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import mobile_computing.delifast.R;


public class ParentOrderFragment extends Fragment {


    private ViewPager viewPagerParentOrder;
    private TabLayout tabParentOrder;

    public ParentOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentOrderView = inflater.inflate(R.layout.fragment_parent_order, container, false);

        viewPagerParentOrder = parentOrderView.findViewById(R.id.viewPagerParentOrder);
        initViewPager(viewPagerParentOrder);

        tabParentOrder = parentOrderView.findViewById(R.id.tabParentOrder);
        tabParentOrder.setupWithViewPager(viewPagerParentOrder);


        return parentOrderView;
    }

    private void initViewPager(ViewPager viewPagerParentOrder) {
        ParentOrderAdapter adapter = new ParentOrderAdapter(getChildFragmentManager());
        adapter.addFragment(new OrderFragment(), "Produkte");
        adapter.addFragment(new OrderFragment(), "Warenkorb");
        viewPagerParentOrder.setAdapter(adapter);
    }

    static class ParentOrderAdapter extends FragmentPagerAdapter{

        private final List<Fragment> orderFragmentsList = new ArrayList<>();
        private final List<String> orderFragmentsTitles = new ArrayList<>();

        public ParentOrderAdapter(@NonNull FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return orderFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return orderFragmentsList.size();
        }

        public void addFragment(Fragment fragment, String title){
            orderFragmentsList.add(fragment);
            orderFragmentsTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return orderFragmentsTitles.get(position);
        }
    }
}