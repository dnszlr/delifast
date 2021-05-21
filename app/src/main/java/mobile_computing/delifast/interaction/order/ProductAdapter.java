package mobile_computing.delifast.interaction.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import mobile_computing.delifast.R;
import mobile_computing.delifast.entities.Product;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private int resource;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String productNameStr = getItem(position).getName();
        String category = getItem(position).getCategory().getProductCategory();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView productNameTextView = convertView.findViewById(R.id.tvProductName);
        productNameTextView.setText(productNameStr);

        return convertView;
    }
}
