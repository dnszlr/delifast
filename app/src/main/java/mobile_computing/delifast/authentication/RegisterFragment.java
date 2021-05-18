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

import java.util.concurrent.Executor;

import mobile_computing.delifast.R;
import mobile_computing.delifast.entities.User;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    FragmentActivity activity;

    private EditText userName, eMailAdress, password;
    private TextView btnRegister;

    private TextInputLayout username, email;
    private LoginButton btnFbLogin;
    private CallbackManager callbackManager;
    private static final String TAG = "FacebookAuthentification";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private AccessTokenTracker accessTokenTracker;
    private AuthenticationViewModel model;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View registerView =  inflater.inflate(R.layout.register_fragment, container);

        model = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        btnRegister = registerView.findViewById(R.id.btnRegRegister);
        btnRegister.setOnClickListener((View.OnClickListener) this);

        userName = registerView.findViewById(R.id.tfRegName);
        eMailAdress = registerView.findViewById(R.id.tfRegEmail);
        password = registerView.findViewById(R.id.tfRegPassword);



        return registerView;
    }

    public EditText getUserName() {
        return userName;
    }

    private void registerUser(){
        String userNameStr = userName.getText().toString().trim();
        String eMailAdressStr = eMailAdress.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if(userNameStr.isEmpty()){
            userName.setError("Name erfordelich");
            userName.requestFocus();
            return;
        }

        if(eMailAdressStr.isEmpty()){
            eMailAdress.setError("Email-Adresse erfordelich");
            eMailAdress.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(eMailAdressStr).matches()){
            eMailAdress.setError("Email ungültig");
            eMailAdress.requestFocus();
            return;
        }

        if(passwordStr.length() < 8){
            password.setError("Passwort muss min. 8 Zeichen haben");
            password.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(eMailAdressStr, passwordStr);
        User user = new User();
        user.setEmail(eMailAdressStr);
        user.setName(userNameStr);
        model.save(user);
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookToken" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Sign in With Credential: successful");
                    FirebaseUser user = mAuth.getCurrentUser();

                    updateUI(user);
                }
                else {
                    Log.d(TAG, "Sign in With Credential: failure");
                    Toast.makeText(getActivity(), "Authentification Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }


    private void updateUI(FirebaseUser user) {
        if(user != null){
            System.out.println("Jaaa");
            username.getEditText().setText(user.getDisplayName());

        }
        else {
            username.getEditText().setText("KEIN USER");
        }
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }
}
