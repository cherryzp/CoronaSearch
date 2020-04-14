package com.cherryzp.coronasearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cherryzp.coronasearch.R;
import com.cherryzp.coronasearch.pojo.CoronaNews;
import com.cherryzp.coronasearch.pojo.Item;

import java.util.ArrayList;

public class CoronaNewsRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    public ArrayList<Item> items = new ArrayList<>();

    public CoronaNewsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setCoronaNewsList(CoronaNews coronaNews) {
        for (int i = 0; i < coronaNews.getItems().size(); i++) {
            items.add(coronaNews.getItems().get(i));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_corona_news, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;

        vh.titleTv.setText(items.get(position).getTitle().replace("<b>", "").replace("</b>", "").replace("&quot;", " "));
        vh.contentTv.setText(items.get(position).getDescription().replace("<b>", "").replace("</b>", "").replace("&quot;", " "));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class VH extends RecyclerView.ViewHolder{

        LinearLayout coronaNewsLinearLayout;
        TextView titleTv;
        TextView contentTv;

        public VH(@NonNull final View itemView) {
            super(itemView);

            coronaNewsLinearLayout = itemView.findViewById(R.id.corona_news_ll);
            titleTv = itemView.findViewById(R.id.title_tv);
            contentTv = itemView.findViewById(R.id.content_tv);

            coronaNewsLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(getLayoutPosition()).getLink())));
                }
            });

        }
    }
}
