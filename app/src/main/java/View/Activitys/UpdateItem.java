package View.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Model.Classes.Accessories;
import Model.Classes.Category;
import Model.Classes.Devices;
import Model.Classes.Game;
import Model.Classes.Response;
import Model.Classes.Shipping;
import Model.ItemValidation;
import View.Activitys.Fragments.MyItemsFragment;
import network.NetworkUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import utils.Constants;

import static utils.Constants.BASE_NO_SLASH_URL;

public class UpdateItem extends AppCompatActivity {

    private  String TITLE;
    private String DESCRIPTION;
    private String PLATFORM;
    private String ITEMTYPE;
    private int SUBSCRIPTION;
    private int WARRANTY;
    private String TYPE;
    private String IMAGE;
    private int IDUSER;
    private int IDITEM;
    private String PRICE;
    private String SHIPPINGTYPE;
    private String PICKUPLOCATION;

    List<Category> categoryList = new ArrayList<>();
     private ImageView iw_photo;
     private EditText et_title;
     private EditText et_price;
     private EditText et_location;
     private EditText et_itemType;
     private EditText et_description;
     private EditText et_category;
     private TextView tw_subscription;
     private TextView tw_warranty;
     private Spinner sp_shippingType;
     private Spinner sp_platform;
     private Spinner sp_warranty;
     private Spinner sp_subscription;
     private Button btn_update;
    private CompositeSubscription mSubscriptionsGame;
    private CompositeSubscription mSubscriptions;
    private Bitmap thumbnail;
    public String imageServerLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);


        Intent i = getIntent();
        IDITEM =  i.getIntExtra("IDITEM",0);
        TITLE = i.getStringExtra("TITLE");
        TYPE =  i.getStringExtra("TYPE");
        PLATFORM =  i.getStringExtra("PLATFORM");
        DESCRIPTION = i.getStringExtra("DESCRIPTION");
        PRICE =  i.getStringExtra("PRICE");
        PICKUPLOCATION =  i.getStringExtra("PICKUPLOCATION");
        SHIPPINGTYPE =  i.getStringExtra("SHIPPINGTYPE");
        IMAGE =  i.getStringExtra("IMAGE");
        IDUSER =  i.getIntExtra("IDUSER",0);
        WARRANTY = i.getIntExtra("WARRANTY", 0);
        SUBSCRIPTION =  i.getIntExtra("SUBSCRIPTION",0);
        ITEMTYPE =  i.getStringExtra("ITEMTYPE");

        et_category = (EditText) findViewById(R.id.et_category);
        et_title = (EditText) findViewById(R.id.et_title);
        et_price = (EditText) findViewById(R.id.et_price);
        et_location = (EditText) findViewById(R.id.et_location);
        et_description = (EditText) findViewById(R.id.et_description);
        et_itemType = (EditText) findViewById(R.id.et_itemType);
        iw_photo = (ImageView) findViewById(R.id.iw_photo);
        tw_subscription = (TextView) findViewById(R.id.tw_subscription);
        tw_warranty = (TextView) findViewById(R.id.tw_warranty);
        sp_shippingType = (Spinner) findViewById(R.id.sp_shippingType);
        sp_platform = (Spinner) findViewById(R.id.sp_platform);
        sp_warranty = (Spinner) findViewById(R.id.sp_warranty);
        sp_subscription = (Spinner) findViewById(R.id.sp_subscription);
        btn_update = (Button) findViewById(R.id.btn_update);

        mSubscriptionsGame = new CompositeSubscription();
        mSubscriptions = new CompositeSubscription();

        String[] sp_shippingTypeArray = getResources().getStringArray(R.array.sp_shippyngTypeStrings);
        ArrayAdapter<String> adapterShipp =new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, sp_shippingTypeArray);
        sp_shippingType.setAdapter(adapterShipp);

        String[] sp_platformArray = getResources().getStringArray(R.array.sp_platformStrings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, sp_platformArray);
        sp_platform.setAdapter(adapter);

        String[] sp_warrantyArray = getResources().getStringArray(R.array.sp_warrantyStrings);
        ArrayAdapter<String> adapterPlatform =new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, sp_warrantyArray);
        sp_warranty.setAdapter(adapterPlatform);

        String[] sp_subscriptionArray = getResources().getStringArray(R.array.sp_subscriptionStrings);
        ArrayAdapter<String> adapterSub =new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, sp_subscriptionArray);
        sp_subscription.setAdapter(adapterSub);

        if(TYPE != null){
        if(TYPE.equals("Game")){
            sp_warranty.setVisibility(View.INVISIBLE);
            tw_warranty.setVisibility(View.INVISIBLE);
            et_itemType.setVisibility(View.INVISIBLE);
        }else if(TYPE.equals("Accessories")){
            sp_warranty.setVisibility(View.INVISIBLE);
            tw_warranty.setVisibility(View.INVISIBLE);
            tw_subscription.setVisibility(View.INVISIBLE);
            sp_subscription.setVisibility(View.INVISIBLE);
        }else if(TYPE.equals("Devices")){
            tw_subscription.setVisibility(View.INVISIBLE);
            sp_subscription.setVisibility(View.INVISIBLE);
            et_itemType.setVisibility(View.INVISIBLE);
        }
        }
        Picasso.get().load(Constants.BASE_NO_SLASH_URL+IMAGE).into(iw_photo);
        et_title.setText(TITLE);
        et_price.setText(PRICE);
        et_itemType.setText(ITEMTYPE);
        et_location.setText(PICKUPLOCATION);
        et_description.setText(DESCRIPTION);

        iw_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        et_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryCheckboxDialog();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String priceString = et_price.getText().toString();
                String locationString = et_location.getText().toString();

                if(!ItemValidation.validatePriceBasicFragment(priceString) || !ItemValidation.validateLocationBasicFragment(locationString)
                        ||!ItemValidation.validateDescriptionCreateItemFragments(et_description.getText().toString()) || !ItemValidation.validateTitleCreateItemFragments(et_title.getText().toString())){
                    if(!ItemValidation.validatePriceBasicFragment(priceString)){
                        et_price.setError("Prosim vnesite ceno, dovoljen je decimalni številski vnos.");
                    }
                    if(!ItemValidation.validateDescriptionCreateItemFragments(et_description.getText().toString())) {
                        et_description.setError("Prosim vnesite vaš opis.");
                    }
                    if(!ItemValidation.validateTitleCreateItemFragments(et_title.getText().toString())) {
                        et_title.setError("Prosim vnesite vaš naslov.");
                    }
                }else{

                    if(TYPE.equals("Game")){
                        typeIsGame();
                    }else if(TYPE.equals("Devices")){
                        typeIsDevice();
                    }else if(TYPE.equals("Accessories")){
                        typeIsAccessories();
                    }

                    Intent myIntent = new Intent(UpdateItem.this, Homepage.class);
                    startActivity(myIntent);

                }

            }
        });

    }
    //Create
    private void typeIsGame(){
        String priceString = et_price.getText().toString();
        double price = Double.parseDouble(priceString);
        String sp_shippingTypeString = sp_shippingType.getSelectedItem().toString();
        String sp_platformString = sp_platform.getSelectedItem().toString();
        String sp_subString = sp_subscription.getSelectedItem().toString();
        int sub;
        if(sp_subString.equals("Da")){
            sub = 1;
        }else{
            sub = 0;
        }
        Shipping shipping = new Shipping();
        shipping.setShippingType(sp_shippingTypeString);
        shipping.setPickUpLocation(et_location.getText().toString());
        int idUser = IDUSER;
        Game game = new Game();
        game.setTitle(et_title.getText().toString());
        game.setType(TYPE);
        game.setPlatform(sp_platformString);
        game.setDescription(et_description.getText().toString());
        game.setPrice(price);
        game.setShipping(shipping);
        if(imageServerLocation == null){
            game.setImages(IMAGE);
        }else{
            game.setImages(imageServerLocation);
        }

        game.setUser_idUser(idUser);
        game.setSubscription(sub);
        if(categoryList != null){
            game.setCategory(categoryList);
        }
        updateGame(game);
    }

    private void typeIsAccessories(){
        String priceString = et_price.getText().toString();
        double price = Double.parseDouble(priceString);

        String sp_shippingTypeString = sp_shippingType.getSelectedItem().toString();
        Shipping shipping = new Shipping();
        shipping.setShippingType(sp_shippingTypeString);
        shipping.setPickUpLocation(et_location.getText().toString());

        int idUser = IDUSER;

        Accessories accessories = new Accessories();
        accessories.setTitle(et_title.getText().toString());
        accessories.setType(TYPE);
        accessories.setPlatform(sp_platform.getSelectedItem().toString());
        accessories.setDescription(et_description.getText().toString());
        accessories.setPrice(price);
        accessories.setShipping(shipping);
        if(imageServerLocation == null){
            accessories.setImages(IMAGE);
        }else{
            accessories.setImages(imageServerLocation);
        }
        accessories.setUser_idUser(idUser);
        accessories.setItemType(et_itemType.getText().toString());
        if(categoryList != null){
            accessories.setCategory(categoryList);
        }
        updateAccessories(accessories);
    }

    private void typeIsDevice(){
        String priceString = et_price.getText().toString();
        double price = Double.parseDouble(priceString);

        String sp_shippingTypeString = sp_shippingType.getSelectedItem().toString();
        Shipping shipping = new Shipping();
        shipping.setShippingType(sp_shippingTypeString);
        shipping.setPickUpLocation(et_location.getText().toString());
        String sp_warrantyString = sp_warranty.getSelectedItem().toString();
        int warrantyInt;
        if(sp_warrantyString.equals("Da")){
            warrantyInt = 1;
        }else{
            warrantyInt = 0;
        }
        int idUser = IDUSER;
        Devices devices = new Devices();
        devices.setTitle(et_title.getText().toString());
        devices.setType(TYPE);
        devices.setPlatform(sp_platform.getSelectedItem().toString());
        devices.setDescription(et_description.getText().toString());
        devices.setPrice(price);
        devices.setShipping(shipping);
        if(imageServerLocation == null){
            devices.setImages(IMAGE);
        }else{
            devices.setImages(imageServerLocation);
        }
        devices.setUser_idUser(idUser);
        devices.setWarranty(warrantyInt);
        if(categoryList != null){
            devices.setCategory(categoryList);
        }
        updateDevice(devices);
    }

    //mSubscriptions
    private void updateGame(Game game){
        mSubscriptionsGame.add(NetworkUtil.getRetrofit().updateOneItem(IDITEM,game)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void updateDevice(Devices devices){
        mSubscriptionsGame.add(NetworkUtil.getRetrofit().updateDeviceItem(IDITEM,devices)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void updateAccessories(Accessories accessories){
        mSubscriptionsGame.add(NetworkUtil.getRetrofit().updateAccessoriesItem(IDITEM,accessories)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    //mSubscription handlers
    private void handleResponse(Response response) {

        Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG ).show();

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

    public void categoryCheckboxDialog(){
        categoryList.clear();
        String [] items = {"FPS", "MMORPG", "RPG", "Simulacije", "Avanturistične", "RTS", "Miselne",
                "Akcijske", "Športne", "Izobraževalne", "Arkadne", "Avto-moto", "Strategijske", "CO-OP", "Naprave", "Dodatki"};
        final boolean[] checkedColors = new boolean[]{
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
        };
        List<Integer> selectedItems = new ArrayList<>();



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMultiChoiceItems(items,checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                if (isChecked) {
                    selectedItems.add(indexSelected);
                }
                else if (selectedItems.contains(indexSelected)) {
                    selectedItems.remove(Integer.valueOf(indexSelected));
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String out = "";
                for(int i = 0; i < selectedItems.size(); i++){
                    Category category = new Category();
                    category.setName(items[selectedItems.get(i)]);
                    categoryList.add(category);
                    if(i == 0){
                        out += " " +items[selectedItems.get(i)].toString();
                    }else{
                        out += ", " +items[selectedItems.get(i)].toString();
                    }

                }
                et_category.setText(out);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                return;
            }
        });
        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    //upload picture
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, 0);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    String path = saveImage(thumbnail);
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    iw_photo.setImageBitmap(thumbnail);
                    multipartImageUpload(thumbnail);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == 1) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            iw_photo.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
            multipartImageUpload(thumbnail);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "/demo");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void multipartImageUpload(Bitmap mBitmap) {
        try {
            File filesDir = this.getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();


            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "file");

            mSubscriptions.add(NetworkUtil.getRetrofit().postImage(body, name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handlePostImage,this::handlePostImageError));



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePostImage(Response response) {

        imageServerLocation = response.getMessage().toString();
        Toast.makeText(this, "image: " + imageServerLocation, Toast.LENGTH_LONG ).show();
    }
    private void handlePostImageError(Throwable error) {

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                imageServerLocation = response.getMessage().toString();
                //Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG ).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this, "Network Error", Toast.LENGTH_LONG ).show();
        }
    }


}
