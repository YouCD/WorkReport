import 'package:WorkReport/module/statistics.dart';
import 'package:WorkReport/module/type.dart';
import 'package:WorkReport/page/home/menu.dart';
import 'package:WorkReport/state/statistics.dart';
import 'package:WorkReport/state/workType.dart';
import 'package:WorkReport/widget/app_bar.dart';
import 'package:WorkReport/widget/barChart.dart';
import 'package:WorkReport/widget/box.dart';
import 'package:WorkReport/widget/pieChart.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Statistics extends StatelessWidget {
  final StatisticsController _statisticsController =
      Get.find<StatisticsController>();
  final WorkTypeController _workTypeController = Get.find<WorkTypeController>();

  Statistics({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: Menu(),
      body: CustomScrollView(
        slivers: [
          AppBarWidget(title: '报表'),
          SliverToBoxAdapter(
              child: Column(
            children: [
              /// 饼图
              const BoxWidget(
                content: PieChartWidget(),
              ),

              /// 柱状图
              BoxWidget(
                content: Obx(() => BarChartWidget(
                      data: _statisticsController.countType1Data.value,
                      titleField: "type1",
                      countField: "count",
                      callback: countType1Callback,
                      barWidth: 15,
                    )),
                height: 200,
              ),

              /// 柱状图
              BoxWidget(
                content: Obx(() => BarChartWidget(
                      data: _statisticsController.countType2Data.value,
                      titleField: "type2",
                      countField: "count",
                      useRandomColor: true,
                      sideTitleAngle: true,
                      sideTitlesReservedSize: 50,
                      barWidth: 10,
                      interval: 3,
                    )),
                height: 200,
              ),
            ],
          )),
        ],
      ),
    );
  }

  countType1Callback(BarTouchResponse barTouchResponse) async {
    int index = barTouchResponse.spot!.touchedBarGroupIndex;
    Type1Count item = _statisticsController.countType1Data.value[index];

    Typ? t = await _workTypeController.typeByName(item.type1);

    _statisticsController.fetchType2CountData(t!.id!);
  }
}
