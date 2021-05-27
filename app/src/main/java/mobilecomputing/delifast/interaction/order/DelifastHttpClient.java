package mobilecomputing.delifast.interaction.order;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class DelifastHttpClient {

    private static final String BASE_URL = "https://shaed.eu-de.mybluemix.net/mobile/";
    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static final String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
