package Model.Classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemResponsePOJOlist {
    @SerializedName("data")
    private List<ItemResponsePOJO> itemResponsePOJOlist;

    public ItemResponsePOJOlist() {
    }

    public ItemResponsePOJOlist(List<ItemResponsePOJO> itemResponsePOJOlist) {
        this.itemResponsePOJOlist = itemResponsePOJOlist;
    }

    public List<ItemResponsePOJO> getItemResponsePOJOlist() {
        return itemResponsePOJOlist;
    }

    public void setItemResponsePOJOlist(List<ItemResponsePOJO> itemResponsePOJOlist) {
        this.itemResponsePOJOlist = itemResponsePOJOlist;
    }
}
