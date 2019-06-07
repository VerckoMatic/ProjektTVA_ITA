package View.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import Model.Classes.Response;
import Model.Classes.User;
import network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    EditText token;
    EditText newPassword;
    boolean init = true;
    Button btn_newPassword;
    private CompositeSubscription mSubscriptions;
    String emailStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mSubscriptions = new CompositeSubscription();
        getSupportActionBar().hide();
        email = (EditText) findViewById(R.id.et_emailFP);
        token = (EditText) findViewById(R.id.et_token);
        newPassword = (EditText) findViewById(R.id.et_newPassword);

        btn_newPassword = (Button) findViewById(R.id.btn_newPassword);

        btn_newPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(init){
                    emailStr = email.getText().toString();
                    goInit();
                }else{
                    goFinish();
                }
            }
        });
    }

    private void goInit(){

        mSubscriptions.add(NetworkUtil.getRetrofit().resetPasswordInit(emailStr)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));

    }

    private void goFinish(){

        String tokenStr = token.getText().toString();
        String newPasswordStr = newPassword.getText().toString();

        User user = new User();
        user.setPassword(newPasswordStr);
        user.setToken(tokenStr);
        mSubscriptions.add(NetworkUtil.getRetrofit().resetPasswordFinish(emailStr,user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));

    }
    private void handleResponse(Response response) {


        if (init) {
            init = false;
            Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
            email.setVisibility(View.GONE);
            token.setVisibility(View.VISIBLE);
            newPassword.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable error) {


        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(getApplicationContext(), "Network error!", Toast.LENGTH_LONG).show();
        }
    }

}
