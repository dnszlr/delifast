package mobile_computing.delifast.interaction.order;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.SSLEngineResult;

import mobile_computing.delifast.R;
import mobile_computing.delifast.others.DelifastConstants;

public class CartFragment extends Fragment {

    private TextInputEditText etCartSum, etSupplyPrice, etServiceFee, etAdress, etDateTime;
    private TextView test_adress;
    private OrderViewModel model;
    private TextInputLayout ilAdress;
    private LinearLayout lvContentLayout;
    private CarmenFeature home;
    private CarmenFeature work;

    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoibWthbGFzaCIsImEiOiJja3AyYWVsNm0xMjltMndsZ3FqZXhnZG11In0.G0zqmJ50IGR31LpPx82LNg";


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

        initView(cartView);

        etDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(etDateTime);
            }
        });

        addUserLocations();
        model = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        model.getOrder().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                for (int i = 0; i < order.getOrderPositions().size(); i++) {
                    LinearLayout li = new LinearLayout(getContext());
                    li.setOrientation(LinearLayout.HORIZONTAL);
                    TextView tv = new TextView(getContext());
                    tv.setText(order.getOrderPositions().get(i).getProduct().getName());
                    li.addView(tv);
                    lvContentLayout.addView(li);
                }
            }
        });

        //Initialize places
        Places.initialize(getActivity().getApplicationContext(), DelifastConstants.APIKEY);

        etAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(MAPBOX_ACCESS_TOKEN)
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .addInjectedFeature(home)
                                .addInjectedFeature(work)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, 100);
            }
        });

        return cartView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AutocompleteActivity.RESULT_OK && requestCode == 100) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            etAdress.setText(feature.placeName());

            Toast.makeText(getActivity(), feature.text(), Toast.LENGTH_LONG).show();
            Log.d("Test_Adress", "ToString: " + feature.toString() + ", PlaceName: " + feature.placeName());
            Log.d("Test_Adress", "BBox: " + feature.bbox() + ", Adress: " + feature.address());
            Log.d("Test_Adress", "ID: " + feature.id() + ", Text: " + feature.text());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getActivity().getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void initView(View cartView) {
        lvContentLayout = cartView.findViewById(R.id.contentLayout);
        etCartSum = cartView.findViewById(R.id.etCartSum);
        etDateTime = cartView.findViewById(R.id.etDateTime);
        etDateTime.setInputType(InputType.TYPE_NULL);
        etSupplyPrice = cartView.findViewById(R.id.etSupplyPrice);
        etServiceFee = cartView.findViewById(R.id.etServiceFee);
        etAdress = cartView.findViewById(R.id.etAdress);
        ilAdress = cartView.findViewById(R.id.ilAdress);
        test_adress = cartView.findViewById(R.id.test_adress);
    }


    private void showDateTimeDialog(TextInputEditText etDateTime) {
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

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

                        etDateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(getActivity(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

            }
        };

        new DatePickerDialog(getActivity(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void addUserLocations() {
        home = CarmenFeature.builder().text("Mapbox SF Office")
                .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
                .placeName("50 Beale St, San Francisco, CA")
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();

        work = CarmenFeature.builder().text("Mapbox DC Office")
                .placeName("740 15th Street NW, Washington DC")
                .geometry(Point.fromLngLat(-77.0338348, 38.899750))
                .id("mapbox-dc")
                .properties(new JsonObject())
                .build();
    }

}