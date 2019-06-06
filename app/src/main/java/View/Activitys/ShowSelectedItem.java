package View.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import Model.Classes.User;
import Model.ResponsePOJO.CategoriesResponsePOJOlist;
import Model.ResponsePOJO.UserResponsePOJOlist;
import network.NetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subscriptions.CompositeSubscription;

import static utils.Constants.BASE_NO_SLASH_URL;


public class ShowSelectedItem extends AppCompatActivity {
    TextView tw_title;
    TextView tw_description;
    TextView tw_price;
    TextView tw_platform;
    TextView tw_categories;
    TextView tw_sellerEmail;
    TextView tw_shippingType;
    TextView tw_pickUpLocation;
    ImageView iw_photo;
    Button btn_contactSeller;
    private CompositeSubscription mSubscriptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selected_item);

        tw_title = (TextView) findViewById(R.id.tw_title1);
        tw_price = (TextView) findViewById(R.id.tw_price);
        tw_platform = (TextView) findViewById(R.id.tw_platform);
        tw_categories = (TextView) findViewById(R.id.tw_categories);
        tw_description = (TextView) findViewById(R.id.tw_description);
        tw_sellerEmail = (TextView) findViewById(R.id.tw_sellerEmail);
        tw_shippingType = (TextView) findViewById(R.id.tw_shippingType);
        tw_pickUpLocation = (TextView) findViewById(R.id.tw_pickUpLocation);
        iw_photo = (ImageView) findViewById(R.id.iw_photo);
        btn_contactSeller = (Button) findViewById(R.id.btn_contactSeller);
        mSubscriptions = new CompositeSubscription();
        getSupportActionBar().hide();
        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String price = intent.getStringExtra("PRICE");
        String platform = intent.getStringExtra("PLATFORM");
        String categories = "";
        String description = intent.getStringExtra("DESCRIPTION");
        String pickUpLocation = intent.getStringExtra("PICKUPLOCATION");
        String shippingType = intent.getStringExtra("SHIPPINGTYPE");
        String image = intent.getStringExtra("IMAGE");
        int User_idUser = intent.getIntExtra("IDUSER", 888);
        int idItem = intent.getIntExtra("IDITEM", 888);
        getSeller(User_idUser);
        getCategories(idItem);
        tw_title.setText(title);
        tw_price.setText(price + " €");
        tw_platform.setText("Platforma: " + platform);
        tw_categories.setText("Kategorije: " + categories);
        tw_description.setText("Opis: " + description);
        tw_shippingType.setText("Možen tip prodaje: " + shippingType);
        tw_pickUpLocation.setText("Lokacija: " + pickUpLocation);
        Picasso.get().load(BASE_NO_SLASH_URL+image).into(iw_photo);


        btn_contactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowSelectedItem.this, Chat.class);
                String idUser = String.valueOf(User_idUser);
                i.putExtra("SELLEREMAIL", tw_sellerEmail.getText().toString());
                startActivity(i);
            }
        });
    }


    private void getSeller(int User_idUser){
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        Call<UserResponsePOJOlist> call = NetworkUtil.getRetrofit(gsonConverterFactory).getUserById(User_idUser);

        retrofit2.Callback<UserResponsePOJOlist> callback = new Callback<UserResponsePOJOlist>(){
            @Override
            public void onResponse(Call<UserResponsePOJOlist> call, retrofit2.Response<UserResponsePOJOlist> response) {
                try {
                    if (response.isSuccessful()) {

                        UserResponsePOJOlist sellerList = response.body();

                        setSeller(sellerList);
                    } else {
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }catch(IOException ex)
                {
                }
            }

            @Override
            public void onFailure(Call<UserResponsePOJOlist> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        };

        // Send request to web server and process response with the callback object.
        call.enqueue(callback);
    }

    private void getCategories(int Item_idItem){
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        Call<CategoriesResponsePOJOlist> call = NetworkUtil.getRetrofit(gsonConverterFactory).getCategoryNames(Item_idItem);

        retrofit2.Callback<CategoriesResponsePOJOlist> callback = new Callback<CategoriesResponsePOJOlist>(){
            @Override
            public void onResponse(Call<CategoriesResponsePOJOlist> call, retrofit2.Response<CategoriesResponsePOJOlist> response) {
                try {
                    if (response.isSuccessful()) {

                        CategoriesResponsePOJOlist categoryList = response.body();
                        setCategories(categoryList);
                    } else {
                        String errorMessage = response.errorBody().string();
                        //Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }catch(IOException ex)
                {
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponsePOJOlist> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        };

        // Send request to web server and process response with the callback object.
        call.enqueue(callback);
    }

    public void setSeller(UserResponsePOJOlist seller){

        tw_sellerEmail.setText(seller.getUserResponsePOJOS().get(0).getEmail());

    }

    public void setCategories(CategoriesResponsePOJOlist categoryList){

        String out = "Kategorije: ";
        for(int i = 0; i < categoryList.getCategories().size(); i++){
            if(i == 0){
                out += "" + categoryList.getCategories().get(i).get(0).getName().toString();
            }else{
                out += ", " + categoryList.getCategories().get(i).get(0).getName().toString();
            }
        }
        tw_categories.setText(out);
    }

}
