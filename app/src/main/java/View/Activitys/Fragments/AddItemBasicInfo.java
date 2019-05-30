package View.Activitys.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.util.Calendar;

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

public class AddItemBasicInfo extends Fragment {

    ImageView img_choose;
    Button btn_image;
    EditText et_pickupLocation;
    EditText et_price;
    Spinner sp_shippingType;
    Button btn_next;
    Context applicationContext = CreateItem.getContextOfApplication();
    public String imageServerLocation;
    private CompositeSubscription mSubscriptions;
    private CompositeSubscription mSubscriptionsGame;

    String TITLE;
    String DESCRIPTION;
    String PLATFORM;
    String SUBSCRIPTION;
    String TYPE;

    Bitmap thumbnail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item_basic_info, container, false);

        if(getArguments()!=null){

            TITLE = getArguments().getString("TITLE");
            DESCRIPTION = getArguments().getString("DESCRIPTION");
            PLATFORM = getArguments().getString("PLATFORM");
            SUBSCRIPTION = getArguments().getString("SUBSCRIPTION");
            TYPE = getArguments().getString("TYPE");
            /*Snackbar.make(getActivity().findViewById(android.R.id.content), TITLE, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
        }

        img_choose = (ImageView) rootView.findViewById(R.id.img_choose);
        btn_image = (Button) rootView.findViewById(R.id.btn_image);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        et_pickupLocation = (EditText) rootView.findViewById(R.id.et_location);
        et_price = (EditText) rootView.findViewById(R.id.et_price);
        sp_shippingType = (Spinner) rootView.findViewById(R.id.sp_shippingType);

        mSubscriptions = new CompositeSubscription();
        mSubscriptionsGame = new CompositeSubscription();
        applicationContext.getContentResolver();

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

                double price = Double.parseDouble(et_price.getText().toString());
                String sp_shippingTypeString = sp_shippingType.getSelectedItem().toString();
                //imageServerLocation;
                Shipping shipping = new Shipping();
                shipping.setShippingType(sp_shippingTypeString);
                shipping.setPickUpLocation(et_pickupLocation.getText().toString());
                Game game = new Game();
                game.setTitle(TITLE);
                game.setType(TYPE);
                game.setPlatform(PLATFORM);
                game.setPrice(price);
                game.setShipping(shipping);
                game.setImages(imageServerLocation);

                game.setSubscription(SUBSCRIPTION);

                createGame(game);
            }
        });


        return rootView;
    }

    //mSubscriptions

    private void createGame(Game game){
        mSubscriptionsGame.add(NetworkUtil.getRetrofit().createGame(game)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }
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
//picture
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
}
