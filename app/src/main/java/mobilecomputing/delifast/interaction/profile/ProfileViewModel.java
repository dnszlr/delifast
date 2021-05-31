package mobilecomputing.delifast.interaction.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.entities.Rating;
import mobilecomputing.delifast.entities.User;
import mobilecomputing.delifast.repositories.OrderRepository;
import mobilecomputing.delifast.repositories.RatingRepository;
import mobilecomputing.delifast.repositories.UserRepository;

public class ProfileViewModel extends ViewModel {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private RatingRepository ratingRepository;
    private MutableLiveData<ArrayList<Order>> deliveries;
    private MutableLiveData<ArrayList<Order>> orders;
    private MutableLiveData<User> currentUser;
    private MutableLiveData<List<Rating>> ratings;

    public ProfileViewModel() {
        this.orderRepository = new OrderRepository();
        this.userRepository = new UserRepository();
        this.ratingRepository = new RatingRepository();
        this.orders = orderRepository.getOrderList();
        this.deliveries = orderRepository.getOrderList();
        this.currentUser = new MutableLiveData<>();
        this.ratings = new MutableLiveData<>();

    }

    public MutableLiveData<ArrayList<Order>> getOrders() {
        return orderRepository.getOrderList();
    }

    public void getAllByCustomerId(String customerId) {
        orderRepository.getAllByCustomerId(customerId);
    }

    public void getAllBySupplierId(String supplierId) {
        orderRepository.getAllByCustomerId(supplierId);
    }

    public MutableLiveData<User> getUserById(String userId) {
        return userRepository.getById(userId);
    }

    public MutableLiveData<List<Rating>> getAllUserRatings(String userId) {
        return ratingRepository.getAllByUserId(userId);
    }

    public void updateOrder(Order order) {
        if (order != null) {
            orderRepository.update(order);
        }
    }


}
