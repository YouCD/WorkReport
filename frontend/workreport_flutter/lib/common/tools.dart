import 'package:WorkReport/common/log.dart';

Map<String, List<String>> convertToMapOfStringList<T>(dynamic data) {
  if (data is Map<String, dynamic>) {
    Map<String, List<String>> result = {};

    data.forEach((key, value) {
    final  contest= value.cast<String>();

      if (value is List<dynamic>) {
        result[key] = List<String>.from(contest);
      }
    });
    return result;
  } else {
    throw Exception('Invalid data format');
  }
}