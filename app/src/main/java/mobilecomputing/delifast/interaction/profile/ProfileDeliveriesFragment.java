package mobilecomputing.delifast.interaction.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import mobilecomputing.delifast.R;
import mobilecomputing.delifast.delifastEnum.OrderStatus;
import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.entities.OrderPosition;
import mobilecomputing.delifast.others.CurrencyFormatter;
import mobilecomputing.delifast.others.DelifastConstants;
import mobilecomputing.delifast.others.DelifastHttpClient;


public class ProfileDeliveriesFragment extends Fragment {


    private CardView cardViewDeliveries;
    private ImageView imgProfiledeliveries;
    private LinearLayout layoutProfileDeliveriesTransation;

    private ProfileViewModel viewModel;

    private LinearLayout containerProfileDeliveries;

    public ProfileDeliveriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileDeliveriesView = inflater.inflate(R.layout.fragment_profile_deliveries, container, false);

        initView(profileDeliveriesView);

        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            containerProfileDeliveries.removeAllViewsInLayout();
            if (orders != null) {
                for (Order order : orders) {
                    createCardForOrder(order, inflater);
                }
            }
        });
        viewModel.getAllBySupplierId(FirebaseAuth.getInstance().getUid());

        return profileDeliveriesView;
    }


    private void initView(View view) {
        cardViewDeliveries = view.findViewById(R.id.cardProfileDeliveries);
        layoutProfileDeliveriesTransation = view.findViewById(R.id.layoutProfileDeliveriesTransation);
        //ViewCompat.setTransitionName(cardViewDeliveries, DelifastConstants.TRANSATION_DELIVERIES_NAME);

        containerProfileDeliveries = view.findViewById(R.id.containerProfileDeliveries);
    }

    private void createCardForOrder(Order order, LayoutInflater inflater) {
        final View orderCard = getLayoutInflater().inflate(R.layout.fragment_profile_deliveries_card, null, false);

        CardView cardView = orderCard.findViewById(R.id.cardViewProfileDelivery);

        TextView address = orderCard.findViewById(R.id.tvProfileDeliveryAddress);
        address.setText(order.getCustomerAddress().getAddressString());

        TextView deadline = orderCard.findViewById(R.id.tvProfileDeliveryDeadline);
        deadline.setText(order.getDeadline().toString());

        TextView customerFee = orderCard.findViewById(R.id.tvProfileDeliveryCustomerFee);
        customerFee.setText(CurrencyFormatter.doubleToUIRep(order.getCustomerFee()));

        TextView status = orderCard.findViewById(R.id.tvProfileDeliveryStatus);
        status.setText(order.getOrderStatus().getOrderType());

        TextView time = orderCard.findViewById(R.id.tvProfileOrderTime);
        time.setText(order.getOrderTime().toString());

        TextView buyerName = orderCard.findViewById(R.id.tvProfileDeliveryBuyerName);
        viewModel.getUserById(order.getCustomerID()).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                buyerName.setText(user.getName());
            }
        });

        LinearLayout products = orderCard.findViewById(R.id.lvProfileDeliveryProducts);
        for (OrderPosition op : order.getOrderPositions()) {
            final View productInCardView = getLayoutInflater().inflate(R.layout.fragment_delivery_cardview_product, null, false);

            TextView productName = productInCardView.findViewById(R.id.tvOrderPositionNameInBacklog);
            TextView amount = productInCardView.findViewById(R.id.tvOrderPositionCountInBacklog);

            productName.setText(op.getProduct().getName());
            amount.setText(Integer.toString(op.getAmount()));


            products.addView(productInCardView);
        }

        ConstraintLayout expandableView = orderCard.findViewById(R.id.constraintLayoutExpandableView);

        Button arrowDownUp = orderCard.findViewById(R.id.btnProfileDeliveryArrowDown);
        arrowDownUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                    arrowDownUp.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                    arrowDownUp.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
            }
        });

        Button btnCancel = cardView.findViewById(R.id.btnProfileDeliveryCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCancelDeliveryDialog(order);
            }
        });

        containerProfileDeliveries.addView(orderCard);
    }

    private void openCancelDeliveryDialog(Order order){
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Auftrag ablehnen")
                .setMessage("Möchten Sie diesen Auftrag ablehnen?")
                .setPositiveButton("Ja", null)
                .setNegativeButton("Nein", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        order.setOrderStatus(OrderStatus.CANCELED);
                        viewModel.updateOrder(order);
                        Toast.makeText(getActivity(), "Ihre Auftrag wurde abgelehnt.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }
}