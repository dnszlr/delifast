package mobile_computing.delifast.authentication;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import mobile_computing.delifast.R;
import mobile_computing.delifast.others.DelifastConstants;

public class LoginFragment extends Fragment {

    private EditText tfEmail;
    private EditText tfPassword;
    private LinearProgressIndicator progressBarLogin;
    private Button btnLogin;
    private AuthenticationViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginView = inflater.inflate(R.layout.login_fragment, container);
        model = new ViewModelProvider(getActivity()).get(AuthenticationViewModel.class);

        initView(loginView);
        return loginView;
    }

    /**
     * Initializes all view components
     *
     * @param loginView
     */
    public void initView(View loginView) {
        tfEmail = loginView.findViewById(R.id.tfEmail);
        tfPassword = loginView.findViewById(R.id.tfPassword);
        progressBarLogin = loginView.findViewById(R.id.progressBarLogin);
        btnLogin = loginView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    /**
     * Triggers viewmodels login process
     */
    private void loginUser() {
        String email = tfEmail.getText().toString().trim();
        String password = tfPassword.getText().toString().trim();
        if (!areCredentailsValid(email, password)) {
            return;
        }
        progressBarLogin.setVisibility(View.VISIBLE);
        model.loginWithEmail(email, password);
    }

    /**
     * Validates the passed user credentials for correct form.
     *
     * @param email
     * @param password
     * @return true if credentails are valid, false if not.
     */
    private boolean areCredentailsValid(String email, String password) {
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
