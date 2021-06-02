package mobilecomputing.delifast.interaction.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import mobilecomputing.delifast.authentication.AuthenticationViewModel;
import mobilecomputing.delifast.delifastEnum.NotificationType;
import mobilecomputing.delifast.delifastEnum.OrderStatus;
import mobilecomputing.delifast.entities.Notification;
import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.entities.Rating;
import mobilecomputing.delifast.entities.User;
import mobilecomputing.delifast.repositories.NotificationRepository;
import mobilecomputing.delifast.repositories.OrderRepository;
import mobilecomputing.delifast.repositories.RatingRepository;
import mobilecomputing.delifast.repositories.UserRepository;

public class ProfileViewModel extends ViewModel {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private RatingRepository ratingRepository;
    private NotificationRepository notificationRepository;
    private MutableLiveData<ArrayList<Order>> deliveries;
    private MutableLiveData<ArrayList<Order>> orders;
    private MutableLiveData<User> currentUser;
    private MutableLiveData<List<Rating>> ratings;

    public ProfileViewModel() {
        this.orderRepository = new OrderRepository();
        this.userRepository = new UserRepository();
        this.ratingRepository = new RatingRepository();
        this.notificationRepository = new NotificationRepository();
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
        orderRepository.getAllBySupplierId(supplierId);
    }

    public MutableLiveData<User> getUserById(String userId) {
        return userRepository.getById(userId);
    }

    public MutableLiveData<Order> getOrderById(String orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public MutableLiveData<List<Rating>> getAllUserRatings(String userId) {
        return ratingRepository.getAllByUserId(userId);
    }

    public void updateOrder(Order order) {
        if (order != null) {
            orderRepository.update(order);
        }
    }

    public void cancelDelivery(Order order) {
        if (order != null) {
            order.setOrderStatus(OrderStatus.OPEN);
            order.setSupplierID(null);
            orderRepository.update(order);
            Notification notification = new Notification(order.getCustomerID(), order.getId(), NotificationType.ORDER_CANCELED_BY_SUPPLIER);
            notificationRepository.save(notification);
        }
    }


    public void createTransactionNotifications(Order order) {
        Notification customerNotification = new Notification(order.getCustomerID(), order.getId(), NotificationType.RATING_CUSTOMER);
        Notification supplierNotification = new Notification(order.getSupplierID(), order.getId(), NotificationType.RATING_SUPPLIER);
        Notification supplierNotification2 = new Notification(order.getSupplierID(), order.getId(), NotificationType.ORDER_DONE_BY_CUSTOMER);
        notificationRepository.save(customerNotification);
        notificationRepository.save(supplierNotification);
        notificationRepository.save(supplierNotification2);
    }
}
