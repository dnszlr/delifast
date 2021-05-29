package mobilecomputing.delifast.interaction.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mobilecomputing.delifast.R;

public class ProfileOrdersFragment extends Fragment {

    private ImageView imgProfileOrders;

    public ProfileOrdersFragment() {
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
        View profileOrdersView = inflater.inflate(R.layout.fragment_profile_orders, container, false);

        imgProfileOrders = profileOrdersView.findViewById(R.id.imgProfileOrders);
        ViewCompat.setTransitionName(imgProfileOrders, "transationOrdersInProfile");

        return profileOrdersView;
    }
}