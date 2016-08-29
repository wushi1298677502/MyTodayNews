package com.example.scxh.myxlistview.MyActivity;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.MyPicFragment.MainTableFragment;
import com.example.scxh.myxlistview.MenuFragment;
import com.example.scxh.myxlistview.R;
import com.example.scxh.myxlistview.MyTextFragment.TabFragment;
import com.example.scxh.myxlistview.VedioFragment.MainVedioFragment;
import com.warmtel.slidingmenu.lib.SlidingMenu;
import com.warmtel.slidingmenu.lib.app.SlidingActivity;

public class SecondActivity extends SlidingActivity implements MenuFragment.FragmentToActivity {
    SlidingMenu slidingMenu;
    int number = 1;//用于判断是否是第一页
    boolean back = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_layout);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.secondFramelayout, TabFragment.newInstance())
                .commit();
        setBehindContentView(R.layout.activity_sliding_menu_layout);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.sliding_menu_layout,MenuFragment.newInstance())
                .commit();

        slidingMenu = getSlidingMenu();
        slidingMenu.setSlidingEnabled(true);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_off_width);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setMode(SlidingMenu.LEFT);
    }

    @Override
    public void excute(View view) {
        switch (view.getId()){
            case R.id.menufragment_newsbtn:
                ReplaceToNews();
                break;
            case R.id.menufragment_picturebtn:
                ReplaceToPicture();
                break;
            case R.id.menufragment_vediobtn:
                ReplaceToVedio();
                break;
            case R.id.menufragment_weatherbtn:
                ReplaceToWether();
                break;
            case R.id.menufragment_mapbtn:
                ReplaceToMap();
                break;
        }
        slidingMenu.toggle();// 点击自动收回
    }
    public void ReplaceToNews(){
        ReplaceFragmentAndAddToStack(TabFragment.newInstance());


    }
    public void ReplaceToPicture(){
        ReplaceFragmentAndAddToStack( MainTableFragment.newInstance());


    }
    public void ReplaceToVedio(){
        ReplaceFragmentAndAddToStack(MainVedioFragment.newInstance());


    }
    public void ReplaceToWether(){
        Toast.makeText(this,"等待完善",Toast.LENGTH_SHORT).show();

    }
    public void ReplaceToMap(){
        Toast.makeText(this,"等待完善",Toast.LENGTH_SHORT).show();

    }
    public void ReplaceFragmentAndAddToStack(Fragment fragment){
        Logs.e("number>>>"+number);
        ++number;
        Logs.e("number>>>"+number);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.secondFramelayout, fragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onBackPressed() {
        --number;
        Logs.e("number>>"+number);
        if(number <= 0){
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            if(back==true){
                finish();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    back=true;
                    SystemClock.sleep(2000);
                    back=false;
                }
            }).start();
        }else {
            getSupportFragmentManager().popBackStack();//弹栈
        }
    }
}
