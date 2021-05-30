package mobilecomputing.delifast.interaction.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import mobilecomputing.delifast.R;
import mobilecomputing.delifast.entities.Order;
import mobilecomputing.delifast.entities.OrderPosition;
import mobilecomputing.delifast.entities.Product;

public class ProfileOrdersFragment extends Fragment {

    private CardView cardProfileOrders;
    private LinearLayout layoutProfileOrdersTransation;
    private TextView tvProfileOrderAddress, tvProfileOrderDeadline, tvProfileOrderStatus, tvProfileOrderSum, tvProfileOrderTime, tvProfileOrderRemarks;
    private ConstraintLayout constraintLayoutExpandableView, layoutProfileOrderSupplier;
    private ImageView imgProfileOrderSupplierImage;
    private RatingBar ratingProfileOrderSupplierRating;
    private Button btnConfirm, btnCancel;
    private ListView lvProfileOrderProducts;

    private LinearLayout containerProfileOrders;

    private ProfileViewModel viewModel;


    public ProfileOrdersFragment() {
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
        View profileOrdersView = inflater.inflate(R.layout.fragment_profile_orders, container, false);

        initView(profileOrdersView);

        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            containerProfileOrders.removeAllViewsInLayout();
            if (orders != null){
                for(Order order: orders){
                    createCardForOrder(order);
                }
            }
        });
        viewModel.getAllByCustomerId(FirebaseAuth.getInstance().getUid());

        return profileOrdersView;
    }

    private void initView(View view) {
        cardProfileOrders = view.findViewById(R.id.cardProfileOrders);
        layoutProfileOrdersTransation = view.findViewById(R.id.layoutProfileOrdersTransation);

        containerProfileOrders = view.findViewById(R.id.containerProfileOrders);
        /**
        tvProfileOrderAddress = view.findViewById(R.id.tvProfileOrderAddress);
        tvProfileOrderDeadline = view.findViewById(R.id.tvProfileOrderDeadline);
        tvProfileOrderStatus = view.findViewById(R.id.tvProfileOrderStatus);

        constraintLayoutExpandableView = view.findViewById(R.id.constraintLayoutExpandableView);
        layoutProfileOrderSupplier = view.findViewById(R.id.layoutProfileOrderSupplier);

        imgProfileOrderSupplierImage = view.findViewById(R.id.imgProfileOrderSupplierImage);

        ratingProfileOrderSupplierRating = view.findViewById(R.id.ratingProfileOrderSupplierRating);

        btnConfirm = view.findViewById(R.id.btnProfileOrderConfirm);
        btnCancel = view.findViewById(R.id.btnProfileOrderCancel);

        tvProfileOrderSum = view.findViewById(R.id.tvProfileOrderSum);
        tvProfileOrderTime = view.findViewById(R.id.tvProfileOrderTime);

        tvProfileOrderRemarks = view.findViewById(R.id.tvProfileOrderRemarks);

        lvProfileOrderProducts = view.findViewById(R.id.lvProfileOrderProducts);
         **/

    }

    private void createCardForOrder(Order order){
        final View orderCard = getLayoutInflater().inflate(R.layout.fragment_profile_orders_card, null, false);

        CardView cardView = orderCard.findViewById(R.id.cardViewProfileOrder);

        TextView address = orderCard.findViewById(R.id.tvProfileOrderAddress);
        address.setText(order.getCustomerAddress().getAddressString());

        TextView deadline = orderCard.findViewById(R.id.tvProfileOrderDeadline);
        deadline.setText(order.getDeadline().toString());

        TextView status = orderCard.findViewById(R.id.tvProfileOrderStatus);
        status.setText(order.getOrderStatus().getOrderType());

        TextView sum = orderCard.findViewById(R.id.tvProfileOrderSum);
        sum.setText(String.valueOf(order.getUserDeposit() + order.getServiceFee() + order.getCustomerFee()));

        TextView time = orderCard.findViewById(R.id.tvProfileOrderTime);
        time.setText(order.getOrderTime().toString());

        TextView remarks = orderCard.findViewById(R.id.tvProfileOrderRemarks);
        remarks.setText(order.getDescription());

        LinearLayout products = orderCard.findViewById(R.id.lvProfileOrderProducts);
        for(OrderPosition op: order.getOrderPositions()){
            final View productInCardView = getLayoutInflater().inflate(R.layout.fragment_delivery_cardview_product, null, false);

            TextView productName = productInCardView.findViewById(R.id.tvOrderPositionNameInBacklog);
            TextView amount = productInCardView.findViewById(R.id.tvOrderPositionCountInBacklog);

            productName.setText(op.getProduct().getName());
            amount.setText(Integer.toString(op.getAmount()));


            products.addView(productInCardView);
        }

        ConstraintLayout expandableView = orderCard.findViewById(R.id.constraintLayoutExpandableView);
        Button arrowDownUp = orderCard.findViewById(R.id.btnProfileOrderArrowDown);

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

        containerProfileOrders.addView(orderCard);


    }
}