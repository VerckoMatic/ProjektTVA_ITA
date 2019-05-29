package View.Activitys.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matic.projekttva.R;

public class AddItemBasicInfo extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_item_basic_info, container, false);
    }

    public void checkType(String type){
        if(type.equals("Game")){
            FragmentManager fragmentManager2 = getFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            AddGameFragment fragment2 = new AddGameFragment();
            fragmentTransaction2.addToBackStack("xyz");
            fragmentTransaction2.hide(AddItemBasicInfo.this);
            fragmentTransaction2.add(android.R.id.content, fragment2);
            fragmentTransaction2.commit();
        }else if(type.equals("Devices")){
            FragmentManager fragmentManager2 = getFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            AddDeviceFragment fragment2 = new AddDeviceFragment();
            fragmentTransaction2.addToBackStack("xyz");
            fragmentTransaction2.hide(AddItemBasicInfo.this);
            fragmentTransaction2.add(android.R.id.content, fragment2);
            fragmentTransaction2.commit();
        }else if(type.equals("Accessories")){
            FragmentManager fragmentManager2 = getFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            AddAccessorieFragment fragment2 = new AddAccessorieFragment();
            fragmentTransaction2.addToBackStack("xyz");
            fragmentTransaction2.hide(AddItemBasicInfo.this);
            fragmentTransaction2.add(android.R.id.content, fragment2);
            fragmentTransaction2.commit();

        }
    }
}
