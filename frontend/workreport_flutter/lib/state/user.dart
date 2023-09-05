import 'package:WorkReport/api/api.dart';
import 'package:WorkReport/api/url.dart';
import 'package:WorkReport/state/local_storage.dart';
import 'package:flutter/material.dart';
import 'package:flutter_smart_dialog/flutter_smart_dialog.dart';
import 'package:get/get.dart';

class UserController extends GetxController {
  final apiService = ApiService();
  final LocalStorage _localStorage = Get.find<LocalStorage>();

  Future<bool> login(String username, String password) async {
    final response = await apiService
        .post(Url.login, data: {"username": username, "password": password});
    if (!response.flag) {
      SmartDialog.showToast(
        response.msg,
        alignment: Alignment.topCenter,
      );
      return response.flag;
    }
    String token = response.data["token"];
    _localStorage.saveData(jwtName, token);
    return response.flag;
  }
}
