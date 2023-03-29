import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';
import 'package:yc_flutter_tool/page/event/provider/pattern_state_service.dart';
import 'business_pattern.dart';

class BusinessPatternServiceImpl extends BusinessPatternService {

  final BuildContext context;
  BusinessPatternServiceImpl(this.context);

  // BusinessPattern _getBusinessPatternState(BuildContext context) {
  //   return Provider.of<BusinessPattern>(context);
  // }

  PatternState get currentPatternState =>
      _getBusinessPatternState(context).currentState;

  BusinessPattern _getBusinessPatternState(BuildContext context) {
    return Provider.of<BusinessPattern>(context, listen: false);
  }

  @override
  void nonePattern() {
    BusinessPattern _patternState = _getBusinessPatternState(context);
    _patternState.updateBusinessPatternState(PatternState.none);
  }

  @override
  void normalPattern() {
    BusinessPattern _patternState = _getBusinessPatternState(context);
    _patternState.updateBusinessPatternState(PatternState.normal);
  }

  @override
  void smallPattern() {
    BusinessPattern _patternState = _getBusinessPatternState(context);
    _patternState.updateBusinessPatternState(PatternState.small);
  }

  @override
  void overviewPattern() {
    BusinessPattern _patternState = _getBusinessPatternState(context);
    _patternState.updateBusinessPatternState(PatternState.overview);
  }

  @override
  void updateNormalState(PatternState state, bool refresh) {
    BusinessPattern normalState = _getBusinessPatternState(context);
    normalState.updateBusinessPatternState(state, refresh: refresh);
  }

}
