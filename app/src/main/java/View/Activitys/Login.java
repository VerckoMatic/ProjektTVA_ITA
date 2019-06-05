package View.Activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.IOException;

import Model.Classes.Response;
import Model.Classes.User;
import network.NetworkUtil;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import utils.Constants;
import Model.UserValidation;

public class Login extends AppCompatActivity {

    public static Context context;
    TextView toRegisterActivity;
    TextView toLostPassword;
    EditText et_email;
    EditText et_password;
    Button button_login;
    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSubscriptions = new CompositeSubscription();
        initSharedPreferences();
        checkIfLogined();
        toRegisterActivity = (TextView) findViewById(R.id.tw_register);
        toLostPassword = (TextView) findViewById(R.id.tw_lostPassword);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        button_login = (Button) findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = et_email.getText().toString();
                String passwordStr = et_password.getText().toString();
                if(!UserValidation.validateLoginEmail(emailStr) || !UserValidation.validateLoginPassword(passwordStr)){
                    if(!UserValidation.validateLoginEmail(emailStr)){
                        et_email.setError("Vpišite email račun!");
                    }else if(!UserValidation.validateLoginPassword(passwordStr)){
                        et_password.setError("Vpišite geslo!");
                    }
                }else{
                    login();
                    et_email.getText().clear();
                    et_password.getText().clear();
                }

            }
        });

        toRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Register.class);
                startActivity(i);
            }
        });

        toLostPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ForgotPassword.class);
                startActivity(i);
            }
        });
    }

    public void login(){

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mSubscriptions.add(NetworkUtil.getRetrofit(email, password).login(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(Response response) {


        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN,response.getToken());
        editor.putString(Constants.EMAIL,response.getMessage());
        editor.putString(Constants.IDUSER, response.getIdUser());
        editor.putString(Constants.IMAGE, response.getImage());
        editor.apply();

        et_email.setText(null);
        et_password.setText(null);

        Intent intent = new Intent(getApplicationContext(), Homepage.class);
        startActivity(intent);

    }

    @SuppressLint("WrongConstant")
    private void handleError(Throwable error) {


        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Toast.makeText(this, "Response error:" + response.getMessage(), 2).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this, "Network Error", 2).show();
        }
    }

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    private void checkIfLogined(){
        String token = mSharedPreferences.getString(Constants.TOKEN,"");

        if(!token.equals("") || token.equals(null)){
            Intent i = new Intent(getApplicationContext(), Homepage.class);
            startActivity(i);
        }
    }
}
