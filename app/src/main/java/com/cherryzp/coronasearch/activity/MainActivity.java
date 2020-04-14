package com.cherryzp.coronasearch.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cherryzp.coronasearch.BuildConfig;
import com.cherryzp.coronasearch.R;
import com.cherryzp.coronasearch.adapter.ClinicRecyclerAdapter;
import com.cherryzp.coronasearch.adapter.CustumCalloutBalloonAdapter;
import com.cherryzp.coronasearch.pojo.CoronaNews;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import static com.cherryzp.coronasearch.activity.IntroActivity.clinicList;
import static com.cherryzp.coronasearch.activity.IntroActivity.clinicTestList;
import static com.cherryzp.coronasearch.activity.IntroActivity.coronaNumHashMap;
import static com.cherryzp.coronasearch.activity.IntroActivity.coronaRegionList;
import static com.cherryzp.coronasearch.activity.IntroActivity.virusList;

public class MainActivity extends AppCompatActivity {

    private final int VIRUS = 1000;
    private final int CLINIC = 2000;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private MapView mapView;

    private ClinicRecyclerAdapter clinicRecyclerAdapter;
    private SharedPreferences sharedPreferences;

    private AdView adView;

    private RecyclerView clinicRecyclerView;
    private RelativeLayout mapViewRelativeLayout;
    private LinearLayout settingViewLinearLayout;
    private LinearLayout clinicViewLinearLayout;

    private LinearLayout infectedPathLinearLayout;
    private LinearLayout clinicLinearLayout;
    private LinearLayout settingLinearLayout;

    private LinearLayout aboutCoronaLinearLayout;
    private LinearLayout coronaPreventionLinearLayout;
    private LinearLayout coronaNewsLinearLayout;
    private LinearLayout coronaHelpLinearLayout;
    private LinearLayout alertLinearLayout;

    private TextView alertStatusTv;

    private TextView confirmedTv;
    private TextView recoveredTv;
    private TextView inspectionTv;
    private TextView deathTv;
    private TextView updateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setFirebaseService();
        setAdmob();

    }

    private void setAdmob() {

        MobileAds.initialize(this, getString(R.string.admob_app_id));


        adView = findViewById(R.id.ad_view);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }

    private void init() {
        mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setZoomLevel(10, true);

        mapView.setCalloutBalloonAdapter(new CustumCalloutBalloonAdapter(this, VIRUS));
        mapView.addPOIItems(setInfectedPathMarker());

        sharedPreferences = getSharedPreferences("alert", MODE_PRIVATE);

        //id 붙이기
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        mapViewRelativeLayout = findViewById(R.id.map_view);
        settingViewLinearLayout = findViewById(R.id.setting_view_ll);
        clinicViewLinearLayout = findViewById(R.id.clinic_view_ll);

        clinicRecyclerView = findViewById(R.id.clinic_rv);

        infectedPathLinearLayout = findViewById(R.id.infected_path_ll);
        clinicLinearLayout = findViewById(R.id.clinic_ll);
        coronaNewsLinearLayout = findViewById(R.id.corona_news_ll);
        settingLinearLayout = findViewById(R.id.setting_ll);

        alertStatusTv = findViewById(R.id.alert_status_tv);

        confirmedTv = findViewById(R.id.total_confirmed_num_tv);
        recoveredTv = findViewById(R.id.total_recovered_num_tv);
        inspectionTv = findViewById(R.id.inspection_progress_num_tv);
        deathTv = findViewById(R.id.total_death_num_tv);
        updateTv = findViewById(R.id.update_tv);

        aboutCoronaLinearLayout = findViewById(R.id.about_corona_ll);
        coronaPreventionLinearLayout = findViewById(R.id.corona_prevention_ll);
        coronaHelpLinearLayout = findViewById(R.id.corona_help_ll);
        alertLinearLayout = findViewById(R.id.alert_ll);

        if (sharedPreferences.getBoolean("alert", true)) {
            alertStatusTv.setText("ON");
        } else {
            alertStatusTv.setText("OFF");
        }

        setNavigation();
        setRecycler();
        setTextNum();

        //리스너 붙이기
        infectedPathLinearLayout.setOnClickListener(infectedPathListener);
        clinicLinearLayout.setOnClickListener(clinicListener);
        settingLinearLayout.setOnClickListener(settingListener);
        aboutCoronaLinearLayout.setOnClickListener(settingViewListener);
        coronaPreventionLinearLayout.setOnClickListener(settingViewListener);
        coronaNewsLinearLayout.setOnClickListener(settingViewListener);
        coronaHelpLinearLayout.setOnClickListener(settingViewListener);
        alertLinearLayout.setOnClickListener(alertListener);
    }

    public void setFirebaseService() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
//                    Log.w("FCM log", "getInstanceId failed", task.getException());
                    return;
                }
                String token = task.getResult().getToken();
