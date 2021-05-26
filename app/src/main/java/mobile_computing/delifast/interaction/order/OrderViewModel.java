package mobile_computing.delifast.interaction.order;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobile_computing.delifast.delifastEnum.OrderStatus;
import mobile_computing.delifast.entities.Address;
import mobile_computing.delifast.entities.Order;
import mobile_computing.delifast.entities.OrderPosition;
import mobile_computing.delifast.entities.Product;
import mobile_computing.delifast.repositories.FirebaseAuthRepository;
import mobile_computing.delifast.repositories.OrderRepository;
import mobile_computing.delifast.repositories.ProductRepository;

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

    public void saveOrder() {
        Order orderToSave = this.order.getValue();
        if (orderToSave != null) {
            orderRepository.save(orderToSave);
        }
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

        if(opList.contains(oldItem) && oldItem.getAmount() == 0){
            opList.remove(oldItem);
        }
        else if (opList.contains(oldItem)) {
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
        double deliveryPrice = order.getOrderPositions().stream().mapToDouble(OrderPosition::getPrice).sum();
        order.setUserDeposit(deliveryPrice);
        order.setDeliveryPrice(deliveryPrice);
        double serviceFee = determineServiceFee(deliveryPrice);
        order.setServiceFee(serviceFee);
    }

    /**
     * Determines the serviceFee for the currentOrder. The more expensive the order, the more expensive the service fee.
     *
     * @param deliveryPrice
     * @return serviceFee
     */
    private double determineServiceFee(double deliveryPrice) {
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
        deposit = Math.round(deposit * 100.0) / 100.0;
        this.order.getValue().setUserDeposit(deposit);
    }

    /**
     * Reacts to customerAddress changes and stores it in the view models livedata
     *
     * @param coordinates
     * @param coordinates - latitude and longitude
     * @param address     - A json String which holds all necessary address data.
     */
    public void setCustomerAddress(List<Double> coordinates, String address) throws JSONException {
        double latitude = coordinates.get(0);
        double longitude = coordinates.get(1);
        Address customerAddress = new Address(latitude, longitude, address);
        this.order.getValue().setCustomerAddress(customerAddress);
    }

    /**
     * Reacts to dealine changes and stores it in the view models livedata
     *
     * @param deadline
     */
    public void setDeadline(Date deadline) {
        this.order.getValue().setDeadline(deadline);
    }

}
