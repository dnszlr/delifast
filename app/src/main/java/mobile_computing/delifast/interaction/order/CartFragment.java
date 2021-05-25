package mobile_computing.delifast.interaction.order;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mobile_computing.delifast.R;
import mobile_computing.delifast.entities.Address;
import mobile_computing.delifast.entities.Order;
import mobile_computing.delifast.entities.OrderPosition;
import mobile_computing.delifast.others.DelifastConstants;

public class CartFragment extends Fragment {

    private TextInputEditText etUserDeposit, etSupplyPrice, etServiceFee, etAddress, etDeadline;
    private MaterialButton btnPay;
    private OrderViewModel model;
    private LinearLayout lvContentLayout;
    private CarmenFeature reutlingen;
    private CarmenFeature berlin;
    private SimpleDateFormat simpleDateFormat;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cartView = inflater.inflate(R.layout.fragment_cart, container, false);
        //Initialize places
        Places.initialize(getActivity().getApplicationContext(), DelifastConstants.APIKEY);
        initView(cartView);
        initListeners();
        addUserLocations();
        simpleDateFormat = new SimpleDateFormat(DelifastConstants.TIMEFORMAT);
        model = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        model.getOrder().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                lvContentLayout.removeAllViewsInLayout();
                updateUI(order);
            }
        });
        return cartView;
    }

    private void initListeners() {
        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(DelifastConstants.MAPBOX_ACCESS_TOKEN)
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .addInjectedFeature(reutlingen)
                                .addInjectedFeature(berlin)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, 100);
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
                if (s.length() > 0) {
                    model.setUserDeposit(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.saveOrder();

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
        for (int i = 0; i < orderPositions.size(); i++) {
            OrderPosition currentOrderPosition = orderPositions.get(i);
            final View positionCarfView = getLayoutInflater().inflate(R.layout.order_position_in_cart, null, false);

            TextView productNameInCart = positionCarfView.findViewById(R.id.tvOrderPositionNameInCart);
            TextView amountInCart = positionCarfView.findViewById(R.id.tvOrderPositionCountInCart);
            MaterialButton btnDeletePosition = positionCarfView.findViewById(R.id.btnOrderPositionInCart);

            productNameInCart.setText(orderPositions.get(i).getProduct().getName());
            amountInCart.setText(Integer.toString(orderPositions.get(i).getAmount()));

            btnDeletePosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOrderPositionInViewModel(currentOrderPosition);
                }
            });


            lvContentLayout.addView(positionCarfView);

            /*
            LinearLayout li = new LinearLayout(getContext());
            li.setOrientation(LinearLayout.HORIZONTAL);
            MaterialTextView tv = new MaterialTextView(getContext());
            tv.setText(orderPositions.get(i).getProduct().getName());
            li.addView(tv);
            lvContentLayout.addView(li);
            */

        }
        etServiceFee.setText(String.valueOf(order.getServiceFee()));
        etUserDeposit.setText(String.valueOf(order.getUserDeposit()));
        if (order.getDeadline() != null) {
            etDeadline.setText(simpleDateFormat.format(order.getDeadline()));
        }
        if (order.getCustomerAddress() != null) {
            etAddress.setText(order.getCustomerAddress().getAddressString());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AutocompleteActivity.RESULT_OK && requestCode == 100) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            try {
                Point point = (Point) feature.geometry();
                model.setCustomerAddress(point.coordinates(), feature.placeName());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Invalid address provided", Toast.LENGTH_SHORT);
            }
            etAddress.setText(feature.placeName());
            Toast.makeText(getActivity(), feature.text(), Toast.LENGTH_LONG).show();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getActivity().getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
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
        btnPay = cartView.findViewById(R.id.btnPay);
    }

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
     * Predefines some user locations for etAddress
     */
    private void addUserLocations() {
        reutlingen = CarmenFeature.builder().text("Reutlingen")
                .geometry(Point.fromLngLat(48.48296039690456, 9.187737058082885))
                .placeName("Fakultät Informatik, 72762 Reutlingen")
                .id("mapbox-inf")
                .properties(new JsonObject())
                .build();
        berlin = CarmenFeature.builder().text("Berlin")
                .placeName("Pariser Platz, 10117 Berlin")
                .geometry(Point.fromLngLat(52.51628219848714, 13.377700670884066))
                .id("mapbox-ber")
                .properties(new JsonObject())
                .build();
    }


    /**
     * Delete the order position from the order in view model
     *
     * @param orderPosition
     */
    public void deleteOrderPositionInViewModel(OrderPosition orderPosition){
        model.deleteOrderPositionFromOrder(orderPosition);
    }
}