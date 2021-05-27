package mobilecomputing.delifast.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import mobilecomputing.delifast.interaction.DelifastActivity;
import mobilecomputing.delifast.R;
import mobilecomputing.delifast.entities.User;
import mobilecomputing.delifast.others.DelifastConstants;

public class RegisterFragment extends Fragment {

    private EditText tfUsername;
    private EditText tfEmail;
    private EditText tfPassword;
    private TextView btnRegister;
    private AuthenticationViewModel model;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View registerView = inflater.inflate(R.layout.register_fragment, container);
        model = new ViewModelProvider(getActivity()).get(AuthenticationViewModel.class);
        model.getFirebaseUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Log.d("FirebaseUser: ", firebaseUser.getDisplayName());
                User user = new User();
                user.setName(tfUsername.toString().trim());
                user.setEmail(tfEmail.toString().trim());
                model.save(user, firebaseUser.getUid());
                Intent homepage = new Intent(getActivity(), DelifastActivity.class);
                startActivity(homepage);
            }
        });
        initView(registerView);
        return registerView;
    }

    /**
     * Initializes all view components
     *
     * @param registerView
     */
    public void initView(View registerView) {
        btnRegister = registerView.findViewById(R.id.btnRegRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        tfUsername = registerView.findViewById(R.id.tfRegName);
        tfEmail = registerView.findViewById(R.id.tfRegEmail);
        tfPassword = registerView.findViewById(R.id.tfRegPassword);
    }

    /**
     * Triggers viewmodels registration process
     */
    private void registerUser() {
        String username = tfUsername.getText().toString().trim();
        String email = tfEmail.getText().toString().trim();
        String password = tfPassword.getText().toString().trim();

        if (!areCredentailsValid(username, email, password)) {
            return;
        }
        model.registration(email, password);
    }


    /**
     * Validates the credentials passed by the user.
     *
     * @param username
     * @param email
     * @param password
     * @return true if the credentials are valid, false if not.
     */
    private boolean areCredentailsValid(String username, String email, String password) {
        if (username.isEmpty()) {
            tfUsername.setError(DelifastConstants.MISSINGUSERNAME);
            tfUsername.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            tfEmail.setError(DelifastConstants.MISSINGEMAIL);
            tfEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tfEmail.setError(DelifastConstants.INVALIDEMAIL);
            tfEmail.requestFocus();
            return false;
        }
        if (password.length() < 8) {
            tfPassword.setError(DelifastConstants.INVALIDPASSWORD);
            tfPassword.requestFocus();
            return false;
        }
        return true;
    }
}
