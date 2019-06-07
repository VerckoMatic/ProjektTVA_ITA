package View.Activitys;

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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.matic.projekttva.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import Model.Classes.Response;
import Model.Classes.User;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import View.Activitys.Fragments.HomeFragment;
import View.Activitys.Fragments.MyItemsFragment;
import View.Activitys.Fragments.CommingSoonFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import network.NetworkUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import utils.Constants;

import static utils.Constants.BASE_NO_SLASH_URL;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CompositeSubscription mSubscriptions;
    private CompositeSubscription mSubscriptions2;
    private SharedPreferences mSharedPreferences;

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView tw_fullname;
    private TextView tw_email;
    private String mEmail;
    private String mImage;
    private String mIdUser;
    private CircleImageView circleImageView;
    public String imageServerLocation;
    private Bitmap thumbnail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mSubscriptions = new CompositeSubscription();
        mSubscriptions2 = new CompositeSubscription();
        contextOfApplication = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initSharredPrefferences();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        tw_email = (TextView) header.findViewById(R.id.tw_email);

        circleImageView = (CircleImageView) header.findViewById(R.id.profile_image);
        if(!mImage.equals("image")){
            Picasso.get().load(BASE_NO_SLASH_URL+mImage).into(circleImageView);

        }
        tw_email.setText(mEmail);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();


            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            //super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_soon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CommingSoonFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_user_data:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyItemsFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_logout:
                mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String mToken = mSharedPreferences.getString(Constants.TOKEN,"");
                String mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Constants.EMAIL,"");
                editor.putString(Constants.TOKEN,"");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void initSharredPrefferences(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
        mIdUser = mSharedPreferences.getString(Constants.IDUSER,"");
        mImage = mSharedPreferences.getString(Constants.IMAGE,"");
    }

    //upload picture
    private void showPictureDialog(){
        Picasso.get().load(BASE_NO_SLASH_URL+mImage).into(circleImageView);
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Izberi");
        String[] pictureDialogItems = {
                "Izberi sliko iz galerije",
                "Zajemi novo sliko s kamero" };
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
                            default:
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
                    circleImageView.setImageBitmap(thumbnail);
                    multipartImageUpload(thumbnail);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == 1) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            circleImageView.setImageBitmap(thumbnail);
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
        updateUser(imageServerLocation);
    }
    private void handlePostImageError(Throwable error) {

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                imageServerLocation = response.getMessage().toString();
                updateUser(imageServerLocation);
                //Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG ).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this, "Network Error", Toast.LENGTH_LONG ).show();
        }
    }


    private void updateUser(String image){
        int idUser = Integer.parseInt(mIdUser);
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        User user = new User();
        user.setImage(image);
        Call<Response> call = NetworkUtil.getRetrofit().updateUsersImage(idUser, user);

        retrofit2.Callback<Response> callback = new Callback<Response>(){
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                try {
                    if (response.isSuccessful()) {

                       updatePrefferences();


                    } else {
                        String errorMessage = response.errorBody().string();
                    }
                }catch(IOException ex)
                {
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                t.getMessage();
            }
        };

        // Send request to web server and process response with the callback object.
        call.enqueue(callback);
    }


    public void updatePrefferences(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.IMAGE,imageServerLocation);
        editor.apply();
        initSharredPrefferences();
    }

}
