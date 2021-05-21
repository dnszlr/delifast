package mobile_computing.delifast.interaction.delivery;

import androidx.lifecycle.ViewModel;

import mobile_computing.delifast.repositories.DeliveryRepository;

public class DeliveryViewModel extends ViewModel {

    private DeliveryRepository deliveryRepository;

    public DeliveryViewModel() {

        this.deliveryRepository = new DeliveryRepository();
    }
}
