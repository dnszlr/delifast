package mobile_computing.delifast.interaction.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import mobile_computing.delifast.R;
import mobile_computing.delifast.entities.OrderPosition;
import mobile_computing.delifast.entities.Product;


public class OrderFragment extends Fragment {

    private OrderViewModel model;
    private ArrayList<Product> products;
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
        model.getAllPositions().observe(getViewLifecycleOwner(), dbOrderPositions -> {

            if (dbOrderPositions != null) {
                Log.d("Observer", "im observing this amount of products: " + dbOrderPositions.size());
                OrderPositionAdapter orderPositionAdapter = new OrderPositionAdapter(getActivity(), R.layout.fragment_order_adapter, dbOrderPositions, this);
                productsList.setAdapter(orderPositionAdapter);
            }
        });
        // Inflate the layout for this fragment
        return orderView;
    }

    private void initView(View orderView) {
        productsList = orderView.findViewById(R.id.listViewOrder);
    }

    public void editOrderPositionsInViewModel(int orderPosition){
        model.changeCountOfOrderPosition(orderPosition);

    }

}
