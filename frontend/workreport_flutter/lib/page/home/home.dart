import 'package:WorkReport/page/home/menu.dart';
import 'package:WorkReport/page/home/worklog/worklog.dart';
import 'package:WorkReport/widget/app_bar.dart';
import 'package:flutter/material.dart';

class Home extends StatelessWidget {
  const Home({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        drawer: Menu(),
        body: CustomScrollView(
          slivers: [
            AppBarWidget(title: '日志浏览'),
            SliverToBoxAdapter(
              child: WorkLogPage(),
            ),
          ],
        ));
  }
}
