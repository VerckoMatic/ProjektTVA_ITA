package View.Activitys.Fragments;

import android.app.SearchManager;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import Model.ResponsePOJO.ItemResponsePOJO;
import Model.ResponsePOJO.ItemResponsePOJOlist;
import View.Activitys.Adapters.RecyclerViewAdapter;
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

public class HomeFragment extends Fragment {
    private String mToken;
    private String mEmail;
    Context mContext;
    TextView tw_out;
    Bitmap bitmap;
    AlertDialog alertDialogWithRadioButtons;
    String selected;
    private CompositeSubscription mSubscriptions;
    private NetworkUtil networkUtil;
    private SharedPreferences mSharedPreferences;
    Context applicationContext = Homepage.getContextOfApplication();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ItemResponsePOJOlist itemResponsePOJOlist = new ItemResponsePOJOlist();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mSubscriptions = new CompositeSubscription();
        initSharedPreferences();
        setHasOptionsMenu(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.rv);


        SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getAllItems();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        getAllItems();

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
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");


    }

    private void getAllItems(){
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        Call<ItemResponsePOJOlist> call = NetworkUtil.getRetrofit(gsonConverterFactory).getAllItems();

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

            //Toast.makeText(applicationContext, "Network Error", Toast.LENGTH_LONG ).show();
        }
    }


    private void showItems(ItemResponsePOJOlist userDtoList)
    {
        if(userDtoList != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            itemResponsePOJOlist.setItemResponsePOJOlist(userDtoList.getItemResponsePOJOlist());
            adapter = new RecyclerViewAdapter(userDtoList.getItemResponsePOJOlist());
            adapter.setOnItemClickListener(onItemClickListener);
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
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }

        });
    }




}
