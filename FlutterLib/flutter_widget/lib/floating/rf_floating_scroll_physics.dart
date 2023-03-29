import 'package:flutter/widgets.dart';

class RFFloatingScrollPhysics extends ClampingScrollPhysics {
  /// Creates scroll physics that prevent the scroll offset from exceeding the
  /// bounds of the content..
  RFFloatingScrollPhysics({ScrollPhysics parent}) : super(parent: parent);

  @override
  RFFloatingScrollPhysics applyTo(ScrollPhysics ancestor) {
    return RFFloatingScrollPhysics(parent: buildParent(ancestor));
  }

  @override
  double applyPhysicsToUserOffset(ScrollMetrics position, double offset) {
    double value = 0;
    // 如果当前位置未对顶部进行超出，则都交给父类处理
    if (parent == null || position.pixels >= position.minScrollExtent) {
      value = offset;
    } else {
      value = parent.applyPhysicsToUserOffset(position, offset);
    }

    isUserOffset = true;
    // print("applyPhysicsToUserOffset:$value");
    return value;
  }

  // 移除边界的距离，
  // 负的表示 underscroll
  // 正的表示 overscroll
  double _overScrollOffset = 0;

  // 是否是用户偏移
  bool isUserOffset = false;

  @override
  double applyBoundaryConditions(ScrollMetrics position, double value) {
    // 是否是用户偏移操作
    if (isUserOffset) {
      double offset;
      if (value < position.minScrollExtent &&
          position.minScrollExtent < position.pixels) // hit top edge
        offset = value - position.minScrollExtent;
      else if (position.pixels <= position.minScrollExtent) // underscroll
        offset = value - position.pixels;

      if (offset != null) {
        // 如果偏移值从负的向正的改变
        double resultOverScrollOffset = _overScrollOffset + offset;

        if (resultOverScrollOffset > 0) {
          // 如果当前偏移距离为 0，则重置 offset
          if (_overScrollOffset == 0) {
            offset = 0;
          }
          _overScrollOffset = 0;
        } else {
          _overScrollOffset = resultOverScrollOffset;
        }

        // print("_overScrollOffset:$_overScrollOffset  ${offset}");
        return offset;
      }
    }

    // 保证滚动到顶部后，不继续滚动到边界
    if (value < position.minScrollExtent &&
        position.minScrollExtent < position.pixels) // hit top edge
      return value - position.minScrollExtent;

    // 如果父类存在，使用父类的滚动效果
    if (parent != null) {
      return parent.applyBoundaryConditions(position, value);
    }

    // print("applyBoundaryConditions:${position.pixels}");
    return super.applyBoundaryConditions(position, value);
  }

  @override
  Simulation createBallisticSimulation(
      ScrollMetrics position, double velocity) {
    isUserOffset = false;

    // 如果当前处于超出范围，则不执行滚动动效
    if (_overScrollOffset != 0) {
      // 重置偏移距离
      _overScrollOffset = 0;
      return null;
    }

    // 如果父类存在，则使用父类的仿真效果
    if (parent != null) {
      return parent.createBallisticSimulation(position, velocity);
    }

    return super.createBallisticSimulation(position, velocity);
  }
}
