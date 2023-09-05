import 'package:WorkReport/common/parser_time.dart';
import 'package:WorkReport/module/work_log.dart';
import 'package:WorkReport/page/home/worklog/workLogForm.dart';
import 'package:WorkReport/state/common.dart';
import 'package:WorkReport/state/workLog.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_slidable/flutter_slidable.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

class WorkLogWidget extends StatelessWidget {
  final WorkLogController _workLogController = Get.find<WorkLogController>();
  final CommonController _commonController = Get.find<CommonController>();

  WorkLogWidget({
    Key? key,
  }) : super(key: key);
  final RefreshController _refreshController =
      RefreshController(initialRefresh: false);

  @override
  Widget build(BuildContext context) {
    return Obx(() => SmartRefresher(
        enablePullDown: true,
        enablePullUp: true,
        header: const WaterDropHeader(),
        footer: CustomFooter(
          builder: (BuildContext context, LoadStatus? mode) {
            Widget body;
            if (mode == LoadStatus.idle) {
              body = const Text("上拉加载");
            } else if (mode == LoadStatus.loading) {
              body = const CupertinoActivityIndicator();
            } else if (mode == LoadStatus.failed) {
              body = const Text("加载失败！点击重试！");
            } else if (mode == LoadStatus.canLoading) {
              body = const Text("松手,加载更多!");
            } else {
              body = const Text("没有更多数据了!");
            }
            return SizedBox(
              height: 55.0,
              child: Center(child: body),
            );
          },
        ),
        controller: _refreshController,
        //刷新回调方法
        onRefresh: _onRefresh,
        //加载下一页回调

        onLoading: _onLoading,
        child: ListView.builder(
          shrinkWrap: true,
          itemCount: _commonController.isSearchPage.value
              ? _workLogController.searchWorkLogList.length
              : _workLogController.workLogList.length,
          itemBuilder: (context, index) {
            final WorkLog item;
            _commonController.isSearchPage.value
                ? item = _workLogController.searchWorkLogList[index]
                : item = _workLogController.workLogList[index];
            return Slidable(
              key: Key(index.toString()),
              startActionPane: ActionPane(
                motion: const ScrollMotion(),
                dismissible: DismissiblePane(onDismissed: () {
                  _workLogController.deleteWorkLog(item.id!);
                }),
                children: [
                  SlidableAction(
                    onPressed: (ctx) {
                      // 删除
                      _workLogController.deleteWorkLog(item.id!);
                    },
                    backgroundColor: const Color(0xFFFE4A49),
                    foregroundColor: Colors.white,
                    icon: Icons.delete,
                    label: '删除',
                  ),
                  SlidableAction(
                    onPressed: (ctx) {
                      Get.to(() => const WorkLogFormPage());
                      //   编辑
                      _workLogController.isEdit.value = true;
                      _workLogController.editWorkLogData.value =
                          item.toWorkLogItem();
                    },
                    backgroundColor: const Color(0xFF21B7CA),
                    foregroundColor: Colors.white,
                    icon: Icons.edit,
                    label: '编辑',
                  ),
                ],
              ),
              child: Container(
                padding: const EdgeInsets.all(5),
                decoration: const BoxDecoration(
                  border: Border(
                    bottom: BorderSide(
                      width: 1,
                      color: Colors.black12,
                    ),
                  ),
                ),
                height: 50,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    /// 日期
                    buildDateExpanded(item),

                    /// 内容
                    buildContentExpanded(item),
                  ],
                ),
              ),
            );
          },
        )));
  }

  Expanded buildContentExpanded(WorkLog item) {
    return Expanded(
      flex: 1,
      child: Text(
        item.content!,
        overflow: TextOverflow.ellipsis,
        maxLines: 2,
      ),
    );
  }

  Expanded buildDateExpanded(WorkLog item) {
    return Expanded(
      flex: 0,
      child: Container(
        width: 75,
        padding: const EdgeInsets.only(right: 10),
        child: Row(children: [
          Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(
                Icons.watch_later,
                size: 16,
                color: Colors.black,
              ),
              Text(parseUnixTime2LogViewTIme(item.date!),
                  style: const TextStyle(
                    fontSize: 8,
                    color: Colors.black,
                  )),
            ],
          ),
          const SizedBox(
            width: 10,
          ),
          Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(item.type1,
                  style: const TextStyle(
                    fontSize: 8,
                    color: Colors.black,
                  )),
              Text(item.type2,
                  style: const TextStyle(
                    fontSize: 8,
                    color: Colors.black,
                  )),
            ],
          ),
        ]),
      ),
    );
  }

  void _onLoading() async {
    if (_commonController.isSearchPage.value) {
      _refreshController.loadNoData();
      return;
    }

    _workLogController.currentPage.value += 1;
    //  不能无限加载
    if (_workLogController.currentPage.value >
        _workLogController.totalPages.value) {
      _refreshController.loadNoData();
      return;
    }
    await _workLogController.fetchWorkLog(_workLogController.currentPage.value,
        _workLogController.defaultPageSize);
    _refreshController.loadComplete();
  }

  void _onRefresh() async {
    if (_commonController.isSearchPage.value) {
      _refreshController.refreshCompleted();
      return;
    }
    _workLogController.currentPage.value = 1;
    _workLogController.totalPages.value = 0;
    _workLogController.workLogList.value = [];
    await _workLogController.fetchWorkLog(_workLogController.currentPage.value,
        _workLogController.defaultPageSize);

    _refreshController.resetNoData();
    _refreshController.refreshCompleted();
  }
}
