package mobile_computing.delifast.interaction.order;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

import mobile_computing.delifast.entities.Order;
import mobile_computing.delifast.entities.OrderPosition;
import mobile_computing.delifast.entities.Product;
import mobile_computing.delifast.repositories.ProductRepository;

public class OrderViewModel extends ViewModel {

    private ProductRepository productRepository;
    private MutableLiveData<ArrayList<Product>> productList;
    private MutableLiveData<Boolean> result;
    private MutableLiveData<ArrayList<OrderPosition>> orderPositionList;
    private MutableLiveData<Order> order;

    public OrderViewModel() {
        this.productRepository = new ProductRepository();
        this.productList = productRepository.getAll();
        this.result = new MutableLiveData<>();
        init();
    }

    private void init() {
        this.orderPositionList = new MutableLiveData<>();
        this.orderPositionList.setValue(new ArrayList<OrderPosition>());
        this.order = new MutableLiveData<>();
        Order order = new Order();
        order.setOrderPositions(new ArrayList<OrderPosition>());
        this.order.setValue(order);
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
        updateOrder(orderPosition, oldItem);
    }

    /**
     * Updates the order entity
     * @param orderPosition
     * @param oldItem
     */
    private void updateOrder(OrderPosition orderPosition, OrderPosition oldItem) {
        Order updateOrder = order.getValue();
        ArrayList<OrderPosition> opList = updateOrder.getOrderPositions();
        if (opList.contains(oldItem)) {
            opList.set(opList.indexOf(oldItem), orderPosition);
        } else {
            opList.add(orderPosition);
        }
        this.order.setValue(updateOrder);
    }
}
