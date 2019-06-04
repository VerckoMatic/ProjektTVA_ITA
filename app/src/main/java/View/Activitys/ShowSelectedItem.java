package View.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import static utils.Constants.BASE_NO_SLASH_URL;


public class ShowSelectedItem extends AppCompatActivity {
    TextView tw_title;
    TextView tw_description;
    TextView tw_price;
    TextView tw_platform;
    TextView tw_categories;
    TextView tw_sellerEmail;
    TextView tw_shippingType;
    TextView tw_shippingLocation;
    ImageView iw_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_image_item);

        tw_title = (TextView) findViewById(R.id.tw_title1);
        tw_price = (TextView) findViewById(R.id.tw_price);
        tw_platform = (TextView) findViewById(R.id.tw_platform);
        tw_categories = (TextView) findViewById(R.id.tw_categories);
        tw_description = (TextView) findViewById(R.id.tw_description);
        iw_photo = (ImageView) findViewById(R.id.iw_photo);

        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String price = intent.getStringExtra("PRICE");
        String platform = intent.getStringExtra("PLATFORM");
        String categories = intent.getStringExtra("CATEGORIES");
        String description = intent.getStringExtra("DESCRIPTION");
        String image = intent.getStringExtra("IMAGE");
        tw_title.setText(title);
        tw_price.setText(price + " €");
        tw_platform.setText("Platforma: " + platform);
        tw_categories.setText("Kategorije: " + categories);
        tw_description.setText("Opis: " + description);
        Picasso.get().load(BASE_NO_SLASH_URL+image).into(iw_photo);

    }
}
