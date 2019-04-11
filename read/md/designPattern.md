#### 设计模式目录介绍
- 01.单例模式
- 02.builder模式
- 03.订阅者模式
- 04.策略模式
- 05.工厂模式
- 06.适配器模式





### 04.策略模式
- 看看下面这段代码，改编某伟大公司产品代码，你觉得可以利用面向对象设计原则如何改进？
    ```
    public class VIPCenter {
        void serviceVIP(T extend User user>) {
         if (user instanceof SlumDogVIP) {
            // 穷 X VIP，活动抢的那种
            // do somthing
          } else if(user instanceof RealVIP) {
            // do somthing
          }
          // ...
    }
    ```
    - 这段代码的一个问题是，业务逻辑集中在一起，当出现新的用户类型时，比如，大数据发现了我们是肥羊，需要去收获一下， 这就需要直接去修改服务方法代码实现，这可能会意外影响不相关的某个用户类型逻辑。
    - 利用开关原则，可以尝试改造为下面的代码。将不同对象分类的服务方法进行抽象，把业务逻辑的紧耦合关系拆开，实现代码的隔离保证了方便的扩展。[技术博客大总结](https://github.com/yangchong211/YCBlogs)
    ```
    public class VIPCenter {
        private Map<User.TYPE, ServiceProvider> providers;
        void serviceVIP(T extend User user） {
            providers.get(user.getType()).service(user);
        }
    }

    interface ServiceProvider{
        void service(T extend User user) ;
    }

    class SlumDogVIPServiceProvider implements ServiceProvider{
        void service(T extend User user){
            // do somthing
        }
    }

    class RealVIPServiceProvider implements ServiceProvider{
        void service(T extend User user) {
            // do something
        }
    }
    ```
