//                Log.d("FCM log", "FCM 토큰" + token);
            }
        });

    }

    public void setNavigation() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24px);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    public void setRecycler() {
        ClinicRecyclerAdapter clinicRecyclerAdapter = new ClinicRecyclerAdapter(this, clinicList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        clinicRecyclerView.setLayoutManager(linearLayoutManager);

        clinicRecyclerView.setAdapter(clinicRecyclerAdapter);
        clinicRecyclerAdapter.notifyDataSetChanged();
    }

    public void setTextNum() {
        confirmedTv.setText(coronaNumHashMap.get("confirmed"));
        recoveredTv.setText(coronaNumHashMap.get("recovered"));
        deathTv.setText(coronaNumHashMap.get("death"));
        inspectionTv.setText(coronaNumHashMap.get("inspection"));
        updateTv.setText("업데이트 날짜 : " + coronaNumHashMap.get("update").split("\\(")[1].split("\\)")[0]);
    }

    //바텀 네비게이션 아이템 리스너
    View.OnClickListener infectedPathListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mapViewRelativeLayout.setVisibility(View.VISIBLE);
            clinicViewLinearLayout.setVisibility(View.GONE);
            settingViewLinearLayout.setVisibility(View.GONE);

//            mapView.setCalloutBalloonAdapter(new CustumCalloutBalloonAdapter(MainActivity.this, VIRUS));
//
//            mapView.removeAllPOIItems();
//            mapView.addPOIItems(setInfectedPathMarker());
        }
    };

    View.OnClickListener clinicListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mapViewRelativeLayout.setVisibility(View.GONE);
            clinicViewLinearLayout.setVisibility(View.VISIBLE);
            settingViewLinearLayout.setVisibility(View.GONE);

//            mapView.setCalloutBalloonAdapter(new CustumCalloutBalloonAdapter(MainActivity.this, CLINIC));

//            mapView.removeAllPOIItems();
//            mapView.addPOIItems(setClinicMarker());
        }
    };

    View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mapViewRelativeLayout.setVisibility(View.GONE);
            clinicViewLinearLayout.setVisibility(View.GONE);
            settingViewLinearLayout.setVisibility(View.VISIBLE);

        }
    };

    View.OnClickListener settingViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.about_corona_ll:
                    startActivity(new Intent(MainActivity.this, AboutCoronaActivity.class));

                    break;

                case R.id.corona_prevention_ll:
                    startActivity(new Intent(MainActivity.this, CoronaPreventionActivity.class));

                    break;

                case R.id.corona_news_ll:
                    startActivity(new Intent(MainActivity.this, CoronaNewsActivity.class));

                    break;

                case R.id.corona_help_ll:
                    startActivity(new Intent(MainActivity.this, CoronaHelpActivity.class));

                    break;
            }
        }
    };

    View.OnClickListener alertListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (sharedPreferences.getBoolean("alert", true)) {
                editor.putBoolean("alert", false);
                editor.commit();
                alertStatusTv.setText("OFF");
                Toast.makeText(MainActivity.this, "알림 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                editor.putBoolean("alert", true);
                editor.commit();
                alertStatusTv.setText("ON");
                Toast.makeText(MainActivity.this, "알림 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private MapPOIItem[] setClinicMarker() {

        MapPOIItem[] mapPOIItems = new MapPOIItem[clinicTestList.size()];

        for (int i = 0; i < clinicTestList.size(); i++) {
            MapPOIItem marker = new MapPOIItem();

            marker.setItemName(clinicTestList.get(i).getClinicName() + "##" + clinicTestList.get(i).getPhone() + "##" + clinicTestList.get(i).getRoadaddr());
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(clinicTestList.get(i).getLattitude(), clinicTestList.get(i).getLongitude()));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            mapPOIItems[i] = marker;
        }
        return mapPOIItems;
    }

    private MapPOIItem[] setInfectedPathMarker() {
//        MapPOIItem[] mapPOIItems = new MapPOIItem[virusList.size()];
//
//        for (int i = 0; i < virusList.size(); i++) {
//            MapPOIItem marker = new MapPOIItem();
//
//            marker.setItemName(virusList.get(i).getTitle() + "##" + virusList.get(i).getContent());
//            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(virusList.get(i).getLatitude(), virusList.get(i).getLongitude()));
//            marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
//            marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
//
//            mapPOIItems[i] = marker;
//        }

        MapPOIItem[] mapPOIItems = new MapPOIItem[coronaRegionList.size()-1];

        for (int i = 1; i < coronaRegionList.size(); i++) {
            MapPOIItem marker = new MapPOIItem();

            marker.setItemName(coronaRegionList.get(i).getRegion() +
                            "##" + "전일대비확진환자증감 : " + coronaRegionList.get(i).getIncreaseAndDecrease() +
                            "##" + "확진자수 : " + coronaRegionList.get(i).getConfirmedTotal() +
                            "##" + "격리해제수 : " + coronaRegionList.get(i).getConfirmedRecovered() +
                            "##" + "사망자수 : " + coronaRegionList.get(i).getConfirmedDeath() +
                            "##" + "발생률 : " + coronaRegionList.get(i).getRate());
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(coronaRegionList.get(i).getLatitude(), coronaRegionList.get(i).getLongitude()));
            marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            mapPOIItems[i-1] = marker;
        }

        return mapPOIItems;

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setMessage("앱을 종료하시겠습니까?").setNegativeButton("아니오", null).setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();

    }
}
