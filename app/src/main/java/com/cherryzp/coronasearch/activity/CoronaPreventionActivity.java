package com.cherryzp.coronasearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cherryzp.coronasearch.R;

public class CoronaPreventionActivity extends AppCompatActivity {

    ImageView coronaPrevention1Iv;
    ImageView coronaPrevention2Iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_prevention);

        init();

    }

    public void init() {

        //id 붙이기
        coronaPrevention1Iv = findViewById(R.id.corona_prevetion_1_iv);
        coronaPrevention2Iv = findViewById(R.id.corona_prevention_2_iv);

        Glide.with(this).load(R.drawable.corona_prevention_info_1).into(coronaPrevention1Iv);
        Glide.with(this).load(R.drawable.corona_prevention_info_2).into(coronaPrevention2Iv);


    }
}
