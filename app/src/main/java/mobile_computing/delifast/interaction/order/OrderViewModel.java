package mobile_computing.delifast.interaction.order;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import mobile_computing.delifast.R;
import mobile_computing.delifast.delifastEnum.ProductCategory;
import mobile_computing.delifast.entities.Order;
import mobile_computing.delifast.entities.OrderPosition;
import mobile_computing.delifast.entities.Product;
import mobile_computing.delifast.interaction.delivery.DeliveryFragment;
import mobile_computing.delifast.interaction.notification.NotificationFragment;
import mobile_computing.delifast.interaction.profile.ProfileFragment;
import mobile_computing.delifast.repositories.OrderRepository;
import mobile_computing.delifast.repositories.ProductRepository;

public class OrderViewModel extends ViewModel {

    private ProductRepository productRepository;
    private MutableLiveData<ArrayList<Product>> productList;
    private MutableLiveData<Boolean> result;
    private MutableLiveData<ArrayList<OrderPosition>> orderPositionList;

    public OrderViewModel() {
        this.productRepository = new ProductRepository();
        this.productList = productRepository.getAll();
        this.result = new MutableLiveData<>();
        this.orderPositionList = new MutableLiveData<>();
    }

    /**
     * This Method gets observered by the OrderFragment to populate the UI with LiveData
     *
     * @return All Product Objects from the database.
     */
    public MutableLiveData<ArrayList<Product>> getAll() {
        return productRepository.getAll();
    }

    /**
     * Updates MutableLiveData List for OrderPositions to trigger Livedata changes on the UI
     *
     * @param products All the products received by the ProductsRepository
     */
    public void updateOrderPositionList(ArrayList<Product> products) {
        ArrayList<OrderPosition> op = new ArrayList<>();
        // If orderPosition already owns a ArrayList of OrderProjects, we have to use it.
        if (orderPositionList.getValue() != null) {
            op = orderPositionList.getValue();
        }
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
    }
}
