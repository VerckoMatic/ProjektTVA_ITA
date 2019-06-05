package View.Activitys.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Model.Classes.Accessories;
import Model.Classes.Devices;
import Model.Classes.Game;
import Model.Classes.Response;
import Model.ResponsePOJO.ItemResponsePOJO;
import Model.ResponsePOJO.ItemResponsePOJOlist;
import View.Activitys.Adapters.RecyclerViewAdapter;
import View.Activitys.Adapters.RecyclerViewAdapterMyItems;
import View.Activitys.CreateItem;
import View.Activitys.Homepage;
import View.Activitys.ShowSelectedItem;
import network.NetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subscriptions.CompositeSubscription;
import utils.Constants;
import View.Activitys.Adapters.RecyclerViewAdapterMyItems.*;
public class MyItemsFragment extends Fragment implements MyClickListener {

    private String mToken;
    private int User_idUser;
    TextView tw_out;
    Bitmap bitmap;
    AlertDialog alertDialogWithRadioButtons;
    String selected;
    private CompositeSubscription mSubscriptions;
    private NetworkUtil networkUtil;
    private SharedPreferences mSharedPreferences;
    Context applicationContext = Homepage.getContextOfApplication();
    RecyclerView recyclerView;
    ItemResponsePOJOlist itemResponsePOJOlist = new ItemResponsePOJOlist();
    SwipeRefreshLayout mySwipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        mSubscriptions = new CompositeSubscription();
        initSharedPreferences();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.rv);
        mySwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        refresh();

        getAllItemsByUserId(User_idUser);

        applicationContext.getContentResolver();

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

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        String tempStringUserId = mSharedPreferences.getString(Constants.IDUSER,"");
        User_idUser = Integer.parseInt(tempStringUserId);

    }
    private void getAllItemsByUserId(int User_idUser){
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        Call<ItemResponsePOJOlist> call = NetworkUtil.getRetrofit(gsonConverterFactory).getAllItemsByUserId(User_idUser);

        retrofit2.Callback<ItemResponsePOJOlist> callback = new Callback<ItemResponsePOJOlist>(){
            @Override
            public void onResponse(Call<ItemResponsePOJOlist> call, retrofit2.Response<ItemResponsePOJOlist> response) {
                try {
                    if (response.isSuccessful()) {

                        ItemResponsePOJOlist userDtoList = response.body();

                        showItems(userDtoList);

                    } else {
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }catch(IOException ex)
                {
                }
            }

            @Override
            public void onFailure(Call<ItemResponsePOJOlist> call, Throwable t) {
                Toast.makeText(applicationContext, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        };

        // Send request to web server and process response with the callback object.
        call.enqueue(callback);
    }

    //mSubscription handlers
    private void handleResponse(Model.Classes.Response response) {

        Toast.makeText(applicationContext, response.getMessage(), Toast.LENGTH_LONG ).show();

    }

    private void handleError(Throwable error) {

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Model.Classes.Response response = gson.fromJson(errorBody, Model.Classes.Response.class);
                Toast.makeText(applicationContext, response.getMessage(), Toast.LENGTH_LONG ).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }
    }


    private void showItems(ItemResponsePOJOlist userDtoList)
    {
        if(userDtoList != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            itemResponsePOJOlist.setItemResponsePOJOlist(userDtoList.getItemResponsePOJOlist());
            RecyclerViewAdapterMyItems adapter = new RecyclerViewAdapterMyItems(applicationContext,userDtoList.getItemResponsePOJOlist());
            adapter.setOnItemClickListener(MyItemsFragment.this);
            recyclerView.setAdapter(adapter);
        }
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO: Step 4 of 4: Finally call getTag() on the view.
            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            // viewHolder.getItemId();
            // viewHolder.getItemViewType();
            // viewHolder.itemView;
            ItemResponsePOJO thisItem = itemResponsePOJOlist.getItemResponsePOJOlist().get(position);
            //Toast.makeText(getActivity(), "You Clicked: " + thisItem.getTitle(), Toast.LENGTH_SHORT).show();
            String categories = "";


            Intent intent = new Intent(getActivity(), ShowSelectedItem.class);
            intent.putExtra("TITLE", thisItem.title);
            intent.putExtra("PRICE", thisItem.price);
            intent.putExtra("PLATFORM", thisItem.platform);
            intent.putExtra("CATEGORIES", categories);
            intent.putExtra("DESCRIPTION", thisItem.description);
            intent.putExtra("IMAGE", thisItem.images);
            intent.putExtra("IDUSER", thisItem.User_idUser);
            intent.putExtra("SHIPPINGTYPE", thisItem.shippingType);
            intent.putExtra("PICKUPLOCATION", thisItem.pickupLocation);

            startActivity(intent);
        }
    };

    @Override
    public void onEdit(int p) {
        updateOneItem(itemResponsePOJOlist.getItemResponsePOJOlist().get(p).getIdItem());
        mySwipeRefreshLayout.setRefreshing(true);
        mySwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDelete(int p) {
        deleteOneItem(itemResponsePOJOlist.getItemResponsePOJOlist().get(p).getIdItem());
        mySwipeRefreshLayout.setRefreshing(true);
        mySwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCardClick(int p) {
        ItemResponsePOJO thisItem = itemResponsePOJOlist.getItemResponsePOJOlist().get(p);

        Intent intent = new Intent(getActivity(), ShowSelectedItem.class);
        intent.putExtra("TITLE", thisItem.title);
        intent.putExtra("PRICE", thisItem.price);
        intent.putExtra("PLATFORM", thisItem.platform);
        intent.putExtra("DESCRIPTION", thisItem.description);
        intent.putExtra("IMAGE", thisItem.images);
        intent.putExtra("IDUSER", thisItem.User_idUser);
        intent.putExtra("SHIPPINGTYPE", thisItem.shippingType);
        intent.putExtra("PICKUPLOCATION", thisItem.pickupLocation);
        intent.putExtra("IDITEM", thisItem.idItem);

        startActivity(intent);
    }


    private void deleteOneItem(int idItem){
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        Call<Response> call = NetworkUtil.getRetrofit(gsonConverterFactory).deleteOneItem(idItem);

        retrofit2.Callback<Response> callback = new Callback<Response>(){
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                try {
                    if (response.isSuccessful()) {

                        Response responseGet = response.body();
                        Toast.makeText(getActivity(), responseGet.getMessage(), Toast.LENGTH_LONG).show();
                        getAllItemsByUserId(User_idUser);
                    } else {
                        String errorMessage = response.errorBody().string();
                    }
                }catch(IOException ex)
                {
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        };

        // Send request to web server and process response with the callback object.
        call.enqueue(callback);
    }

    private void updateOneItem(int idItem){
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        Call<Response> call = NetworkUtil.getRetrofit(gsonConverterFactory).updateOneItem(idItem);

        retrofit2.Callback<Response> callback = new Callback<Response>(){
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                try {
                    if (response.isSuccessful()) {

                        Response responseGet = response.body();
                        Toast.makeText(getActivity(), responseGet.getMessage(), Toast.LENGTH_LONG).show();
                        getAllItemsByUserId(User_idUser);
                    } else {
                        String errorMessage = response.errorBody().string();
                    }
                }catch(IOException ex)
                {
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        };

        // Send request to web server and process response with the callback object.
        call.enqueue(callback);
    }

    public void refresh(){
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getAllItemsByUserId(User_idUser);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }
}