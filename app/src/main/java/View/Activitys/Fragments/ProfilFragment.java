package View.Activitys.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.matic.projekttva.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import View.Activitys.Adapters.RecyclerViewAdapter;
import View.Activitys.Adapters.RecyclerViewPS4StoreAdapter;

public class ProfilFragment extends Fragment {
    private ProgressDialog mProgressDialog;
    private String url = "https://store.playstation.com/en-si/grid/STORE-MSF75508-COMINGSOON/1";
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> priceList = new ArrayList<>();
    private ArrayList<String> imageList = new ArrayList<>();
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);
        new Description().execute();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.rv_ps4store);

        return rootView;
    }

    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Android Basic JSoup Tutorial");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document mBlogDocument = Jsoup.connect(url).get();
                // Using Elements to get the Meta data
                Elements mElementDataSize = mBlogDocument.select("div[class=\"grid-cell__body\"]");
                // Locate the content attribute
                int mElementSize = mElementDataSize.size();
                Elements elementImage = null;
              for (int i = 0; i < mElementSize; i++) {
                  Elements elementTitle = mBlogDocument.select("div[class=\"grid-cell__title \"]").select("span").eq(i);
                  String titleString = elementTitle.text();
                  Elements elementPrice = mBlogDocument.select("div[class=\"grid-cell__footer\"]").eq(i);
                  String priceString = elementPrice.text();

                  elementImage = mBlogDocument.select("div[class=\"product-image__img product-image__img--main\"]").select("img");
                  String imageString = null;

                  titleList.add(titleString);
                  priceList.add(priceString);

                }
                fillingImageList(elementImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            RecyclerViewPS4StoreAdapter adapter = new RecyclerViewPS4StoreAdapter(titleList,priceList,imageList);
            recyclerView.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }

    public void fillingImageList(Elements elementImage){
        String imageString = null;
        for (Element element : elementImage) {
            if (element.hasAttr("src"))
                imageString =  element.attr("src");
            imageList.add(imageString);
        }
    }

}
