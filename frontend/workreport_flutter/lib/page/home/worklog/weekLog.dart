import 'package:WorkReport/state/workLog.dart';
import 'package:WorkReport/widget/box.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class WeekLog extends StatelessWidget {
  final WorkLogController _workLogController = Get.find<WorkLogController>();
  final List<Widget> widgets = <Widget>[];
  WeekLog({super.key}){
    _workLogController.workLogFromWeekData.forEach((key, value) {
      widgets.add(Align(
        alignment: Alignment.centerLeft,
        child:  Text(key+"："),
      ));
      for (var element in value) {
        widgets.add(Container(alignment:Alignment.centerLeft,padding: const EdgeInsets.only(left: 20),child: Text(element)));
      }
      widgets.add(const Divider());

    });
  }

  @override
  Widget build(BuildContext context) {


    return  SizedBox(width: double.infinity,child: SingleChildScrollView(child: Column(children: widgets,),),);
  }
}
