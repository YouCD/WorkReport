import 'package:WorkReport/api/api.dart';
import 'package:WorkReport/api/url.dart';
import 'package:WorkReport/common/loading.dart';
import 'package:WorkReport/module/statistics.dart';
import 'package:get/get.dart';

class StatisticsController extends GetxController {
  final apiService = ApiService();
  final RxList<Type1Count> countType1Data = <Type1Count>[].obs;
  final RxList<Type2Count> countType2Data = <Type2Count>[].obs;

  void fetchType1CountData() async {
    showLoading(true);

    final resp = await apiService.get(
      Url.type1Count,
    );
    resp.flag
        ? countType1Data.value =
            Type1Count.fromJsonList(resp.data["countType1Data"])
        : null;
    showLoading(resp.flag ? false : true);
  }

  void fetchType2CountData(int id) async {
    final response = await apiService.get(
      Url.type2Count,
      params: {"id": id},
    );
    response.flag
        ? countType2Data.value =
            Type2Count.fromJsonList(response.data["countType2Data"])
        : null;
  }
}
