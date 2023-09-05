import 'package:WorkReport/page/app.dart';
import 'package:WorkReport/state/local_storage.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized(); // 初始化Flutter绑定

  SharedPreferences prefs = await SharedPreferences.getInstance();
  Get.put(LocalStorage(prefs));
  initializeDateFormatting().then((_) => runApp(const App()));
}
