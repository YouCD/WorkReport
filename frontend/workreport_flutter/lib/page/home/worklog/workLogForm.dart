import 'package:WorkReport/page/home/menu.dart';
import 'package:WorkReport/widget/app_bar.dart';
import 'package:WorkReport/widget/work_log_form_page.dart';
import 'package:flutter/material.dart';

class WorkLogFormPage extends StatelessWidget {
  const WorkLogFormPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        drawer: Menu(),
        body: CustomScrollView(
          slivers: [
            AppBarWidget(title: '本周日志'),
            SliverToBoxAdapter(
              child: WorkLogFormPageWidget(),
            ),
          ],
        ));
  }
}
