import 'package:WorkReport/page/home/home.dart';
import 'package:WorkReport/page/home/worklog/workLogForm.dart';
import 'package:WorkReport/page/search/search.dart';
import 'package:WorkReport/page/setting/setting.dart';
import 'package:WorkReport/page/statistics/statistics.dart';
import 'package:WorkReport/page/welcome/login.dart';
import 'package:WorkReport/page/welcome/welcome.dart';
import 'package:WorkReport/router/middleware.dart';
import 'package:WorkReport/state/common.dart';
import 'package:WorkReport/state/workLog.dart';
import 'package:WorkReport/state/workType.dart';
import 'package:get/get.dart';

class Routes {
  static String welcome = "/welcome";
  static String login = "/login";
  static String home = "/home";
  static String statistics = "/statistics";
  static String searchPage = "/searchPage";
  static String workLogForm = "/workLogForm";
  static String setting = "/setting";
}

List<GetPage<dynamic>> routers = [
  GetPage(
    name: Routes.home,
    page: () => const Home(),
    middlewares: [JwtMiddleware()],
    transition: Transition.rightToLeftWithFade,
    transitionDuration: const Duration(milliseconds: 500),
    binding: BindingsBuilder(() {
      WorkLogController workLogController = Get.put(WorkLogController());
      workLogController.isFirstEnter.value = false;

      Get.lazyPut<CommonController>(() => CommonController());
      Get.lazyPut<WorkTypeController>(() => WorkTypeController());
    }),
  ),
  GetPage(
    name: Routes.login,
    page: () => Login(),
    transition: Transition.rightToLeftWithFade,
    transitionDuration: const Duration(milliseconds: 500),
  ),
  GetPage(
    name: Routes.statistics,
    page: () => Statistics(),
    transition: Transition.rightToLeftWithFade,
    transitionDuration: const Duration(milliseconds: 500),
  ),
  GetPage(
    name: Routes.searchPage,
    page: () => SearchPage(),
    transition: Transition.rightToLeftWithFade,
    transitionDuration: const Duration(milliseconds: 500),
  ),
  GetPage(
    name: Routes.workLogForm,
    page: () => const WorkLogFormPage(),
    transition: Transition.rightToLeftWithFade,
    transitionDuration: const Duration(milliseconds: 500),
  ),
  GetPage(
    name: Routes.welcome,
    middlewares: [WelcomeMiddleware()],
    page: () => const Welcome(),
  ),
  GetPage(
    name: Routes.setting,
    // middlewares: [WelcomeMiddleware()],
    page: () => Setting(),
  ),
];
