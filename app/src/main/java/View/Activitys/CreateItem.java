package View.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.matic.projekttva.R;

import View.Activitys.Fragments.AddAccessorieFragment;
import View.Activitys.Fragments.AddDeviceFragment;
import View.Activitys.Fragments.AddGameFragment;
import View.Activitys.Fragments.AddItemBasicInfo;

public class CreateItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        Intent intent = getIntent();
        String type = intent.getStringExtra("TYPE");


        getSupportFragmentManager().beginTransaction().replace(R.id.createItem_fragment_container,
                new AddItemBasicInfo()).commit();
        //checkType(type);
    }


}
