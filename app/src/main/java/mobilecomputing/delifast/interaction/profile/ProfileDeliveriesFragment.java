package mobilecomputing.delifast.interaction.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import mobilecomputing.delifast.R;
import mobilecomputing.delifast.others.DelifastConstants;


public class ProfileDeliveriesFragment extends Fragment {


    private CardView cardViewDeliveries;
    private ImageView imgProfiledeliveries;

    private LinearLayout layoutProfileDeliveriesTransation;

    public ProfileDeliveriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileDeliveriesView = inflater.inflate(R.layout.fragment_profile_deliveries, container, false);

        initView(profileDeliveriesView);

        return profileDeliveriesView;

    }

    private void initView(View view) {
        cardViewDeliveries = view.findViewById(R.id.cardProfileDeliveries);
        layoutProfileDeliveriesTransation = view.findViewById(R.id.layoutProfileDeliveriesTransation);
        //ViewCompat.setTransitionName(cardViewDeliveries, DelifastConstants.TRANSATION_DELIVERIES_NAME);

    }
}