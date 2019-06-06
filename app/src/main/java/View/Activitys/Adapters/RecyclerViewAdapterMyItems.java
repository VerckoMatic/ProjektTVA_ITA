package View.Activitys.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matic.projekttva.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.ResponsePOJO.ItemResponsePOJO;

import static utils.Constants.BASE_NO_SLASH_URL;

public class RecyclerViewAdapterMyItems extends RecyclerView.Adapter<RecyclerViewAdapterMyItems.ItemViewHolder> {

    public List<ItemResponsePOJO> itemList;
    public MyClickListener onClickListener;
    private Context mContext;

    public void setOnItemClickListener(MyClickListener itemClickListener) {
        onClickListener = itemClickListener;
    }

    public RecyclerViewAdapterMyItems(Context context, List<ItemResponsePOJO> itemList){
        this.mContext = context;
        this.itemList = itemList;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder  {
        CardView cardView;
        TextView tw_title;
        TextView tw_platform;
        TextView tw_price;
        ImageView iw_photo;
        ImageView iw_delete;
        ImageView iw_update;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cv);
            tw_title = (TextView)itemView.findViewById(R.id.et_title);
            tw_platform = (TextView)itemView.findViewById(R.id.tw_platform);
            tw_price = (TextView)itemView.findViewById(R.id.tw_price);
            iw_photo = (ImageView)itemView.findViewById(R.id.iw_photo);
            iw_delete = (ImageView)itemView.findViewById(R.id.iw_delete);
            iw_update = (ImageView)itemView.findViewById(R.id.iw_update);

            iw_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener != null){
                        onClickListener.onDelete(getAdapterPosition());
                    }
                }
            });

            iw_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener != null){
                        onClickListener.onEdit(getAdapterPosition());
                    }
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener != null){
                        onClickListener.onCardClick(getAdapterPosition());
                    }
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_my_items, viewGroup, false);

        return new ItemViewHolder(v);
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



    public interface MyClickListener {
        void onEdit(int p);
        void onDelete(int p);
        void onCardClick(int p);
    }

}