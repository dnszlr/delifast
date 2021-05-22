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

    public void updateOrderPositionList(ArrayList<Product> products) {
        ArrayList<OrderPosition> op = new ArrayList<>();
        if (orderPositionList.getValue() != null) {
            op = orderPositionList.getValue();
        }
        for (Product product : products) {
            OrderPosition foundOp = op.stream().filter(position ->
                    position.getProduct().equals(product)).findFirst().orElse(null);
            if (foundOp == null) {
                OrderPosition newOrderPosition = new OrderPosition(product, 0);
                op.add(newOrderPosition);
            }
        }
        orderPositionList.setValue(op);
    }

    public MutableLiveData<ArrayList<OrderPosition>> getOrderPositionList() {
        return this.orderPositionList;
    }

    public void changeCountOfOrderPosition(OrderPosition orderPosition) {
        ArrayList<OrderPosition> opList = this.orderPositionList.getValue();
        opList.add(orderPosition);
        this.orderPositionList.setValue(opList);
    }
}
