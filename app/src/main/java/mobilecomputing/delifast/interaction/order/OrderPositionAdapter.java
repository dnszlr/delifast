package mobilecomputing.delifast.interaction.order;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import mobilecomputing.delifast.R;
import mobilecomputing.delifast.entities.OrderPosition;
import mobilecomputing.delifast.others.IconHelper;

public class OrderPositionAdapter extends ArrayAdapter<OrderPosition> {

    private Context context;
    private int resource;
    // Declaration of the elements in a one row of the list
    private ImageView imgProduct;
    private TextView tvProductName;
    private TextView tvProductCount;
    private Button btnPlus;
    private Button btnMinus;
    private OrderFragment fragment;


    public OrderPositionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrderPosition> objects, OrderFragment fragment) {
        super(context, resource, objects);
        Log.d("", "objects size: " + objects.size());
        this.context = context;
        this.resource = resource;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OrderPosition orderPosition = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);
        initView(convertView);

        tvProductName.setText(orderPosition.getProduct().getName());

        tvProductCount.setText(String.valueOf(orderPosition.getAmount()));
        imgProduct.setImageResource(IconHelper.getIcon(orderPosition.getProduct().getCategory()));
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = orderPosition.getAmount() + 1;
                orderPosition.setAmount(amount);
                fragment.editOrderPositionsInViewModel(orderPosition);
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = orderPosition.getAmount() - 1;
                if (amount >= 0) {
                    orderPosition.setAmount(amount);
                    fragment.editOrderPositionsInViewModel(orderPosition);
                }
            }
        });
        return convertView;
    }

    /**
     * Initializes all view components
     *
     * @param convertView
     */
    private void initView(View convertView) {
        tvProductName = convertView.findViewById(R.id.tvProductName);
        imgProduct = convertView.findViewById(R.id.imgProduct);
        tvProductCount = convertView.findViewById(R.id.tvProductCount);
        btnPlus = convertView.findViewById(R.id.btnPlus);
        btnMinus = convertView.findViewById(R.id.btnMinus);
    }
}
