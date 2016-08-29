package com.example.scxh.myxlistview.MyTextFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil;
import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.MyActivity.ContentActivity;
import com.example.scxh.myxlistview.R;
import com.scxh.slider.library.SliderLayout;
import com.scxh.slider.library.SliderTypes.BaseSliderView;
import com.scxh.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentDetailFragment extends Fragment {

    private ConnectionUtil connectionUtil;
    String  docid =   "A90HHI6I00014SEH" ; //新闻ID ,从新闻列表项目获取
    TextView mtitle,mcontent,mresource;
    SliderLayout sliderLayout;
    public ProgressBar mProgressBar;
    public ContentDetailFragment() {
    }
    String msg = null;

    public static Fragment newInstance(String msg){
        ContentDetailFragment contentDetailFragment = new ContentDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MESSAGE",msg);
        contentDetailFragment.setArguments(bundle);
        return contentDetailFragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logs.e("BundleFragment>>>onCreate");

        Bundle bundle = getArguments();
        docid = bundle.getString("MESSAGE");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content_detail_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        connectionUtil = new ConnectionUtil(getContext());//记住实例化，否这会 java.lang.NullPointerException: Attempt to invoke virtual method 'void com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil.asyncConnect(java.lang.String, com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil$Mothod, com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil$HttpConnectionInterface)' on a null object reference
        mtitle = (TextView)getView().findViewById(R.id.contentfragment_title);
        mcontent = (TextView)getView(). findViewById(R.id.contentfragment_body);
        mresource = (TextView)getView().findViewById(R.id.contentfragment_source);
        mProgressBar = (ProgressBar)getView(). findViewById(R.id.content_progressbar);
        sliderLayout = (SliderLayout)getView().findViewById(R.id.contentfragment_sliderlayout);
        sliderLayout.animate();
        mProgressBar.setVisibility(View.VISIBLE);
        getDataLists(docid);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);


    }
    public void getDataLists(String  docid) {
        String baseUrl = "http://c.m.163.com/nc/article/"+docid +"/full.html";
        Logs.e("docid>>>"+docid);
        connectionUtil.asyncConnect(baseUrl, ConnectionUtil.Mothod.GET, new ConnectionUtil.HttpConnectionInterface() {

            public void excute(String cont) {
                if (cont == null) {
                    Toast.makeText(getContext(), "请求出错!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressBar.setVisibility(View.GONE);
                setViewpage(cont);

            }

        });

    }
    public void setViewpage(String content){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(content);
            JSONObject jsonObjectone = jsonObject.getJSONObject(docid);
            String body = jsonObjectone.getString("body");
            Logs.e("body>>>>"+body);
            String title = jsonObjectone.getString("title");
            String source = jsonObjectone.getString("source");
            mtitle.setText(Html.fromHtml(title));
            mcontent.setText(Html.fromHtml(body));
            mresource.setText(Html.fromHtml(source));
            JSONArray jsonArray = jsonObjectone.getJSONArray("img");
            int len = jsonArray.length();
            Logs.e("length>>>"+len);
            if(len == 0){
                sliderLayout.setVisibility(View.GONE);//如果没有图就隐藏掉sliderlayout
            }
            for(int g = 0;g<len;g++) {
                JSONObject jsonObjItem = jsonArray.getJSONObject(g);
                String imgsrc = jsonObjItem.getString("src");
                String tex = jsonObjItem.getString("alt");
                Logs.e("imgsrc>>>>" + imgsrc);

                TextSliderView textSliderView = new TextSliderView(getContext());
                textSliderView
                        .description(tex)
                        .image(imgsrc)
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                sliderLayout.addSlider(textSliderView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}