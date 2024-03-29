package mobilecomputing.delifast.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mobilecomputing.delifast.entities.Product;
import mobilecomputing.delifast.others.DelifastConstants;
import mobilecomputing.delifast.others.DelifastTags;

public class ProductRepository {

    private String collection;
    private CollectionReference dbCollection;

    public ProductRepository() {
        this.collection = DelifastConstants.PRODUCTCOLLECTION;
        this.dbCollection = FirebaseFirestore.getInstance().collection(collection);
    }

    /**
     * Adds a new Products to the database
     *
     * @param product
     * @return true if save was successful, false if not
     */
    public MutableLiveData<Boolean> save(Product product) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        dbCollection
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        result.setValue(true);
                        Log.d(DelifastTags.PRODUCTSAVE, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result.setValue(false);
                Log.w(DelifastTags.PRODUCTSAVE, "Error adding document", e);
            }
        });
        return result;
    }

    /**
     * Returns all stored Products from the database
     *
     * @return Products as MutableLiveData
     */
    public MutableLiveData<ArrayList<Product>> getAll() {
        final MutableLiveData<ArrayList<Product>> products = new MutableLiveData<>();
        dbCollection
                .orderBy("category")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Product> resultList = new ArrayList<>();
                if (task.isSuccessful()) {
                    resultList = task.getResult().toObjects(Product.class);
                    products.setValue((ArrayList) resultList);
                } else {
                    Log.d(DelifastTags.PRODUCTGETALL, "Error getting documents: ", task.getException());
                }
            }
        });
        return products;
    }
}
