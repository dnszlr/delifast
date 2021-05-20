package mobile_computing.delifast.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import mobile_computing.delifast.entities.User;
import mobile_computing.delifast.repositories.UserRepository;

public class AuthenticationViewModel extends ViewModel {

    private LiveData<User> user;
    private UserRepository userRepository;

    public AuthenticationViewModel() {
        this.userRepository = new UserRepository();
    }

    public LiveData<User> getUserById(String userId) {
        if (this.user == null) {
            this.user = userRepository.findById(userId);
        }
        return user;
    }

    public LiveData<List<User>> getAll() {
        return userRepository.getAll();
    }

    public void save(User user) {
        if(user != null) {
            userRepository.save(user);
        }
    }

    public void update(User user) {
        if(user != null) {
            userRepository.update(user);
        }
    }

    public LiveData<User> getUser() {
        return this.user;
    }

}
