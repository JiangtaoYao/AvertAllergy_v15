package com.example.xukaijun.avertallergy.activities;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xukaijun.avertallergy.MainActivity;
import com.example.xukaijun.avertallergy.R;
import com.example.xukaijun.avertallergy.model.Allergy;
import com.example.xukaijun.avertallergy.model.AllergyDatabase;
import com.example.xukaijun.avertallergy.model.User;
import com.example.xukaijun.avertallergy.model.UserDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditFragment extends Fragment {
    AllergyDatabase db1 = null;
    CheckBox peanut;
    CheckBox milk;
    CheckBox egg;
    CheckBox soy;
    private ArrayList<Allergy> allergyArrayList;
    private AppCompatButton appCompatButtonEdit;
    View vMain;
    /**
     * This method is to initialize views
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_edit, container, false);
        getActivity().setTitle("Allergy Preference");
        peanut = (CheckBox) vMain.findViewById(R.id.peanut);
        milk = (CheckBox) vMain.findViewById(R.id.milk);
        soy = (CheckBox) vMain.findViewById(R.id.soy);
        egg = (CheckBox) vMain.findViewById(R.id.egg);
        db1 = Room.databaseBuilder(getContext(),
                AllergyDatabase.class, "AllergyDatabase").fallbackToDestructiveMigration()
                .build();
        allergyArrayList = new ArrayList<>();
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();
        Button appCompatButtonEdit = (Button)vMain.findViewById(R.id.appCompatButtonEdit);
        appCompatButtonEdit.setOnClickListener(new View.OnClickListener() { //including onClick() method
            public void onClick(View v) {
                        String temp1 = "";
                        if (peanut.isChecked()) {
                            temp1 = "true";
                        } else {
                            temp1 = "false";
                        }
                        String temp2 = "";
                        if (milk.isChecked()) {
                            temp2 = "true";
                        } else {
                            temp2 = "false";
                        }
                        String temp3 = "";
                        if (egg.isChecked()) {
                            temp3 = "true";
                        } else {
                            temp3 = "false";
                        }
                        String temp4 = "";
                        if (soy.isChecked()) {
                            temp4 = "true";
                        } else {
                            temp4 = "false";
                        }
                        UpdateDatabase updateDatabase = new UpdateDatabase();
                        updateDatabase.execute(temp1, temp2, temp3, temp4);
            }
        });
        return vMain;
    }
    //read local database
    private class ReadDatabase extends AsyncTask<Void, Void, ArrayList<Allergy>> {
        @Override
        protected ArrayList doInBackground(Void... params) {
            List<Allergy> allergies = db1.allergyDao().getAll();
            if (!allergies.isEmpty()){
            for (Allergy temp : allergies) {
                allergyArrayList.add(temp);
            }}
            return allergyArrayList;
        }
        @Override
        protected void onPostExecute(ArrayList<Allergy> allergies) {
            if (!allergies.isEmpty()){
            for (Allergy allergy:allergies){
                if (allergy.getAllergyName().equals("peanut")){
                    peanut.setChecked(true);
                }
                if (allergy.getAllergyName().equals("milk")){
                    milk.setChecked(true);
                }
                if (allergy.getAllergyName().equals("soy")){
                    soy.setChecked(true);
                }
                if (allergy.getAllergyName().equals("egg")){
                    egg.setChecked(true);
                }
            }
        }}
    }
    //update to local database
    private class UpdateDatabase extends AsyncTask<String, Void, String> {
        @Override
    protected String doInBackground(String... params) {
            db1.allergyDao().deleteAll();
            String temp1 = params[0];
            String temp2 = params[1];
            String temp3 = params[2];
            String temp4 = params[3];
            if (temp1.equals("true")) {
                Allergy allergy = new Allergy();
                allergy.setId(1);
                allergy.setAllergyName("peanut");
                db1.allergyDao().insert(allergy);
            }
            if (temp2.equals("true")) {
                Allergy allergy = new Allergy();
                allergy.setId(2);
                allergy.setAllergyName("milk");
                db1.allergyDao().insert(allergy);
            }
            if (temp3.equals("true")) {
                Allergy allergy = new Allergy();
                allergy.setId(3);
                allergy.setAllergyName("egg");
                db1.allergyDao().insert(allergy);
            }
            if (temp4.equals("true")) {
                Allergy allergy = new Allergy();
                allergy.setId(4);
                allergy.setAllergyName("soy");
                db1.allergyDao().insert(allergy);
            }
        return "Success"; }
        @Override
        protected void onPostExecute(String message) {
            AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
            builder.setTitle("Update" ) ;
            builder.setMessage(message) ;
            builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    startActivity(new Intent(getContext(),LoginActivity.class));
                }
            });
            builder.show();
        }
    }

}
