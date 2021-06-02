package mobilecomputing.delifast.interaction;

import androidx.lifecycle.ViewModel;

import mobilecomputing.delifast.interaction.delivery.DeliveryFragment;
import mobilecomputing.delifast.interaction.notification.NotificationFragment;
import mobilecomputing.delifast.interaction.order.ParentOrderFragment;
import mobilecomputing.delifast.interaction.profile.ProfileFragment;

public class DelifastViewModel extends ViewModel {

    private ParentOrderFragment orderFragment;
    private DeliveryFragment deliveryFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;

    public DelifastViewModel() {
        this.orderFragment = new ParentOrderFragment();
        this.deliveryFragment = new DeliveryFragment();
        this.notificationFragment = new NotificationFragment();
        this.profileFragment = new ProfileFragment();
    }

    public ParentOrderFragment getOrderFragment() {
        return orderFragment;
    }

    public void setOrderFragment(ParentOrderFragment orderFragment) {
        this.orderFragment = orderFragment;
    }

    public DeliveryFragment getDeliveryFragment() {
        return deliveryFragment;
    }

    public void setDeliveryFragment(DeliveryFragment deliveryFragment) {
        this.deliveryFragment = deliveryFragment;
    }

    public NotificationFragment getNotificationFragment() {
        return notificationFragment;
    }

    public void setNotificationFragment(NotificationFragment notificationFragment) {
        this.notificationFragment = notificationFragment;
    }

    public ProfileFragment getProfileFragment() {
        return profileFragment;
    }

    public void setProfileFragment(ProfileFragment profileFragment) {
        this.profileFragment = profileFragment;
    }
}
