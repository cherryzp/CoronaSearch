package com.cherryzp.coronasearch.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.cherryzp.coronasearch.R;
import com.cherryzp.coronasearch.adapter.CoronaNewsRecyclerAdapter;
import com.cherryzp.coronasearch.network.NetRetrofit;
import com.cherryzp.coronasearch.pojo.CoronaNews;
import com.cherryzp.coronasearch.service.CoronaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoronaNewsActivity extends AppCompatActivity {

    private RecyclerView coronaNewsRecyclerView;
    private CoronaService coronaService;

    private int display = 20;
    private int start = 1;
    private String sort = "sim";

    private final String NAVER_ID = "HWk7uuTKQHjl2MtANGmT";
    private final String NAVER_SECRET = "KghonegoPI";

    CoronaNewsRecyclerAdapter coronaNewsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_news);

        init();
        setRetrofit();
    }

    public void init() {
        coronaNewsRecyclerView = findViewById(R.id.corona_news_rv);

        coronaNewsRecyclerAdapter = new CoronaNewsRecyclerAdapter(this);
        coronaNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        coronaNewsRecyclerView.setAdapter(coronaNewsRecyclerAdapter);
        coronaNewsRecyclerAdapter.notifyDataSetChanged();


        coronaNewsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    start += 20;
                    setRetrofit();

                }
            }
        });

    }

    public void setRetrofit() {

        Call<CoronaNews> coronaNewsCall = NetRetrofit.getInstance().getCoronaService().doGetCoronaNews( display, start, sort, NAVER_ID, NAVER_SECRET);

        Callback<CoronaNews> coronaNewsCallback = new Callback<CoronaNews>() {
            @Override
            public void onResponse(Call<CoronaNews> call, Response<CoronaNews> response) {
                CoronaNews coronaNews = response.body();

                coronaNewsRecyclerAdapter.setCoronaNewsList(coronaNews);
                coronaNewsRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<CoronaNews> call, Throwable t) {

            }
        };

        coronaNewsCall.enqueue(coronaNewsCallback);
    }
}
