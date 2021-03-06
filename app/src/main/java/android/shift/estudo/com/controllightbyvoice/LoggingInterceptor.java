package android.shift.estudo.com.controllightbyvoice;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by ricardoogliari on 7/22/16.
 */

public class LoggingInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.e("BIKOO", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        okhttp3.Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.e("BIKOO", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));


        final String responseString = new String(response.body().bytes());

        Log.e("BIKOO", "Response: " + responseString);

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), responseString))
                .build();
    }
}
