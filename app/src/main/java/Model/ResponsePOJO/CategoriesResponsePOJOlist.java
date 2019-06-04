package Model.ResponsePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import Model.Classes.Category;

public class CategoriesResponsePOJOlist {

    @SerializedName("categories")
    @Expose
    private List<List<Category>> categories = null;

    public List<List<Category>> getCategories() {
        return categories;
    }

    public void setCategories(List<List<Category>> categories) {
        this.categories = categories;
    }
}
