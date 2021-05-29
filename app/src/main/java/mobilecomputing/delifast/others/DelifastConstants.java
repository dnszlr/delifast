package mobilecomputing.delifast.others;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;

public class DelifastConstants {

    public final static int TIMEOUT = 1500;

    // Authentication
    public static final String MISSINGUSERNAME = "Benutzername erforderlich";
    public static final String MISSINGEMAIL = "Email-Adresse erforderlich";
    public static final String INVALIDEMAIL = "Ungültige Email-Adresse";
    public static final String INVALIDPASSWORD = "Passwort muss min. 8 Zeichen haben";
    public static final int RC_SIGN_IN = 123;

    // CartFragment
    public static final String MISSING_ADRESS = "Adresse erforderlich";
    public static final String MISSING_DEADLINE = "Zeit erforderlich";
    public static final String MISSING_PRODUCTS = "Keine Produkte ausgewählt";

    // Collections
    public static final String USERCOLLECTION = "users";
    public static final String PRODUCTCOLLECTION = "products";
    public static final String ORDERCOLLECTION = "orders";

    // Order
    public static final String PRODUCTS = "Produkte";
    public static final String CART = "Warenkorb";

    //Google API key
    public static final String APIKEY = "AIzaSyCiFrXdvgEz5REWj3jWC8cptl72kQ59gl0";
    // Mapbox API key
    public static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoibWthbGFzaCIsImEiOiJja3AyYWVsNm0xMjltMndsZ3FqZXhnZG11In0.G0zqmJ50IGR31LpPx82LNg";
    public static final String TIMEFORMAT = "dd.MM.yy HH:mm";
    public static final int MAPBOX_REQUEST_TOKEN = 100;

    // Braintree
    public static final int PAYMENT_REQUEST_CODE = 7171;
    public static final String TOKENIZATION_KEY = "sandbox_jysdqffh_kw5tm45ddfp9f34v";

    public static final CarmenFeature REUTLINGEN = CarmenFeature.builder().text("Reutlingen")
            .geometry(Point.fromLngLat(9.187737058082885, 48.48296039690456))
            .placeName("Fakultät Informatik, 72762 Reutlingen")
            .id("mapbox-inf")
            .properties(new JsonObject())
            .build();
    public static final CarmenFeature BERLIN = CarmenFeature.builder().text("Berlin")
            .placeName("Pariser Platz, 10117 Berlin")
            .geometry(Point.fromLngLat(13.377700670884066, 52.51628219848714))
            .id("mapbox-ber")
            .properties(new JsonObject())
            .build();
    public static final int PERMISSION_REQUEST_CODE = 200;
}
