package mobilecomputing.delifast.entities;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

import mobilecomputing.delifast.delifastEnum.OrderStatus;


public class Order extends DelifastEntity {

    private String customerID;
    private String supplierID;
    @ServerTimestamp
    private Date orderTime;
    private Date deadline;
    private OrderStatus orderStatus;
    private double userDeposit;
    private double customerFee;
    private double serviceFee;
    private String description;
    private Address customerAddress;
    private ArrayList<OrderPosition> orderPositions;

    /**
     * Empty firebase constructor, don't remove.
     */
    public Order() {
        super();
    }

    public Order(String customerID, String supplierID, Date orderTime, Date deadline, OrderStatus orderStatus, double userDeposit, double customerFee, double serviceFee, String description, Address customerAddress, ArrayList<OrderPosition> orderPositions) {
        this.customerID = customerID;
        this.supplierID = supplierID;
        this.orderTime = orderTime;
        this.deadline = deadline;
        this.orderStatus = orderStatus;
        this.userDeposit = userDeposit;
        this.customerFee = customerFee;
        this.serviceFee = serviceFee;
        this.description = description;
        this.customerAddress = customerAddress;
        this.orderPositions = orderPositions;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getUserDeposit() {
        return userDeposit;
    }

    public void setUserDeposit(double userDeposit) {
        this.userDeposit = userDeposit;
    }

    public double getCustomerFee() {
        return customerFee;
    }

    public void setCustomerFee(double customerFee) {
        this.customerFee = customerFee;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Address getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(Address customerAddress) {
        this.customerAddress = customerAddress;
    }

    public ArrayList<OrderPosition> getOrderPositions() {
        return orderPositions;
    }

    public void setOrderPositions(ArrayList<OrderPosition> orderPositions) {
        this.orderPositions = orderPositions;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
