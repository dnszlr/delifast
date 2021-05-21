package mobile_computing.delifast.authentication;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

import mobile_computing.delifast.entities.User;
import mobile_computing.delifast.repositories.FirebaseAuthRepository;
import mobile_computing.delifast.repositories.UserRepository;

public class AuthenticationViewModel extends ViewModel {

    private UserRepository userRepository;
    private FirebaseAuthRepository firebaseAuthRepository;
    private MutableLiveData<FirebaseUser> firebaseUser;

    public AuthenticationViewModel() {

        this.userRepository = new UserRepository();
        this.firebaseAuthRepository = new FirebaseAuthRepository();
        this.firebaseUser = new MutableLiveData<>();
    }

    /**
     * @return Returns the MutableLiveData Object from the Repository
     */
    public MutableLiveData<FirebaseUser> getFirebaseUser() {
        if (this.firebaseUser == null) {
            this.firebaseUser = firebaseAuthRepository.getFirebaseUser();
        }
        return firebaseUser;
    }

    /**
     * @return Returnes the currently logged in user
     */
    public FirebaseUser getCurrentUser() {
        return firebaseAuthRepository.getCurrentUser();
    }

    /**
     * Save a new user object to the firestore database
     *
     * @param user
     */
    public void save(User user) {
        if (user != null) {
            userRepository.save(user);
        }
    }

    /**
     * Initialize registration process with email
     *
     * @param email    Email give by user
     * @param password Password given by user
     */
    public void registration(String email, String password) {
        if (email != null && password != null) {
            firebaseAuthRepository.createAccount(email, password);
        } else {
            Log.w("Email login registration", "Credentials unreadable");
        }
    }

    /**
     * Initialize login process with email
     *
     * @param email    Email give by user
     * @param password Password given by user
     */
    public void loginWithEmail(String email, String password) {
        if (email != null && password != null) {
            firebaseAuthRepository.loginWithEmailAndPassword(email, password);
        } else {
            Log.w("Email login failed", "Credentials unreadable");
        }

    }

    /**
     * Initialze login process with google
     *
     * @param idToken
     */
    public void loginWithGoogle(String idToken) {
        if (idToken != null) {
            firebaseAuthRepository.loginWithGoogle(idToken);
        } else {
            Log.w("Google login failed", "id unreadable");
        }
    }

    /**
     * Initialize login process with facebook
     *
     * @param accessToken
     */
    public void loginWithFacebook(AccessToken accessToken) {
        if (accessToken != null) {
            firebaseAuthRepository.loginWithFacebook(accessToken);
        } else {
            Log.w("Facebook login failed", "accessToken unreadable");
        }
    }
}
