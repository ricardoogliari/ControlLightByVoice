package android.shift.estudo.com.controllightbyvoice;

import android.app.VoiceInteractor;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.VoiceInteractor;
import android.app.VoiceInteractor.PickOptionRequest;
import android.app.VoiceInteractor.PickOptionRequest.Option;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    
    public MyServices service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();

        Gson gson = new GsonBuilder() .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.51.27:8080")
                .addConverterFactory(PrimitiveConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        service = retrofit.create(MyServices.class);
    }

    public void go(){
/*        Option option = new Option("cheese", 0);
        option.addSynonym("ready");
        option.addSynonym("go");
        option.addSynonym("take it");
        option.addSynonym("ok");

        VoiceInteractor.Prompt prompt = new VoiceInteractor.Prompt(
                new String[]{
                        "cheese", "ready", "go", "take it", "ok"
                },
                "Bosta");

        Bundle bundle = new Bundle();

        VoiceInteractor vi = getVoiceInteractor();

        if (vi == null){
            Log.e("VOICE", "vi nulo");
        } else {
            vi.submitRequest(new PickOptionRequest(prompt, new Option[]{option}, bundle) {
                @Override
                public void onPickOptionResult(boolean finished, Option[] selections, Bundle result) {
                    if (finished && selections.length == 1) {
                        Log.e("VOICE", "entrou no primeiro if");
                    } else {
                        getActivity().finish();
                    }
                }

                @Override
                public void onCancel() {
                    getActivity().finish();
                }
            });
        }
*/
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Ligar ou Desligar?");
        try {
            startActivityForResult(intent, 10);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "speech not supported",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // here the string converted from your voice
                    String converted_text = (result.get(0));
                    if (converted_text.equalsIgnoreCase("ligar")){
                        ligar();
                    } else if (converted_text.equalsIgnoreCase("desligar")){
                        desligar();
                    }
                    Log.e("VOCE", "texto: " + converted_text);
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        go();
        return true;
    }

    public void ligar(){
        Call<Object> call = service.ligar();
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("VOCE", "sucesso: " + response.body());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("VOCE", "erro: " + t.getLocalizedMessage());
            }
        });
    }

    public void desligar(){
        Call<Object> call = service.desligar();
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("VOCE", "sucesso: " + response.body());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("VOCE", "erro: " + t.getLocalizedMessage());
            }
        });
    }
}
