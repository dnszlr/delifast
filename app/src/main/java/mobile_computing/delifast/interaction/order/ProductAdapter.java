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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import mobile_computing.delifast.R;
import mobile_computing.delifast.entities.Product;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private int resource;
    private ArrayList<Product> products;
    // Declaration of the elements in a one row of the list
    private ImageView imgProduct;
    private TextView tvProductName;
    private TextView tvProductCount;
    private Button btnPlus;
    private Button btnMinus;


    public ProductAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.products = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        tvProductName = convertView.findViewById(R.id.tvProductName);
        tvProductName.setText(product.getName());

        imgProduct = convertView.findViewById(R.id.imgProduct);
        tvProductCount = convertView.findViewById(R.id.tvProductCount);
        btnPlus = convertView.findViewById(R.id.btnPlus);

        // TODO: implement the onClick method for the plus and minus bttons

        return convertView;
    }


    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
}
