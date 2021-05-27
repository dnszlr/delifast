package mobilecomputing.delifast.interaction.delivery;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import mobilecomputing.delifast.R;
import mobilecomputing.delifast.entities.Order;

public class DeliveryFragment extends Fragment {

    private DeliveryViewModel viewModel;

    private LinearLayout backlog;


    public DeliveryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View deliveryView = inflater.inflate(R.layout.fragment_delivery, container, false);

        initView(deliveryView);

        viewModel = new ViewModelProvider(this).get(DeliveryViewModel.class);
        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders.size() > 0){
                for (int i = 0; i < orders.size(); i++){
                    createCardInBacklog(orders.get(i));
                }
            }


        });


        // Inflate the layout for this fragment
        return deliveryView;
    }

    private void initView(View view) {
        backlog = view.findViewById(R.id.llBacklog);
    }

    private void createCardInBacklog(Order order){
        final View orderCard = getLayoutInflater().inflate(R.layout.fragment_delivery_cardview, null, false);


        ConstraintLayout layoutCardViewBacklog = orderCard.findViewById(R.id.layoutCardViewBacklog);
        CardView cardView = orderCard.findViewById(R.id.cardViewBacklog);

        TextView orderAdress = orderCard.findViewById(R.id.tvCardBacklogAddress);
        orderAdress.setText(order.getCustomerAddress().getAddressString());

        TextView orderDeadline = orderCard.findViewById(R.id.tvCardBacklogDeadline);
        orderDeadline.setText(order.getDeadline().toString());

        TextView orderSupplyPrice = orderCard.findViewById(R.id.tvCardBacklogSupplyPrice);
        orderSupplyPrice.setText(String.valueOf(order.getDeliveryPrice()));

        TextView orderBuyer = orderCard.findViewById(R.id.tvCardBacklogUserName);
        orderBuyer.setText(order.getCustomerID());

        TextView orderDeposit = orderCard.findViewById(R.id.tvCardBacklogUserDeposit);
        orderDeposit.setText(String.valueOf(order.getUserDeposit()));

        TextView orderSupplyPrice2 = orderCard.findViewById(R.id.tvCardBacklogSupplyPriceSmall);
        orderSupplyPrice2.setText(String.valueOf(order.getDeliveryPrice()));

        ConstraintLayout expandableView = orderCard.findViewById(R.id.constraintLayoutExpandableView);
        Button arrowDownUp = orderCard.findViewById(R.id.btnCardBacklogArrowDown);

        arrowDownUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandableView.getVisibility() == View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                    arrowDownUp.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }
                else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                    arrowDownUp.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
            }
        });

        backlog.addView(orderCard);
    }
}