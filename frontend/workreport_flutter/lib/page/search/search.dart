import 'package:WorkReport/page/home/menu.dart';
import 'package:WorkReport/state/workLog.dart';
import 'package:WorkReport/widget/app_bar.dart';
import 'package:WorkReport/widget/box.dart';
import 'package:WorkReport/widget/work_log_listview_builder.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class SearchPage extends StatelessWidget {
  SearchPage({super.key});

  final WorkLogController _workLogController = Get.find<WorkLogController>();

  final TextEditingController _searchController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        drawer: Menu(),
        body: CustomScrollView(slivers: [
          AppBarWidget(title: '搜索日志'),
          SliverToBoxAdapter(
            child: BoxWidget(
              height: 600,
              content: WorkLogWidget(),
            ),
          )
        ]));
  }

  void submit() {
    if (_searchController.text!.isEmpty) {
      return;
    }
    _workLogController.search(_searchController.text);
  }
}
