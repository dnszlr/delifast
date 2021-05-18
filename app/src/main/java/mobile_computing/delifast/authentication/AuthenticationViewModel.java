package mobile_computing.delifast.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
