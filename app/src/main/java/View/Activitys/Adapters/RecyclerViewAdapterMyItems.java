package View.Activitys.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import Model.Classes.Response;
import Model.ResponsePOJO.ItemResponsePOJO;
import Model.ResponsePOJO.ItemResponsePOJOlist;
import View.Activitys.ShowSelectedItem;
import network.NetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;

import static utils.Constants.BASE_NO_SLASH_URL;

public class RecyclerViewAdapterMyItems extends RecyclerView.Adapter<RecyclerViewAdapterMyItems.ItemViewHolder> {

    public List<ItemResponsePOJO> itemList;
    private View.OnClickListener mOnItemClickListener;


    public RecyclerViewAdapterMyItems(List<ItemResponsePOJO> itemList){
        this.itemList = itemList;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        MyClickListener listener;

        CardView cardView;
        TextView tw_title;
        TextView tw_platform;
        TextView tw_price;
        ImageView iw_photo;
        ImageView iw_delete;
        ImageView iw_update;

        public ItemViewHolder(View itemView, MyClickListener listener) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cv);
            tw_title = (TextView)itemView.findViewById(R.id.tw_title);
            tw_platform = (TextView)itemView.findViewById(R.id.tw_platform);
            tw_price = (TextView)itemView.findViewById(R.id.tw_price);
            iw_photo = (ImageView)itemView.findViewById(R.id.iw_photo);
            iw_delete = (ImageView)itemView.findViewById(R.id.iw_delete);
            iw_update = (ImageView)itemView.findViewById(R.id.iw_update);
            this.listener = listener;
            itemView.setTag(this);

            iw_delete.setOnClickListener(this);
            iw_update.setOnClickListener(this);
             cardView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iw_update:
                    listener.onEdit(this.getLayoutPosition());
                    break;
                case R.id.iw_delete:
                    listener.onDelete(this.getLayoutPosition());
                    break;
                case R.id.cv:
                    listener.onCardClick(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }
        public interface MyClickListener {
            void onEdit(int p);
            void onDelete(int p);
            void onCardClick(int p);
        }

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_my_items, viewGroup, false);
        ItemViewHolder pvh = new ItemViewHolder(v, new ItemViewHolder.MyClickListener() {
            @Override
            public void onEdit(int p) {
                Toast.makeText(v.getContext(), "Klik na edit", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDelete(int p) {
                Toast.makeText(v.getContext(), "Klik na delete", Toast.LENGTH_LONG).show();
                deleteOneItem(itemList.get(p).idItem);
            }
            @Override
            public void onCardClick(int p) {

                Intent intent = new Intent(v.getContext(), ShowSelectedItem.class);
                intent.putExtra("TITLE", itemList.get(p).title);
                intent.putExtra("PRICE", itemList.get(p).price);
                intent.putExtra("PLATFORM", itemList.get(p).platform);
                //intent.putExtra("CATEGORIES", categories);
                intent.putExtra("DESCRIPTION", itemList.get(p).description);
                intent.putExtra("IMAGE", itemList.get(p).images);
                intent.putExtra("IDUSER", itemList.get(p).User_idUser);
                intent.putExtra("SHIPPINGTYPE", itemList.get(p).shippingType);
                intent.putExtra("PICKUPLOCATION", itemList.get(p).pickupLocation);

                v.getContext().startActivity(intent);
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
                                Toast.makeText(v.getContext(), responseGet.getMessage(), Toast.LENGTH_LONG).show();

                            } else {
                                String errorMessage = response.errorBody().string();
                            }
                        }catch(IOException ex)
                        {
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        Toast.makeText(v.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                };

                // Send request to web server and process response with the callback object.
                call.enqueue(callback);
            }

            //mSubscription handlers
            private void handleResponse(Model.Classes.Response response) {

                Toast.makeText(v.getContext(), response.getMessage(), Toast.LENGTH_LONG ).show();

            }

            private void handleError(Throwable error) {

                if (error instanceof HttpException) {

                    Gson gson = new GsonBuilder().create();

                    try {

                        String errorBody = ((HttpException) error).response().errorBody().string();
                        Model.Classes.Response response = gson.fromJson(errorBody, Model.Classes.Response.class);
                        Toast.makeText(v.getContext(), response.getMessage(), Toast.LENGTH_LONG ).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(v.getContext(), "Network Error", Toast.LENGTH_LONG ).show();
                }
            }

        });


        return pvh;
    }


    @Override
    public void onBindViewHolder(ItemViewHolder personViewHolder, int i) {
        personViewHolder.tw_title.setText(itemList.get(i).title);
        personViewHolder.tw_platform.setText(itemList.get(i).platform);
        personViewHolder.tw_price.setText(String.valueOf(itemList.get(i).price)  + "€");
        Picasso.get().load(BASE_NO_SLASH_URL+itemList.get(i).images).into(personViewHolder.iw_photo);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
