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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.entities.Rating;
import mobilecomputing.delifast.entities.User;
import mobilecomputing.delifast.others.DelifastConstants;
import mobilecomputing.delifast.others.DelifastTags;

public class RatingRepository {
    private String collection;
    private CollectionReference dbCollection;

    public RatingRepository() {
        this.collection = DelifastConstants.RATINGCOLLECTION;
        this.dbCollection = FirebaseFirestore.getInstance().collection(collection);
    }

    public MutableLiveData<Boolean> save(Rating rating) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        dbCollection.add(rating)
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

    public MutableLiveData<Boolean> update(Rating rating) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        DocumentReference entityToUpdate = dbCollection.document(rating.getId());
        entityToUpdate
                .set(rating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.setValue(true);
                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.setValue(false);
                        Log.w("TAG", "Error updating document", e);
                    }
                });
        return result;
    }

    public MutableLiveData<Rating> getById(String id) {
        final MutableLiveData<Rating> entity = new MutableLiveData<>();
        dbCollection.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "DocumentSnapshot successfully found!");
                entity.setValue(documentSnapshot.toObject(Rating.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("TAG", "Error finding document", e);
            }
        });
        return entity;
    }

    public MutableLiveData<List<Rating>> getAllByUserId(String userId) {
        final MutableLiveData<List<Rating>> entity = new MutableLiveData<>();
        dbCollection
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        List<Rating> resultList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            resultList = task.getResult().toObjects(Rating.class);
                        } else {
                            Log.d(DelifastTags.RATINGETALLBYUSERID, "Error getting documents: ", task.getException());
                        }
                        entity.setValue(resultList);
                    }
                });
        return entity;
    }

    public MutableLiveData<List<Rating>> getAll() {
        MutableLiveData<List<Rating>> ratingList = new MutableLiveData<>();
        dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Rating> resultList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(DelifastTags.ORDERGETALL, document.getId() + " => " + document.getData());
                        resultList.add(document.toObject(Rating.class));
                    }
                    ratingList.postValue(resultList);
                } else {
                    Log.d(DelifastTags.ORDERGETALL, "Error getting documents: ", task.getException());
                }
            }
        });
        return ratingList;
    }

}
