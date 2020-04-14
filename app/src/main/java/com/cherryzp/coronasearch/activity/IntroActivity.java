package com.cherryzp.coronasearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.cherryzp.coronasearch.R;
import com.cherryzp.coronasearch.model.Clinic;
import com.cherryzp.coronasearch.model.ClinicTest;
import com.cherryzp.coronasearch.model.CoronaRegion;
import com.cherryzp.coronasearch.model.Virus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class IntroActivity extends AppCompatActivity {

    private final String CLINIC_LOCATION = "json/Clinic.json";
    private final String VIRUS_LOCATION = "json/Virus.json";

    private SharedPreferences sharedPreferences;

    public static ArrayList<ClinicTest> clinicTestList = new ArrayList<>();
    public static ArrayList<CoronaRegion> coronaRegionList = new ArrayList<>();
    public static ArrayList<Clinic> clinicList = new ArrayList<>();
    public static ArrayList<Virus> virusList = new ArrayList<>();
    public static HashMap<String, String> coronaNumHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        loadShared();

        clinicJsonParsing(getJsonString(CLINIC_LOCATION));
//        virusJsonParsing(getJsonString(VIRUS_LOCATION));

        CoronaDataCrawling coronaDataCrawling = new CoronaDataCrawling();
        coronaDataCrawling.execute();

//        getHashKey();

    }

    private void loadShared() {
        sharedPreferences = getSharedPreferences("alert", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("alert", true)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("alert", true);
            editor.commit();
        }
    }

    //KeyHash 탐색
    private void getHashKey () {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.cherryzp.coronasearch", PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
//                Log.e("KeyHash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //메인 액티비티로 넘기기
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            finish();

        }
    };

    private String getJsonString(String location) {
        String json = "";

        try {
            InputStream is = getAssets().open(location);
            int fileSize = is.available();
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    private void clinicJsonParsing(String json) {

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ClinicTest clinicTest = new ClinicTest();

                clinicTest.setClinicName(jsonObject.getString("clinicName"));
                clinicTest.setLattitude(jsonObject.getDouble("lattitude"));
                clinicTest.setLongitude(jsonObject.getDouble("longtitude"));
                clinicTest.setPhone(jsonObject.getString("phone"));
                clinicTest.setRoadaddr(jsonObject.getString("roadaddr"));

                clinicTestList.add(clinicTest);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void virusJsonParsing(String json) {

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Virus virus = new Virus();
                virus.setTitle(jsonObject.getString("title"));
                virus.setContent(jsonObject.getString("content"));
                virus.setLatitude(splitLatlng(jsonObject.getString("latlng"))[0]);
                virus.setLongitude(splitLatlng(jsonObject.getString("latlng"))[1]);

                virusList.add(virus);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private double[] splitLatlng(String latlng) {
        String[] latlngString = latlng.split(",");
        double[] latlngs = new double[2];
        latlngs[0] = Double.valueOf(latlngString[0]);
        latlngs[1] = Double.valueOf(latlngString[1]);

        return latlngs;
    }

    public class CoronaDataCrawling extends AsyncTask<Void, Void, Void> {

        private String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

        private String coronaNumUrl = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=11&ncvContSeq=&contSeq=&board_id=&gubun=";
        private String coronaRegionUrl = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13&ncvContSeq=&contSeq=&board_id=&gubun=";
        private String coronaClinicUrl = "http://www.mohw.go.kr/react/popup_200128_3.html";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            loadCoronaNum();
            loadClinic();

            loadCoronaRegion();
//            makeJson();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Handler handler = new Handler();

            handler.postDelayed(runnable, 2000);
        }

        public void loadCoronaNum() {
            try {

//                Jsoup.connect(coronaNumUrl).header("Content-Type", "application/json;charset=UTF-8").userAgent(USER_AGENT).ignoreContentType(true).get();

                //코로나 감염자수
                Document coronaNumDoc = Jsoup.connect(coronaNumUrl).get();
                Elements coronaNumElements = coronaNumDoc.select("div.data_table.mgt16").get(0).select("td");
                System.out.println(coronaNumElements.size());
                ArrayList<String> coronaNumList = new ArrayList<>();

                for (Element element : coronaNumElements) {
                    coronaNumList.add(element.text());
                }

                coronaNumHashMap.put("confirmed", coronaNumList.get(0));
                coronaNumHashMap.put("recovered", coronaNumList.get(1));
                coronaNumHashMap.put("inspection", coronaNumList.get(2));
                coronaNumHashMap.put("death", coronaNumList.get(3));
                coronaNumHashMap.put("update", coronaNumDoc.select("p.s_descript").get(0).text());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void loadCoronaRegion() {
            try {

                int regionNum = 0;

                Document coronaRegionDocument = Jsoup.connect(coronaRegionUrl).get();
                Elements coronaRegionElements = coronaRegionDocument.select("div.data_table.mgt24").get(0).select("td.number");

                CoronaRegion coronaRegion = new CoronaRegion();
                for (int i=0; i<coronaRegionElements.size(); i++) {

                    switch (regionNum){
                        case 0:
                            coronaRegion.setIncreaseAndDecrease(coronaRegionElements.get(i).text());
                            break;

                        case 1:
                            coronaRegion.setConfirmedTotal(coronaRegionElements.get(i).text());
                            break;

                        case 2:
                            coronaRegion.setConfirmedRecovered(coronaRegionElements.get(i).text());
                            break;

                        case 3:
                            coronaRegion.setConfirmedDeath(coronaRegionElements.get(i).text());
                            break;

                        case 4:
                            coronaRegion.setRate(coronaRegionElements.get(i).text());
                            break;
                    }

                    regionNum++;
                    if (regionNum == 5) {
                        regionNum = 0;
                        coronaRegionList.add(coronaRegion);
                        coronaRegion = new CoronaRegion();
                    }

                }

                Elements regionElements = coronaRegionDocument.select("th");

                int arrNum = 0;
                for (int i =7; i<regionElements.size(); i++) {
                    coronaRegionList.get(arrNum).setRegion(regionElements.get(i).text());

                    arrNum++;
                }

                setLatLng();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setLatLng() {
            String seoul = "37.5664261,126.9781831";
            String busan = "35.1797957,129.0727983";
            String daegu = "35.8819733,128.5923414";
            String inchun = "37.4568565,126.7029735";
            String gwangju = "35.1561256,126.8425089";
            String daejeon = "36.350461,127.38263";
            String ulsan = "35.5396267,129.3093389";
            String saejong = "36.4801027,127.2868467";
            String gyunggi = "37.2750552,127.0072561";
            String gangwon = "37.8854127,127.7297365";
            String chungbuk = "36.6358136,127.4891451";
            String chungnam = "36.6588327,126.6706662";
            String jeonbuk = "35.8203643,127.1065383";
            String jeonnam = "34.816223,126.4607355";
            String gyungbuk = "36.576025,128.5034069";
            String gyungnam = "35.2382949,128.6902093";
            String jeju = "33.3836818,126.413127";

            for (int i = 1; i < coronaRegionList.size(); i++) {
                switch (i) {
                    case 1:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(seoul.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(seoul.split(",")[1]));
                        break;

                    case 2:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(busan.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(busan.split(",")[1]));
                        break;

                    case 3:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(daegu.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(daegu.split(",")[1]));
                        break;

                    case 4:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(inchun.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(inchun.split(",")[1]));
                        break;

                    case 5:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(gwangju.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(gwangju.split(",")[1]));
                        break;

                    case 6:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(daejeon.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(daejeon.split(",")[1]));
                        break;

                    case 7:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(ulsan.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(ulsan.split(",")[1]));
                        break;

                    case 8:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(saejong.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(saejong.split(",")[1]));
                        break;

                    case 9:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(gyunggi.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(gyunggi.split(",")[1]));
                        break;

                    case 10:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(gangwon.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(gangwon.split(",")[1]));
                        break;

                    case 11:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(chungbuk.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(chungbuk.split(",")[1]));
                        break;

                    case 12:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(chungnam.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(chungnam.split(",")[1]));
                        break;

                    case 13:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(jeonbuk.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(jeonbuk.split(",")[1]));
                        break;

                    case 14:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(jeonnam.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(jeonnam.split(",")[1]));
                        break;

                    case 15:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(gyungbuk.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(gyungbuk.split(",")[1]));
                        break;

                    case 16:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(gyungnam.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(gyungnam.split(",")[1]));
                        break;

                    case 17:
                        coronaRegionList.get(i).setLatitude(Double.parseDouble(jeju.split(",")[0]));
                        coronaRegionList.get(i).setLongitude(Double.parseDouble(jeju.split(",")[1]));
                        break;
                }

                System.out.println(coronaRegionList.get(i).toString());
            }

        }

//        public void makeJson() {
//            JSONArray jsonArray = new JSONArray();
//
//            try {
//                for (int i = 1; i < coronaRegionList.size() - 1; i++) {
//                    JSONObject jsonObject = new JSONObject();
//
//                    jsonObject.put("region", coronaRegionList.get(i).getRegion());
//                    jsonObject.put("increaseAndDecrease", coronaRegionList.get(i).getIncreaseAndDecrease());
//                    jsonObject.put("confirmedTotal", coronaRegionList.get(i).getConfirmedTotal());
//                    jsonObject.put("confirmedIsolation", coronaRegionList.get(i).getConfirmedIsolation());
//                    jsonObject.put("confirmedRecovered", coronaRegionList.get(i).getConfirmedRecovered());
//                    jsonObject.put("confirmedDeath", coronaRegionList.get(i).getConfirmedDeath());
//                    jsonObject.put("inspectionTotal", coronaRegionList.get(i).getInspectionTotal());
//                    jsonObject.put("underInspection", coronaRegionList.get(i).getUnderInspection());
//                    jsonObject.put("inspectionNegative", coronaRegionList.get(i).getInspectionNegative());
//                    jsonObject.put("total", coronaRegionList.get(i).getTotal());
//                    jsonObject.put("latitude", coronaRegionList.get(i).getLatitude());
//                    jsonObject.put("longitude", coronaRegionList.get(i).getLongitude());
//
//                    jsonArray.put(jsonObject);
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            System.out.println(jsonArray.toString());
//        }

        public void loadClinic() {
            try {
                Document clinicDocument = Jsoup.connect(coronaClinicUrl).get();

                Elements clinicElements = clinicDocument.select("table.tb_base").get(0).select("td");

                int clinicNum = 0;
                Clinic clinic = new Clinic();
                for (int i = 0; i < clinicElements.size(); i++) {

                    switch (clinicNum) {
                        case 0:
                            clinic.setCity(clinicElements.get(i).text());
                            break;

                        case 1:
                            clinic.setAddress(clinicElements.get(i).text());
                            break;

                        case 2:
                            clinic.setClinic(clinicElements.get(i).text());
                            break;

                        case 3:
                            clinic.setTel(clinicElements.get(i).text());
                            break;
                    }
                    clinicNum++;
                    if (clinicNum == 4) {
                        clinicList.add(clinic);
                        System.out.println(clinic.toString());

                        clinic = new Clinic();
                        clinicNum = 0;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
