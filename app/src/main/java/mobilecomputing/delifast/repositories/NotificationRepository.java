package mobilecomputing.delifast.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import mobilecomputing.delifast.entities.Notification;
import mobilecomputing.delifast.others.DelifastConstants;
import mobilecomputing.delifast.others.DelifastTags;

public class NotificationRepository {

    private String collection;
    private CollectionReference dbCollection;

    public NotificationRepository() {
        this.collection = DelifastConstants.NOTIFICATIONCOLLECTION;
        this.dbCollection = FirebaseFirestore.getInstance().collection(collection);
    }

    public MutableLiveData<Boolean> save(Notification notification) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        dbCollection
                .add(notification)
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

    public MutableLiveData<List<Notification>> getAllByUserId(String userId) {
        MutableLiveData<List<Notification>> result = new MutableLiveData<>();
        dbCollection
                .whereEqualTo("userId", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException exception) {
                        if (exception != null) {
                            Log.w("NotificationRepositoryGetAllByUserId", "Listen failed.", exception);
                            return;
                        }

                        List<Notification> resultList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc != null) {
                                resultList.add(doc.toObject(Notification.class));
                            }
                        }
                        result.setValue(resultList);
                    }
                });
        return result;
    }


}
