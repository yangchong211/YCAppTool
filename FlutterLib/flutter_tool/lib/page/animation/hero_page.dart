


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/image/images.dart';
import 'package:yc_flutter_utils/flutter_utils.dart';
import 'package:yc_flutter_utils/router/animation_type.dart';

class HeroPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new HeroState();
  }

}

class HeroState extends State<HeroPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Hero动画"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text(
              "这个是一个 Hero动画",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("Hero指的是可以在路由(页面)之间“飞行”的widget，"
                "简单来说Hero动画就是在路由切换时，有一个共享的widget可以在新旧路由间切换。"
                "由于共享的widget在新旧路由页面上的位置、外观可能有所差异，"
                "所以在路由切换时会从旧路逐渐过渡到新路由中的指定位置，这样就会产生一个Hero动画。"),
            Container(
              height: 200,
              color: Colors.blue[50],
              child: getFuture(),
            ),


          ],
        ),
      ),
    );
  }

  Widget getFuture() {
    var heroAnimationRoute = new HeroAnimationRoute();
    return heroAnimationRoute;
  }
}


// 路由A
class HeroAnimationRoute extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.topCenter,
      child: InkWell(
        child: Hero(
          //唯一标记，前后两个路由页Hero的tag必须相同
          tag: "avatar",
          child: ClipOval(
            child: Image.asset(Images.person, width: 50.0,),
          ),
        ),
        onTap: () {
          //打开B路由
          NavigatorUtils.pushAnimation(context, HeroAnimationRouteB());
        },
      ),
    );
  }
}

class HeroAnimationRouteB extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Hero(
        //唯一标记，前后两个路由页Hero的tag必须相同
        tag: "avatar",
        child: Image.asset(Images.person),
      ),
    );
  }
}