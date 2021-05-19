package mobile_computing.delifast.authentication;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import mobile_computing.delifast.R;

public class LoginFragment extends Fragment {

    private EditText email, password;
    private LinearProgressIndicator progressBarLogin;
    private FirebaseAuth mAuth;
    private Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginView =  inflater.inflate(R.layout.login_fragment, container);

        email = loginView.findViewById(R.id.tfEmail);
        password = loginView.findViewById(R.id.tfPassword);
        progressBarLogin = loginView.findViewById(R.id.progressBarLogin);
        btnLogin = loginView.findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                String eMailAdressStr = email.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();

                if(eMailAdressStr.isEmpty()){
                    email.setError("Email-Adresse erfordelich");
                    email.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(eMailAdressStr).matches()){
                    email.setError("Email ungültig");
                    email.requestFocus();
                    return;
                }

                if(passwordStr.length() < 8){
                    password.setError("Passwort muss min. 8 Zeichen haben");
                    password.requestFocus();
                    return;
                }

                progressBarLogin.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(eMailAdressStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Login erfolgreich", Toast.LENGTH_SHORT).show();

                            //TODO: start next activity
                        }
                        else {
                            Toast.makeText(getActivity(), "Falsche Login-Daten", Toast.LENGTH_SHORT).show();
                            progressBarLogin.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });



        return loginView;
    }
}
