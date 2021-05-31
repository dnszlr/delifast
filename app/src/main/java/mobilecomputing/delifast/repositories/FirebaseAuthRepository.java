package mobilecomputing.delifast.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import mobilecomputing.delifast.others.DelifastHttpClient;
import mobilecomputing.delifast.others.DelifastTags;

public class FirebaseAuthRepository {

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> firebaseUser;

    public FirebaseAuthRepository() {

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUser = new MutableLiveData<>();
    }

    /**
     * Gives access to the observable MutableLiveData FirebaseUser Object
     *
     * @return MutableLiveData<FirebaseUser> firebaseUser
     */
    public MutableLiveData<FirebaseUser> getFirebaseUser() {

        return this.firebaseUser;
    }

    /**
     * Returns the currently logged in firebase user object
     *
     * @return FirebaseUser firebaseUser
     */
    public FirebaseUser getCurrentUser() {

        return firebaseAuth.getCurrentUser();
    }

    /**
     * Creates a new account at firebase authentication
     *
     * @param email    - the email provided by the user
     * @param password - the password provided by the user
     */
    public void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(DelifastTags.AUTHCREATEACCOUNT, "createUserWithEmail:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    firebaseUser.postValue(user);
                    createPaypalCustomer(user.getUid(), user.getEmail());
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(DelifastTags.AUTHCREATEACCOUNT, "createUserWithEmail:failure", task.getException());
                    // TODO create a toast to notify user that account creation was not successful.
                }
            }
        });
    }

    /**
     * Login validation for login with email and password
     *
     * @param email    - the email provided by the user
     * @param password - the password provided by the user
     */
    public void loginWithEmailAndPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(DelifastTags.AUTHLOGINEMAIL, "signInWithEmail:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    firebaseUser.postValue(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(DelifastTags.AUTHLOGINEMAIL, "signInWithEmail:failure", task.getException());
                    // TODO create a toast to notify user that login was not successful;
                }
            }
        });
    }

    /**
     * Sign in with google credentials
     *
     * @param idToken the id token provided by google
     */
    public void loginWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(DelifastTags.AUTHLOGINGOOGLE, "signInWithCredential:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                        Log.d("HAHA", "DID SOMETHING");
                        createPaypalCustomer(user.getUid(), user.getEmail());
                    }
                    firebaseUser.postValue(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(DelifastTags.AUTHLOGINGOOGLE, "signInWithCredential:failure", task.getException());
                    // TODO create a toast to notify user that login was not successful;
                }
            }
        });
    }

    /**
     * Sign in with facebook credentials
     *
     * @param token the AccessToken provided by facebook.
     */
    public void loginWithFacebook(AccessToken token) {

        Log.d("Facebook accessToken: ", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(DelifastTags.AUTHLOGINFB, "signInWithCredential:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                        createPaypalCustomer(user.getUid(), user.getEmail());
                    }
                    firebaseUser.postValue(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(DelifastTags.AUTHLOGINFB, "signInWithCredential:failure", task.getException());
                    // TODO create a toast to notify user that login was not successful;
                }
            }
        });
    }

    /**
     * Creates a new paypal customer for
     *
     * @param id    - the user id provided by firebase
     * @param email - the email provided by the user
     */
    private void createPaypalCustomer(String id, String email) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        requestParams.put("email", email);
        DelifastHttpClient.get("createuser", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // nothing to handle process done
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO maybe error handling in a later state of the project when creating paypal customer failed
            }
        });
    }
}