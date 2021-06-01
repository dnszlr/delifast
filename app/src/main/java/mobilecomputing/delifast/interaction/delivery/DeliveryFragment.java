package mobilecomputing.delifast.interaction.delivery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.io.IOException;
import java.util.List;

import mobilecomputing.delifast.R;
import mobilecomputing.delifast.delifastEnum.NotificationType;
import mobilecomputing.delifast.entities.Notification;
import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.others.CurrencyFormatter;
import mobilecomputing.delifast.others.DelifastConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryFragment extends Fragment {

    private DeliveryViewModel viewModel;

    private LinearLayout backlog;
    private ImageView btnBacklogLocation;
    private LinearLayout linearLayoutLocation;
    private TextInputEditText etBacklogAddress;
    private Slider sliderRadius;
    private FusedLocationProviderClient fusedLocationClient;
    private MaterialButton btnLocationPicker;


    public DeliveryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View deliveryView = inflater.inflate(R.layout.fragment_delivery, container, false);

        initView(deliveryView);
        initListeners();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        viewModel = new ViewModelProvider(this).get(DeliveryViewModel.class);
        //viewModel.getAllOrderByRadius(48.48166419984722, 9.204247876280458, 20000).observe(getViewLifecycleOwner(), orders -> {
        viewModel.getOrderList().observe(getViewLifecycleOwner(), orders -> {
            Log.d("onCreateView", "iteration test outside");
            if (orders != null) {
                backlog.removeAllViewsInLayout();
                Log.d("onCreateView", "Orders size: " + orders.size());
                for (int i = 0; i < orders.size(); i++) {
                    Log.d("onCreateView", "iteration test inside");
                    createCardInBacklog(orders.get(i));
                }
            }
        });
        viewModel.getLocationHelper().observe(getViewLifecycleOwner(), locationHelper -> {
            if (locationHelper != null) {
                if (locationHelper.getLocation() == null) {
                    viewModel.getAll();
                } else {
                    double latitude = locationHelper.getLocation().latitude();
                    double longitude = locationHelper.getLocation().longitude();
                    viewModel.getAllOrderByRadius(latitude, longitude, locationHelper.getRadiusInM());
                }
            }
        });
        return deliveryView;
    }


    /**
     * Initialize the view elements
     *
     * @param view
     */
    private void initView(View view) {
        backlog = view.findViewById(R.id.llBacklog);
        btnBacklogLocation = view.findViewById(R.id.btnBacklogLocation);
        linearLayoutLocation = view.findViewById(R.id.linearLayoutLocation);
        etBacklogAddress = view.findViewById(R.id.etBacklogAddress);
        sliderRadius = view.findViewById(R.id.sliderRadius);
        btnLocationPicker = view.findViewById(R.id.btnLocationPicker);
    }


    /**
     * Initialize the listeners for the buttons in the view
     *
     * @param
     */
    private void initListeners() {
        btnBacklogLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutLocation.getVisibility() == View.GONE) {
                    linearLayoutLocation.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutLocation.setVisibility(View.GONE);
                }
            }
        });
        etBacklogAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(DelifastConstants.MAPBOX_ACCESS_TOKEN)
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .addInjectedFeature(DelifastConstants.REUTLINGEN)
                                .addInjectedFeature(DelifastConstants.BERLIN)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, DelifastConstants.MAPBOX_REQUEST_TOKEN);
            }
        });
        sliderRadius.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                // calculate kilometers in meters, +1 for
                viewModel.setLocalHelperRadius((value * 1000) + 1);
            }
        });
        btnLocationPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    locationPicker();
                } else {
                    // You can directly ask for the permission.
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            DelifastConstants.PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case DelifastConstants.PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    locationPicker();
                } else {
                    Toast.makeText(getActivity(), "Aufgrund fehlender Rechte kann der Standord nicht lokalisiert werden", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    public void locationPicker() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.d("latitude:", "" + location.getLatitude());
                        Log.d("longitude:", "" + location.getLongitude());
                        Point point = Point.fromLngLat(location.getLongitude(), location.getLatitude());
                        Log.d("Point latitude:", "" + point.latitude());
                        Log.d("Point longitude:", "" + point.longitude());
                        MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
                                .accessToken(DelifastConstants.MAPBOX_ACCESS_TOKEN)
                                .query(point)
                                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                                .build();
                        reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
                            @Override
                            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                                List<CarmenFeature> results = response.body().features();
                                if (results.size() > 0) {
                                    etBacklogAddress.setText(results.get(0).placeName());
                                } else {
                                    Toast.makeText(getActivity(), "Es konnte keine Addresse gefunden werden", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                        viewModel.setLocationHelperPlace(point);
                    } else {
                        Toast.makeText(getActivity(), "Bitte GPS einschalten", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Keine Rechte vorhanden", Toast.LENGTH_LONG).show();
        }
    }

    private void createCardInBacklog(Order order) {
        final View orderCard = getLayoutInflater().inflate(R.layout.fragment_delivery_cardview, null, false);


        ConstraintLayout layoutCardViewBacklog = orderCard.findViewById(R.id.layoutCardViewBacklog);
        CardView cardView = orderCard.findViewById(R.id.cardViewBacklog);

        TextView orderAddress = orderCard.findViewById(R.id.tvCardBacklogAddress);
        orderAddress.setText(order.getCustomerAddress().getAddressString());

        TextView orderDeadline = orderCard.findViewById(R.id.tvCardBacklogDeadline);
        orderDeadline.setText(order.getDeadline().toString());

        TextView orderSupplyPrice = orderCard.findViewById(R.id.tvCardBacklogSupplyPrice);
        orderSupplyPrice.setText(CurrencyFormatter.doubleToUIRep(order.getCustomerFee()));

        TextView orderBuyer = orderCard.findViewById(R.id.tvCardBacklogUserName);

        viewModel.getUserByID(order.getCustomerID()).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                orderBuyer.setText(user.getName());
            }
        });


        TextView orderDeposit = orderCard.findViewById(R.id.tvCardBacklogUserDeposit);
        orderDeposit.setText(CurrencyFormatter.doubleToUIRep(order.getUserDeposit()));

        TextView orderSupplyPrice2 = orderCard.findViewById(R.id.tvCardBacklogSupplyPriceSmall);
        orderSupplyPrice2.setText(CurrencyFormatter.doubleToUIRep(order.getCustomerFee()));

        EditText etOrderDescription = orderCard.findViewById(R.id.tvCardBacklogRemarks);
        etOrderDescription.setText(order.getDescription());

        ConstraintLayout expandableView = orderCard.findViewById(R.id.constraintLayoutExpandableView);
        Button arrowDownUp = orderCard.findViewById(R.id.btnCardBacklogArrowDown);

        arrowDownUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                    arrowDownUp.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                    arrowDownUp.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
            }
        });

        LinearLayout productsList = orderCard.findViewById(R.id.lvCardBacklogProducts);
        for (int j = 0; j < order.getOrderPositions().size(); j++) {
            final View productInCardView = getLayoutInflater().inflate(R.layout.fragment_delivery_cardview_product, null, false);

            TextView productNameInCardView = productInCardView.findViewById(R.id.tvOrderPositionNameInBacklog);
            TextView amountInCardView = productInCardView.findViewById(R.id.tvOrderPositionCountInBacklog);

            productNameInCardView.setText(order.getOrderPositions().get(j).getProduct().getName());
            amountInCardView.setText(Integer.toString(order.getOrderPositions().get(j).getAmount()));


            productsList.addView(productInCardView);
        }

        MaterialButton btnAcceptOrder = orderCard.findViewById(R.id.btnCardBacklogAcceptOrder);
        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAcceptOrderDialog(order);
            }
        });

        backlog.addView(orderCard);
    }

    private void openAcceptOrderDialog(Order order) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Bestellung annehmen")
                .setMessage("Möchten Sie diesen Auftrag verbindlich annehmen?")
                .setPositiveButton("Ja", null)
                .setNegativeButton("Nein", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        assignOrdertoUser(order);
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ActivityResult", "RequestCode arrived:" + requestCode);

        if (requestCode == DelifastConstants.MAPBOX_REQUEST_TOKEN) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {
                CarmenFeature feature = PlaceAutocomplete.getPlace(data);
                Point point = (Point) feature.geometry();
                viewModel.setLocationHelperPlace(point);
                etBacklogAddress.setText(feature.placeName());
                Toast.makeText(getActivity(), feature.text(), Toast.LENGTH_LONG).show();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getActivity().getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void assignOrdertoUser(Order order) {
        viewModel.setOrderAcceptedinDB(order, FirebaseAuth.getInstance().getUid());
        Toast.makeText(getActivity(), "Sie haben den Auftrag angenommen", Toast.LENGTH_SHORT).show();
    }
}