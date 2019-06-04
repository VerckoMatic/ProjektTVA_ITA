package View.Activitys.Adapters;

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

public class RecyclerViewPS4StoreAdapter extends RecyclerView.Adapter<RecyclerViewPS4StoreAdapter.contentViewHolder>{
    public List<String> titleList;
    public List<String> priceList;
    public List<String> imageList;


    public RecyclerViewPS4StoreAdapter(List<String> titleList, List<String> priceList, List<String> imageList){
        this.titleList = titleList;
        this.priceList = priceList;
        this.imageList = imageList;
    }


    public class contentViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tw_title;
        TextView tw_price;
        ImageView iw_photo;

        public contentViewHolder(View contentView) {
            super(contentView);
            cardView = (CardView)contentView.findViewById(R.id.cv_content);
            tw_title = (TextView)contentView.findViewById(R.id.tw_title_content);
            tw_price = (TextView)contentView.findViewById(R.id.tw_price_content);
            iw_photo = (ImageView)contentView.findViewById(R.id.iw_photo_content);

            contentView.setTag(this);
        }
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    @Override
    public RecyclerViewPS4StoreAdapter.contentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_ps4_store_content, viewGroup, false);
        RecyclerViewPS4StoreAdapter.contentViewHolder pvh = new RecyclerViewPS4StoreAdapter.contentViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(RecyclerViewPS4StoreAdapter.contentViewHolder contentViewHolder, int i) {
        contentViewHolder.tw_title.setText(titleList.get(i));
        contentViewHolder.tw_price.setText(priceList.get(i));
        Picasso.get().load(imageList.get(i)).into(contentViewHolder.iw_photo);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
