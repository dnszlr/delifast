package mobilecomputing.delifast.interaction.order;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.R;
import mobilecomputing.delifast.entities.OrderPosition;
import mobilecomputing.delifast.others.CurrencyFormatter;
import mobilecomputing.delifast.others.DelifastConstants;
import mobilecomputing.delifast.others.DelifastHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private TextInputEditText etUserDeposit, etSupplyPrice, etServiceFee, etAddress, etDeadline, etDescription;
    private TextView tvCartSum;
    private MaterialButton btnPay;
    private OrderViewModel model;
    private LinearLayout lvContentLayout;
    private SimpleDateFormat simpleDateFormat;
    private MaterialButton btnLocationPicker;
    private FusedLocationProviderClient fusedLocationClient;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cartView = inflater.inflate(R.layout.fragment_cart, container, false);
        //Initialize places
        Places.initialize(getActivity().getApplicationContext(), DelifastConstants.APIKEY);
        initView(cartView);
        initListeners();
        simpleDateFormat = new SimpleDateFormat(DelifastConstants.TIMEFORMAT);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        model = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        model.getOrder().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                lvContentLayout.removeAllViewsInLayout();
                updateUI(order);
            }
        });
        return cartView;
    }

    /**
     * Initialize all listeners for the UI components
     */
    private void initListeners() {
        etAddress.setOnClickListener(new View.OnClickListener() {
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
        etDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog();
            }
        });
        etUserDeposit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged", String.valueOf(s));
                if (s.length() > 0) {
                    model.setUserDeposit(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    model.setDescription(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReadyToPay(Double.parseDouble(tvCartSum.getText().toString().trim()),
                        etAddress.getText().toString().trim(),
                        etDeadline.getText().toString().trim())) {
                    onBraintreeSubmit(v);
                }

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
                                    String placeName = results.get(0).placeName();
                                    etAddress.setText(placeName);
                                    try {
                                        model.setCustomerAddress(point.coordinates(), placeName);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Es konnte keine Addresse gefunden werden", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Bitte GPS einschalten", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Keine Rechte vorhanden", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Submit Method for the payment process
     *
     * @param v
     */
    public void onBraintreeSubmit(View v) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        DelifastHttpClient.get("clienttoken", requestParams, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String clientToken) {
                DropInRequest dropInRequest = new DropInRequest();
                dropInRequest.clientToken(clientToken);
                startActivityForResult(dropInRequest.getIntent(v.getContext()), DelifastConstants.PAYMENT_REQUEST_CODE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), "Bezahlung fehlgeschlagen, entschuldigen sie die Störung", Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * Displays the passed order to the UI.
     *
     * @param order
     */
    private void updateUI(Order order) {
        ArrayList<OrderPosition> orderPositions = order.getOrderPositions();
        Log.d("updateUI", "orderPosition size:" + orderPositions.size());
        for (int i = 0; i < orderPositions.size(); i++) {
            OrderPosition currentOrderPosition = orderPositions.get(i);
            final View positionCardView = getLayoutInflater().inflate(R.layout.order_position_in_cart, null, false);

            TextView productNameInCart = positionCardView.findViewById(R.id.tvOrderPositionNameInCart);
            TextView amountInCart = positionCardView.findViewById(R.id.tvOrderPositionCountInCart);
            MaterialButton btnDeletePosition = positionCardView.findViewById(R.id.btnOrderPositionInCart);

            productNameInCart.setText(orderPositions.get(i).getProduct().getName());
            amountInCart.setText(Integer.toString(orderPositions.get(i).getAmount()));

            btnDeletePosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOrderPositionInViewModel(currentOrderPosition);
                }
            });
            lvContentLayout.addView(positionCardView);
        }
        etUserDeposit.setError(null);
        etAddress.setError(null);
        etDeadline.setError(null);
        etDescription.setText(order.getDescription());
        Log.d("Vorkasse: ", "Deposit" + order.getUserDeposit());
        if (order.getDeadline() != null) {
            etDeadline.setText(simpleDateFormat.format(order.getDeadline()));
        }
        if (order.getCustomerAddress() != null) {
            etAddress.setText(order.getCustomerAddress().getAddressString());
        }
        etServiceFee.setText(CurrencyFormatter.doubleToUIRep(order.getServiceFee() + order.getCustomerFee()));
        etUserDeposit.setText(CurrencyFormatter.doubleToUIRep(order.getUserDeposit()));
        tvCartSum.setText(CurrencyFormatter.doubleToUIRep(order.getUserDeposit() + order.getServiceFee() + order.getCustomerFee()));
        String deadline = order.getDeadline() != null ? simpleDateFormat.format(order.getDeadline()) : "";
        etDeadline.setText(deadline);
        String address = order.getCustomerAddress() != null ? order.getCustomerAddress().getAddressString() : "";
        etAddress.setText(address);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ActivityResult", "RequestCode arrived:" + requestCode);

        if (requestCode == DelifastConstants.MAPBOX_REQUEST_TOKEN) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {
                CarmenFeature feature = PlaceAutocomplete.getPlace(data);
                try {
                    Point point = (Point) feature.geometry();
                    model.setCustomerAddress(point.coordinates(), feature.placeName());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Invalid address provided", Toast.LENGTH_SHORT).show();
                }
                etAddress.setText(feature.placeName());
                Toast.makeText(getActivity(), feature.text(), Toast.LENGTH_LONG).show();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getActivity().getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == DelifastConstants.PAYMENT_REQUEST_CODE) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {
                // use the result to update your UI and send the payment method nonce to your server
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                RequestParams params = new RequestParams();
                Log.d("ActivityResult", "payment_method_nonce:" + result.getPaymentMethodNonce());
                params.put("payment_method_nonce", result.getPaymentMethodNonce().getNonce());
                params.put("amount", tvCartSum.getText());
                DelifastHttpClient.post("checkout", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String transactionId = "";
                        try {
                            JSONObject response = new JSONObject(new String(responseBody));
                            transactionId = (String) response.getJSONObject("transaction").get("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Payment was Successful, persist the current order.
                        model.saveOrder(transactionId);
                        model.resetOrder();
                        Toast.makeText(getActivity(), "Ihre Bestellung wurde erfolgreich gespeichert", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("Payment", "failure" + statusCode);
                        Toast.makeText(getActivity(), "Bezahlung fehlgeschlagen, entschuldigen sie die Störung", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                // the user canceled
                Log.d("Braintree Result", "User canceled transaction");
                Toast.makeText(getActivity(), "Bezahlung wurde von abgebrochen", Toast.LENGTH_LONG).show();
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("Braintree Result", "error: " + error.getMessage());
                Toast.makeText(getActivity(), "Bezahlung fehlgeschlagen, entschuldigen sie die Störung", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Initialize all needed components for the current view
     *
     * @param cartView
     */
    private void initView(View cartView) {
        lvContentLayout = cartView.findViewById(R.id.contentLayout);
        etDeadline = cartView.findViewById(R.id.etDeadline);
        etDeadline.setInputType(InputType.TYPE_NULL);
        etUserDeposit = cartView.findViewById(R.id.etUserDeposit);
        etServiceFee = cartView.findViewById(R.id.etServiceFee);
        etAddress = cartView.findViewById(R.id.etAddress);
        etDescription = cartView.findViewById(R.id.etDescription);
        tvCartSum = cartView.findViewById(R.id.tvCartSum);
        btnPay = cartView.findViewById(R.id.btnPay);
        btnLocationPicker = cartView.findViewById(R.id.btnLocationPicker);
    }

    /**
     * ClickListener Method for etDeadline
     */
    private void showDateTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        model.setDeadline(calendar.getTime());
                        etDeadline.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(getActivity(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };
        new DatePickerDialog(getActivity(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Delete the order position from the order in view model
     *
     * @param orderPosition
     */
    public void deleteOrderPositionInViewModel(OrderPosition orderPosition) {
        model.deleteOrderPositionFromOrder(orderPosition);
    }

    /**
     * Check if all required details for the order
     * are available (address, price > 0 and time)
     *
     * @param sum: the order price
     *             address: the order address
     *             dateTime: the required time of order
     */
    private boolean isReadyToPay(double sum, String address, String dateTime) {
        if (sum < 1) {
            etUserDeposit.setError(DelifastConstants.MISSING_PRODUCTS);
            etUserDeposit.requestFocus();
            return false;
        }
        if (address.isEmpty()) {
            etAddress.setError(DelifastConstants.MISSING_ADRESS);
            etAddress.requestFocus();
            return false;
        }
        if (dateTime.isEmpty()) {
            etDeadline.setError(DelifastConstants.MISSING_DEADLINE);
            etDeadline.requestFocus();
            return false;
        }
        return true;
    }
}