package mobilecomputing.delifast.interaction.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobilecomputing.delifast.R;

public class ProfileUserDataFragment extends Fragment {

    private CardView cardProfileUserData;
    private ConstraintLayout layoutProfileUserdataTransation;

    public ProfileUserDataFragment() {
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
        View profileUserDataView = inflater.inflate(R.layout.fragment_profile_user_data, container, false);

        initView(profileUserDataView);

        return profileUserDataView;
    }

    private void initView(View view) {
        cardProfileUserData = view.findViewById(R.id.cardProfileUserData);
        layoutProfileUserdataTransation = view.findViewById(R.id.layoutProfileUserdataTransation);

    }
}