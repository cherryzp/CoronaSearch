package com.cherryzp.coronasearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cherryzp.coronasearch.R;
import com.cherryzp.coronasearch.model.Clinic;

import java.util.ArrayList;

public class ClinicRecyclerAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<Clinic> clinics;

    public ClinicRecyclerAdapter(Context context, ArrayList<Clinic> clinics) {
        this.context = context;
        this.clinics = clinics;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinic, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        vh.cityTv.setText(clinics.get(position).getCity());
        vh.addressTv.setText(clinics.get(position).getAddress());
        vh.clinicTv.setText(clinics.get(position).getClinic());
        vh.telTv.setText(clinics.get(position).getTel());
    }

    @Override
    public int getItemCount() {
        return clinics.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        private TextView cityTv;
        private TextView addressTv;
        private TextView clinicTv;
        private TextView telTv;

        public VH(@NonNull View itemView) {
            super(itemView);

            cityTv = itemView.findViewById(R.id.city_tv);
            addressTv = itemView.findViewById(R.id.address_tv);
            clinicTv = itemView.findViewById(R.id.clinic_tv);
            telTv = itemView.findViewById(R.id.tel_tv);
        }
    }
}
