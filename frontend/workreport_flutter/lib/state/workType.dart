import 'dart:convert';

import 'package:WorkReport/api/api.dart';
import 'package:WorkReport/api/url.dart';
import 'package:WorkReport/module/type.dart';
import 'package:WorkReport/state/local_storage.dart';
import 'package:flutter/material.dart';
import 'package:flutter_smart_dialog/flutter_smart_dialog.dart';
import 'package:get/get.dart';

class WorkTypeController extends GetxController {
  final apiService = ApiService();
  final RxList<Typ> type1List = <Typ>[].obs;
  final LocalStorage _localStorage = Get.find<LocalStorage>();
  final RxList<Typ> type2List = <Typ>[].obs;

  final RxMap<int, List<Typ>> type2ListMap = <int, List<Typ>>{}.obs;

  WorkTypeController() {
    _init();
  }

  _init() async {
    fetchTypeList();
  }

  Future<List<Typ>> fetchTypeList() async {
    if (type1List.isNotEmpty) {
      return type1List;
    }
    // _localStorage.removeData("type1List");
    /// 从缓存中获取
    String l = _localStorage.getData("type1List");
    if (l.isEmpty) {
      final response = await apiService.get(
        Url.workType1,
      );
      response.flag
          ? type1List.value = Typ.fromJsonList(response.data["type_list"])
          : null;
      _localStorage.saveData(
          "type1List", jsonEncode(response.data["type_list"]));
      return type1List;
    }

    List<dynamic> dynamicList = json.decode(l);
    List<Typ> list =
        List<Typ>.from(dynamicList.map((item) => Typ.fromJson(item)));

    type1List.value = list;

    update();
    return type1List;
  }

  Future getType2ByPid(int pid) async {
    /// 先从Map中看看有没有
    List<Typ>? list = type2ListMap[pid];
    if (list != null) {
      type2List.value = list;
      update();
      return;
    }

    /// 没有从缓存中看看有没有
    Map<int, List<Typ>> m = _localStorage.loadType2ListMap();
    if (m.isNotEmpty) {
      List<Typ>? tl = m[pid];

      if (tl != null) {
        type2ListMap[pid] = tl;
        type2List.value = tl;
        update();
        return;
      }
    }
    final response = await apiService.get(
      Url.workType2,
      params: {"pid": pid},
    );

    if (!response.flag) {
      SmartDialog.showToast(
        response.msg,
        alignment: Alignment.topCenter,
      );
      return;
    }

    List<Typ> data = Typ.fromJsonList(response.data["type_list"]);
    type2ListMap[pid] = Typ.fromJsonList(response.data["type_list"]);
    type2List.value = data;

    customEncoder(dynamic item) {
      if (item is List<Typ>) {
        return item.map((e) => e.toJson()).toList();
      }
      return item;
    }

    // 将 type2ListMap 转换为可序列化的对象
    Map<String, dynamic> serializableMap = type2ListMap.map(
      (key, value) => MapEntry(key.toString(), customEncoder(value)),
    );

    _localStorage.saveData("type2ListMap", jsonEncode(serializableMap));
    update();
  }

  Future<Typ?> typeByName(String name) async {
    if (type1List.value.isEmpty) {
      await fetchTypeList();
    }
    for (var element in type1List.value) {
      if (element.description == name) {
        return element;
      }
    }
  }

  Future createWorkType1(String ty) async {
    Typ data = Typ(description: ty, pid: 0, type: 1);
    final response = await apiService.post(
      Url.workType,
      data: data.toJson(),
    );

    if (!response.flag) {
      SmartDialog.showToast(
        response.msg,
        alignment: Alignment.topCenter,
      );
      return;
    }
    SmartDialog.showToast(
      response.msg,
      alignment: Alignment.topCenter,
    );
  }

  Future createWorkType2(String desc, int pid) async {
    Typ data = Typ(description: desc, pid: pid, type: 2);
    final response = await apiService.post(
      Url.workType,
      data: data.toJson(),
    );

    if (!response.flag) {
      SmartDialog.showToast(
        response.msg,
        alignment: Alignment.topCenter,
      );
      return;
    }
    SmartDialog.showToast(
      response.msg,
      alignment: Alignment.topCenter,
    );
  }
}
