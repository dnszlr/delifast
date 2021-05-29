package mobilecomputing.delifast.interaction.delivery;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import mobilecomputing.delifast.delifastEnum.OrderStatus;
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
        orderList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Order>> getOrderList() {
        return this.orderList;
    }

    public MutableLiveData<ArrayList<Order>> getAllOrder() {

        return orderRepository.getAll();
    }

    public void getAllOrderByRadius(double latitude, double longitude, double radiusInM) {
        Log.d("getAllOrderByRadius", "Iteration test");
        MutableLiveData<ArrayList<Order>> updatedOrderList = orderRepository.getAllByRadius(latitude, longitude, radiusInM);
        if(updatedOrderList.getValue() != null){
            orderList.setValue(updatedOrderList.getValue());
        }
    }

    public MutableLiveData<User> getUserByID(String userID) {
        return userRepository.findById(userID);
    }

    public void setOrderAcceptedinDB(Order order, String uid) {
        ArrayList<Order> updatedOrderList = this.orderList.getValue();
        updatedOrderList.remove(order);

        order.setSupplierID(uid);
        order.setOrderStatus(OrderStatus.ACCEPTED);
        orderRepository.update(order);

        this.orderList.setValue(updatedOrderList);
    }
}
