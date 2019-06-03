package View.Activitys.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.matic.projekttva.R;

import Model.ItemValidation;

public class AddDeviceFragment extends Fragment {

    EditText et_title;
    EditText et_description;
    Spinner sp_platform;
    Spinner sp_warranty;
    Button btn_toBasic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device, container, false);

        et_title = (EditText) rootView.findViewById(R.id.et_title);
        et_description = (EditText) rootView.findViewById(R.id.et_description);
        sp_platform = (Spinner) rootView.findViewById(R.id.sp_platform);
        sp_warranty = (Spinner) rootView.findViewById(R.id.sp_warranty);
        btn_toBasic = (Button) rootView.findViewById(R.id.btn_toBasic);

        String[] sp_platformArray = getResources().getStringArray(R.array.sp_platformStrings);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.spinner_layout,R.id.text, sp_platformArray);
        sp_platform.setAdapter(adapter);

        String[] sp_warrantyArray = getResources().getStringArray(R.array.sp_warrantyStrings);
        ArrayAdapter<String> adapterPlatform =new ArrayAdapter<String>(getActivity(),R.layout.spinner_layout,R.id.text, sp_warrantyArray);
        sp_warranty.setAdapter(adapterPlatform);

        btn_toBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = et_title.getText().toString();
                String description = et_description.getText().toString();
                String platformString = sp_platform.getSelectedItem().toString();
                String warrantyString = sp_warranty.getSelectedItem().toString();
                int warranty;
                if(warrantyString.equals("Da")){
                    warranty = 1;
                }else{
                    warranty = 0;
                }
                if(!ItemValidation.validateDescriptionCreateItemFragments(description) || !ItemValidation.validateTitleCreateItemFragments(title)){
                    if(!ItemValidation.validateDescriptionCreateItemFragments(description)){
                        et_description.setError( "Vpišite opis, vpišete lahko do 250 znakov!" );
                    }
                    if(!ItemValidation.validateTitleCreateItemFragments(title)){
                        et_title.setError( "Vpišite naslov, vpišete lahko do 40 znakov!" );
                    }
                }else{
                    Intent i = getActivity().getIntent();
                    String type = i.getExtras().getString("TYPE");
                    AddItemBasicInfo fragment2 = new AddItemBasicInfo();
                    Bundle bundle = new Bundle();
                    bundle.putString("TITLE", title);
                    bundle.putString("DESCRIPTION", description);
                    bundle.putString("PLATFORM", platformString);
                    bundle.putInt("WARRANTY", warranty);
                    bundle.putString("TYPE", type);
                    fragment2.setArguments(bundle);
                    FragmentManager fragmentManager2 = getFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.addToBackStack("xyz");
                    fragmentTransaction2.hide(AddDeviceFragment.this);
                    fragmentTransaction2.add(android.R.id.content, fragment2);
                    fragmentTransaction2.commit();
                }
            }
        });
        return  rootView;
    }
}
