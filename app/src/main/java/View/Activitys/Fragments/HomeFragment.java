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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import View.Activitys.Homepage;
import network.NetworkUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import utils.Constants;

public class HomeFragment extends Fragment {
    private String mToken;
    private String mEmail;
    Button btn_logout;
    Button btn_createItem;
    Button btn_image;
    TextView tw_out;
    ImageView img_choose;
    Bitmap bitmap;
    AlertDialog alertDialogWithRadioButtons;
    String selectedImagePath;
    String selected;
    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;
    Context applicationContext = Homepage.getContextOfApplication();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        tw_out = (TextView) rootView.findViewById(R.id.tw_out);
        btn_logout = (Button) rootView.findViewById(R.id.btn_logout);
        btn_image = (Button) rootView.findViewById(R.id.btn_image);
        btn_createItem = (Button) rootView.findViewById(R.id.btn_createItem);
        tw_out.setText(mEmail);
        img_choose = (ImageView) rootView.findViewById(R.id.img_choose);
        mSubscriptions = new CompositeSubscription();
        initSharedPreferences();
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showCameraDialog();
                //showGalleryDialog();
                showPictureDialog();
            }
        });

        applicationContext.getContentResolver();

        btn_createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item();
                Game game = new Game();
                Shipping shipping = new Shipping();
                shipping.setPickUpLocation("mb");
                shipping.setShippingType("posta");
                item.setTitle("God of War");
                item.setPlatform("ps4");
                item.setDescription("asfasf");
                item.setPrice(23);
                item.setShipping(shipping);

                createItem(item);
            }

        });

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_itemType);
                int itemSelected = 0;
                CharSequence[] values = {"Igra", "Naprava", "Dodatki"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Kakšen je tip izdelka, ki ga dodajate?");

                builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialogInterface, int item)
                            {
                                switch (item)
                                {
                                    case 0:
                                        Snackbar.make(view, "Izbrano: Igra", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        selected = "Game";
                                        break;
                                    case 1:
                                        Snackbar.make(view, "Izbrano: Naprava", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        selected = "Devices";
                                        break;
                                    case 2:
                                        Snackbar.make(view, "Izbrano: Dodatki", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        selected = "Accessories";
                                        break;
                                }


                            }
                        }
                );
                builder.setPositiveButton("Potrdi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(applicationContext, CreateItem.class);
                        intent.putExtra("TYPE", selected);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Prekini", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                alertDialogWithRadioButtons = builder.create();
                alertDialogWithRadioButtons.show();
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        return rootView;
    }

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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(applicationContext, "Image Saved!", Toast.LENGTH_SHORT).show();
                    img_choose.setImageBitmap(bitmap);
                    multipartImageUpload(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == 1) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
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

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");

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

        Toast.makeText(applicationContext, response.getMessage(), Toast.LENGTH_LONG ).show();
        tw_out.setText(response.getMessage().toString());
    }

    private void createItem(Item item){
        mSubscriptions.add(NetworkUtil.getRetrofit().createItem(item)
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



    private void handlePostImageError(Throwable error) {

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

}
