
- 单个 view 的自定义动画

  ```java
  SofaAnimatorCompat
           .play(text)
           .alpha(1f, 0.2f)
           .scaleX(0f, 1f)
           .scaleY(0f, 1f)
           .rotate(0f, 180f)
           .translateX(0f, 200f)
           .translateY(0f, 200f)
           .x(text.getX(), 0f)
           .y(text.getY(), 0f)
           .duration(1000)
           .decelerate()
           .build()
           .start();
  ```

- 自定义动画的多 view 组合

  ```java
  SofaAnimatorCompat
          .play(image2).rotate(0f, 360).alpha(0f, 1f)
          .before(text).alpha(0f, 1f).scaleX(0f, 1f).startDelay(2000)
          .after(image1).rotate(360f, 0f).alpha(0f, 1f)
          .build()
          .start();
  ```

- 单个 view 的常用动画效果

  ```java
  SofaAnimatorCompat
          .play(image1).fadeIn()
          .play(image2).fadeOut()
          .play(image1).translateInFromTop()
          .play(image1).translateOutToTop()
          .play(text).translateInFromBottom()
          .play(text).translateOutToBottom()
          .play(text).scaleIn()
          .play(text).scaleOut()
          .play(image1).rotateCW()
          .play(image2).rotateCCW()
          .build()
          .start();
  ```

- 常用动画效果的多 view 组合

  ```java
  SofaAnimatorCompat
          .play(image1).translateInFromTop()
          .with(image2).translateInFromTop()
          .before(text).scaleIn()
          .build()
          .start();
  ```

-  自定义 Interpolator 和 Evaluator

  ```java
  // 库中自带两个 Evaluator 的实现
  SofaAnimatorCompat
          .play(image1).translateInFromTop()
          .bounceEaseOut()
          .elasticEaseOut()
          .build()
          .start();
  // 自定义 TypeEvaluator
  SofaAnimatorCompat
          .play(image1).translateInFromTop().evaluator(typeEvaluator)
          .build()
          .start();
  // 自定义 Interpolator
  SofaAnimatorCompat
          .play(image1).translateInFromTop().interpolator(interpolator)
          .build()
          .start();
  ```

- 自定义属性

  ```java
  SofaAnimatorCompat
          .play(customView).property("Scale", 0f, 0.6f)
          .build()
          .start();
  ```

- Animation Listener

  ```java
  SofaAnimatorCompat
          .play(image1).rotate(0f, 360).alpha(0f, 1f)
          .withListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationStart(Animator animator, View view) {
                  super.onAnimationStart(animator, view);
                  LoggerUtil.d("动画事件", "onAnimationStart " + animator + " view: " + view);
              }

              @Override
              public void onAnimationEnd(Animator animator, View view) {
                  super.onAnimationEnd(animator, view);
                  LoggerUtil.d("动画事件", "onAnimationEnd " + animator + " view: " + view);
              }
          })
          .before(text).alpha(0f, 1f).scaleX(0f, 1f).startDelay(2000)
          .withListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationStart(Animator animator, View view) {
                  super.onAnimationStart(animator, view);
                  LoggerUtil.d("动画事件", "onAnimationStart " + animator + " view: " + view);
              }

              @Override
              public void onAnimationEnd(Animator animator, View view) {
                  super.onAnimationEnd(animator, view);
                  LoggerUtil.d("动画事件", "onAnimationEnd " + animator + " view: " + view);
              }
          })
          .buildWithListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationStart(Animator animator, View view) {
                  super.onAnimationStart(animator, view);
                  LoggerUtil.d("动画事件", "onAnimationStart " + animator + " view: " + view);
              }

              @Override
              public void onAnimationEnd(Animator animator, View view) {
                  super.onAnimationEnd(animator, view);
                  LoggerUtil.d("动画事件", "onAnimationEnd " + animator + " view: " + view);
              }
          })
          .start();
  ```

- 自定义动画序列

  ```java
  Animator animator1 = SofaAnimatorCompat
          .play(image1).rotate(0f, 360).alpha(0f, 1f)
          .before(text).alpha(0f, 1f).scaleX(0f, 1f).build();
  Animator animator2 = SofaAnimatorCompat
          .play(image2).rotateCCW()
          .before(customView).property("Scale", 0f, 0.6f).build();
  // 同时启动多个 Animator
  SofaAnimatorCompat.playTogether(animator1, animator2).start();
  // 顺序启动多个 Animator
  SofaAnimatorCompat.playSequentially(animator1, animator2).start();
  ```

- ValueAnimator的使用

  ```java                
  SofaAnimatorCompat.valueAnimation().overshoot().duration(1000)
          .withListener(new AnimatorUpdateListener() {
              @Override
              public void onAnimationUpdate(Animator animator, Object animatedValue) {
                  LoggerUtil.d("动画事件", "onAnimationUpdate animator: " + animator + " animatedValue: " + animatedValue);
                  imageSmall.setScaleX((Float) animatedValue);
                  imageSmall.setScaleY((Float) animatedValue);
              }
          }).build().start();
  ```
  
  ```java
  Animator animator1 = SofaAnimatorCompat.valueAnimation().overshoot().duration(1000)
          .withListener(new AnimatorUpdateListener() {
              @Override
              public void onAnimationUpdate(Animator animator, Object animatedValue) {
                  LoggerUtil.d("动画事件", "onAnimationUpdate animator: " + animator + " animatedValue: " + animatedValue);
                  imageSmall.setScaleX((Float) animatedValue);
                  imageSmall.setScaleY((Float) animatedValue);
              }
          }).build();
  Animator animator2 = SofaAnimatorCompat.valueAnimation().duration(1000)
          .withListener(new AnimatorUpdateListener() {
              @Override
              public void onAnimationUpdate(Animator animator, Object animatedValue) {
                  LoggerUtil.d("动画事件", "onAnimationUpdate animator: " + animator + " animatedValue: " + animatedValue);
                  imageLarge.setAlpha((Float) animatedValue);
              }
          }).build();
  SofaAnimatorCompat.playTogether(animator1, animator2).start();
  SofaAnimatorCompat.playSequentially(animator1, animator2).start();
  ```

