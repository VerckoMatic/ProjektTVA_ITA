package View.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.matic.projekttva.R;

import View.Activitys.Fragments.AddAccessorieFragment;
import View.Activitys.Fragments.AddDeviceFragment;
import View.Activitys.Fragments.AddGameFragment;

public class CreateItem extends AppCompatActivity {

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        contextOfApplication = getApplicationContext();

        Intent i = getIntent();
        String type = i.getStringExtra("TYPE");

       checkType(type);

    }

    //checking type to put new fragment
    public void checkType(String type){
        if(type.equals("Game")){
            getSupportFragmentManager().beginTransaction().replace(R.id.createItem_fragment_container,
                    new AddGameFragment()).commit();
        }else if(type.equals("Devices")){
            getSupportFragmentManager().beginTransaction().replace(R.id.createItem_fragment_container,
                    new AddDeviceFragment()).commit();
        }else if(type.equals("Accessories")){
            getSupportFragmentManager().beginTransaction().replace(R.id.createItem_fragment_container,
                    new AddAccessorieFragment()).commit();

        }
    }

}
