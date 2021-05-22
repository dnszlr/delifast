package mobile_computing.delifast.interaction.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

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
    private ListView productsList;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View orderView = inflater.inflate(R.layout.fragment_order, container, false);

        initView(orderView);
        model = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        model.getAll().observe(getViewLifecycleOwner(), dbPositions -> {
            if (dbPositions != null) {
                Log.d("Observer", "I'm observing this amount of products: " + dbPositions.size());
                model.updateOrderPositionList(dbPositions);
            }
        });

        model.getOrderPositionList().observe(getViewLifecycleOwner(), orderPositions -> {
            if (orderPositions != null) {
                OrderPositionAdapter orderPositionAdapter = new OrderPositionAdapter(getActivity(), R.layout.fragment_order_adapter, orderPositions, this);
                productsList.setAdapter(orderPositionAdapter);
            }
        });
        // Inflate the layout for this fragment
        return orderView;
    }

    private void initView(View orderView) {
        productsList = orderView.findViewById(R.id.listViewOrder);
    }

    public void editOrderPositionsInViewModel(OrderPosition orderPosition) {
        model.changeCountOfOrderPosition(orderPosition);
    }

}
