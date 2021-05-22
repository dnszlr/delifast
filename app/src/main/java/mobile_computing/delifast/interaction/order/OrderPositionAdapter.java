package mobile_computing.delifast.interaction.order;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import mobile_computing.delifast.R;
import mobile_computing.delifast.entities.OrderPosition;
import mobile_computing.delifast.entities.Product;

public class OrderPositionAdapter extends ArrayAdapter<OrderPosition> {

    private Context context;
    private int resource;
    private ArrayList<OrderPosition> orderPositions;
    // Declaration of the elements in a one row of the list
    private ImageView imgProduct;
    private TextView tvProductName;
    private TextView tvProductCount;
    private Button btnPlus;
    private Button btnMinus;
    private OrderFragment fragment;


    public OrderPositionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrderPosition> objects, OrderFragment fragment) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.orderPositions = objects;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OrderPosition orderPosition = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        tvProductName = convertView.findViewById(R.id.tvProductName);
        tvProductName.setText(orderPosition.getProduct().getName());

        imgProduct = convertView.findViewById(R.id.imgProduct);
        tvProductCount = convertView.findViewById(R.id.tvProductCount);
        btnPlus = convertView.findViewById(R.id.btnPlus);

        tvProductCount.setText(String.valueOf(orderPosition.getAmount()));

        // TODO: implement the onClick method for the plus and minus bttons
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.editOrderPositionsInViewModel(position);
            }
        });

        return convertView;
    }

}
