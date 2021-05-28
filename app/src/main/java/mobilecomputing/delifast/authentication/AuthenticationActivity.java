package mobilecomputing.delifast.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mobilecomputing.delifast.entities.User;
import mobilecomputing.delifast.interaction.DelifastActivity;
import mobilecomputing.delifast.others.DelifastConstants;
import mobilecomputing.delifast.others.DelifastTags;
import mobilecomputing.delifast.R;

public class AuthenticationActivity extends AppCompatActivity {

    private TabLayout authTable;
    private ViewPager2 authViewpager;
    private LoginButton btnFbLogin;
    private SignInButton btnGoogleSignIn;
    private CallbackManager mCallbackManager;
    private AuthenticationViewModel model;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        model.getFirebaseUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Log.d("FirebaseUser: ", firebaseUser.getDisplayName());
                User user = new User();
                user.setName(firebaseUser.getDisplayName());
                user.setEmail(firebaseUser.getEmail());
                model.save(user, firebaseUser.getUid());
                Intent homepage = new Intent(AuthenticationActivity.this, DelifastActivity.class);
                startActivity(homepage);
            }
        });
        setContentView(R.layout.activity_authentication);
        initFragments();
        initFacebookSignIn();
        initGoogleSignIn();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == DelifastConstants.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(DelifastTags.GOOGLE, "firebaseAuthWithGoogle:" + account.getId());
                model.loginWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(DelifastTags.GOOGLE, "Google sign in failed", e);
                Toast.makeText(this, "Error Google Auth", Toast.LENGTH_SHORT).show();
            }
        }
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = model.getCurrentUser();
        if (currentUser != null) {
            //User already loggedIn, no need to login again.
            Log.w(DelifastTags.MAILLOGIN, "User" + currentUser.getDisplayName() + " loggedIn");
            Intent homepage = new Intent(AuthenticationActivity.this, DelifastActivity.class);
            startActivity(homepage);
        } else {
            // No user logged in, continue with AuthenticationActivity.
            Log.w(DelifastTags.MAILLOGIN, "No user loggedIn");
        }
    }

    /**
     * Setting up the Fragments used by this activity.
     */
    protected void initFragments() {
        authTable = findViewById(R.id.authTable);
        authViewpager = findViewById(R.id.authViewpager);
        authViewpager.setAdapter(new TabAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(authTable, authViewpager, new TabConfigurationStrategy());
        tabLayoutMediator.attach();
    }

    /**
     * Sets up facebooks SignIn
     */
    private void initFacebookSignIn() {
        // Facebook signIn
        mCallbackManager = CallbackManager.Factory.create();
        btnFbLogin = findViewById(R.id.btnFbLogin);
        btnFbLogin.setReadPermissions("email", "public_profile");
        btnFbLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(DelifastTags.FB, "facebook:onSuccess:" + loginResult);
                model.loginWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(DelifastTags.FB, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(DelifastTags.FB, "facebook:onError", error);
            }
        });
    }

    /**
     * Sets up googles sign in
     */
    private void initGoogleSignIn() {
        // Google signIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    /**
     * Initialize Google SignIn Method
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, DelifastConstants.RC_SIGN_IN);
    }
}