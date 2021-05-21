package mobile_computing.delifast.repositories;

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

public class FirebaseAuthRepository {

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> firebaseUser;

    public FirebaseAuthRepository() {

        firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUser = new MutableLiveData<>();
    }

    public MutableLiveData<FirebaseUser> getFirebaseUser() {

        return firebaseUser;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Account created", "createUserWithEmail:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    firebaseUser.postValue(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Account not created", "createUserWithEmail:failure", task.getException());
                    // TODO create a toast to notify user that account creation was not successful.
                }
            }
        });
    }


    public void loginWithEmailAndPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoggedIn", "signInWithEmail:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    firebaseUser.postValue(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login failed", "signInWithEmail:failure", task.getException());
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
                    Log.d("LoggedIn", "signInWithCredential:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    firebaseUser.postValue(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login failed", "signInWithCredential:failure", task.getException());
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
                    Log.d("LoggedIn", "signInWithCredential:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    firebaseUser.postValue(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login failed", "signInWithCredential:failure", task.getException());
                    // TODO create a toast to notify user that login was not successful;
                }
            }
        });
    }
}
