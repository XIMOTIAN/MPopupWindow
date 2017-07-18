package com.dali.admin.mpopupwindow;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import static com.dali.admin.mpopupwindow.R.id.btn_cancel;
import static com.dali.admin.mpopupwindow.R.id.btn_select_photo;
import static com.dali.admin.mpopupwindow.R.id.btn_take_photo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showPop(View view){
        //PopupWindow 布局
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_up,null);
        //创建 PopupWindow
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //子View点击事件
        Button btnTakePhoto = (Button) contentView.findViewById(btn_take_photo);
        Button btnSelectPhoto = (Button) contentView.findViewById(btn_select_photo);
        Button btnCancel = (Button) contentView.findViewById(btn_cancel);
        btnTakePhoto.setOnClickListener(this);
        btnSelectPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        popupWindow.setAnimationStyle(R.style.AnimBackground);
        popupWindow.setTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //显示popupWindow
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }

    public void showEdit(View view){
        //PopupWindow 布局
        View contentView = LayoutInflater.from(this).inflate(R.layout.edit_item,null);
        //创建 PopupWindow
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setAnimationStyle(R.style.AnimBackground);
        popupWindow.setTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLUE));

        // x y
        int[] location = new int[2];
        //获取在整个屏幕内的绝对坐标
        view.getLocationOnScreen(location);
        if (popupWindow != null)
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_take_photo:
                toast("拍照");
                popupWindow.dismiss();
                break;
            case R.id.btn_select_photo:
                toast("相册选取");
                popupWindow.dismiss();
                break;
            case R.id.btn_cancel:
                toast("取消");
                popupWindow.dismiss();
                break;
        }
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
