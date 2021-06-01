package mobilecomputing.delifast.interaction.notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import mobilecomputing.delifast.entities.Notification;
import mobilecomputing.delifast.repositories.NotificationRepository;

public class NotificationViewModel extends ViewModel {

    private NotificationRepository notificationRepository;

    public NotificationViewModel() {
        this.notificationRepository = new NotificationRepository();
    }

    public void save(Notification notification) {
        if (notification != null) {
            notificationRepository.save(notification);
        }
    }

    public MutableLiveData<List<Notification>> getAllByUserId(String userId) {
        return notificationRepository.getAllByUserId(userId);
    }
}
