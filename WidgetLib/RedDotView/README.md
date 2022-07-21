### 目录介绍
- 01.该库介绍
- 02.效果展示
- 03.如何使用
- 04.注意要点
- 05.优化问题
- 06.部分代码逻辑





### 01.该库介绍
- 自定义红点控件，不用修改之前的代码，完全解耦，既可以设置红点数字控件，使用十分方便。
- 网上看到有些案例是继承View，然后去测量和绘制红点的相关操作，此案例则不需要这样，继承TextView也可以完成设置红点功能。
- 可以支持设置在TextView，Button，LinearLayout，RelativeLayout，TabLayout等等控件上……
- 大概的原理是：继承TextView，然后设置LayoutParams，设置内容，设置Background等等属性，然后在通过addView添加到父控件中。



### 02.效果展示
- ![image](https://github.com/yangchong211/YCRedDotView/blob/master/image/red.jpg)


### 03.如何使用
- 如何引用
    ```
    implementation 'com.github.yangchong211:YCRedDotView:1.0.4'
    ```
- 如下所示
    ```
    //创建红点View
    YCRedDotView ycRedDotView = new YCRedDotView(this);
    //设置依附的View
    ycRedDotView.setTargetView(tv1);
    //设置红点的数字
    ycRedDotView.setBadgeCount(10);
    //设置红点位置
    ycRedDotView.setRedHotViewGravity(Gravity.END);
    //获取小红点的数量
    int count = ycRedDotView.getBadgeCount();
    //如果是设置小红点，不设置数字，则可以用这个，设置属性是直径
    ycRedDotView.setBadgeView(10);
    //设置margin
    ycRedDotView.setBadgeMargin(0,10,20,0);
    ```


### 04.注意要点
- 如果设置数字大于99，则会显示“99+”。如果设置为0，则不可见。



### 05.优化问题
- 相比网上一些案例，该库不需要做测量绘制方面操作，代码十分简洁，但却也可以完成你需要的功能。
- 有些红点View，需要你在布局中设置，要是很多地方有设置红点操作，则比较麻烦。而该案例不需要改变你之前的任何代码，只是需要按照步骤即可设置红点。
- 设置红点view到你的控件的逻辑操作代码如下所示。如果控件父布局是帧布局，则直接添加；如果控件父布局是ViewGroup，则创建一个帧布局，然后添加红点。
    ```
    /**
     * 设置红点依附的view
     * @param view                  view
     */
    public void setTargetView(View view){
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (view == null) {
            return;
        }
        if(view.getParent() instanceof FrameLayout){
            ((FrameLayout) view.getParent()).addView(this);
        }else if(view.getParent() instanceof ViewGroup){
            ViewGroup parentContainer = (ViewGroup) view.getParent();
            int groupIndex = parentContainer.indexOfChild(view);
            parentContainer.removeView(view);

            FrameLayout badgeContainer = new FrameLayout(getContext());
            ViewGroup.LayoutParams parentLayoutParams = view.getLayoutParams();
            badgeContainer.setLayoutParams(parentLayoutParams);
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
            badgeContainer.addView(view);
            badgeContainer.addView(this);
        }else {
            Log.e(getClass().getSimpleName(), "ParentView is must needed");
        }
    }
    ```


### 6.关于License
- 如下所示
    ```
       Copyright 2017 yangchong211（github.com/yangchong211）

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
    ```














