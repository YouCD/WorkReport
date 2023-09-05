import 'package:WorkReport/common/color.dart';
import 'package:WorkReport/state/statistics.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class PieChartWidget extends StatefulWidget {
  const PieChartWidget({super.key});

  @override
  State<StatefulWidget> createState() => _PieChartWidgetState();
}

class _PieChartWidgetState extends State {
  int touchedIndex = -1;
  final StatisticsController _statisticsController =
      Get.find<StatisticsController>();

  @override
  Widget build(BuildContext context) {
    return AspectRatio(
      aspectRatio: 1.3,
      child: AspectRatio(
        aspectRatio: 1,
        child: Obx(() => PieChart(
              PieChartData(
                pieTouchData: PieTouchData(
                  touchCallback: (FlTouchEvent event, pieTouchResponse) {
                    setState(() {
                      if (!event.isInterestedForInteractions ||
                          pieTouchResponse == null ||
                          pieTouchResponse.touchedSection == null) {
                        touchedIndex = -1;
                        return;
                      }
                      touchedIndex =
                          pieTouchResponse.touchedSection!.touchedSectionIndex;
                    });
                  },
                ),
                borderData: FlBorderData(
                  show: false,
                ),
                sectionsSpace: 0,
                centerSpaceRadius: 0,
                sections: showingSections(),
              ),
            )),
      ),
    );
  }

  List<PieChartSectionData> showingSections() {
    int totalCount = 0;
    for (var element in _statisticsController.countType1Data.value) {
      totalCount += element.count;
    }
    return List.generate(_statisticsController.countType1Data.length, (i) {
      final isTouched = i == touchedIndex;
      final fontSize = isTouched ? 20.0 : 16.0;
      final radius = isTouched ? 110.0 : 100.0;
      final widgetSize = isTouched ? 50.0 : 40.0;
      const shadows = [Shadow(color: Colors.black, blurRadius: 2)];
      percentage() {
        return (_statisticsController.countType1Data[i].count /
                totalCount *
                100)
            .truncate();
      }

      return PieChartSectionData(
        color: colorByType(_statisticsController.countType1Data[i].type1),
        value: _statisticsController.countType1Data[i].count.toDouble(),
        showTitle: percentage() > 20 ? true : false,
        title: percentage() > 20
            ? "${_statisticsController.countType1Data[i].type1}: ${_statisticsController.countType1Data[i].count}"
            : null,
        radius: radius,
        titleStyle: TextStyle(
          fontSize: fontSize,
          fontWeight: FontWeight.bold,
          color: const Color(0xffffffff),
          shadows: shadows,
        ),
        badgeWidget: isTouched
            ? _Badge(
                size: widgetSize,
                borderColor: Colors.black,
                percentage: percentage(),
                typ: _statisticsController.countType1Data[i].type1,
              )
            : null,
        badgePositionPercentageOffset: .98,
      );
    });
  }
}

class _Badge extends StatelessWidget {
  const _Badge({
    required this.size,
    required this.borderColor,
    required this.percentage,
    required this.typ,
  });

  final double size;
  final Color borderColor;
  final int percentage;
  final String typ;

  @override
  Widget build(BuildContext context) {
    return AnimatedContainer(
        alignment: Alignment.center,
        duration: PieChart.defaultDuration,
        width: size,
        height: size,
        decoration: BoxDecoration(
          color: Colors.white,
          shape: BoxShape.circle,
          border: Border.all(
            color: borderColor,
            width: 2,
          ),
          boxShadow: <BoxShadow>[
            BoxShadow(
              color: Colors.black.withOpacity(.5),
              offset: const Offset(3, 3),
              blurRadius: 3,
            ),
          ],
        ),
        padding: EdgeInsets.all(size * .15),
        child: Text(
          "$typ $percentage%",
          style: const TextStyle(fontSize: 12),
        ));
  }
}
