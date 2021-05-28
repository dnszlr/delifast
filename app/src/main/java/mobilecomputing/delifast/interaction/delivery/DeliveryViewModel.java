package mobilecomputing.delifast.interaction.delivery;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.entities.User;
import mobilecomputing.delifast.repositories.OrderRepository;
import mobilecomputing.delifast.repositories.UserRepository;

public class DeliveryViewModel extends ViewModel {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private MutableLiveData<ArrayList<Order>> orderList;

    public DeliveryViewModel() {

        this.orderRepository = new OrderRepository();
        this.userRepository = new UserRepository();
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

    public MutableLiveData<User> getUserByID(String userID) {
        return userRepository.findById(userID);
    }

}
