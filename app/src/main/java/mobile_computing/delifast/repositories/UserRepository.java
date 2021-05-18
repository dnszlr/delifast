package mobile_computing.delifast.repositories;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
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
import java.util.ArrayList;
import java.util.List;
import mobile_computing.delifast.entities.User;

public class UserRepository {

    // Get a firestore instance for this class
    private FirebaseFirestore db;
    private String collection;
    private CollectionReference dbCollection;

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.collection = "users";
        this.dbCollection = db.collection(collection);
    }

    public boolean save(User user) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        dbCollection
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        result.setValue(true);
                        Log.d("saved", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result.setValue(false);
                Log.w("saved", "Error adding document", e);
            }
        });
        return result.getValue();
    }

    public boolean update(User user) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        DocumentReference entityToUpdate = dbCollection.document(user.getId());

        entityToUpdate
                .set(user)
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
        return result.getValue();
    }

    public boolean delete(User user) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        dbCollection.document(user.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.setValue(true);
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.setValue(false);
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
        return result.getValue();
    }

    public LiveData<User> findById(String id) {
        final MutableLiveData<User> entity = new MutableLiveData<>();
        dbCollection.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "DocumentSnapshot successfully found!");
                entity.setValue(documentSnapshot.toObject(User.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error finding document", e);
            }
        });
        return entity;
    }

    public LiveData<List<User>> getAll() {
        final MutableLiveData<List<User>> entities = new MutableLiveData<>();
        dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> resultList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        resultList.add(document.toObject(User.class));
                    }
                    entities.setValue(resultList);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
        return entities;
    }





}
