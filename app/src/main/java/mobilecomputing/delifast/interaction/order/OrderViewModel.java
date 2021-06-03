package mobilecomputing.delifast.interaction.order;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobilecomputing.delifast.delifastEnum.OrderStatus;
import mobilecomputing.delifast.entities.Address;
import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.entities.OrderPosition;
import mobilecomputing.delifast.entities.Product;
import mobilecomputing.delifast.repositories.ProductRepository;
import mobilecomputing.delifast.repositories.OrderRepository;

public class OrderViewModel extends ViewModel {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private MutableLiveData<ArrayList<Product>> productList;
    private MutableLiveData<ArrayList<OrderPosition>> orderPositionList;
    private MutableLiveData<Order> order;

    public OrderViewModel() {
        this.productRepository = new ProductRepository();
        this.orderRepository = new OrderRepository();
        this.productList = productRepository.getAll();
        init();
    }

    private void init() {
        this.orderPositionList = new MutableLiveData<>();
        this.orderPositionList.setValue(new ArrayList<OrderPosition>());
        this.order = new MutableLiveData<>();
        Order order = new Order();
        order.setOrderStatus(OrderStatus.OPEN);
        order.setCustomerID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        order.setOrderPositions(new ArrayList<OrderPosition>());
        this.order.setValue(order);
    }

    public void saveOrder(String transactionId) {
        Order orderToSave = this.order.getValue();
        if (orderToSave != null) {
            orderToSave.setTransactionID(transactionId);
            orderRepository.save(orderToSave);
        }
    }


