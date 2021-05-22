package mobile_computing.delifast.interaction.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mobile_computing.delifast.R;
import mobile_computing.delifast.delifastEnum.ProductCategory;
import mobile_computing.delifast.entities.Order;
import mobile_computing.delifast.entities.Product;
import mobile_computing.delifast.authentication.AuthenticationViewModel;
import mobile_computing.delifast.entities.Product;


public class OrderFragment extends Fragment {

    private OrderViewModel model;
    private ListView productsList;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View orderView = inflater.inflate(R.layout.fragment_order, container, false);

        initView(orderView);
        model = new ViewModelProvider(this).get(OrderViewModel.class);
        model.getAll().observe(getViewLifecycleOwner(), dbProducts -> {
            if (dbProducts != null) {
                Log.d("Observer", "Observing this amount of products: " + dbProducts.size());
                ProductAdapter productAdapter = new ProductAdapter(getActivity(), R.layout.fragment_order_adapter, dbProducts);
                productsList.setAdapter(productAdapter);
            }
        });



        // Inflate the layout for this fragment
        return orderView;
    }

    private void initView(View orderView) {
        productsList = orderView.findViewById(R.id.listViewOrder);
    }
}
