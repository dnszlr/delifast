package mobile_computing.delifast.interaction.order;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import mobile_computing.delifast.entities.OrderPosition;
import mobile_computing.delifast.entities.Product;
import mobile_computing.delifast.repositories.OrderRepository;
import mobile_computing.delifast.repositories.ProductRepository;

public class OrderViewModel extends ViewModel {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private MutableLiveData<ArrayList<Product>> productList;
    private MutableLiveData<Boolean> result;
    private MutableLiveData<ArrayList<OrderPosition>> orderPositionList;

    public OrderViewModel() {
        this.productRepository = new ProductRepository();
        this.productList = productRepository.getAll();
        this.result = new MutableLiveData<>();
        this.orderPositionList = getAllPositions();
    }

    /**
     * This Method gets observered by the OrderFragment to populate the UI with LiveData
     *
     * @return All Product Objects from the database.
     */
    public MutableLiveData<ArrayList<Product>> getAll() {
        return productRepository.getAll();
    }

    public MutableLiveData<ArrayList<OrderPosition>> getAllPositions() {
        MutableLiveData<ArrayList<OrderPosition>> orderPositions = new MutableLiveData<>();
        MutableLiveData<ArrayList<Product>> products = productRepository.getAll();
        Log.d("Fucking Method:", "Product list length" + products.getValue().size());

        for (Product product : products.getValue()) {
            OrderPosition foundOp = orderPositions.getValue().stream().filter(position ->
                    position.getProduct().equals(product)).findFirst().orElse(null);

            if (foundOp == null) {
                OrderPosition newOrderPosition = new OrderPosition(product, 0);
                orderPositions.getValue().add(newOrderPosition);
            }
        }


        return orderPositions;
    }

    public void changeCountOfOrderPosition(int orderPosition) {
        OrderPosition op = this.orderPositionList.getValue().get(orderPosition);
        op.setAmount(op.getAmount() + 1);
    }
}
