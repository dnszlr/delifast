package mobile_computing.delifast.interaction.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import mobile_computing.delifast.R;
import mobile_computing.delifast.entities.Order;
import mobile_computing.delifast.entities.Product;


public class OrderFragment extends Fragment {

    private ListView productsList;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View orderView = inflater.inflate(R.layout.fragment_order, container, false);

        initView(orderView);

        ArrayList<Product> products = new ArrayList<>();

        //TODO: Add all Products of the DB in the array




        ProductAdapter adapter = new ProductAdapter(getActivity(), R.layout.fragment_order_adapter, products);
        productsList.setAdapter(adapter);



        // Inflate the layout for this fragment
        return orderView;
    }

    private void initView(View orderView) {
        productsList = orderView.findViewById(R.id.listViewOrder);
    }
}