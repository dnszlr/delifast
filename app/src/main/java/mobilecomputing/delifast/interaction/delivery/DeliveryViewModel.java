package mobilecomputing.delifast.interaction.delivery;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.repositories.OrderRepository;

public class DeliveryViewModel extends ViewModel {

    private OrderRepository orderRepository;
    private MutableLiveData<ArrayList<Order>> orderList;

    public DeliveryViewModel() {

        this.orderRepository = new OrderRepository();
        orderList = orderRepository.getAll();
    }

    public MutableLiveData<ArrayList<Order>> getOrders() {
        return this.orderList;
    }

    public MutableLiveData<ArrayList<Order>> getAllOrder() {

        return orderRepository.getAll();
    }

    public MutableLiveData<ArrayList<Order>> getAllOrderByRadius(double latitude, double longitude, double radiusInM) {
        Log.d("getAllOrderByRadius", "Iteration test2");
        return orderRepository.getAllByRadius(latitude, longitude, radiusInM);
    }


}
