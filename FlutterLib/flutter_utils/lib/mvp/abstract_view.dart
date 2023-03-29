

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/mvp/abstract_view_state.dart';

abstract class AbstractView extends StatefulWidget {

  @override
  AbstractViewState createState() => getState();

  ///子类实现
  AbstractViewState getState();

}