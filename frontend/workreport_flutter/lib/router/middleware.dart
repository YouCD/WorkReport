import 'package:WorkReport/router/router.dart';
import 'package:WorkReport/state/local_storage.dart';
import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';

class JwtMiddleware extends GetMiddleware {
  final LocalStorage _localStorage = Get.find<LocalStorage>();

  @override
  // 优先级越低越先执行
  int? get priority => -1;

  // 重定向，当正在搜索被调用路由的页面时，将调用该函数
  @override
  RouteSettings? redirect(String? route) {
    String jwt = _localStorage.getData(jwtName);
    if (jwt.isEmpty) {
      return RouteSettings(name: Routes.login);
    }
    if (_localStorage.jwtIsExpiration()) {
      return RouteSettings(name: Routes.login);
    }

    return null;
  }

  // 创建任何内容之前调用此函数
  @override
  GetPage? onPageCalled(GetPage? page) {
    return page;
  }

//这个函数将在绑定初始化之前被调用。在这里您可以更改此页面的绑定。
// @override
// List<Bindings>? onBindingsStart(List<Bindings>? bindings) {
//   print('onBindingsStart1----');
//   //return super.onBindingsStart(bindings);
//   // bindings?.add(LoginBinding());
//   return bindings;
// }

//此函数将在绑定初始化后立即调用。在这里，您可以在创建绑定之后和创建页面小部件之前执行一些操作
// @override
// GetPageBuilder? onPageBuildStart(GetPageBuilder? page) {
//   print('onPageBuildStart1----');
//   //return super.onPageBuildStart(page);
//   return page;
// }

//该函数将在调用 GetPage.page 函数后立即调用，并为您提供函数的结果。并获取将显示的小部件
// @override
// Widget onPageBuilt(Widget page) {
//   print('onPageBuilt1 ----');
//   //return super.onPageBuilt(page);
//   return page;
// }

//此函数将在处理完页面的所有相关对象（控制器、视图等）后立即调用
// @override
// void onPageDispose() {
//   print('onPageDispose1 ----');
//   super.onPageDispose();
// }
}

class MainMiddleware extends GetMiddleware {
  @override
  // 优先级越低越先执行
  int? get priority => -1;

  // 重定向，当正在搜索被调用路由的页面时，将调用该函数
  @override
  RouteSettings? redirect(String? route) {
    return RouteSettings(name: Routes.home);
  }
}

class WelcomeMiddleware extends GetMiddleware {
  @override
  // 优先级越低越先执行
  int? get priority => -1;

  // 重定向，当正在搜索被调用路由的页面时，将调用该函数
  @override
  RouteSettings? redirect(String? route) {
    final LocalStorage _localStorage = Get.find<LocalStorage>();

    /// 检查是否打开过，如果没有数据 代表第一次安装，需要简单浏览一下 欢迎页面
    String flag = _localStorage.getData("IsFirstOpen");
    if (flag.isEmpty) {
      return null;
    }

    /// 检查是否有 jwt ，如果有接着判断是否过期
    String jwt = _localStorage.getData(jwtName);
    if (jwt.isEmpty) {
      return RouteSettings(name: Routes.login);
    }
    if (_localStorage.jwtIsExpiration()) {
      return RouteSettings(name: Routes.login);
    }

    return RouteSettings(name: Routes.home);
  }
}
