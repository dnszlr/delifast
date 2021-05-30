package mobilecomputing.delifast.interaction.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;

import mobilecomputing.delifast.R;
import mobilecomputing.delifast.entities.OrderPosition;


public class OrderFragment extends Fragment {

    private OrderViewModel model;
    private ListView productsList;
    private TextInputEditText filter;
    private OrderPositionAdapter orderPositionAdapter;

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
                Log.d("Observer", "I'm observing this amount of orderPositions: " + orderPositions.size());
                orderPositionAdapter = new OrderPositionAdapter(getActivity(), R.layout.fragment_order_adapter, orderPositions, this);
                productsList.setAdapter(orderPositionAdapter);
            }
        });
        setUpFilter(orderView);
        // Inflate the layout for this fragment
        return orderView;
    }

    /**
     * Initializes all view components
     *
     * @param orderView
     */
    private void initView(View orderView) {
        productsList = orderView.findViewById(R.id.listViewOrder);
    }

    private void setUpFilter(View orderView) {
        filter = orderView.findViewById(R.id.etFilter);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (orderPositionAdapter != null) {
                    orderPositionAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Calls ViewModel method to de or increase the amount value of the OrderPosition
     *
     * @param orderPosition the changed OrderPosition
     */
    public void editOrderPositionsInViewModel(OrderPosition orderPosition) {
        model.changeCountOfOrderPosition(orderPosition);
    }

}
