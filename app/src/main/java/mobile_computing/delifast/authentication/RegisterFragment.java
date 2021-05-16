package mobile_computing.delifast.authentication;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import mobile_computing.delifast.R;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    private EditText userName, eMailAdress, password;
    private TextView btnRegister;

    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View registerView =  inflater.inflate(R.layout.register_fragment, container);

        mAuth = FirebaseAuth.getInstance();

        btnRegister = registerView.findViewById(R.id.btnRegRegister);
        btnRegister.setOnClickListener((View.OnClickListener) this);

        userName = registerView.findViewById(R.id.tfRegName);
        eMailAdress = registerView.findViewById(R.id.tfRegEmail);
        password = registerView.findViewById(R.id.tfRegPassword);



        return registerView;
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
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }
}
