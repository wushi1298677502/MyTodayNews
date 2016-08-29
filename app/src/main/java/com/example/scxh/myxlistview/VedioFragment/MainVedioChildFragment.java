package com.example.scxh.myxlistview.VedioFragment;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil;
import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.MyActivity.PicActivity;
import com.example.scxh.myxlistview.MyActivity.VedioActivity;
import com.example.scxh.myxlistview.R;
import com.example.scxh.myxlistview.VedioGsonJava.VedioContent;
import com.example.scxh.myxlistview.VedioGsonJava.VedioContentNews;
import com.example.xlistviewlibrary.XListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainVedioChildFragment extends Fragment implements XListView.IXListViewListener,AdapterView.OnItemClickListener{

    public String url;
    private int mPageNo;
    XListView mXListView;
    int pageNo = 0; //页号 ，表示第几页,第一页从0开始
    int pageSize = 10; //页大小，显示每页多少条数据
    String news_type_id = "T1348647909107";  //新闻类型标识, 此处表示头条新闻
    String video_type_id = "V9LG4B3A0";//hot
    private int mCurrentPageNo = 0; //当前页号
    private int mTotalPageCount = 5; //总页数
    private static final String MAINVEDIO_CACHE_NAME = "com.example.scxh.myxlistview.VedioFragment.MainVedioChildFragment";
    private  String baseUrl = "http://c.3g.163.com/nc/video/list/"+ video_type_id + "/n/" +pageNo*pageSize+ "-" +pageSize+ ".html";
    private ConnectionUtil connectionUtil;
    int length;
    private MyPagerAdapter mPagerAdapter;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");//hh 小写是十二进制，HH 大写是24进制
    List<VedioContentNews> list = new ArrayList<>();
    Context mContext;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private ImageView mImageView;
    private int mVideoWidth;
    private int mVideoHeight;
    private String httpUrl;
    public MainVedioChildFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String str,int num) {
        MainVedioChildFragment mainVedioChildFragment = new MainVedioChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MESSAGE",str);
        bundle.putInt("NUM",num);
        mainVedioChildFragment.setArguments(bundle);
        return mainVedioChildFragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Logs.e("onAttach>>>");
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments()==null?null:getArguments().getString("MESSAGE");
        mPageNo = getArguments()==null?null:getArguments().getInt("NUM");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_vedio_child_layout, container, false);
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mXListView = (XListView) getView().findViewById(R.id.mainvediodchildfragment_xlistview);

        connectionUtil = new ConnectionUtil(getContext());
        mPagerAdapter = new MyPagerAdapter(getContext());
        getDataLists(pageNo);
        mXListView.setAdapter(mPagerAdapter);

        mXListView.setXListViewListener(this);
        mXListView.setPullLoadEnable(true); //上拉加载更多开关
        mXListView.setPullRefreshEnable(true);   //下拉刷新开关
        mXListView.setOnItemClickListener(this);


    }



    public void getDataLists(int pageNo) {
        final String baseUrl = "http://c.3g.163.com/nc/video/list/"+ url + "/n/" +pageNo*pageSize+ "-" +pageSize+ ".html";
        connectionUtil.asyncConnect(baseUrl, ConnectionUtil.Mothod.GET, new ConnectionUtil.HttpConnectionInterface() {

            public void excute(String cont) {
                if (cont == null) {
                    Toast.makeText(getContext(), "请求出错!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mContext.getSharedPreferences(MAINVEDIO_CACHE_NAME, Context.MODE_PRIVATE).edit().putString(baseUrl, cont).commit();
                }
                setListview(cont);

            }

        });

    }

    public void setListview(String content) {
        Logs.e("content :" + content);
        mXListView.stopLoadMore();
        mXListView.stopRefresh();
        mXListView.setRefreshTime(simpleDateFormat.format(new Date(System.currentTimeMillis())));

        Gson gson = new Gson();
        VedioContent conten = gson.fromJson(content, VedioContent.class);
        switch (mPageNo){
            case 0:
                setHotGson(conten);
                break;
            case 1:
                setEntertainment(conten);
                break;
            case 2:
                setFunny(conten);
                break;
            case 3:
                setSelected(content);
                break;
        }

    }
    public void setHotGson( VedioContent conten){
        ArrayList<VedioContentNews> vedioContentNews = conten.getV9LG4B3A0();
        mPagerAdapter.addDataList(vedioContentNews);
    }
    public void setEntertainment( VedioContent conten){
        ArrayList<VedioContentNews> vedioContentNews = conten.getV9LG4CHOR();
        mPagerAdapter.addDataList(vedioContentNews);
    }
    public void setFunny( VedioContent conten){
        ArrayList<VedioContentNews> vedioContentNews = conten.getV9LG4E6VR();
        mPagerAdapter.addDataList(vedioContentNews);
    }
    public void setSelected(String content) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(content);
            JSONArray jsonObj = jsonObject.getJSONArray("00850FRB");
            length = jsonObj.length();
            Logs.e("length>>>>" + length);
            for (int g = 0; g < length; g++) {
                VedioContentNews vedioContentNews = new VedioContentNews();
                JSONObject jsonObjItemads = jsonObj.getJSONObject(g);
                vedioContentNews.setTitle(jsonObjItemads.getString("title"));
                vedioContentNews.setCover(jsonObjItemads.getString("cover"));
                vedioContentNews.setMp4_url(jsonObjItemads.getString("mp4_url"));
                list.add(vedioContentNews);
            }
            }catch(JSONException e){
                e.printStackTrace();
            }
        mPagerAdapter.addDataList(list);
        }
    /**
     * 这个方法计算现在View的可见度百分比。这个方法仅在View所在屏幕大小小于本来的大小时正常工作。
     * currentView - 要被计算可见度的View * @return currentView的可见度百分比
//     */

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Logs.e("onItemClick>>>>");
        VedioContentNews vedioContentNews = (VedioContentNews) adapterView.getAdapter().getItem(i);
        httpUrl = vedioContentNews.getMp4_url();
        Logs.e("onItemClick>>>>"+httpUrl);
        SurfaceView surfaceView=(SurfaceView) view.findViewById(R.id.picitem_surfaceview);
        mImageView=(ImageView) view.findViewById(R.id.picitem_img);
        mImageView.setVisibility(View.INVISIBLE);

        Logs.e("mSurfaceView"+surfaceView);
        mSurfaceHolder = surfaceView.getHolder();
        Logs.e("surface Created"+mSurfaceHolder);
        initVideo();
