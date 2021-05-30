package mobilecomputing.delifast.interaction.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import mobilecomputing.delifast.R;

public class ProfileRatingFragment extends Fragment {


    private LinearLayout layoutProfileRatingTransition;

    public ProfileRatingFragment() {
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
       View profileRatingView = inflater.inflate(R.layout.fragment_profile_rating, container, false);

       initView(profileRatingView);

        return profileRatingView;
    }

    private void initView(View view) {
        layoutProfileRatingTransition = view.findViewById(R.id.layoutProfileRatingTransition);

    }
}