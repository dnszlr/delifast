package mobile_computing.delifast.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderRepository {

    // Get a firestore instance for this class
    private FirebaseFirestore db;
    private String collection;
    private CollectionReference dbCollection;

    public OrderRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.collection = "orders";
        this.dbCollection = db.collection(collection);
    }
}