//        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
//
//            public void surfaceCreated(SurfaceHolder holder) {
//
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                Logs.e("Surface Changed");
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                Logs.e("Surface Destroyed");
//                if (mMediaPlayer != null) {
//                    mMediaPlayer.release();
//                    mMediaPlayer = null;
//                }
//            }
//        });


    }
    public void initVideo(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(httpUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMediaPlayer.setDisplay(mSurfaceHolder);
        try {
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Logs.e("onPrepared called");
                mVideoWidth = mMediaPlayer.getVideoWidth();
                mVideoHeight = mMediaPlayer.getVideoHeight();
                if (mVideoWidth != 0 && mVideoHeight != 0) {
            /* 设置视频的宽度和高度 */
                    mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
			/* 开始播放 */
                    mMediaPlayer.start();

                }
            }
        });//预处理结束监听

        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        }); //播放进度监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });//播放完成监听

        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
            }
        }); //播放进度监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mImageView.setVisibility(View.VISIBLE);
            }
        });//播放完成监听


    }
    @Override
    public void onRefresh() {
        pageNo = 0;
        getDataLists(pageNo);
        mXListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        ++pageNo;
        if (pageNo > mTotalPageCount) {
            pageNo = mTotalPageCount;
            mXListView.stopLoadMore();
            Toast.makeText(getContext(), "已加载到最后一页", Toast.LENGTH_SHORT).show();
            return;
        }
        Logs.e("pageNo>>>"+pageNo);
        getDataLists(pageNo);
    }


    class MyPagerAdapter extends BaseAdapter {
        List<VedioContentNews> list = new ArrayList<>();
        LayoutInflater layoutInflater;
        SurfaceView surfaceView;
        public MyPagerAdapter(Context context) {
            this.layoutInflater = LayoutInflater.from(context);

        }

        public void addDataList(List<VedioContentNews> list) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override


        public View getView(int i, View view, ViewGroup viewGroup) {
            HoldView holdView;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.activity_picitem_layout, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.picitem_img);
                TextView title = (TextView) view.findViewById(R.id.picitem_title);
                surfaceView = (SurfaceView) view.findViewById(R.id.picitem_surfaceview);
                holdView = new HoldView();
                holdView.pic = imageView;
                holdView.title = title;
                view.setTag(holdView);
            }
            holdView = (HoldView) view.getTag();
            VedioContentNews vedioContentNews = (VedioContentNews) getItem(i);
            Glide.with(getContext()).load(vedioContentNews.getCover()).into(holdView.pic);
            holdView.title.setText(vedioContentNews.getTitle());//对于直接从网络取数据的图片使用第三方包，还可以缓存
            surfaceView = (SurfaceView) view.findViewById(R.id.picitem_surfaceview);
            return view;
        }
        public class HoldView {
            ImageView pic;
            TextView title;
        }
    }

}




