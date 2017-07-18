package com.dali.admin.mpopupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * PopupWindow 封装
 * Created by dali on 2017/7/15.
 */

public class CustomPopupWindow {

    private static PopupWindow mPopupWindow;
    private static Builder mBuilder;
    private static View mContentView;

    public static Builder newBuilder() {
        if (mBuilder == null)
            mBuilder = new Builder();
        return mBuilder;
    }

    private CustomPopupWindow() {
        mBuilder = new Builder();
    }

    public interface ViewClickListener {
        void getChildView(View view, int layoutResId);
    }

    public int getWidth() {
        if (mPopupWindow != null)
            return mContentView.getMeasuredWidth();
        return -1;
    }

    public int getHeight() {
        if (mPopupWindow != null)
            return mContentView.getMeasuredHeight();
        return -1;
    }

    public void dismiss() {
        if (mPopupWindow != null)
            mPopupWindow.dismiss();
    }

    /**
     * 显示在控件的下方
     *
     * @param parent parent
     */
    public CustomPopupWindow showDownPop(View parent) {
        if (parent.getVisibility() == View.GONE) {
            mPopupWindow.showAtLocation(parent, 0, 0, 0);
        } else {
            // x y
            int[] location = new int[2];
            //获取在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            if (mPopupWindow != null)
                mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1] + parent.getHeight());
//                mPopupWindow.showAsDropDown(parent);
        }
        return this;
    }

    /**
     * 显示在控件的上方
     *
     * @param parent parent
     */
    public CustomPopupWindow showUpPop(View parent) {
        if (parent.getVisibility() == View.GONE) {
            mPopupWindow.showAtLocation(parent, 0, 0, 0);
        } else {
            // x y
            int[] location = new int[2];
            //获取在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            if (mPopupWindow != null)
                mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1] - mPopupWindow.getHeight());
//                mPopupWindow.showAsDropDown(parent, 0, -(mPopupWindow.getHeight() + parent.getMeasuredHeight()));
        }
        return this;
    }

    /**
     * 显示在控件的左方
     *
     * @param parent parent
     */
    public CustomPopupWindow showLeftPop(View parent) {
        if (parent.getVisibility() == View.GONE) {
            mPopupWindow.showAtLocation(parent, 0, 0, 0);
        } else {
            // x y
            int[] location = new int[2];
            //获取在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            if (mPopupWindow != null)
                mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - getWidth(), location[1]);

//            mPopupWindow.showAsDropDown(parent, -mPopupWindow.getWidth(), -parent.getHeight());
        }
        return this;
    }

    /**
     * 显示在控件的右方
     *
     * @param parent parent
     */
    public CustomPopupWindow showRightPop(View parent) {
        if (parent.getVisibility() == View.GONE) {
            mPopupWindow.showAtLocation(parent, 0, 0, 0);
        } else {
            // x y
            int[] location = new int[2];
            //获取在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            if (mPopupWindow != null)
                mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0] + parent.getWidth(), location[1]);

//            mBuilder.mPopupWindow.showAsDropDown(parent, parent.getWidth(), -parent.getHeight());
        }
        return this;
    }

    /**
     * 全屏弹出
     *
     * @param parent parent
     */
    public CustomPopupWindow showAllPop(View parent) {
        if (parent.getVisibility() == View.GONE) {
            mPopupWindow.showAtLocation(parent, 0, 0, 0);
        } else {
            // x y
            int[] location = new int[2];
            //获取在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            if (mPopupWindow != null)
                mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        }
        return this;
    }

    public CustomPopupWindow showAtLocation(View anchor, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(anchor, gravity, x, y);
        }
        return this;
    }

    public CustomPopupWindow showAsDropDown(View anchor) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    public CustomPopupWindow showAsDropDown(View anchor, int xoff, int yoff) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xoff, yoff);
        }
        return this;
    }

    public CustomPopupWindow showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xoff, yoff, gravity);
        }
        return this;
    }


    public static class Builder {
        public int mLayoutResId;//布局id
        public int mWidth, mHeight;//弹窗的宽和高
        public int mAnimationStyle;//动画样式Id
        private ViewClickListener mListener;//子View监听回调
        private Context mContext;//上下文
        private Drawable mDrawable;//Drawable用于设置背景
        private boolean mTouchable = true;//是否响应touch事件
        private boolean mFocusable = false;//是否获取焦点
        private boolean mOutsideTouchable = false;//设置外部是否可点击

        public CustomPopupWindow build(Context context) {
            this.mContext = context;
            CustomPopupWindow customPopupWindow = new CustomPopupWindow();
            apply();
            if (mListener != null && mLayoutResId != 0) {
                mListener.getChildView(mContentView, mLayoutResId);
            }
            return customPopupWindow;
        }

        //初始化PopupWindow
        private void apply() {
            if (mLayoutResId != 0) {
                mContentView = LayoutInflater.from(mContext).inflate(mLayoutResId, null);
            }

            if (mWidth != 0 && mHeight != 0) {
                mPopupWindow = new PopupWindow(mContentView, mWidth, mHeight);
            } else {
                mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            if (mTouchable)
                mPopupWindow.setTouchable(mTouchable);

            if (mFocusable)
                mPopupWindow.setFocusable(mFocusable);

            if (mOutsideTouchable)
                mPopupWindow.setOutsideTouchable(mOutsideTouchable);

            if (mDrawable != null) {
                mPopupWindow.setBackgroundDrawable(mDrawable);
            } else {
                mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            }

            if (mAnimationStyle != -1) {
                mPopupWindow.setAnimationStyle(mAnimationStyle);
            }

            if (mWidth == 0 || mHeight == 0) {
                measureWidthAndHeight(mContentView);
                //如果没有设置高度的情况下，设置宽高并赋值
                mWidth = mPopupWindow.getContentView().getMeasuredWidth();
                mHeight = mPopupWindow.getContentView().getMeasuredHeight();
            }

            mPopupWindow.update();
        }

        /**
         * 测量View的宽高
         *
         * @param view View
         */
        public void measureWidthAndHeight(View view) {
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         * @return Builder
         */
        public Builder setView(int layoutResId) {
            mContentView = null;
            this.mLayoutResId = layoutResId;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            mWidth = width;
            mHeight = height;
            return this;
        }

        /**
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        public Builder setView(View view) {
            mContentView = view;
            mLayoutResId = 0;
            return this;
        }

        /**
         * 设置子View点击事件回调
         *
         * @param listener ViewClickListener
         * @return Builder
         */
        public Builder setViewOnclickListener(ViewClickListener listener) {
            this.mListener = listener;
            return this;
        }


        /**
         * 设置背景
         *
         * @param drawable
         * @return Builder
         */
        public Builder setBackgroundDrawable(Drawable drawable) {
            mDrawable = drawable;
            return this;
        }

        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            mOutsideTouchable = touchable;
            return this;
        }

        /**
         * 是否相应Touch事件
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setTouchable(boolean touchable) {
            mTouchable = touchable;
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            mAnimationStyle = animationStyle;
            return this;
        }

        /**
         * 是否获取焦点
         *
         * @param focusable
         * @return
         */
        public Builder setFocusable(boolean focusable) {
            mFocusable = focusable;
            return this;
        }
    }

}
