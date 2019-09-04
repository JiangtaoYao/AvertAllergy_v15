package com.example.xukaijun.avertallergy.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xukaijun.avertallergy.MainActivity;
import com.example.xukaijun.avertallergy.R;
import com.example.xukaijun.avertallergy.helpers.CaptureActivityPortrait;
import com.example.xukaijun.avertallergy.helpers.HttpUrlHelper;
import com.example.xukaijun.avertallergy.helpers.MyRecyclerViewAdapter;
import com.example.xukaijun.avertallergy.model.Allergy;
import com.example.xukaijun.avertallergy.model.AllergyDatabase;
import com.example.xukaijun.avertallergy.model.Food;
import com.example.xukaijun.avertallergy.model.FoodDatabase;
import com.example.xukaijun.avertallergy.model.User;
import com.example.xukaijun.avertallergy.model.UserDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
//import com.example.xukaijun.avertallergy.sql.RestClient;

import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class MainFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {
    UserDatabase db = null;
    AllergyDatabase db1 = null;
    FoodDatabase db2 = null;
    MyRecyclerViewAdapter adapter;
    private ArrayList<Allergy> allergyArrayList = new ArrayList<>();
    private TextView textView_FoodName,textView_Suggestion,textView_barcode;
    private static final String EXTRA_CODE = "com.example.testingcodereading.code";
    private AppCompatImageView appCompatImageView_food,appCompatImageView_emoji;
    private Button appCompatButtonHome,appCompatButtonIngredient,appCompatButtonClear;
    private ImageButton imageButton_scanner,imageButton_history;
    private int id;
    private String url;
    private Food food1=new Food();
    private TextView mTextMessage;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    private ArrayList<String> foodInfo = new ArrayList<>();
    private ArrayList<Food> food_list = new ArrayList<>();
    private RecyclerView recyclerView;
    View vMain;

    public static MainFragment newInstance(String code) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CODE, code);

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * This method is to initialize views
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder((MainActivity)getContext(),
                UserDatabase.class, "UserDatabase") .fallbackToDestructiveMigration()
                .build();
        db1 = Room.databaseBuilder((MainActivity)getContext(),
                AllergyDatabase.class, "AllergyDatabase") .fallbackToDestructiveMigration()
                .build();
        db2 = Room.databaseBuilder((MainActivity)getContext(),
                FoodDatabase.class, "FoodDatabase") .fallbackToDestructiveMigration()
                .build();
        HttpUrlHelper httpUrlHelper = new HttpUrlHelper();
        id =  ((MainActivity)getActivity()).getId();
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle("Home");
        textView_FoodName = vMain.findViewById(R.id.textView_FoodName);
        textView_Suggestion= vMain.findViewById(R.id.textView_Suggestion);
        textView_barcode = vMain.findViewById(R.id.textView_barcode);
        appCompatImageView_food = vMain.findViewById(R.id.AppCompatImageView_food);
        appCompatImageView_emoji = vMain.findViewById(R.id.AppCompatImageView_emoji);
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();
        appCompatButtonIngredient = vMain.findViewById(R.id.appCompatButtonIngredient);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("loading...");
        appCompatButtonHome = vMain.findViewById(R.id.appCompatButtonhome);
        appCompatButtonClear= vMain.findViewById(R.id.appCompatButtonClear);
        imageButton_scanner = vMain.findViewById(R.id.imageButton_scanner);
        imageButton_history = vMain.findViewById(R.id.imageButton_history);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int hight = dm.heightPixels;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageButton_scanner.getLayoutParams());
        lp.setMargins((int) (width/12), hight*3/12, (int) 0, 0);
        lp.width=(int) (width/3);
        lp.height=(int) (width/3);
        imageButton_scanner.setLayoutParams(lp);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(imageButton_history.getLayoutParams());
        lp1.setMargins((int) (width*3/24), hight*3/12, 0, 0);
        lp1.width=(int) (width/3);
        lp1.height=(int) (width/3);
        imageButton_history.setLayoutParams(lp1);
        linearLayout = vMain.findViewById(R.id.choose_list);
        imageButton_scanner.setOnClickListener(new View.OnClickListener() { //including onClick() method
            public void onClick(View v) {
                scanNow(v);
            }
        });
        appCompatButtonIngredient.setOnClickListener(new View.OnClickListener() { //including onClick() method
            public void onClick(View v) {
                appCompatButtonHome.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
                builder.setTitle("Ingredient" ) ;
                builder.setMessage(food1.getIngredients_text() ) ;
                builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                    }
                    });
                builder.show();
            }
        });
        appCompatButtonHome.setOnClickListener(new View.OnClickListener() { //including onClick() method
            public void onClick(View v) {
                startActivity(new Intent(getContext(),LoginActivity.class));
            }
        });
        appCompatButtonClear.setOnClickListener(new View.OnClickListener() { //including onClick() method
            public void onClick(View v) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
                builder.setTitle("Clear History?" ) ;
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        ClearDatabase clearDatabase = new ClearDatabase();
                        clearDatabase.execute();
                        startActivity(new Intent(getContext(),LoginActivity.class));
                    }
                });
                builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        imageButton_history.setOnClickListener(new View.OnClickListener() { //including onClick() method
            public void onClick(View v) {
                getActivity().setTitle("History");
                Food temp=new Food();
                if (food_list.size()>1) {
                    for (int p = 0; p < food_list.size() - 1; p++) {
                        for (int q = p + 1; q < food_list.size(); q++) {
                            String time1 = food_list.get(p).getSdate();
                            String time2 = food_list.get(q).getSdate();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//此处对应需要转化的时间格式，若转化时间为“2007-9-15”，将"yyyy.MM.dd"改为“yyyy-MM-dd”
                            Date date1 = null;
                            try {
                                date1 = format.parse(time1);
                                Date date2 = format.parse(time2);

                                if (date1.before(date2)) {
                                    temp = food_list.get(p);
                                    food_list.set(p,food_list.get(q));
                                    food_list.set(q,temp);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                if (food_list.isEmpty()){
                    foodInfo.add("no record yet");
                }else {
                    for (Food temp1:food_list){
                        foodInfo.add(String.valueOf(temp1.getCode())+" \t"+temp1.getProduct_name()+"\t"+temp1.getSdate());
                    }
                }
                setupRecyclerView(v);
            }
        });
        return vMain;
    }

    public void setupRecyclerView(View view){
        // set up the RecyclerView
        recyclerView = vMain.findViewById(R.id.food_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), foodInfo);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        appCompatButtonClear.setVisibility(View.VISIBLE);
        appCompatButtonHome.setVisibility(View.VISIBLE);
        imageButton_scanner.setVisibility(View.GONE);
        imageButton_history.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

    }

    //scan method
    public void scanNow(View view){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.forSupportFragment(MainFragment.this).setPrompt("Place the barcode perpendicular to the red line.").setCameraId(0)
                    .setOrientationLocked(true).setBeepEnabled(true).setCaptureActivity(CaptureActivityPortrait.class).initiateScan();
        }
        getActivity().setTitle("Results");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    IntentIntegrator integrator = new IntentIntegrator(getActivity());
                    integrator.forSupportFragment(MainFragment.this).setPrompt("Place the barcode perpendicular to the red line.").initiateScan();
                } else {
                    Toast.makeText(getContext(), "Camera can't open", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //scan result return
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            imageButton_scanner.setVisibility(View.GONE);
            imageButton_history.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            super.onActivityResult(requestCode, resultCode, intent);
            System.out.println("never here");
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                textView_barcode.setText(scanContent);
                appCompatImageView_food.setVisibility(View.GONE);
                appCompatButtonIngredient.setVisibility(View.GONE);
                textView_Suggestion.setVisibility(View.GONE);
                textView_barcode.setVisibility(View.INVISIBLE);
                if (textView_barcode.getText().equals("")){
                    startActivity(new Intent(getContext(),LoginActivity.class));
                }else {
                    food1.setCode(textView_barcode.getText().toString());
                    url = "https://world.openfoodfacts.org/api/v0/product/"+textView_barcode.getText()+".json";
                    progressDialog.show();
                    SearchCode searchCode = new SearchCode();
                    searchCode.execute(url);
                }}
            else{
                Toast toast = Toast.makeText(getContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }catch (Exception e){}

    }

    //read local database
    private class ReadDatabase extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
    protected ArrayList doInBackground(Void... params) {
        List<User> users = db.userDao().getAll();
        User user = new User();
        List<Allergy> allergies = db1.allergyDao().getAll();
        List<Food> foodList = db2.foodDao().getAll();
        if (!foodList.isEmpty())
        {
            for (Food temp : foodList) {
                food_list.add(temp);
            }
        }else {
            }
        Allergy allergy = new Allergy();
        user = users.get(0);
        ArrayList<String> strings = new ArrayList<>();
        strings.add(0,user.getFirstName());
        allergyArrayList = new ArrayList<>();
        for (Allergy temp : allergies) {
            allergyArrayList.add(temp);
        }
        return strings;
        }
        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            String name = strings.get(0);
            strings.remove(0);
            String temp = "";
            for (String i:strings){
                temp += i;
                temp += " ";
            }
            //textInputLayoutGoalstep.setHint(String.valueOf(user1.getGoalsteps()));
        }
    }

    //search the code on open food fact
    public class SearchCode extends AsyncTask<String,String,String > {
        @Override
        protected String doInBackground(String... params) {
            String test = new HttpUrlHelper().getHttpUrlConnection(params[0]);
            return test;
        }

        @Override
        protected void onPostExecute(String mix) {
            textView_FoodName.setText(mix);
            try {
                JSONObject job = new JSONObject(mix);
                Food food = new Food();
                textView_Suggestion.setVisibility(View.VISIBLE);
                if(job.getJSONObject("product").has("product_name_en")){
                food.setProduct_name(job.getJSONObject("product").getString("product_name_en"));}
                else if (job.getJSONObject("product").has("product_name")){
                    food.setProduct_name(job.getJSONObject("product").getString("product_name"));
                }
                else if (job.getJSONObject("product").has("product_name_fr"))
                {
                    food.setProduct_name(job.getJSONObject("product").getString("product_name_fr"));
                }else {food.setProduct_name("None");}
                if(job.getJSONObject("product").has("brands")){
                food.setBrands(job.getJSONObject("product").getString("brands"));}
                else {food.setBrands(" ");}
                if (job.getJSONObject("product").has("ingredients_text_en"))
                food.setIngredients_text(job.getJSONObject("product").getString("ingredients_text_en"));
                else if (job.getJSONObject("product").has("ingredients_text")){
                    food.setIngredients_text(job.getJSONObject("product").getString("ingredients_text"));
                } else if (job.getJSONObject("product").has("ingredients_tags")){
                    food.setIngredients_text(job.getJSONObject("product").getString("ingredients_tags"));}
                    else {food.setIngredients_text("none");}
                if (!Pattern.compile("(?i)[a-z]").matcher(food.getIngredients_text()).find() ) {//contain character is true
                    food.setIngredients_text("none");
                }
                food1.setProduct_name(food.getProduct_name());
                food1.setIngredients_text(food.getIngredients_text());
                UpdateDatabase updateDatabase = new UpdateDatabase();
                updateDatabase.execute(food1);
                    if (job.getJSONObject("product").has("image_url")){
                food.setImage_url(job.getJSONObject("product").getString("image_url"));
                        try {
                            ImageTask imageTask = new ImageTask();
                            imageTask.execute(food.getImage_url());
                        }catch (Exception e){}}
                textView_FoodName.setText(food.getProduct_name()+" " + food.getBrands());
                textView_FoodName.setVisibility(View.VISIBLE);
                appCompatButtonHome.setVisibility(View.VISIBLE);
                boolean judge = false;
                String test1="";
                String test2="";
                if (!allergyArrayList.isEmpty()&&!food.getIngredients_text().equals("None")){
                for (Allergy allergy:allergyArrayList)
                {
                    if (!test2.equals("")){
                        test2+= " and ";
                    }
                    test2+=allergy.getAllergyName();
                    if (food.getIngredients_text().toLowerCase().indexOf(allergy.getAllergyName())!=-1)
                    {
                        if (!test1.equals("")){
                            test1+= " and ";
                        }
                        test1 += allergy.getAllergyName();
                        judge = true;
                    }
                }}
                if (food.getProduct_name().length()>100 || food.getIngredients_text().equals("none")||food.getProduct_name().equals("none"))
                {   textView_Suggestion.setText("Sorry, this item is not in our database or relevant information is not fully recorded yet");
                    textView_Suggestion.setTextColor(Color.parseColor("#000000"));
                    appCompatButtonHome.setVisibility(View.VISIBLE);
                    appCompatImageView_emoji.setBackgroundResource(R.drawable.emoji3);
                    appCompatImageView_emoji.setVisibility(View.VISIBLE);
                }
                else if (judge)
                {
                    textView_Suggestion.setText("The published product information says this product contains "+test1);
                    textView_Suggestion.setTextColor(Color.parseColor("#FF0000"));
                    textView_FoodName.setText(food.getProduct_name());
                    appCompatButtonIngredient.setVisibility(View.VISIBLE);
                    appCompatButtonIngredient.setVisibility(View.VISIBLE);
                    appCompatImageView_emoji.setBackgroundResource(R.drawable.emoji2);
                    appCompatImageView_emoji.setVisibility(View.VISIBLE);

                }
                else{
                    textView_Suggestion.setText("The published product information says this product is "+test2+" free");
                    textView_FoodName.setText(food.getProduct_name());
                    appCompatButtonIngredient.setVisibility(View.VISIBLE);
                    textView_Suggestion.setTextColor(Color.parseColor("#000000"));
                    appCompatButtonIngredient.setVisibility(View.VISIBLE);
                    appCompatImageView_emoji.setBackgroundResource(R.drawable.emoji1);
                    appCompatImageView_emoji.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                textView_Suggestion.setText("Sorry, this item is not in our database or relevant information is not fully recorded yet");
                textView_Suggestion.setTextColor(Color.parseColor("#000000"));
                appCompatButtonHome.setVisibility(View.VISIBLE);
                appCompatImageView_emoji.setBackgroundResource(R.drawable.emoji3);
                appCompatImageView_emoji.setVisibility(View.VISIBLE);
            }
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 500);
        }
    }
    //get image
    private class ImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... keyword) {
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(keyword[0]).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            appCompatImageView_food.setVisibility(View.VISIBLE);
            appCompatImageView_food.setImageBitmap(result);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String[] info = adapter.getItem(position).split(" ");
        try{
            food1.setCode(info[0]);
            url = "https://world.openfoodfacts.org/api/v0/product/"+info[0]+".json";
            progressDialog.show();
            SearchCode searchCode = new SearchCode();
            searchCode.execute(url);
            appCompatButtonClear.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);}catch (Exception e){}
    }

    private class UpdateDatabase extends AsyncTask<Food, Void, Void> {
        @Override
        protected Void doInBackground(Food... params) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String scanDate = sdf.format(new Date());
            params[0].setSdate(scanDate);
            boolean judge = true;
            for (Food temp:db2.foodDao().getAll()){
                if (temp.getCode().equals(params[0].getCode())){
                    db2.foodDao().updateFoods(params[0]);
                    judge = false;
                }
            }
            if (judge){
            db2.foodDao().insert(params[0]);}
            return null;
        }
    }
    private class ClearDatabase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
           db2.foodDao().deleteAll();
           return "Successful";
        }

        @Override
        protected void onPostExecute(String user1) {
        }
    }
}
