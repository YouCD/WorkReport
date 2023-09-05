import 'dart:convert';

import 'package:WorkReport/api/api.dart';
import 'package:WorkReport/api/url.dart';
import 'package:WorkReport/common/loading.dart';
import 'package:WorkReport/common/log.dart';
import 'package:WorkReport/common/tools.dart';
import 'package:WorkReport/module/work_log.dart';
import 'package:flutter/material.dart';
import 'package:flutter_smart_dialog/flutter_smart_dialog.dart';
import 'package:get/get.dart';

class WorkLogController extends GetxController {
  final apiService = ApiService();
  final RxList<WorkLog> workLogList = <WorkLog>[].obs;
  final RxList<WorkLog> searchWorkLogList = <WorkLog>[].obs;
  final pageSize = 10.obs;
  final Rx<WorkLogItem> addData = Rx<WorkLogItem>(WorkLogItem());
  final Rx<WorkLogItem> editWorkLogData = Rx<WorkLogItem>(WorkLogItem());
  final RxBool isEdit = false.obs;
  final RxBool isFirstEnter = true.obs;
  RxInt currentPage = 1.obs;
  RxInt totalPages = 1.obs;
  final defaultPageSize = 12;

  final RxMap<String, List<String>> workLogFromWeekData =
      <String, List<String>>{}.obs;

  @override
  void onInit() {
    super.onInit();
    workLogList.clear();
    fetchWorkLog(1, defaultPageSize);

    ever(workLogList, (_) => update(["workLogList"])); // 添加监听器并指定要监听的字段
  }

  fetchWorkLog(int index, int size) async {
    showLoading(true);
    pageSize.value = size;
    final resp = await apiService
        .get(Url.workLog, params: {"pageIndex": index, "pageSize": pageSize});
    workLogList
        .addAll(WorkLog.fromJsonList(resp.data["work_content_resp_list"]));

    int sumNumber = resp.data["sum"];

    /// 通过 sumNumber 和 pageSize 计算总页数
    totalPages.value = sumNumber ~/ index + 1;

    showLoading(resp.flag ? false : true);
    isFirstEnter.value = false;
  }

  void deleteWorkLog(int id) async {
    final resp = await apiService.del(Url.workLog, params: {"id": id});
    resp.flag
        ? SmartDialog.showToast(
            resp.msg,
            alignment: Alignment.topCenter,
          )
        : SmartDialog.showToast(
            resp.msg,
            alignment: Alignment.topCenter,
          );
  }

  void addWorkLog() async {
    final resp =
        await apiService.post(Url.workLog, data: addData.value.toJson());
    resp.flag
        ? SmartDialog.showToast(
            resp.msg,
            alignment: Alignment.topCenter,
          )
        : SmartDialog.showToast(
            resp.msg,
            alignment: Alignment.topCenter,
          );
  }

  Future<bool> editWorkLog() async {
    var resp =
        await apiService.put(Url.workLog, data: editWorkLogData.toJson());
    return resp.flag;
  }

  search(String text) async {
    showLoading(true);
    final resp =
        await apiService.get(Url.workLogFromContent, params: {"content": text});
    if (resp.data["work_content_resp_list"] == null) {
      //  提示
      SmartDialog.showToast(
        "未能搜索到相关日志",
        alignment: Alignment.topCenter,
      );
    }
    showLoading(resp.flag ? false : true);

    searchWorkLogList.value =
        WorkLog.fromJsonList(resp.data["work_content_resp_list"]);
  }

  getWorkLogFromDate(int date) async {
    logger.i(date);
    showLoading(true);
    final resp =
        await apiService.get(Url.workLogFromDate, params: {"date": date});
    if (resp.data["work_content_resp_list"] == null) {
      SmartDialog.showToast(
        "未能搜索到相关日志",
        alignment: Alignment.topCenter,
      );
    }
    showLoading(resp.flag ? false : true);

    workLogList.value =
        WorkLog.fromJsonList(resp.data["work_content_resp_list"]);
  }

 workLogFromWeek() async {
    showLoading(true);
    final resp = await apiService.get(
      Url.workLogFromWeek,
    );
    if (resp.data == null) {
      SmartDialog.showToast(
        "未能获取到相关日志",
        alignment: Alignment.topCenter,
      );
    }
    showLoading(resp.flag ? false : true);
    Map<String, dynamic> responseData = json.decode(jsonEncode(resp.data));
    Map<String, List<String>> resultMap =
        convertToMapOfStringList(responseData);
    workLogFromWeekData.value = resultMap;
  }
}
