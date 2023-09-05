import 'package:WorkReport/common/color.dart';
import 'package:WorkReport/state/workLog.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:table_calendar/table_calendar.dart';

class Calendar extends StatefulWidget {
  @override
  _CalendarState createState() => _CalendarState();
}

class _CalendarState extends State<Calendar> {
  CalendarFormat _calendarFormat = CalendarFormat.week;
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;
  var date = DateTime.now();
  final WorkLogController _workLogController = Get.find<WorkLogController>();

  @override
  Widget build(BuildContext context) {
    return TableCalendar(
      // 最大开始日期，一年前
      firstDay: DateTime(date.year, date.month - 12, 1).toLocal(),
      // 最小开始日期
      lastDay: DateTime(date.year, date.month + 1, 0).toLocal(),
      focusedDay: _focusedDay,
      // 设置为周一为每周的开始时间
      startingDayOfWeek: StartingDayOfWeek.monday,
      // 设置日历的显示格式，默认进入首屏是 以周为显示单位
      calendarFormat: _calendarFormat,
      locale: 'zh_CN',
      // 设置头部属性显示和隐藏
      headerStyle: const HeaderStyle(
        titleCentered: true,
        // leftChevronVisible: false,
        // rightChevronVisible: false,
        formatButtonVisible: false,
      ),

      // 设置日历的显示属性
      // headerVisible: false,
      // 设置日历的下拉和上拉的显示的格式
      availableCalendarFormats: const {
        CalendarFormat.month: 'Month',
        CalendarFormat.week: 'Week',
      },
      calendarStyle: const CalendarStyle(
        // holidayTextStyle: TextStyle(color: Colors.red),
        // holidayDecoration: BoxDecoration(
        //     color: Colors.transparent, shape: BoxShape.circle),
        // Use `CalendarStyle` to customize the UI
        // outsideDaysVisible: true,
        // outsideDecoration: BoxDecoration(
        //   color: Colors.cyan,
        // ),
        // markersMaxCount: 1,
        // outsideDecoration: ,
        // markerSize: 10,
        // markersAlignment: Alignment.bottomCenter,
        // markerMargin: EdgeInsets.only(top: 8),
        // cellMargin: EdgeInsets.symmetric(horizontal: 10, vertical: 10),
        todayDecoration: BoxDecoration(
          color: Colors.red,
          shape: BoxShape.circle,
        ),

        // markerSizeScale: 5,
        markerDecoration:
            BoxDecoration(color: Colors.cyan, shape: BoxShape.circle),
        canMarkersOverflow: true,
        selectedDecoration:
            BoxDecoration(color: daddyLabGreen, shape: BoxShape.circle),
      ),

      selectedDayPredicate: (day) {
        return isSameDay(_selectedDay, day);
      },
      onDaySelected: (selectedDay, focusedDay) {
        if (!isSameDay(_selectedDay, selectedDay)) {
          /// 减去8小时的时间差
          int unixTime = selectedDay.millisecondsSinceEpoch ~/ 1000;
          _workLogController.getWorkLogFromDate(unixTime - 28800);
          setState(() {
            _selectedDay = selectedDay;
            _focusedDay = focusedDay;
          });
        }
      },
      onFormatChanged: (format) {
        if (_calendarFormat != format) {
          // Call `setState()` when updating calendar format
          setState(() {
            _calendarFormat = format;
          });
        }
      },
      onPageChanged: (focusedDay) {
        // No need to call `setState()` here
        _focusedDay = focusedDay;
      },
    );
  }
}
