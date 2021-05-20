package mobile_computing.delifast.authentication;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.appevents.AppEventsLogger;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;

import mobile_computing.delifast.R;
import mobile_computing.delifast.entities.User;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText tfUsername;
    private EditText tfEmail;
    private EditText tfPassword;
    private TextView btnRegister;
    private AuthenticationViewModel model;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View registerView = inflater.inflate(R.layout.register_fragment, container);
        model = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        model.getFirebaseUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Log.d("FirebaseUser: ", firebaseUser.getDisplayName());
                User user = new User();
                user.setName(tfUsername.toString().trim());
                user.setEmail(tfEmail.toString().trim());
                model.save(user);
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
        btnRegister.setOnClickListener((View.OnClickListener) this);
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

    @Override
    public void onClick(View v) {
        registerUser();
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
            tfUsername.setError("Name erfordelich");
            tfUsername.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            tfEmail.setError("Email-Adresse erfordelich");
            tfEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tfEmail.setError("Email ungültig");
            tfEmail.requestFocus();
            return false;
        }
        if (password.length() < 8) {
            tfPassword.setError("Passwort muss min. 8 Zeichen haben");
            tfPassword.requestFocus();
            return false;
        }
        return true;
    }
}
