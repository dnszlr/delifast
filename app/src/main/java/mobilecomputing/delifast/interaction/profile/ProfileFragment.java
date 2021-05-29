package mobilecomputing.delifast.interaction.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import mobilecomputing.delifast.MainActivity;
import mobilecomputing.delifast.R;
import mobilecomputing.delifast.authentication.AuthenticationActivity;

public class ProfileFragment extends Fragment {

    private ImageView btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        initView(profileView);

        return profileView;
    }

    private void initView(View profileView) {
        btnLogout = profileView.findViewById(R.id.btnProfileLogout);
        setOnClickListenerForLogout();
    }

    private void setOnClickListenerForLogout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }
}