package mobilecomputing.delifast.interaction.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import mobilecomputing.delifast.MainActivity;
import mobilecomputing.delifast.R;
import mobilecomputing.delifast.authentication.AuthenticationActivity;
import mobilecomputing.delifast.others.DelifastConstants;

public class ProfileFragment extends Fragment {

    private ImageView btnLogout;
    private Button btnLogout2;
    private ImageView imgProfileOrders, imgProfiledeliveries, imgProfileUserImage;
    private CardView cardProfileUserData, cardProfileOrders, cardProfileDeliveries, cardProfileRating;
    private TextView userName;

    private LinearLayout layoutProfileOrdersTransation, layoutProfileRatingTransition, layoutProfileDeliveriesTransation;
    private ConstraintLayout layoutProfileUserdataTransation;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ProfileViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        initView(profileView);
        setOnClickListener();

        return profileView;
    }

    private void initView(View view) {
        btnLogout = view.findViewById(R.id.btnProfileLogout);
        btnLogout2 = view.findViewById(R.id.btnProfileLogout2);

        imgProfileUserImage = view.findViewById(R.id.imgProfileUserImage);
        downloadPicture(FirebaseAuth.getInstance().getUid());

        cardProfileUserData = view.findViewById(R.id.cardProfileUserData);
        layoutProfileUserdataTransation = view.findViewById(R.id.layoutProfileUserdataTransation);

        userName = view.findViewById(R.id.tvProfileUserName);
        viewModel.getUserById(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), user -> {
            userName.setText(user.getName());
        });

        cardProfileOrders = view.findViewById(R.id.cardProfileOrders);
        layoutProfileOrdersTransation = view.findViewById(R.id.layoutProfileOrdersTransation);

        cardProfileDeliveries = view.findViewById(R.id.cardProfileDeliveries);
        layoutProfileDeliveriesTransation = view.findViewById(R.id.layoutProfileDeliveriesTransation);

        cardProfileRating = view.findViewById(R.id.cardProfileRating);
        layoutProfileRatingTransition = view.findViewById(R.id.layoutProfileRatingTransition);

    }

    private void setOnClickListener() {
        // on click listner for the logout button in the profile view
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delifastLogout();
            }
        });

        // on click listner for the logout2 button in the profile view
        btnLogout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delifastLogout();
            }
        });

        // on click listner for the user data card in the profile view
        cardProfileUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileUserDataFragment nextFrag = new ProfileUserDataFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addSharedElement(layoutProfileUserdataTransation, "transationDeliveriesInProfile")
                        .replace(R.id.fragmentLayout, nextFrag, null)
                        .addToBackStack(null)
                        .commit();
            }
        });


        // on click listner for the orders card in the profile view
        cardProfileOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileOrdersFragment nextFrag = new ProfileOrdersFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addSharedElement(layoutProfileOrdersTransation, "translationOrdersInProfile")
                        .replace(R.id.fragmentLayout, nextFrag, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // on click listner for the deliveries card in the profile view
        cardProfileDeliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDeliveriesFragment nextFrag = new ProfileDeliveriesFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addSharedElement(layoutProfileDeliveriesTransation, "transationDeliveriesInProfile")
                        .replace(R.id.fragmentLayout, nextFrag, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // on click listner for the rating card in the profile view
        cardProfileRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileRatingFragment nextFrag = new ProfileRatingFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addSharedElement(layoutProfileRatingTransition, "transationRatingInProfile")
                        .replace(R.id.fragmentLayout, nextFrag, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private void delifastLogout() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Logout")
                .setMessage("Möchten Sie sich wirklich abmelden?")
                .setPositiveButton("Logout", null)
                .setNegativeButton("abbrechen", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent loginPage = new Intent(getActivity(), AuthenticationActivity.class);
                        startActivity(loginPage);
                        getActivity().finish();
                        FirebaseAuth.getInstance().signOut();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void downloadPicture(String uId) {

        storageReference.child("images/" + uId).getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgProfileUserImage.setImageBitmap(bmp);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getContext(), "Das Bild wurde nicht geladen.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}