    public void resetOrder() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.OPEN);
        order.setCustomerID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        order.setOrderPositions(new ArrayList<OrderPosition>());
        this.order.setValue(order);
        resetOrderPositionList();
    }

    private void resetOrderPositionList() {
        ArrayList<OrderPosition> opList = this.orderPositionList.getValue();
        opList.forEach(op -> op.setAmount(0));
        this.orderPositionList.setValue(opList);
    }

    /**
     * This Method gets observed by the OrderFragment to populate the UI with LiveData
     *
     * @return All Product Objects from the database.
     */
    public MutableLiveData<ArrayList<Product>> getAll() {

        return productRepository.getAll();
    }

    public MutableLiveData<Order> getOrder() {
        return order;
    }

    /**
     * Updates MutableLiveData List for OrderPositions to trigger Livedata changes on the UI
     *
     * @param products All the products received by the ProductsRepository
     */
    public void updateOrderPositionList(ArrayList<Product> products) {
        ArrayList<OrderPosition> op = orderPositionList.getValue();
        // Search through all given products
        for (Product product : products) {
            // Search through all orderProducts if the current product already exists
            OrderPosition foundOp = op.stream().filter(position ->
                    position.getProduct().equals(product)).findFirst().orElse(null);
            // If the product doesn't exist in the OrderPosition list
            if (foundOp == null) {
                // create a new one to update the UI with the new OrderPosition Object.
                OrderPosition newOrderPosition = new OrderPosition(product, 0);
                op.add(newOrderPosition);
            }
        }
        orderPositionList.setValue(op);
    }

    public MutableLiveData<ArrayList<OrderPosition>> getOrderPositionList() {
        return this.orderPositionList;
    }


    /**
     * Gets called by the Fragment to in- or decrease the amount of a OrderPosition.
     *
     * @param orderPosition
     */
    public void changeCountOfOrderPosition(OrderPosition orderPosition) {
        ArrayList<OrderPosition> opList = this.orderPositionList.getValue();

        OrderPosition oldItem = opList.stream().filter(op ->
                op.getProduct().equals(orderPosition.getProduct())).findFirst().orElse(null);
        if (oldItem != null) {
            opList.set(opList.indexOf(oldItem), orderPosition);
        }
        this.orderPositionList.setValue(opList);
        updateOrderItems(orderPosition, oldItem);
    }

    /**
     * Gets called by the Fragment to delete the order position from the order.
     *
     * @param orderPosition: the order position to be deleted.
     */
    public void deleteOrderPositionFromOrder(OrderPosition orderPosition) {
        Order currentOrder = this.order.getValue();
        currentOrder.getOrderPositions().remove(orderPosition);
        calculateCosts(currentOrder);
        this.order.setValue(currentOrder);

        ArrayList<OrderPosition> opList = this.orderPositionList.getValue();
        OrderPosition deletedItem = opList.stream().filter(op ->
                op.getProduct().equals(orderPosition.getProduct())).findFirst().orElse(null);
        deletedItem.setAmount(0);
        this.orderPositionList.setValue(opList);
    }

    /**
     * Updates the order entity
     *
     * @param orderPosition
     * @param oldItem
     */
    private void updateOrderItems(OrderPosition orderPosition, OrderPosition oldItem) {
        Order updateOrder = order.getValue();
        ArrayList<OrderPosition> opList = updateOrder.getOrderPositions();

        if (opList.contains(oldItem) && oldItem.getAmount() == 0) {
            opList.remove(oldItem);
        } else if (opList.contains(oldItem)) {
            opList.set(opList.indexOf(oldItem), orderPosition);
        } else {
            opList.add(orderPosition);
        }
        calculateCosts(updateOrder);
        this.order.setValue(updateOrder);
    }

    /**
     * Calculates all costs for the current order.
     *
     * @param order
     */
    private void calculateCosts(Order order) {
        double userDeposit = order.getOrderPositions().stream().mapToDouble(OrderPosition::getPrice).sum();
        order.setUserDeposit(userDeposit);
        double fee = determineFee(userDeposit);
        order.setServiceFee(fee * 0.3);
        //
        order.setCustomerFee(fee * 0.7);
    }

    /**
     * Determines the serviceFee for the currentOrder. The more expensive the order, the more expensive the service fee.
     *
     * @param deliveryPrice
     * @return serviceFee
     */
    private double determineFee(double deliveryPrice) {
        // Price > 75€ = 10% fee
        double servicePercentage = 0.1;
        if (deliveryPrice <= 5.0) {
            // Price < 5€ = 50% fee
            servicePercentage = 0.5;
        } else if (deliveryPrice <= 25) {
            // Price < 25€ = 40% fee
            servicePercentage = 0.4;
        } else if (deliveryPrice <= 50) {
            // Price < 50€ = 30% fee
            servicePercentage = 0.3;
        } else if (deliveryPrice <= 75) {
            // Price < 75€ = 20% fee
            servicePercentage = 0.2;
        }
        return deliveryPrice * servicePercentage;
    }

    /**
     * Reacts to userDeposit changes and stores it in the view models livedata
     *
     * @param userDeposit
     */
    public void setUserDeposit(CharSequence userDeposit) {
        double deposit = Double.valueOf(String.valueOf(userDeposit));
        Order updatedOrder = this.order.getValue();
        if(updatedOrder.getUserDeposit() != deposit){
            updatedOrder.setUserDeposit(deposit);
            this.order.setValue(updatedOrder);
        }

    }

    /**
     * Reacts to customerAddress changes and stores it in the view models livedata
     *
     * @param coordinates
     * @param coordinates - latitude and longitude
     * @param address     - A json String which holds all necessary address data.
     */
    public void setCustomerAddress(List<Double> coordinates, String address) throws JSONException {
        double longitude = coordinates.get(0);
        double latitude = coordinates.get(1);
        Address customerAddress = new Address(latitude, longitude, address);
        Order updatedOrder = this.order.getValue();
        updatedOrder.setCustomerAddress(customerAddress);
        this.order.setValue(updatedOrder);
    }

    /**
     * Reacts to dealine changes and stores it in the view models livedata
     *
     * @param deadline
     */
    public void setDeadline(Date deadline) {
        Order updatedOrder = this.order.getValue();
        updatedOrder.setDeadline(deadline);
        this.order.setValue(updatedOrder);
    }


    /**
     * Reacts to userDeposit changes and stores it in the view models livedata
     *
     * @param userDeposit
     */
    public void setDescription(CharSequence userDeposit) {
        String deposit = String.valueOf(userDeposit);
        this.order.getValue().setDescription(deposit);
    }

}
