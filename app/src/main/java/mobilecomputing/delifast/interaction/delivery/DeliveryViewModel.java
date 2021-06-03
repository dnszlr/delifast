package mobilecomputing.delifast.interaction.delivery;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mapbox.geojson.Point;

import java.util.ArrayList;

import mobilecomputing.delifast.delifastEnum.NotificationType;
import mobilecomputing.delifast.delifastEnum.OrderStatus;
import mobilecomputing.delifast.entities.LocationHelper;
import mobilecomputing.delifast.entities.Notification;
import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.entities.User;
import mobilecomputing.delifast.repositories.NotificationRepository;
import mobilecomputing.delifast.repositories.OrderRepository;
import mobilecomputing.delifast.repositories.UserRepository;

public class DeliveryViewModel extends ViewModel {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private MutableLiveData<ArrayList<Order>> orderList;
    private MutableLiveData<LocationHelper> locationHelper;

    public DeliveryViewModel() {

        this.orderRepository = new OrderRepository();
        this.userRepository = new UserRepository();
        this.notificationRepository = new NotificationRepository();
        this.orderList = orderRepository.getOrderList();
        init();
    }

    public void init() {
        if (this.locationHelper == null) {
            LocationHelper lh = new LocationHelper();
            locationHelper = new MutableLiveData<>();
            locationHelper.setValue(lh);
        }
    }

    public MutableLiveData<ArrayList<Order>> getOrderList() {
        return orderList;
    }

    public void getAll() {

        orderRepository.getAll();
    }

    public void getAllOrderByRadius(double latitude, double longitude, double radiusInM) {
        Log.d("getAllOrderByRadius", "Iteration test");
        orderRepository.getAllByRadius(latitude, longitude, radiusInM);
    }

    public MutableLiveData<User> getUserByID(String userID) {
        return userRepository.getById(userID);
    }

    public void setOrderAcceptedinDB(Order order, String uid) {
        ArrayList<Order> updatedOrderList = this.orderList.getValue();
        updatedOrderList.remove(order);
        order.setSupplierID(uid);
        order.setOrderStatus(OrderStatus.ACCEPTED);
        orderRepository.update(order);

        Notification notification = new Notification(order.getCustomerID(), order.getId(), NotificationType.ORDER_ACCEPTED_BY_SUPPLIER);
        notificationRepository.save(notification);

        orderList.setValue(updatedOrderList);
    }

    public MutableLiveData<LocationHelper> getLocationHelper() {
        return this.locationHelper;
    }

    public void setLocationHelperPlace(Point location) {
        LocationHelper lh = this.locationHelper.getValue();
        lh.setLocation(location);
        this.locationHelper.setValue(lh);
    }

    public void setLocalHelperRadius(float radiusInM) {
        LocationHelper lh = locationHelper.getValue();
        lh.setRadiusInM(radiusInM);
        this.locationHelper.setValue(lh);
    }

}
