# PopupWindow浮动窗

## 一、概述

### 1.1、PopupWindow和AlertDialog的区别

Android 对话框分为两种：[PopupWindow](https://developer.android.com/reference/android/widget/PopupWindow.html) 和 AlertDialog。他们的不同在于：AlertDialog 的位置固定，只能显示在屏幕中间（可以通过设置WindowManager参数来改变位置），而PopupWindow 位置可以随意设置。

### 1.2、PopupWindow 的构造函数

```java
public PopupWindow(View contentView);
public PopupWindow(Context context);
public PopupWindow(View contentView, int width, int height);
public PopupWindow(View contentView, int width, int height, boolean focusable)  
```

PopupWindow 虽然有4个构造函数，但是要显示 PopupWindow，必须满足设置三个参数：View contentView, int width, int height。缺一个都不可能弹出 PopupWindow。为什么要设置 width 和 height，因为 PopupWindow 没有默认的布局，必须由我们设置布局和宽高才可以正常弹出。不像 AlertDialog 那样只 setTitle 就能弹出来一个框。

要构造出 PopupWindow ，完整的代码应该是这样

```java
View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popuplayout, null);  
PopupWindow popWnd = PopupWindow (context);  
popWnd.setContentView(contentView);  
popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);  
popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);  

//因此，使用最多的构造函数是第三个，刚好满足 PopupWindow 的弹出条件
View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popuplayout, null);  
PopupWindow popWnd = PopupWindow (contentView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);  
```

### 1.3、弹出 PopupWindow 的显示函数

PopupWindow 的位置按照有偏移和无偏移两种。
PopupWindow 按照参照物的不同，分为相对于某个控件（Anchor 锚）和相对父控件

```java
//相对某个控件的位置（正左下方），无偏移  
showAsDropDown(View anchor)：  
//相对某个控件的位置，有偏移;xoff表示x轴的偏移，正值表示向左，负值表示向右；yoff表示相对y轴的偏移，正值是向下，负值是向上；  
showAsDropDown(View anchor, int xoff, int yoff)：  
//相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移  
showAtLocation(View parent, int gravity, int x, int y)：  
```

### 1.4、常用的其他函数

```Java
public void dismiss()//隐藏 PopupWindow
public void setAnimationStyle(int animationStyle)//设置弹出和退出动画样式
public void setBackgroundDrawable(Drawable background)//设置背景
public void setClippingEnabled(boolean enabled)//允许弹出窗口超出屏幕范围。
public void setFocusable(boolean focusable)//设置是否获取焦点
public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener)//设置在关闭窗口时调用的监听器。
public void setOutsideTouchable(boolean touchable)//外部触摸事件是否可点击
public void setTouchable(boolean touchable)//设置是否响应Touch事件
public void setTouchInterceptor(View.OnTouchListener l)//为所有正在分发的事件设置监听
public void update()//更新PopupWindow的状态
```

## 二、弹出简单 PopupWindow

### 2.1、主布局

activity_main.xml，一个按钮，并设置点击事件，点击按钮弹出 PopupWindow

```Xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/cccccc"
    tools:context="com.dali.admin.mpopupwindow.MainActivity">

    <Button
        android:onClick="showPop"
        android:text="底部弹出 PopupWindow"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</LinearLayout>
```

### 2.2、PopupWindow 的布局

PopupWindow 的布局 popup_up.xml，前面说过，PopupWindow 想要弹出，必须设置其弹出的布局和宽高

```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:id="@+id/pop_layout"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:layout_alignParentBottom="true"
              android:background="@color/white"
              android:orientation="vertical">

    <Button
        android:id="@+id/btn_take_photo"
        style="?android:attr/borderlessButtonStyle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingBottom="8dp"
        android:paddingTop="8dp"
        android:text="拍照"
        android:textColor="#282828"
        android:textSize="16sp"/>

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="@color/gray_holo_light"/>

    <Button
        android:id="@+id/btn_select_photo"
        style="?android:attr/borderlessButtonStyle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingBottom="8dp"
        android:paddingTop="8dp"
        android:text="相册选取"
        android:textColor="#282828"
        android:textSize="16sp"/>

    <View
        android:layout_width="match_parent"
        android:layout_height="10dp"
        android:background="@color/gray_holo_light"/>

    <Button
        android:id="@+id/btn_cancel"
        style="?android:attr/borderlessButtonStyle"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:paddingBottom="8dp"
        android:paddingTop="8dp"
        android:text="取消"
        android:textColor="#282828"
        android:textSize="16sp"/>
</LinearLayout>
```

### 2.3、PopupWindow 的弹出

按钮点击事件响中初始化 PopupWindow，先渲染 PopupWindow 的 布局 popup_up.xml，

```java
public void showPop(View view){
    //PopupWindow 布局
    View contentView = LayoutInflater.from(this).inflate(R.layout.popup_up,null);
    //创建 PopupWindow，满足三个必要条件，View，width，height
    popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    //子 View 点击事件
    Button btnTakePhoto = (Button) contentView.findViewById(btn_take_photo);
    Button btnSelectPhoto = (Button) contentView.findViewById(btn_select_photo);
    Button btnCancel = (Button) contentView.findViewById(btn_cancel);
    btnTakePhoto.setOnClickListener(this);
    btnSelectPhoto.setOnClickListener(this);
    btnCancel.setOnClickListener(this);

    //显示popupWindow
    popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
}
```

### 2.4、子 View 点击事件

当子 View 点击之后，弹出文字，并将 PopupWindow  设置消失

```java
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
```

### 2.5、运行效果

![](https://raw.githubusercontent.com/angelOnly/MPopupWindow/master/demo12.gif)

> ##### 注意：如果代码中设置了 PopupWindow 的宽高，则以代码中的宽高为准，并不会以布局中的宽高为准。

## 三、提高篇

### 3.1、setAnimationStyle(int animationStyle)

为 popupWindow 添加动画效果或者阴影

![](https://raw.githubusercontent.com/angelOnly/MPopupWindow/master/demo11.gif)

效果中明显看到有阴影，而添加阴影效果只需要一个函数即可。

```java
//设置动画对应的style
popupWindow.setAnimationStyle(R.style.AnimBackground);
```

#### 1、生成动画对应的 style

进入时动画 menu_enter.xml

```Xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="@android:integer/config_shortAnimTime"
        android:fromXDelta="0"
        android:fromYDelta="100%p"
        android:interpolator="@android:anim/accelerate_decelerate_interpolator"
        android:toXDelta="0"
        android:toYDelta="0"/>
</set>
```

退出时动画 menu_exit.xml

```Xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android" >
    <translate
        android:duration="@android:integer/config_shortAnimTime"
        android:fromXDelta="0"
        android:fromYDelta="0"
        android:interpolator="@android:anim/accelerate_decelerate_interpolator"
        android:toXDelta="0"
        android:toYDelta="100%p" />
</set>
```

生成动画对应的 style — AnimBackground

```xml
<style name="AnimBackground">
    <item name="android:windowEnterAnimation">@anim/menu_enter</item>
    <item name="android:windowExitAnimation">@anim/menu_exit</item>
</style>
```

#### 2、使用 setAnimationStyle

```java
public void showPop(View view){
    //PopupWindow 布局
    View contentView = LayoutInflater.from(this).inflate(R.layout.popup_up,null);
    //创建 PopupWindow
    popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
	//......
    popupWindow.setAnimationStyle(R.style.AnimBackground);
    //显示popupWindow
    popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
}
```

### 3.2、setTouchable(boolean touchable)

设置 PopupWindow 是否响应 touch 事件，默认为 true。若为 false，则 view 将不可点击

![](https://raw.githubusercontent.com/angelOnly/MPopupWindow/master/demo13.gif)

### 3.3、setFocusable(boolean focusable)

设置 PopupWindow 是否获取焦点，默认为 false。一般控件不需要获取焦点，对于 EditText 则需要获取焦点，否则无法编辑。

```java
//将 PopupWindow 获取焦点设为true即可，布局代码类似，不贴了，后面会上传源码
popupWindow.setFocusable(true);
```

![](https://raw.githubusercontent.com/angelOnly/MPopupWindow/master/demo14.gif)

### 3.4、setOutsideTouchable(boolean touchable)

设置 PopupWindow 外部是否可点击，如果点击 PopupWindow 以外区域，PopupWindow 是否会消失。

```java
//设置 popupWindow 外部是否可点击
popupWindow.setOutsideTouchable(true);
```

![](https://raw.githubusercontent.com/angelOnly/MPopupWindow/master/demo15.gif)

### 3.5、setBackgroundDrawable(Drawable background)

这个函数很屌，不仅仅能设置背景，设置之后，setOutsideTouchable(boolean touchable) 才会生效。只有加上它之后，PopupWindow 才会对手机的返回按钮有响应：点击手机返回按钮，可以关闭 PopupWindow，如果不加setBackgroundDrawable() 将关闭的 PopupWindow 所在的 Activity。填充各种 Drawable，如 BitmapDrawable()，ColorDrawable()

```java
popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
```

![](https://raw.githubusercontent.com/angelOnly/MPopupWindow/master/demo16.gif)

还有很多方法没有讲解，步骤差不多，感兴趣的查看源码吧。
