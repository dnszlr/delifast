package mobile_computing.delifast.interaction.order;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import mobile_computing.delifast.entities.Product;
import mobile_computing.delifast.repositories.OrderRepository;
import mobile_computing.delifast.repositories.ProductRepository;

public class OrderViewModel extends ViewModel {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private MutableLiveData<ArrayList<Product>> productList;
    private MutableLiveData<Boolean> result;

    public OrderViewModel() {
        this.productRepository = new ProductRepository();
        this.productList = productRepository.getAll();
        this.result = new MutableLiveData<>();
    }

    /**
     * This Method gets observered by the OrderFragment to populate the UI with LiveData
     *
     * @return All Product Objects from the database.
     */
    public MutableLiveData<ArrayList<Product>> getAll() {
        return productRepository.getAll();
    }
}
