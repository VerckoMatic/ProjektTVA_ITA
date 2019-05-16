package View.Activitys;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.matic.projekttva.R;

import Model.Classes.User;
import Model.UserValidation;

public class Register extends AppCompatActivity {
    //private static FirebaseAuth mAuth;
    Context context = getApplicationContext();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText name = (EditText) findViewById(R.id.et_name);
        EditText email = (EditText) findViewById(R.id.et_email);
        EditText password = (EditText) findViewById(R.id.et_password);
        final EditText confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
        final Button confirmButton = (Button) findViewById(R.id.button_register);

        final User user = new User();
        user.setFullName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CRUDoperations.registerUser(this, user );
                if(UserValidation.validateRegistrationConfirmPassword(confirmPassword.getText().toString()) && UserValidation.validateRegistrationEmail(user.getEmail()) &&
                UserValidation.validateRegistrationName(user.getFullName()) && UserValidation.validateRegistrationPassword(user.getPassword())){

                }

                }

        });
    }
}
