package mobilecomputing.delifast.interaction.delivery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobilecomputing.delifast.R;

public class DeliveryFragment extends Fragment {

    private DeliveryViewModel viewModel;


    public DeliveryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(DeliveryViewModel.class);
        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {

        });


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery, container, false);
    }
}