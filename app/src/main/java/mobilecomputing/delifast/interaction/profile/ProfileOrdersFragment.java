package mobilecomputing.delifast.interaction.profile;

import android.app.Dialog;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.Json;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.WriterException;
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
import mobilecomputing.delifast.entities.Product;
import mobilecomputing.delifast.others.CurrencyFormatter;
import mobilecomputing.delifast.others.DelifastConstants;
import mobilecomputing.delifast.others.DelifastHttpClient;
import mobilecomputing.delifast.others.QRCodeGenerator;

public class ProfileOrdersFragment extends Fragment {

    private CardView cardProfileOrders;
    private LinearLayout layoutProfileOrdersTransation;
    private TextView tvProfileOrderAddress, tvProfileOrderDeadline, tvProfileOrderStatus, tvProfileOrderSum, tvProfileOrderTime, tvProfileOrderRemarks;
    private ConstraintLayout constraintLayoutExpandableView, layoutProfileOrderSupplier;
    private ImageView imgProfileOrderSupplierImage;
    private RatingBar ratingProfileOrderSupplierRating;
    private MaterialButton btnConfirm, btnCancel;
    private ListView lvProfileOrderProducts;
    private LinearLayout containerProfileOrders;

    private SimpleDateFormat simpleDateFormat;

    private ProfileViewModel viewModel;


    public ProfileOrdersFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);

         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileOrdersView = inflater.inflate(R.layout.fragment_profile_orders, container, false);

        simpleDateFormat = new SimpleDateFormat(DelifastConstants.TIMEFORMAT, Locale.GERMANY);

        initView(profileOrdersView);

        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            containerProfileOrders.removeAllViewsInLayout();
            if (orders != null) {
                for (Order order : orders) {
                    createCardForOrder(order, inflater);
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
    }

    private void createCardForOrder(Order order, LayoutInflater inflater) {
        final View orderCard = getLayoutInflater().inflate(R.layout.fragment_profile_orders_card, null, false);

        CardView cardView = orderCard.findViewById(R.id.cardViewProfileOrder);

        TextView address = orderCard.findViewById(R.id.tvProfileOrderAddress);
        address.setText(order.getCustomerAddress().getAddressString());

        TextView deadline = orderCard.findViewById(R.id.tvProfileOrderDeadline);
        deadline.setText(simpleDateFormat.format(order.getDeadline()));

        TextView status = orderCard.findViewById(R.id.tvProfileOrderStatus);
        status.setText(order.getOrderStatus().getOrderType());

        TextView sum = orderCard.findViewById(R.id.tvProfileOrderSum);
        sum.setText(CurrencyFormatter.doubleToUIRep((order.getUserDeposit() + order.getServiceFee() + order.getCustomerFee())));

        TextView time = orderCard.findViewById(R.id.tvProfileOrderTime);
        time.setText(simpleDateFormat.format(order.getOrderTime()));

        TextView remarks = orderCard.findViewById(R.id.tvProfileOrderRemarks);
        remarks.setText(order.getDescription());

        LinearLayout products = orderCard.findViewById(R.id.lvProfileOrderProducts);
        for (OrderPosition op : order.getOrderPositions()) {
            final View productInCardView = getLayoutInflater().inflate(R.layout.fragment_delivery_cardview_product, null, false);

            TextView productName = productInCardView.findViewById(R.id.tvOrderPositionNameInBacklog);
            TextView amount = productInCardView.findViewById(R.id.tvOrderPositionCountInBacklog);

            productName.setText(op.getProduct().getName());
            amount.setText(Integer.toString(op.getAmount()));


            products.addView(productInCardView);
        }

        ConstraintLayout expandableView = orderCard.findViewById(R.id.constraintLayoutExpandableView);
        Button arrowDownUp = orderCard.findViewById(R.id.btnProfileOrderArrowDown);

        btnConfirm = cardView.findViewById(R.id.btnProfileOrderConfirm);
        btnCancel = cardView.findViewById(R.id.btnProfileOrderCancel);

        btnConfirm.setEnabled(order.getOrderStatus().equals(OrderStatus.ACCEPTED));
        btnCancel.setEnabled(order.getOrderStatus().equals(OrderStatus.OPEN));


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inflate the layout of the popup window
                View popupView = inflater.inflate(R.layout.popup_window, null);
                ImageView qrCode = popupView.findViewById(R.id.imgQRCode);
                try {
                    String amount = CurrencyFormatter.doubleToUIRep(order.getCustomerFee());
                    String supplierId = order.getSupplierID();
                    String transactionId = order.getTransactionID();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("amount", amount);
                    jsonObject.put("transactionId", transactionId);
                    jsonObject.put("supplierId", supplierId);
                    jsonObject.put("orderId", order.getId());

                    Bitmap bitmap = QRCodeGenerator.textToImage(jsonObject.toString(), 500, 500);

                    final Dialog dialog= new Dialog(getContext());
                    dialog.setContentView(R.layout.qr_popup_layout);
                    ImageView qrImage = dialog.findViewById(R.id.imgDialogQrCode);
                    qrImage.setImageBitmap(bitmap);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                } catch (WriterException | JSONException e) {
                    // TODO Implemented Toast response
                    e.printStackTrace();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Dialog with "Are you sure would be good"
                RequestParams requestParams = new RequestParams();
                requestParams.put("transactionId", order.getTransactionID());
                requestParams.put("amount", CurrencyFormatter.doubleToUIRep(order.getUserDeposit() + order.getServiceFee() + order.getCustomerFee()));
                requestParams.put("customerId", order.getCustomerID());
                DelifastHttpClient.post(DelifastConstants.SENDREFUND, requestParams, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        order.setOrderStatus(OrderStatus.CANCELED);
                        viewModel.updateOrder(order);
                        Toast.makeText(getActivity(), "Ihre Bestellung wurde storniert", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("Payment", "failure" + statusCode);
                        Toast.makeText(getActivity(), "Stornierung fehlgeschlagen, entschuldigen sie die Störung", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

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