package com.example.xukaijun.avertallergy.activities;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.xukaijun.avertallergy.R;
import com.example.xukaijun.avertallergy.model.Allergy;
import com.example.xukaijun.avertallergy.model.AllergyDatabase;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment {

    View vMain;
    /**
     * This method is to initialize views
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_help, container, false);
        getActivity().setTitle("Help");

        return vMain;
    }
}
