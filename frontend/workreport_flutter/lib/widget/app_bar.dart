import 'package:WorkReport/router/router.dart';
import 'package:WorkReport/state/common.dart';
import 'package:WorkReport/state/workLog.dart';
import 'package:animated_search_bar/animated_search_bar.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class AppBarWidget extends StatelessWidget implements PreferredSizeWidget {
  final String title;
  final bool showSearch;
  final WorkLogController _workLogController = Get.put(WorkLogController());
  final CommonController _commonController = Get.find<CommonController>();

  AppBarWidget({
    Key? key,
    required this.title,
    this.showSearch = true,
  }) : super(key: key);

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
  final TextEditingController _searchController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return SliverAppBar(
      title: AnimatedSearchBar(
        label: title,
        controller: _searchController,
        labelStyle: const TextStyle(fontSize: 16),
        searchStyle: const TextStyle(color: Colors.white),
        cursorColor: Colors.white,
        textInputAction: TextInputAction.done,
        searchDecoration: const InputDecoration(
          hintText: "输入内容",
          alignLabelWithHint: true,
          fillColor: Colors.white,
          focusColor: Colors.white,
          hintStyle: TextStyle(color: Colors.white70),
          border: InputBorder.none,
        ),
        onChanged: (value) {
          // Get.toNamed(AppRoutes.searchPage);
          // if (value.isEmpty) {
          //   _commonController.isSearchPage.value = false;
          // }
        },
        onFieldSubmitted: (value) {
          _commonController.isSearchPage.value = true;
          submit(value);
          Get.toNamed(Routes.searchPage);
        },
      ),
      floating: true,
      snap: true,
    );
  }

  void submit(String value) {
    if (value.isEmpty) {
      return;
    }
    _workLogController.search(value);
  }
}
