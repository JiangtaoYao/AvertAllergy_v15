package com.example.xukaijun.avertallergy.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xukaijun.avertallergy.MainActivity;
import com.example.xukaijun.avertallergy.activities.LoginActivity;
import com.example.xukaijun.avertallergy.R;
import com.example.xukaijun.avertallergy.model.User;
import com.example.xukaijun.avertallergy.model.UserDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Activity
 */
public class SplashActivity extends Activity {
    private RelativeLayout rl_splash_root;
    UserDatabase db = null;
    /**
     * This method is to initialize views
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(getApplicationContext(),
                UserDatabase.class, "UserDatabase").fallbackToDestructiveMigration()
                .build();
        setContentView(R.layout.activity_splash);
        rl_splash_root = findViewById(R.id.rl_splash_root);
        //animation
        AlphaAnimation aa = new AlphaAnimation(0, 1);//0to1，from invisible to visible
//        aa.setDuration(500);//duration time
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
//        sa.setDuration(500);
        sa.setFillAfter(true);

        RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//        ra.setDuration(500);
        ra.setFillAfter(true);

        //Create AnimationSet make three anime act in same time
        AnimationSet set = new AnimationSet(false);
        //not order, act at same time
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.addAnimation(ra);
        set.setDuration(2000);

        //play anime
        rl_splash_root.startAnimation(set);

        //listening，AnimationListener interface，create a class to implement
        set.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements Animation.AnimationListener {

        /**
         * call this function when animation play
         *
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * call this function when animation play
         *
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            int id1 = 0;
            CheckAsyncTask  checkAsyncTask = new CheckAsyncTask();
            try {
                id1 = checkAsyncTask.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (id1 == 0){
                dialogShow2();
            /*AlertDialog.Builder builder  = new AlertDialog.Builder(SplashActivity.this);
                TextView title = new TextView(SplashActivity.this);
                title.setGravity(Gravity.CENTER);
                title.setText("Disclaimer");
                builder.setCustomTitle(title);
            builder.setMessage("The content displayed in this application is not intended to be a substitute for professional " +
                    "medical advice, diagnosis or treatment. Never rely on information in this application in place of seeking " +
                    "professional medical advice." ) ;
            builder.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//
                    startActivity(intent); //intent to next activity
                    finish();
                } //including onClick() method
            });
            builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());   //getPID
                    System.exit(0);   //jump out application
                } //including onClick() method
            });
                builder.show();*/
        }else{
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putInt("id", 1);
                intent.putExtras(bundle);
                startActivity(intent); //intent to next activity
                finish();
            }
        }

        private class CheckAsyncTask extends AsyncTask<Void, Void, Integer> {
            @Override
            protected Integer doInBackground(Void... params) {
                List<User> users = db.userDao().getAll();
                if (users.isEmpty())
                {return 0;}
                else {
                    User user = users.get(0);
                    return user.uid;}
            }
        }

        /**
         * call this function when animation repeat
         *
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        /**
         * customize the dialog
         *
         */
        private void dialogShow2() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            LayoutInflater inflater = LayoutInflater.from(SplashActivity.this);
            View v = inflater.inflate(R.layout.custom_dialog, null);
            TextView content = (TextView) v.findViewById(R.id.dialog_content);
            Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
            Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
            final Dialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setContentView(v);//customize the layout
            btn_sure.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//
                    startActivity(intent); //intent to next activity
                    finish();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    android.os.Process.killProcess(android.os.Process.myPid());   //getPID
                    System.exit(0);   //jump out application
                }
            });
        }
    }
}