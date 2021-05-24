package mobile_computing.delifast.interaction.order;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLEngineResult;

import mobile_computing.delifast.R;
import mobile_computing.delifast.others.DelifastConstants;

public class CartFragment extends Fragment {

    private TextInputEditText etCartSum, etSupplyPrice, etServiceFee, etAdress;
    private TextView test_adress;
    private CartPositionAdapter cartPositionAdapter;
    private OrderViewModel model;
    private ListView orderPositionList;


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

        model = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        model.getOrder().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                cartPositionAdapter = new CartPositionAdapter(getActivity(), R.layout.fragment_cart_adapter, order.getOrderPositions(), this);
                orderPositionList.setAdapter(cartPositionAdapter);
            }
        });

        //Initialize places
        Places.initialize(getActivity().getApplicationContext(), DelifastConstants.APIKEY);

        etAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getActivity());
                startActivityForResult(intent, 100);
            }
        });

        return cartView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);

            etAdress.setText(place.getAddress());

            test_adress.setText(String.format("Location: %s", place.getName()));

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getActivity().getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView(View cartView) {
        orderPositionList = cartView.findViewById(R.id.listViewCart);
        etCartSum = cartView.findViewById(R.id.etCartSum);
        etSupplyPrice = cartView.findViewById(R.id.etSupplyPrice);
        etServiceFee = cartView.findViewById(R.id.etServiceFee);
        etAdress = cartView.findViewById(R.id.etAdress);
        test_adress = cartView.findViewById(R.id.test_adress);
    }

}