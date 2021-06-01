package mobilecomputing.delifast.interaction.notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import mobilecomputing.delifast.entities.Notification;
import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.repositories.NotificationRepository;
import mobilecomputing.delifast.repositories.OrderRepository;

public class NotificationViewModel extends ViewModel {

    private NotificationRepository notificationRepository;
    private OrderRepository orderRepository;

    public NotificationViewModel() {
        this.notificationRepository = new NotificationRepository();
        this.orderRepository = new OrderRepository();
    }

    public void save(Notification notification) {
        if (notification != null) {
            notificationRepository.save(notification);
        }
    }

    public MutableLiveData<List<Notification>> getAllByUserId(String userId) {
        return notificationRepository.getAllByUserId(userId);
    }

    public MutableLiveData<Order> getOrder(String orderId){
        return orderRepository.getOrderById(orderId);
    }
}
