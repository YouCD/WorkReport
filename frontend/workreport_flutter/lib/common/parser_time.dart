import 'package:intl/intl.dart';

String parseUnixTime2Date(int unixTimestamp) {
  var dateTime = DateTime.fromMillisecondsSinceEpoch(unixTimestamp * 1000);
  final formatter = DateFormat('yyyy-MM-dd');
  return formatter.format(dateTime);
}

String parseUnixTime2LogViewTIme(int unixTimestamp) {
  var dateTime = DateTime.fromMillisecondsSinceEpoch(unixTimestamp * 1000);
  return DateFormat.MMMd().format(dateTime);
}

List<String> parseUnixTime2LogViewTImeList(int unixTimestamp) {
  var dateTime = DateTime.fromMillisecondsSinceEpoch(unixTimestamp * 1000);
  final formatter = DateFormat('E M-d', 'zh_CN');
  String dateSrt = formatter.format(dateTime);
  String week = dateSrt.split(" ")[0].replaceAll("周", "");
  String date = dateSrt.split(" ")[1];

  return List<String>.from([week, date]);
}

String formatDate(DateTime dateTime) {
  final formatter = DateFormat('yyyy-MM-dd');
  return formatter.format(dateTime);
}

int parseDate2UnixTime(String date) {
  final formatter = DateFormat('yyyy-MM-dd');
  return formatter.parse(date).millisecondsSinceEpoch ~/ 1000;
}
