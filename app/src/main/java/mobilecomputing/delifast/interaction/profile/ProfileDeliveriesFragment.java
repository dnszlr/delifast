package mobilecomputing.delifast.interaction.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
    private LinearLayout containerProfileDeliveries;

    private ProfileViewModel viewModel;

    private SimpleDateFormat simpleDateFormat;


    private static final int QR_CODE_RQ_CODE = 85;

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

        simpleDateFormat = new SimpleDateFormat(DelifastConstants.TIMEFORMAT, Locale.GERMANY);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_CODE_RQ_CODE){
            if(resultCode == Activity.RESULT_OK){
                String qrCodeContent = data.getStringExtra("SCAN_RESULT");
                try {
                    JSONObject jsonObject = new JSONObject(qrCodeContent);

                    RequestParams rp = new RequestParams();
                    rp.put("amount", jsonObject.get("amount"));
                    rp.put("transactionId", jsonObject.get("transactionId"));
                    rp.put("supplierId", jsonObject.get("supplierId"));


                    DelifastHttpClient.post(DelifastConstants.SENDPAYMENT, rp, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if(statusCode == 200){
                                try {
                                    viewModel.getOrderById(jsonObject.get("orderId").toString()).observe(getViewLifecycleOwner(), order -> {
                                        if (order != null){
                                            order.setOrderStatus(OrderStatus.DONE);
                                            viewModel.createTransactionNotifications(order);
                                            viewModel.updateOrder(order);
                                            openThankYouDialog();
                                        }
                                    });
                                } catch (JSONException e) { e.printStackTrace(); }
                            }
                            else { Toast.makeText(getContext(), "Serverfehler" + statusCode, Toast.LENGTH_SHORT).show(); }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getContext(), "Abgebrochen", Toast.LENGTH_SHORT).show();
            }
        }
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
        deadline.setText(simpleDateFormat.format(order.getDeadline()));

        TextView customerFee = orderCard.findViewById(R.id.tvProfileDeliveryCustomerFee);
        customerFee.setText(CurrencyFormatter.doubleToUIRep(order.getCustomerFee()));

        TextView status = orderCard.findViewById(R.id.tvProfileDeliveryStatus);
        status.setText(order.getOrderStatus().getOrderType());

        TextView time = orderCard.findViewById(R.id.tvProfileOrderTime);
        time.setText(simpleDateFormat.format(order.getOrderTime()));

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

        Button btnConfirm = cardView.findViewById(R.id.btnProfileDeliveryConfirm);
        Button btnCancel = cardView.findViewById(R.id.btnProfileDeliveryCancel);

        btnConfirm.setEnabled(order.getOrderStatus().equals(OrderStatus.ACCEPTED));
        btnCancel.setEnabled(order.getOrderStatus().equals(OrderStatus.ACCEPTED));

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

                    startActivityForResult(intent, QR_CODE_RQ_CODE);
                }
                catch (Exception e) {
                    Log.d("QR_CODE: ", "Error --> " + e);
                    Toast.makeText(getContext(), "Fehler beom Öffnen der Kamera", Toast.LENGTH_SHORT).show();

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);
                }
            }
        });

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

                        viewModel.cancelDelivery(order);
                        Toast.makeText(getActivity(), "Ihre Auftrag wurde abgelehnt.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    private void openThankYouDialog() {
        final Dialog dialog= new Dialog(getContext());
        dialog.setContentView(R.layout.popup_thank_you);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 8000);
    }
}