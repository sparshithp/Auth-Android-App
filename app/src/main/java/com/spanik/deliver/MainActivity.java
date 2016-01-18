package com.spanik.deliver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private EditText emailET;
    private EditText pwdET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String token = pref.getString("token", null);
        if(token != null){
            AsyncHttpClient cl = new AsyncHttpClient();
            cl.addHeader("x-access-token", token);
            cl.get("http://10.0.2.2:8080/api/check", new RequestParams(), new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                    JSONObject res = null;
                    try {
                        res = new JSONObject(new String(bytes));

                        if(res.getBoolean("success")){
                            Intent createOrderIntent = new Intent(getApplicationContext(), OrderActivity.class);
                            startActivity(createOrderIntent);
                            finish();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Invalid JSON response", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {

                }
            });
        }
    }

    public void login(View view) {
        emailET = (EditText) findViewById(R.id.userEmailEditText);
        pwdET = (EditText) findViewById(R.id.userPasswordEditText);
        String email = emailET.getText().toString();
        String pwd = pwdET.getText().toString();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", pwd);
        String url = "http://10.0.2.2:8080/api/authenticate";

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    JSONObject obj = new JSONObject(new String(bytes));
                    if(!(Boolean)obj.get("success")){
                        Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                    }else{
                       // Toast.makeText(getApplicationContext(),"Token: "+ obj.get("token"), Toast.LENGTH_LONG).show();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token", (String) obj.get("token"));
                        editor.commit();
                        Log.i("token", (String) obj.get("token"));
                        Log.i("token", pref.getString("token", null));
                        Intent createOrderIntent = new Intent(getApplicationContext(), OrderActivity.class);
                        startActivity(createOrderIntent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {

            }


        });

    }

    public void navigateToSignUp(View view) {
        Intent navToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(navToSignUp);
    }
}
