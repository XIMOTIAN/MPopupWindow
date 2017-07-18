# PopupWindow 封装

### 先看效果图

![](https://raw.githubusercontent.com/angelOnly/MPopupWindow/master/demo17.gif)

### PopupWindow 的简单使用

```java
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
//设置其他属性
popupWindow.setAnimationStyle(R.style.AnimBackground);
popupWindow.setTouchable(false);
popupWindow.setFocusable(true);
popupWindow.setOutsideTouchable(true);
//显示popupWindow
popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
```

从上面可以看出，要封装一个通用的 PopupWindow，必须将外部设置的属性暴露接口或者setxxx方法，对于 PopupWindow 布局中的子 View，还要封装监听实习子 View 的点击事件。

### 封装的 PopupWindow 具有上述特性

以代码中的一个 向下弹出的 PopupWindow 为例，PopupWindow 的属性都设置了默认值，暴露 setxxx 方法对属性进行修改，设置子 View 的监听事件，重写弹窗的方法。

#### CustomPopupWindow 的构建

```java
 //向下弹出
public void showDownPop(View view) {
        popupWindow = CustomPopupWindow.newBuilder()
                .setView(R.layout.popup_down)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .setTouchable(true)
                .build(this)
                .showDownPop(view);
//        popupWindow.showAsDropDown(view);
}
```

#### CustomPopupWindow 设置动画

只要设置好动画等 style 文件即可

```java
setAnimationStyle(R.style.AnimUp)
```

AnimUp 向上弹出

```java
 <style name="AnimUp" parent="@android:style/Animation">
        <item name="android:windowEnterAnimation">@anim/set_up_in</item>
        <item name="android:windowExitAnimation">@anim/set_up_out</item>
</style>
```

set_up_in.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android" >
    <translate
        android:duration="200"
        android:fromYDelta="50%p"
        android:toYDelta="0" />
</set>
```

set_up_out.xml

```Xml
<?xml version="1.0" encoding="utf-8"?><!-- 上下滑入式 -->
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="200"
        android:fromYDelta="0"
        android:toYDelta="50%p" />
</set>
```

### CustomPopupWindow 的弹出

封装了 public void showAsDropDown(View anchor)，public void showAsDropDown(View anchor, int xoff, int yoff)，public void showAtLocation(View parent, int gravity, int x, int y)，并添加了上下左右弹出位置等简单计算，方便直接使用，如果对位置有特殊需求，可以使用原生的方法进行设置弹出位置。

```java
//显示在控件的下方
public CustomPopupWindow showDownPop(View parent) {}
//显示在控件的上方
public CustomPopupWindow showUpPop(View parent) {}
//显示在控件的左方
public CustomPopupWindow showLeftPop(View parent) {}
//显示在控件的右方
public CustomPopupWindow showRightPop(View parent) {}
//全屏弹出
public CustomPopupWindow showAllPop(View parent) {}
//showAsDropDown(View anchor)
public CustomPopupWindow showAsDropDown(View anchor) {}
//showAtLocation(View anchor, int gravity, int x, int y)
public CustomPopupWindow showAtLocation(View anchor) {}
//showAsDropDown(View anchor, int xoff, int yoff)
public CustomPopupWindow showAsDropDown(View anchor) {}
//showAsDropDown(View anchor, int xoff, int yoff, int gravity)
public CustomPopupWindow showAsDropDown(View anchor) {}
```

## 一、封装思路

1、声明 PopupWindow 中属性的变量，对外提供 set 方法

2、自定义接口实现子 View 的点击事件

3、利用 Builde 模式链式调用方法设置 PopupWindow 的属性，最后调用 build() 方法构建 PopupWindow

4、PopupWindow 重写弹窗方法，对位置等准确定位，弹出 PopupWindow

## 二、封装实战

### 2.1、Builder 模式对 PopupWindow 进行构造

通过 Builder 类对 PopupWindow 的属性进行设置，并暴露setxxx 方法和接口回调，封装了常使用到的属性

#### 2.1.1、声明 PopupWindow 常用的属性

```java
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
}
```

#### 2.1.2、提供 set 方法

对以上所有属性提供 set 方法，要注意一点的是 set 方法一定是在 build() 方法之前调用

```java
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
 * @param listener ViewClickListener
 * @return Builder
 */
