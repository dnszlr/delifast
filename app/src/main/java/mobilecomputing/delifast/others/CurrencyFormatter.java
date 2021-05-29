package mobilecomputing.delifast.others;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyFormatter {

    /**
     * Round a double value to a value with only
     * 2 positions after the comma and returns the currency representation 0.00
     *
     * @param value: the double value to be rounded
     */
    public static String doubleToUIRep(double value) {
        DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        decimalSymbols.setDecimalSeparator('.');
        decimalSymbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", decimalSymbols);
        int places = 2;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return df.format(bd);
    }
}
