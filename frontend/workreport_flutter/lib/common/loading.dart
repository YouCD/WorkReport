import 'package:flutter_smart_dialog/flutter_smart_dialog.dart';

void showLoading(bool flag) {
  flag
      ? SmartDialog.showLoading(
          animationType: SmartAnimationType.scale, msg: "数据加载中...")
      : SmartDialog.dismiss();
}
