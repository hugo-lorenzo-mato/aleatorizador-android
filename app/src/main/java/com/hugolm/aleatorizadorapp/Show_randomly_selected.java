package com.hugolm.aleatorizadorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Show_randomly_selected extends AppCompatActivity {
    Bundle b;
    String selected;
    TextView tvWinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_randomly_selected);
        b = getIntent().getExtras();
        String selected = b.getString("winner").toString();
        tvWinner = (TextView)findViewById(R.id.tvSelected);
        tvWinner.setText(selected);
        ImageView ivRot = (ImageView)findViewById(R.id.ivSelected) ;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        ivRot.startAnimation(animation);
    }


    public void onStartAgain(View view) {
        Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMain);
    }
}
