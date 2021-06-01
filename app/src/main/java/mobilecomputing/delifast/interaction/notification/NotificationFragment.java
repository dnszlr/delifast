package mobilecomputing.delifast.interaction.notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import mobilecomputing.delifast.R;
import mobilecomputing.delifast.entities.Notification;
import mobilecomputing.delifast.interaction.delivery.DeliveryViewModel;

public class NotificationFragment extends Fragment {

    private NotificationViewModel viewModel;

    private LinearLayout llNotificationContainer;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View notificationView = inflater.inflate(R.layout.fragment_notification, container, false);

        llNotificationContainer = notificationView.findViewById(R.id.llNotificationContainer);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        viewModel.getAllByUserId(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), notifications -> {
            if (notifications != null) {
                llNotificationContainer.removeAllViewsInLayout();
                Log.d("onCreateView", "Notifications size: " + notifications.size());
                for (int i = 0; i < notifications.size(); i++) {
                    createNotificationCard(notifications.get(i));
                }
            }
        });

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

        viewModel.getOrder(notification.getOrderId()).observe(getViewLifecycleOwner(), order -> {
            notification.createText(order);
        });

        text.setText(notification.getText());
        date.setText(notification.getNotificationTime().toString());

        llNotificationContainer.addView(notificationCard);
    }
}