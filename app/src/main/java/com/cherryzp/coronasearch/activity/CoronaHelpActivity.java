package com.cherryzp.coronasearch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cherryzp.coronasearch.R;
import com.cherryzp.coronasearch.adapter.CoronaHelpRecyclerAdapter;

public class CoronaHelpActivity extends AppCompatActivity {

    TextView copyEmailTextView;
    TextView sendEmailTextView;
    CoronaHelpRecyclerAdapter coronaHelpRecyclerAdapter;
    RecyclerView coronaHelpRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_help);

        init();
        setRecycler();
    }

    //ca-app-pub-2488110704514213~3294163740

    public void init() {
        copyEmailTextView = findViewById(R.id.copy_email_tv);
        sendEmailTextView = findViewById(R.id.send_email_tv);
        coronaHelpRecyclerView = findViewById(R.id.corona_help_rv);
        
        copyEmailTextView.setOnClickListener(emailListener);
        sendEmailTextView.setOnClickListener(emailListener);
    }

    View.OnClickListener emailListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.copy_email_tv:

                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("email", "coronasearchhelp@gmail.com");
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(CoronaHelpActivity.this, "주소가 복사되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                    
                    
                case R.id.send_email_tv:
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setType("plain/text");
                    String[] address = {"coronasearchhelp@gmail.com"};
                    email.putExtra(Intent.EXTRA_EMAIL, address);
                    email.putExtra(Intent.EXTRA_SUBJECT, "코로나 감염자 이동 지역 제보합니다.");
                    email.putExtra(Intent.EXTRA_TEXT, "1. 제보자 성명 / 별명 : \n\n2. 나를 소개할 수 있는 제보자 한마디(선택사항) : \n\n3. 감염자 이동 지역(ex: 인천공항) : \n\n4. 감염자 이동 지역 방문 날짜 : \n\n5.감염자 이동지역 좌표(선택사항):");
                    startActivity(email);

                    break;
            }
        }
    };

    private void setRecycler() {
        coronaHelpRecyclerAdapter = new CoronaHelpRecyclerAdapter(this);

        coronaHelpRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        coronaHelpRecyclerView.setAdapter(coronaHelpRecyclerAdapter);

        coronaHelpRecyclerAdapter.notifyDataSetChanged();
    }

}
