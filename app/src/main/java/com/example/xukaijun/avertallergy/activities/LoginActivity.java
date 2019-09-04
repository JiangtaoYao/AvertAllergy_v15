package com.example.xukaijun.avertallergy.activities;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CheckBox;

import com.example.xukaijun.avertallergy.MainActivity;
import com.example.xukaijun.avertallergy.R;
import com.example.xukaijun.avertallergy.helpers.InputValidation;
import com.example.xukaijun.avertallergy.model.Allergy;
import com.example.xukaijun.avertallergy.model.AllergyDatabase;
import com.example.xukaijun.avertallergy.model.User;
import com.example.xukaijun.avertallergy.model.UserDatabase;

/*import com.example.xukaijun.login.MainActivity;
import com.example.xukaijun.login.model.Credential;
import com.example.xukaijun.login.model.Cuser;
import com.example.xukaijun.login.model.User;
import com.example.xukaijun.login.sql.DatabaseHelper;
import com.example.xukaijun.login.sql.RestClient;*/

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    UserDatabase db = null;
    AllergyDatabase db1 = null;
    CheckBox peanut;
    CheckBox milk;
    CheckBox egg;
    CheckBox soy;
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private AppCompatButton appCompatButtonLogin;

    private InputValidation inputValidation;
    //private DatabaseHelper databaseHelper;

    private User user;
    private Allergy allergy;
    private int id = 1;
    //private List<Cuser> cuserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = Room.databaseBuilder(getApplicationContext(),
                UserDatabase.class, "UserDatabase").fallbackToDestructiveMigration()
                .build();
        db1 = Room.databaseBuilder(getApplicationContext(),
                AllergyDatabase.class, "AllergyDatabase").fallbackToDestructiveMigration()
                .build();
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        peanut = (CheckBox) findViewById(R.id.peanut);
        milk = (CheckBox) findViewById(R.id.milk);
        soy = (CheckBox) findViewById(R.id.soy);
        egg = (CheckBox) findViewById(R.id.egg);
        CheckAsyncTask checkAsyncTask = new CheckAsyncTask();
        int id1 = 0;
        try {
            id1 = checkAsyncTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (id1 == 1) {
            Intent mainActivity = new Intent(this, MainActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putInt("id", 1);
            mainActivity.putExtras(bundle);
            startActivity(mainActivity);
        } else {
        }
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        user = new User();
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
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
                SignAsyncTask signAsyncTask = new SignAsyncTask();
                signAsyncTask.execute("Friend", temp1, temp2, temp3, temp4);
        }
    }

    /**
     * This method is to pass information to SQLite
     */
    private class SignAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            List<User> users = db.userDao().getAll();
            if (users.isEmpty()) {
                User user = new User();
                String temp1 = params[1];
                String temp2 = params[2];
                String temp3 = params[3];
                String temp4 = params[4];
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
                user.setId(id);
                user.setFirstName(params[0]);
                db.userDao().insert(user);
                return "Successful";
            } else return "Successful";
        }

        @Override
        protected void onPostExecute(String response) {
            Snackbar.make(nestedScrollView, response, Snackbar.LENGTH_LONG).show();
            Intent mainActivity = new Intent(getApplication(), MainActivity.class);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            mainActivity.putExtras(bundle);
            startActivity(mainActivity);
            //emptyInputEditText();
        }
    }

    //check the user exist or not
    private class CheckAsyncTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            List<User> users = db.userDao().getAll();
            if (users.isEmpty()) {
                return 0;
            } else {
                User user = users.get(0);
                return user.uid;
            }
        }
    }
}
