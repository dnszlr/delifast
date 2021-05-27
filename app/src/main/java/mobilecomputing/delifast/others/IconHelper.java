package mobilecomputing.delifast.others;

import mobilecomputing.delifast.delifastEnum.ProductCategory;
import mobilecomputing.delifast.R;

public class IconHelper {

    /**
     * function, that selects automatically which img to display depending
     * on category the  of the item in the database
     */
    public static int getIcon(ProductCategory productCategory) {
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
