package mobile_computing.delifast.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository extends AbstractRepository{

    // Get a firestore instance for this class
    private FirebaseFirestore db;
    private String collection;

    public UserRepository(FirebaseFirestore db, String collection) {
        super(FirebaseFirestore.getInstance(), "users");
        this.db = db;
        this.collection = collection;
    }




}
