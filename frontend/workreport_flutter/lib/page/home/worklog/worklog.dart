import 'package:WorkReport/page/home/worklog/weekLog.dart';
import 'package:WorkReport/router/router.dart';
import 'package:WorkReport/state/workLog.dart';
import 'package:WorkReport/state/workType.dart';
import 'package:WorkReport/widget/box.dart';
import 'package:WorkReport/widget/button.dart';
import 'package:WorkReport/widget/calendar.dart';
import 'package:WorkReport/widget/work_log_listview_builder.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class WorkLogPage extends StatelessWidget {
  WorkLogPage({super.key});

  final WorkLogController _workLogController = Get.find<WorkLogController>();
  final WorkTypeController _workTypeController = Get.find<WorkTypeController>();

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        children: [
          BoxWidget(content: Calendar()),
          SizedBox(
            height: 700,
            child: PageView(
              onPageChanged: (index) {
                if (index == 1) {
                  _workLogController.workLogFromWeek();
                }
              },
              children: [
                Column(
                  children: [
                    Button("添加", onTap: () {
                      _workTypeController.fetchTypeList();
                      _workLogController.isEdit.value = false;
                      Get.toNamed(Routes.workLogForm);
                    }, icon: Icons.add),
                    BoxWidget(
                      height: 600,
                      content: WorkLogWidget(),
                    ),
                  ],
                ),
                Column(
                  children: [
                    const SizedBox(
                      height: 35,
                      child:  Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(Icons.calendar_month),
                          Text("本周日志"),
                        ],
                      ),
                    ),
                    BoxWidget(height: 600,content: Obx(() => WeekLog())),
                  ],
                )
              ],
            ),
          ),
        ],
      ),
    );
  }
}
