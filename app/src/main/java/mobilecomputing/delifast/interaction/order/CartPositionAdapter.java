package mobilecomputing.delifast.interaction.order;

import android.content.Context;
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

public class CartPositionAdapter extends ArrayAdapter<OrderPosition> {

    private Context context;
    private int resource;
    private ArrayList<OrderPosition> orderPositions;
    private CartFragment fragment;
    // Declaration of the elements in a one row of the list
    private ImageView imgProduct;
    private TextView tvProductName;
    private TextView tvProductCount;
    private Button btnCancel;


    public CartPositionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrderPosition> objects, CartFragment fragment) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.orderPositions = objects;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OrderPosition selectedPosition = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);
        initView(convertView);

        tvProductName.setText(selectedPosition.getProduct().getName());
        tvProductCount.setText(String.valueOf(selectedPosition.getAmount()));
        imgProduct.setImageResource(IconHelper.getIcon(selectedPosition.getProduct().getCategory()));
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
        btnCancel = convertView.findViewById(R.id.btnCancel);
    }


}