public Builder setViewOnclickListener(ViewClickListener listener) {
    this.mListener = listener;
    return this;
}


/**
 * 设置背景
 * @param drawable
 * @return Builder
 */
public Builder setBackgroundDrawable(Drawable drawable) {
    mDrawable = drawable;
    return this;
}

/**
 * 是否可点击Outside消失
 * @param touchable 是否可点击
 * @return Builder
 */
public Builder setOutsideTouchable(boolean touchable) {
    mOutsideTouchable = touchable;
    return this;
}

/**
 * 是否相应Touch事件
 * @param touchable 是否可点击
 * @return Builder
 */
public Builder setTouchable(boolean touchable) {
    mTouchable = touchable;
    return this;
}

/**
 * 设置动画
 * @return Builder
 */
public Builder setAnimationStyle(int animationStyle) {
    mAnimationStyle = animationStyle;
    return this;
}

/**
 * 是否获取焦点
 * @param focusable
 * @return
 */
public Builder setFocusable(boolean focusable) {
    mFocusable = focusable;
    return this;
}
```

#### 2.1.3、将属性作用到 PopupWindow

```java
public class CustomPopupWindow {
	private static PopupWindow mPopupWindow;
    private static View mContentView;
  	//......
  	public static class Builder {
      	//......
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
  	}
}
```

#### 2.1.4、构造 PopupWindow

```java
public static class Builder {
  	//......
    public CustomPopupWindow build(Context context) {
        this.mContext = context;
        CustomPopupWindow customPopupWindow = new CustomPopupWindow();
        apply();
        if (mListener != null && mLayoutResId != 0) {
            mListener.getChildView(mContentView, mLayoutResId);
        }
        return customPopupWindow;
    }
}
```

### 2.2、CustomPopupWindow 构造

#### 2.2.1、实例化 Builder 类，将其构造函数设为private，通过 build() 方法对其进行实例化

```java
public class CustomPopupWindow {
	private static PopupWindow mPopupWindow;
    private static Builder mBuilder;
    private static View mContentView;
  	//子 View 点击事件
  	public interface ViewClickListener {
        void getChildView(View view, int layoutResId);
    }

	public static Builder newBuilder() {
        if (mBuilder == null)
            mBuilder = new Builder();
        return mBuilder;
    }

    private CustomPopupWindow() {
        mBuilder = new Builder();
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

	public static class Builder {
		public CustomPopupWindow build(Context context) {
            //......
            return customPopupWindow;
        }
	}
}
```

#### 2.2.2、弹出方法重写

```java
/**
     * 显示在控件的下方
     * @param parent PopupWindow布局
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
     * @param parent PopupWindow布局
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
     * @param parent PopupWindow布局
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
     * @param parent PopupWindow布局
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
     * @param parent PopupWindow布局
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
	//showAtLocation(View anchor, int gravity, int x, int y)
    public CustomPopupWindow showAtLocation(View anchor, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(anchor, gravity, x, y);
        }
        return this;
    }
	//showAsDropDown(View anchor)
    public CustomPopupWindow showAsDropDown(View anchor) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }
	//showAsDropDown(View anchor, int xoff, int yoff)
    public CustomPopupWindow showAsDropDown(View anchor, int xoff, int yoff) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xoff, yoff);
        }
        return this;
    }
	//showAsDropDown(View anchor, int xoff, int yoff, int gravity)
    public CustomPopupWindow showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xoff, yoff, gravity);
        }
        return this;
    }
```

### 注意：

细心的朋友会注意到 mPopupWindow 和 mContentView 我是声明在 CustomPopupWindow 中，而不是 Builder 中，这是为什么？原因是我们先构造的是 Builder，然后才是构造 CustomPopupWindow，而在 CustomPopupWindow 中会使用到 mPopupWindow 对 PopupWindow 的弹窗方法重写和调用， 使用 mContentView 获取 mContentView 的宽高。为什么要获取宽高？计算弹窗位置的时候会用到这些值，所以要把这两个变量定义在外面，作为全局变量，增长变量生命周期，否则会出现空指针异常。

```java
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
	public static class Builder {
		public CustomPopupWindow build(Context context) {
            //......
            return customPopupWindow;
        }
	}
}
```
