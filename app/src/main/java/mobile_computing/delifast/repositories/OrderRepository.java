package mobile_computing.delifast.repositories;

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

import mobile_computing.delifast.entities.Order;
import mobile_computing.delifast.others.DelifastConstants;
import mobile_computing.delifast.others.DelifastTags;

public class OrderRepository {

    private String collection;
    private CollectionReference dbCollection;

    public OrderRepository() {
        this.collection = DelifastConstants.ORDERCOLLECTION;
        this.dbCollection = FirebaseFirestore.getInstance().collection(collection);
    }

    public MutableLiveData<Boolean> save(Order order) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        dbCollection
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        result.setValue(true);
                        Log.d(DelifastTags.ORDERSAVE, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result.setValue(false);
                Log.w(DelifastTags.ORDERSAVE, "Error adding document", e);
            }
        });
        return result;
    }

    public MutableLiveData<List<Order>> getAll() {
        final MutableLiveData<List<Order>> orders = new MutableLiveData<>();
        dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Order> resultList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(DelifastTags.ORDERGETALL, document.getId() + " => " + document.getData());
                        resultList.add(document.toObject(Order.class));
                    }
                    orders.setValue(resultList);
                } else {
                    Log.d(DelifastTags.ORDERGETALL, "Error getting documents: ", task.getException());
                }
            }
        });
        return orders;
    }


}
