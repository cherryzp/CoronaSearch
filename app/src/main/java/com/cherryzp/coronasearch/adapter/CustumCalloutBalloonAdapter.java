package com.cherryzp.coronasearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cherryzp.coronasearch.R;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

public class CustumCalloutBalloonAdapter implements CalloutBalloonAdapter {

    private final int VIRUS = 1000;
    private final int CLINIC = 2000;

    private int status;

    private final View mCalloutBalloon;
    private LayoutInflater layoutInflater;

    public CustumCalloutBalloonAdapter (Context context, int status) {
        this.status = status;

        layoutInflater = LayoutInflater.from(context);
        if (status == VIRUS) {
            mCalloutBalloon = layoutInflater.inflate(R.layout.virus_callout_balloon_layout, null);
        } else {
            mCalloutBalloon = layoutInflater.inflate(R.layout.clinic_callout_balloon_layout, null);
        }

    }


//    marker.setItemName(coronaRegionList.get(i).getRegion() + "##" + "확진자수 : " + coronaRegionList.get(i).getConfirmedTotal() + " 명" +
//            "##" + "격리해제수 : " + coronaRegionList.get(i).getConfirmedRecovered() +
//            "##" + "사망자수 : " + coronaRegionList.get(i).getConfirmedDeath() +
//            "##" + "발생률 : " + coronaRegionList.get(i).getRate());

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        if (status == VIRUS) {
            ((TextView)mCalloutBalloon.findViewById(R.id.title_tv)).setText(mapPOIItem.getItemName().split("##")[0]);
            ((TextView)mCalloutBalloon.findViewById(R.id.increase_tv)).setText(mapPOIItem.getItemName().split("##")[1]);
            ((TextView)mCalloutBalloon.findViewById(R.id.total_tv)).setText(mapPOIItem.getItemName().split("##")[2]);
            ((TextView)mCalloutBalloon.findViewById(R.id.recovered_tv)).setText(mapPOIItem.getItemName().split("##")[3]);
            ((TextView)mCalloutBalloon.findViewById(R.id.death_tv)).setText(mapPOIItem.getItemName().split("##")[4]);
            ((TextView)mCalloutBalloon.findViewById(R.id.rate_tv)).setText(mapPOIItem.getItemName().split("##")[5]);
        } else {
            ((TextView)mCalloutBalloon.findViewById(R.id.name_tv)).setText(mapPOIItem.getItemName().split("##")[0]);
            ((TextView)mCalloutBalloon.findViewById(R.id.phone_tv)).setText(mapPOIItem.getItemName().split("##")[1]);
            ((TextView)mCalloutBalloon.findViewById(R.id.address_tv)).setText(mapPOIItem.getItemName().split("##")[2]);
        }


        return mCalloutBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }
}
