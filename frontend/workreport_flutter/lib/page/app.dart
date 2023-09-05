import 'package:WorkReport/router/router.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_smart_dialog/flutter_smart_dialog.dart';
import 'package:get/get.dart';

class App extends StatefulWidget {
  const App({super.key});

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  @override
  Widget build(BuildContext context) {
    return ScreenUtilInit(
      designSize: const Size(360, 640),
      minTextAdapt: true,
      splitScreenMode: true,
      builder: (context, child) {
        return GetMaterialApp(
          // home: const Welcome(),
          getPages: routers,
          initialRoute: Routes.welcome,
          builder: FlutterSmartDialog.init(),
          // 本地化
          localizationsDelegates: const [
            GlobalMaterialLocalizations.delegate,
            //是Flutter的一个本地化委托，用于提供Material组件库的本地化支持
            GlobalWidgetsLocalizations.delegate,
            //用于提供通用部件（Widgets）的本地化支持
            GlobalCupertinoLocalizations.delegate,
            //用于提供Cupertino风格的组件的本地化支持
          ],
          supportedLocales: const [
            Locale('zh', 'CN'), // 支持的语言和地区
          ],
        );
      },
    );

    return GetMaterialApp(
      // home: const Welcome(),
      getPages: routers,
      initialRoute: Routes.welcome,
      builder: FlutterSmartDialog.init(),
    );
  }
}
