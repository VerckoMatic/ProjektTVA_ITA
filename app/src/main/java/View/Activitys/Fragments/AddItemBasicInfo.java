package View.Activitys.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import Model.Classes.Item;
import Model.Classes.Response;
import Model.Classes.Shipping;
import View.Activitys.CreateItem;
import network.NetworkUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import Model.ItemValidation;
import utils.Constants;

public class AddItemBasicInfo extends Fragment {

    ImageView img_choose;
    Button btn_image;
    EditText et_pickupLocation;
    EditText et_category;
    EditText et_price;
    Spinner sp_shippingType;
    Button btn_next;
    Context applicationContext = CreateItem.getContextOfApplication();
    public String imageServerLocation;
    private CompositeSubscription mSubscriptions;
    private CompositeSubscription mSubscriptionsGame;
    private SharedPreferences mSharedPreferences;

    String TITLE;
    String DESCRIPTION;
    String PLATFORM;
    String ITEMTYPE;
    int SUBSCRIPTION;
    int WARRANTY;
    String TYPE;
    List<Category> categoryList = new ArrayList<>();
    Bitmap thumbnail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item_basic_info, container, false);

        if(getArguments()!=null){

            TITLE = getArguments().getString("TITLE");
            DESCRIPTION = getArguments().getString("DESCRIPTION");
            PLATFORM = getArguments().getString("PLATFORM");
            WARRANTY = getArguments().getInt("WARRANTY");
            SUBSCRIPTION = getArguments().getInt("SUBSCRIPTION");
            ITEMTYPE = getArguments().getString("ITEMTYPE");
            TYPE = getArguments().getString("TYPE");
            /*Snackbar.make(getActivity().findViewById(android.R.id.content), TITLE, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
        }
        initSharedPreferences();
        img_choose = (ImageView) rootView.findViewById(R.id.img_choose);
        btn_image = (Button) rootView.findViewById(R.id.btn_image);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        et_pickupLocation = (EditText) rootView.findViewById(R.id.et_location);
        et_price = (EditText) rootView.findViewById(R.id.et_price);
        et_category = (EditText) rootView.findViewById(R.id.et_category);
        sp_shippingType = (Spinner) rootView.findViewById(R.id.sp_shippingType);

        mSubscriptions = new CompositeSubscription();
        mSubscriptionsGame = new CompositeSubscription();
        applicationContext.getContentResolver();

        et_category.setText("");

        String[] sp_shippingTypeArray = getResources().getStringArray(R.array.sp_shippyngTypeStrings);
        ArrayAdapter<String> adapterPlatform =new ArrayAdapter<String>(getActivity(),R.layout.spinner_layout,R.id.text, sp_shippingTypeArray);
        sp_shippingType.setAdapter(adapterPlatform);

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();


            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String priceString = et_price.getText().toString();
                String locationString = et_pickupLocation.getText().toString();
                if(!ItemValidation.validatePriceBasicFragment(priceString) || !ItemValidation.validateLocationBasicFragment(locationString)){
                    if(!ItemValidation.validatePriceBasicFragment(priceString)){
                        et_price.setError("Prosim vnesite ceno, dovoljen je decimalni številski vnos.");
                    }
                    if(!ItemValidation.validateLocationBasicFragment(locationString)) {
                        et_pickupLocation.setError("Prosim vnesite vašo lokacijo/regijo.");
                    }
                }else{

                    if(TYPE.equals("Game")){
                        typeIsGame();
                    }else if(TYPE.equals("Devices")){
                        typeIsDevice();
                    }else if(TYPE.equals("Accessories")){
                        typeIsAccessories();
                    }

                }

            }
        });

        et_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryCheckboxDialog();
            }
        });

        return rootView;
    }

    //Create
    private void typeIsGame(){
        String priceString = et_price.getText().toString();
        double price = Double.parseDouble(priceString);
        String sp_shippingTypeString = sp_shippingType.getSelectedItem().toString();
        Shipping shipping = new Shipping();
        shipping.setShippingType(sp_shippingTypeString);
        shipping.setPickUpLocation(et_pickupLocation.getText().toString());
        String idUserString = mSharedPreferences.getString(Constants.IDUSER,"");
        int idUser = Integer.parseInt(idUserString);
        Game game = new Game();
        game.setTitle(TITLE);
        game.setType(TYPE);
        game.setPlatform(PLATFORM);
        game.setDescription(DESCRIPTION);
        game.setPrice(price);
        game.setShipping(shipping);
        game.setImages(imageServerLocation);
        game.setUser_idUser(idUser);
        game.setSubscription(SUBSCRIPTION);
        if(categoryList != null){
            game.setCategory(categoryList);
        }
        createGame(game);
    }

    private void typeIsAccessories(){
        String priceString = et_price.getText().toString();
        double price = Double.parseDouble(priceString);

        String sp_shippingTypeString = sp_shippingType.getSelectedItem().toString();
        Shipping shipping = new Shipping();
        shipping.setShippingType(sp_shippingTypeString);
        shipping.setPickUpLocation(et_pickupLocation.getText().toString());

        String idUserString = mSharedPreferences.getString(Constants.IDUSER,"");
        int idUser = Integer.parseInt(idUserString);

        Accessories accessories = new Accessories();
        accessories.setTitle(TITLE);
        accessories.setType(TYPE);
        accessories.setPlatform(PLATFORM);
        accessories.setDescription(DESCRIPTION);
        accessories.setPrice(price);
        accessories.setShipping(shipping);
        accessories.setImages(imageServerLocation);
        accessories.setUser_idUser(idUser);
        accessories.setItemType(ITEMTYPE);
        if(categoryList != null){
            accessories.setCategory(categoryList);
        }
        createAccessories(accessories);
    }

    private void typeIsDevice(){
        String priceString = et_price.getText().toString();
        double price = Double.parseDouble(priceString);

        String sp_shippingTypeString = sp_shippingType.getSelectedItem().toString();
        Shipping shipping = new Shipping();
        shipping.setShippingType(sp_shippingTypeString);
        shipping.setPickUpLocation(et_pickupLocation.getText().toString());

        String idUserString = mSharedPreferences.getString(Constants.IDUSER,"");
        int idUser = Integer.parseInt(idUserString);

        Devices devices = new Devices();
        devices.setTitle(TITLE);
        devices.setType(TYPE);
        devices.setPlatform(PLATFORM);
        devices.setDescription(DESCRIPTION);
        devices.setPrice(price);
        devices.setShipping(shipping);
        devices.setImages(imageServerLocation);
        devices.setUser_idUser(idUser);
        devices.setWarranty(WARRANTY);
        if(categoryList != null){
            devices.setCategory(categoryList);
        }
        createDevice(devices);
    }

    //mSubscriptions
    private void createGame(Game game){
        mSubscriptionsGame.add(NetworkUtil.getRetrofit().createGame(game)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void createDevice(Devices devices){
        mSubscriptionsGame.add(NetworkUtil.getRetrofit().createDevices(devices)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void createAccessories(Accessories accessories){
        mSubscriptionsGame.add(NetworkUtil.getRetrofit().createAccessories(accessories)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    //mSubscription handlers
    private void handleResponse(Response response) {

        Toast.makeText(applicationContext, response.getMessage(), Toast.LENGTH_LONG ).show();

    }

    private void handleError(Throwable error) {

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Toast.makeText(applicationContext, response.getMessage(), Toast.LENGTH_LONG ).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(applicationContext, "Network Error", Toast.LENGTH_LONG ).show();
        }
    }
//upload picture
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
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
                    thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(thumbnail);
                    Toast.makeText(applicationContext, "Image Saved!", Toast.LENGTH_SHORT).show();
                    img_choose.setImageBitmap(thumbnail);
                    multipartImageUpload(thumbnail);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == 1) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            img_choose.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(applicationContext, "Image Saved!", Toast.LENGTH_SHORT).show();
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
            MediaScannerConnection.scanFile(applicationContext,
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
            File filesDir = applicationContext.getFilesDir();
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
        Toast.makeText(applicationContext, "image: " + imageServerLocation, Toast.LENGTH_LONG ).show();
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

            Toast.makeText(applicationContext, "Network Error", Toast.LENGTH_LONG ).show();
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



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }
}
