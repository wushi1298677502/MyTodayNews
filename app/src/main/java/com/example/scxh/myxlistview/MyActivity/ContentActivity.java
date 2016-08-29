package com.example.scxh.myxlistview.MyActivity;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil;
import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.MyTextFragment.ContentDetailFragment;
import com.example.scxh.myxlistview.MyTextFragment.ContentDetailNoPicFragment;
import com.example.scxh.myxlistview.R;
import com.scxh.slider.library.SliderLayout;
import com.scxh.slider.library.SliderTypes.BaseSliderView;
import com.scxh.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContentActivity extends AppCompatActivity {
    String  docid =   "A90HHI6I00014SEH" ; //新闻ID ,从新闻列表项目获取
    public Toolbar mToolbar;
    private ConnectionUtil connectionUtil;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content_layout);
        Bundle bundle = getIntent().getBundleExtra("BUNDLE");
        docid= bundle.getString("DOCID");
        Logs.e("ContentActivity>>>>"+docid);

        connectionUtil = new ConnectionUtil(this);
        mToolbar = (Toolbar) findViewById(R.id.content_ToolBar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.contentActivity_framlayout, ContentDetailFragment.newInstance(docid)).commit();
    }
}