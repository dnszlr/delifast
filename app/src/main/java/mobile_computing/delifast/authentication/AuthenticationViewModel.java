package mobile_computing.delifast.authentication;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import mobile_computing.delifast.entities.User;
import mobile_computing.delifast.repositories.UserRepository;

public class AuthenticationViewModel extends ViewModel {

    private LiveData<User> user;
    private UserRepository userRepository;

    public AuthenticationViewModel() {
        this.userRepository = new UserRepository();
    }

    public void init(String userId) {
        if (this.user != null) {
            return;
        }
        user = userRepository.findById(userId);
    }

    public void save(User user) {
        if(user != null) {
            userRepository.save(user);
        }
    }

    public LiveData<User> getUser() {
        return this.user;
    }


}
