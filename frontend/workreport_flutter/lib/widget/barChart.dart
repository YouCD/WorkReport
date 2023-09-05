import 'package:WorkReport/common/color.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';

class BarChartWidget extends StatefulWidget {
  List data = [];
  String titleField = "";
  String countField = "";
  bool useRandomColor = false;
  bool sideTitleAngle;
  int barWidth;

  int sideTitlesReservedSize;

  final Function? callback;

  final num interval;

  final double fontSize;

  BarChartWidget({
    Key? key,
    required this.data,
    required this.titleField,
    required this.countField,
    this.callback,
    this.useRandomColor = false,
    this.barWidth = 15,
    this.sideTitleAngle = false,
    this.sideTitlesReservedSize = 0,
    this.interval = 1,
    this.fontSize = 10,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _BarChartState();
  }
}

class _BarChartState extends State<BarChartWidget> {
  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 300,
      child: BarChart(
        BarChartData(
          // 栅格
          gridData: FlGridData(show: false),
          borderData: FlBorderData(
              border: const Border(
            top: BorderSide.none,
            right: BorderSide.none,
            left: BorderSide.none,
            bottom: BorderSide(width: 1, color: Colors.grey),
          )),
          groupsSpace: 10,
          titlesData: FlTitlesData(
              show: true,
              topTitles: AxisTitles(
                sideTitles: SideTitles(showTitles: false),
              ),
              leftTitles: AxisTitles(
                sideTitles: SideTitles(showTitles: false),
              ),
              rightTitles: AxisTitles(
                sideTitles: SideTitles(showTitles: false),
              ),
              bottomTitles: AxisTitles(
                // 高度
                axisNameSize: 20,

                // axisNameWidget: Container(
                //   child: Text(
                //     "单位：${widget.titleField}",
                //     style: TextStyle(
                //         color: Colors.blueGrey[700],
                //         fontWeight: FontWeight.bold,
                //         fontSize: 14),
                //   ),
                // ),
                sideTitles: SideTitles(
                  showTitles: true,
                  getTitlesWidget: getBottomTitles,

                  /// 高度
                  reservedSize: widget.sideTitlesReservedSize != 0
                      ? widget.sideTitlesReservedSize.toDouble()
                      : null,
                ),
              )),
          barTouchData: BarTouchData(
            enabled: true,
            touchCallback: (FlTouchEvent event, barTouchResponse) {
              if (barTouchResponse?.spot != null && widget.callback != null) {
                widget.callback!(barTouchResponse);
              }
            },
            // touchTooltipData: BarTouchTooltipData(
            //   tooltipBgColor: Colors.transparent,
            //   tooltipPadding: const EdgeInsets.all(0),
            //   tooltipMargin: 8,
            //   getTooltipItem: (
            //       BarChartGroupData group,
            //       int groupIndex,
            //       BarChartRodData rod,
            //       int rodIndex,
            //       ) {
            //     return BarTooltipItem(
            //       rod.y.round().toString(),
            //       TextStyle(
            //         color: Colors.black,
            //         fontWeight: FontWeight.bold,
            //       ),
            //     );
            //   },
            // ),
          ),

          barGroups: buildBarChartGroupData(),
        ),
      ),
    );
  }

  Widget getBottomTitles(double value, TitleMeta meta) {
    TextStyle style = TextStyle(
        color: Colors.blueGrey[700], fontWeight: FontWeight.bold, fontSize: 14);

    Widget text = Container();
    for (var index = 0; index < widget.data.length; index++) {
      if (index == value.toInt()) {
        // 通过 求余数 interval 控制间隔 如果数据量少于10条 则不控制间隔
        if (index % widget.interval == 0 || widget.data.length < 10) {
          style = TextStyle(
            color: Colors.blueGrey[700],
            fontWeight: FontWeight.bold,
            fontSize: widget.fontSize,
          );
        } else {
          style = TextStyle(
              color: Colors.blueGrey[700],
              fontWeight: FontWeight.bold,
              fontSize: 0);
        }

        text = Text(
          widget.data[index].toJson()[widget.titleField],
          style: style,
        );
        break;
      }
    }

    return SideTitleWidget(
      space: 5,
      axisSide: meta.axisSide,
      // 旋转
      angle: widget.sideTitleAngle ? 1 : 0,
      child: text,
    );
  }

  List<BarChartGroupData> buildBarChartGroupData() {
    List<BarChartGroupData> data = [];

    for (var index = 0; index < widget.data.length; index++) {
      data.add(BarChartGroupData(x: index, barRods: [
        BarChartRodData(
            fromY: 0,
            toY: widget.data[index].toJson()[widget.countField].toDouble(),
            width: widget.barWidth.toDouble(),
            color: widget.useRandomColor
                ? randomColor()
                : colorByType(widget.data[index].toJson()[widget.titleField])),
      ]));
    }
    return data;
  }
}
