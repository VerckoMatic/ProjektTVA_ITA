package View.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
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

import java.io.IOException;

import Model.Classes.Response;
import Model.Classes.User;
import Model.UserValidation;
import network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class Register extends AppCompatActivity {
    //private static FirebaseAuth mAuth;
    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mSubscriptions = new CompositeSubscription();
        getSupportActionBar().hide();
        EditText name = (EditText) findViewById(R.id.et_name);
        EditText email = (EditText) findViewById(R.id.et_email);
        EditText password = (EditText) findViewById(R.id.et_password);
        final EditText confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
        final Button confirmButton = (Button) findViewById(R.id.button_register);
        TextView tw_login = (TextView) findViewById(R.id.tw_login);

        tw_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = new User();
                user.setFullName(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                user.setImage("/uploads/af1d6452ebd89ba1c20338f6b08ee5a6.png");

                if(UserValidation.validateRegistrationConfirmPassword(confirmPassword.getText().toString(),password.getText().toString()) && UserValidation.validateRegistrationEmail(user.getEmail()) &&
                UserValidation.validateRegistrationName(user.getFullName()) && UserValidation.validateRegistrationPassword(user.getPassword())){
                    registerUser(user);
                }else{
                    if(!UserValidation.validateRegistrationConfirmPassword(confirmPassword.getText().toString(),password.getText().toString())){
                        confirmPassword.setError("Vpišite geslo, pazite da se ujema z prej vpisanim geslom!");
                    }
                    if(!UserValidation.validateRegistrationEmail(user.getEmail())){
                        email.setError("Vpišite veljaven email račun!");
                    }
                    if(!UserValidation.validateRegistrationName(user.getFullName())){
                        name.setError("Vpišite vaš ime in priimek!");
                    }
                    if(!UserValidation.validateRegistrationPassword(user.getPassword())){
                        password.setError("Vpišite vaše geslo. Najmanj 5 znakov!");
                    }
                }
                }
        });
    }

    private void registerUser(User user) {

        mSubscriptions.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }
    private void handleResponse(Response response) {

        Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG ).show();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    private void handleError(Throwable error) {

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG ).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this, "Network Error", Toast.LENGTH_LONG ).show();
        }
    }

}
