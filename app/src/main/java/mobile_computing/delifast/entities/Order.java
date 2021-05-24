package mobile_computing.delifast.entities;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.List;

import mobile_computing.delifast.delifastEnum.OrderStatus;


public class Order extends DelifastEntity {

    private long orderID;
    @ServerTimestamp
    private DateTime orderTime;
    private DateTime deadline;
    private OrderStatus orderStatus;
    private float deliveryPrice;
    private float serviceFee;
    private ArrayList<OrderPosition> orderPositions;

    /**
     * Empty firebase constructor, don't remove.
     */
    public Order() {

    }

    public Order(long orderID, DateTime orderTime, DateTime deadline, OrderStatus orderStatus, float deliveryPrice, float serviceFee, ArrayList<OrderPosition> orderPositions) {
        this.orderID = orderID;
        this.orderTime = orderTime;
        this.deadline = deadline;
        this.orderStatus = orderStatus;
        this.deliveryPrice = deliveryPrice;
        this.serviceFee = serviceFee;
        this.orderPositions = orderPositions;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public DateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(DateTime orderTime) {
        this.orderTime = orderTime;
    }

    public DateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(DateTime deadline) {
        this.deadline = deadline;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public float getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(float deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public float getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(float serviceFee) {
        this.serviceFee = serviceFee;
    }

    public ArrayList<OrderPosition> getOrderPositions() {
        return orderPositions;
    }

    public void setOrderPositions(ArrayList<OrderPosition> orderPositions) {
        this.orderPositions = orderPositions;
    }
}
