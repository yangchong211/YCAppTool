import 'package:flutter/material.dart';

class SliverBoxPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("SliverBox"),
      ),
      body: CustomScrollView(),
    );
  }
}
