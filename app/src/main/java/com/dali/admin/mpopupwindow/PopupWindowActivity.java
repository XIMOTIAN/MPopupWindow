package com.dali.admin.mpopupwindow;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PopupWindowActivity extends AppCompatActivity implements CustomPopupWindow.ViewClickListener {

    private CustomPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);
    }

    //向下弹出
    public void showDownPop(View view) {
        popupWindow = CustomPopupWindow.newBuilder()
                .setView(R.layout.popup_down)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .build(this)
                .showDownPop(view);
//        popupWindow.showAsDropDown(view);
    }

    //向右弹出
    public void showRightPop(View view) {
        popupWindow = new CustomPopupWindow.Builder()
                .setView(R.layout.popup_right_or_left)
                .setAnimationStyle(R.style.AnimRightOrLeft)
                .setViewOnclickListener(this)
                .build(this);
//                .showRightPop(view);
        popupWindow.showAsDropDown(view, view.getWidth(), -view.getHeight());
    }

    //向左弹出
    public void showLeftPop(View view) {
        popupWindow = new CustomPopupWindow.Builder()
                .setView(R.layout.popup_right_or_left)
                .setAnimationStyle(R.style.AnimRightOrLeft)
                .setViewOnclickListener(this)
                .build(this)
                .showLeftPop(view);
    }


    //全屏弹出
    public void showAll(View view) {
        popupWindow = new CustomPopupWindow.Builder()
                .setView(R.layout.popup_up)
                .setAnimationStyle(R.style.AnimUp)
                .setBackgroundDrawable(new BitmapDrawable())
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener(this)
                .build(this)
                .showAllPop(view);
    }

    //向上弹出
    public void showUpPop(View view) {

        int[] location = new int[2];
        //获取在整个屏幕内的绝对坐标
        view.getLocationOnScreen(location);

        popupWindow = new CustomPopupWindow.Builder()
                .setView(R.layout.popup_right_or_left)
                .setViewOnclickListener(this)
                .build(this);
//                .showUpPop(view);
        popupWindow.showAsDropDown(view, 0, -(popupWindow.getHeight() + view.getMeasuredHeight()));
    }

    @Override
    public void getChildView(final View view, int layoutResId) {
        //获得PopupWindow布局里的View
        switch (layoutResId) {
            case R.layout.popup_down:
                RecyclerView recycle_view = (RecyclerView) view.findViewById(R.id.recycle_view);
                recycle_view.setLayoutManager(new LinearLayoutManager(this));
                PopupAdapter mAdapter = new PopupAdapter(this);
                recycle_view.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new PopupAdapter.MyOnclickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        toast("position is " + position);
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                break;
            case R.layout.popup_up:
                Button btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
                Button btn_select_photo = (Button) view.findViewById(R.id.btn_select_photo);
                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast("拍照");
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                btn_select_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast("相册选取");
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        return true;
                    }
                });
                break;
            case R.layout.popup_right_or_left:
                ImageView good = (ImageView) view.findViewById(R.id.good);
                ImageView collection = (ImageView) view.findViewById(R.id.collection);
                good.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast("点赞成功");
                        popupWindow.dismiss();
                    }
                });
                collection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast("收藏成功");
                        popupWindow.dismiss();
                    }
                });
                break;
        }
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


}
