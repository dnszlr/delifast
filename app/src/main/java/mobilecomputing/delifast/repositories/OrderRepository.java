package mobilecomputing.delifast.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import mobilecomputing.delifast.delifastEnum.OrderStatus;
import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.entities.User;
import mobilecomputing.delifast.others.DelifastConstants;
import mobilecomputing.delifast.others.DelifastTags;

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

    public MutableLiveData<Boolean> update(Order order) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        DocumentReference entityToUpdate = dbCollection.document(order.getId());

        entityToUpdate
                .set(order)
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

    public MutableLiveData<ArrayList<Order>> getAll() {
        final MutableLiveData<ArrayList<Order>> orders = new MutableLiveData<>();
        dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Order> resultList = new ArrayList<>();
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

    /**
     * Returns all Orders in the range for the given latitude, longitude and radiusInM
     *
     * @param latitude
     * @param longitude
     * @param radiusInM radius in meters
     * @return a ArrayList containing all fitting Orders
     */
    public MutableLiveData<ArrayList<Order>> getAllByRadius(double latitude, double longitude, double radiusInM) {
        final MutableLiveData<ArrayList<Order>> orders = new MutableLiveData<>();
        final GeoLocation center = new GeoLocation(latitude, longitude);
        // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // depending on overlap, but in most cases there are 4.
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds geoQueryBounds : bounds) {
            Query query = dbCollection
                    .orderBy("customerAddress.geoHash")
                    .startAt(geoQueryBounds.startHash)
                    .endAt(geoQueryBounds.endHash);
            tasks.add(query.get());
        }
        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        ArrayList<Order> matchingOrders = new ArrayList<>();
                        for (Task<QuerySnapshot> singleTask : tasks) {
                            QuerySnapshot snap = singleTask.getResult();
                            for (DocumentSnapshot docSnap : snap.getDocuments()) {
                                Order order = docSnap.toObject(Order.class);
                                double documentLatitude = order.getCustomerAddress().getLatitude();
                                double documentLongitude = order.getCustomerAddress().getLongitude();
                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                GeoLocation docLocation = new GeoLocation(documentLatitude, documentLongitude);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= radiusInM && order.getOrderStatus().equals(OrderStatus.OPEN)) {
                                    matchingOrders.add(docSnap.toObject(Order.class));
                                }
                            }
                        }
                        orders.setValue(matchingOrders);
                    }
                });
        return orders;
    }


}
