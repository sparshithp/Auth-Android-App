package com.spanik.deliver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameET;
    private EditText phoneET;
    private EditText pwdET;
    private EditText addressET;
    private EditText emailET;
    private EditText zipET;
    private EditText cityET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    public void signUp(View view) {
        nameET = (EditText) findViewById(R.id.fullNameSignUpEditText);
        pwdET = (EditText) findViewById(R.id.passwordSignUpEditText);
        phoneET = (EditText) findViewById(R.id.phoneSignUpEditText);
        addressET = (EditText) findViewById(R.id.addressSignUpEditText);
        emailET = (EditText) findViewById(R.id.emailSignUpEditText);
        cityET = (EditText) findViewById(R.id.citySignUpEditText);
        zipET = (EditText) findViewById(R.id.zipSignUpEditText);
        RequestParams params = new RequestParams();
        params.put("email", emailET.getText().toString());
        params.put("password", pwdET.getText().toString());
        params.put("name", nameET.getText().toString());
        params.put("address", addressET.getText().toString());
        params.put("city", cityET.getText().toString());
        params.put("zip", zipET.getText().toString());
        params.put("phone", phoneET.getText().toString());
        String url = "http://10.0.2.2:8080/signup";

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    JSONObject obj = new JSONObject(new String(bytes));
                    if(!(Boolean)obj.get("success")){
                        Log.i("yoyo", obj.getString("message"));
                        Log.i("yoyo", "heehaw");
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                    }else{
                        // Toast.makeText(getApplicationContext(),"Token: "+ obj.get("token"), Toast.LENGTH_LONG).show();
                        
                        Intent navigateToLoginIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(navigateToLoginIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("failure", "heehaw");

            }


        });

    }
}
