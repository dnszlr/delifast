package mobilecomputing.delifast.interaction.notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mobilecomputing.delifast.R;
import mobilecomputing.delifast.entities.Notification;
import mobilecomputing.delifast.interaction.delivery.DeliveryViewModel;

public class NotificationFragment extends Fragment {

    private NotificationViewModel viewModel;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View notificationView = inflater.inflate(R.layout.fragment_notification, container, false);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);


        return notificationView;
    }

    private void createNotificationCard(Notification notification){
        final View notificationCard = getLayoutInflater().inflate(R.layout.fragment_notification_card, null, false);

        ImageView image = notificationCard.findViewById(R.id.imgNotificationCard);
        TextView text = notificationCard.findViewById(R.id.tvNotificationCard);
        TextView date = notificationCard.findViewById(R.id.tvNotificationCardDate);

        switch (notification.getType()){
            case ORDER_ACCEPTED_BY_SUPPLIER:
                image.setImageResource(R.drawable.ic_baseline_done_24);

                break;
            case ORDER_CANCELED_BY_SUPPLIER:
                image.setImageResource(R.drawable.ic_baseline_close_24);
                break;
            case ORDER_DONE_BY_CUSTOMER:
                image.setImageResource(R.drawable.ic_baseline_qr_code_2_24);
                break;
            case RATING_CUSTOMER:
                image.setImageResource(R.drawable.ic_outline_star_24);
                break;
            case RATING_SUPPLIER:
                image.setImageResource(R.drawable.ic_outline_star_24);
                break;
            default:
                image.setImageResource(R.drawable.ic_baseline_close_24);
        }







    }
}