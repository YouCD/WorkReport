import 'dart:convert';

import 'package:WorkReport/common/jwt.dart';
import 'package:WorkReport/module/type.dart';
import 'package:get/get.dart';
import 'package:shared_preferences/shared_preferences.dart';

const String jwtName = "Jwt";

class LocalStorage extends GetxController {
  late SharedPreferences prefs;

  LocalStorage(this.prefs);

  // Future<void> _initSharedPreferences() async {
  //   prefs = await SharedPreferences.getInstance();
  // }

  void saveData(String key, String value) {
    prefs.setString(key, value);
  }

  String getData(String key) {
    return prefs.getString(key) ?? '';
  }

  void removeData(String key) {
    prefs.remove(key);
  }

  String getUserId() {
    String str = prefs.getString(jwtName) ?? "";
    String user = JwtToken(str).getUserId();
    return user;
  }

  int getExpirationDate() {
    String str = prefs.getString(jwtName) ?? "";
    int unixTime = JwtToken(str).getExpirationDate();
    return unixTime;
  }

  Map<int, List<Typ>> loadType2ListMap() {
    String mStr = getData("type2ListMap");
    if (mStr.isEmpty) {
      return {};
    }
    Map<int, List<Typ>> m = {};
    Map<String, dynamic> decodedMap = json.decode(mStr);

    decodedMap.forEach((key, value) {
      int id = int.parse(key);
      List<Typ> typList =
          (value as List<dynamic>).map((item) => Typ.fromJson(item)).toList();
      m[id] = typList;
    });
    return m;
  }

  /// false 没有过期
  /// true 过期
  bool jwtIsExpiration() {
    String str = prefs.getString(jwtName) ?? "";
    int expirationTime = JwtToken(str).getExpirationDate();
    // 获取UNIX时间戳（秒数）
    DateTime now = DateTime.now();
    int nowUnixTimestamp = now.millisecondsSinceEpoch ~/ 1000;
    return nowUnixTimestamp > expirationTime;
  }

  /// 欢迎页标志位
  void setIsFirstOpenFlag() {
    saveData("IsFirstOpen", "IsFirstOpen");
  }
}
