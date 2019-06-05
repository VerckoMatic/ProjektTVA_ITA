package View.Activitys.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matic.projekttva.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Model.ResponsePOJO.ItemResponsePOJO;

import static utils.Constants.BASE_NO_SLASH_URL;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> implements Filterable {

    public List<ItemResponsePOJO> itemList;
    public List<ItemResponsePOJO> itemSearchFull;
    private View.OnClickListener mOnItemClickListener;


    public RecyclerViewAdapter(List<ItemResponsePOJO> itemList){
        this.itemList = itemList;
        itemSearchFull = new ArrayList<>(itemList);
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<ItemResponsePOJO> filteredList = new ArrayList<>();

           if(constraint == null ||constraint.length() == 0){
               filteredList.addAll(itemSearchFull);
           }else{
               String filterPattern = constraint.toString().toLowerCase().trim();
               for(ItemResponsePOJO item : itemSearchFull){
                   if(item.getTitle().toLowerCase().contains(filterPattern)){
                       filteredList.add(item);
                   }
               }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

           return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            itemList.clear();
            itemList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tw_title;
        TextView tw_platform;
        TextView tw_price;
        ImageView iw_photo;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cv);
            tw_title = (TextView)itemView.findViewById(R.id.tw_title);
            tw_platform = (TextView)itemView.findViewById(R.id.tw_platform);
            tw_price = (TextView)itemView.findViewById(R.id.tw_price);
            iw_photo = (ImageView)itemView.findViewById(R.id.iw_photo);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        ItemViewHolder pvh = new ItemViewHolder(v);
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
