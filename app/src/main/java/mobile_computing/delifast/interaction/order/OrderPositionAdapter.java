package mobile_computing.delifast.interaction.order;

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

import mobile_computing.delifast.R;
import mobile_computing.delifast.delifastEnum.ProductCategory;
import mobile_computing.delifast.entities.OrderPosition;

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

        initView(convertView);
        tvProductName.setText(orderPosition.getProduct().getName());
        tvProductCount.setText(String.valueOf(orderPosition.getAmount()));
        imgProduct.setImageResource(getIcon(orderPosition.getProduct().getCategory()));
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

    /**
     *
     * function, that selects automaticly which img to display depending
     * on category the  of the item in the database
     *
     */


    private int getIcon(ProductCategory productCategory) {
        int image = 0;
        switch (productCategory) {
            case MEAT:
                image = R.drawable.meat;
                break;
            case BREAD:
                image = R.drawable.bread;
                break;
            case DRINK:
                image = R.drawable.drink;
                break;
            case FRUIT:
                image = R.drawable.fruit;
                break;
            case MILKPRODUCT:
                image = R.drawable.milkproduct;
                break;
            case OIL:
                image = R.drawable.oil;
                break;
            case OTHERS:
                image = R.drawable.others;
                break;
            case PASTA:
                image = R.drawable.pasta;
                break;
            case SEASONING:
                image = R.drawable.seasoning;
                break;
            case SWEETS:
                image = R.drawable.sweets;
                break;
            case VEGETABLES:
                image = R.drawable.vegetable;
                break;
        }
        return image;
    }
}
