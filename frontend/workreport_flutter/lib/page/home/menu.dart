import 'package:WorkReport/router/router.dart';
import 'package:WorkReport/state/common.dart';
import 'package:WorkReport/state/statistics.dart';
import 'package:WorkReport/state/workType.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Menu extends StatelessWidget {
  final StatisticsController _statisticsController =
      Get.put(StatisticsController());
  final WorkTypeController _workTypeController = Get.put(WorkTypeController());
  final CommonController _commonController = Get.find<CommonController>();

  Menu({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          const Row(
            children: <Widget>[
              Expanded(
                  child: UserAccountsDrawerHeader(
                accountName:
                    Text('YouCD', style: TextStyle(color: Colors.white)),
                accountEmail: Text('hnyoucd@gmailcom',
                    style: TextStyle(color: Colors.white)),
                currentAccountPicture: CircleAvatar(
                  //圆形图片
                  backgroundImage: AssetImage("assets/images/jenkins.png"), //头像
                ),
                decoration: BoxDecoration(
                  //顶部背景颜色或者图片
                  image: DecorationImage(
                      image: AssetImage("assets/images/devops.png"),
                      // image: AssetImage("assets/images/jenkins.png"),
                      fit: BoxFit.cover),
                ),
                // otherAccountsPictures: <Widget>[//当前用户其他账号的头像
                //   //http://5b0988e595225.cdn.sohucs.com/images/20171108/d29a69f9c7fc41d1aae4516df8ebfac3.jpeg
                //   Image.network('http://5b0988e595225.cdn.sohucs.com/images/20171108/d29a69f9c7fc41d1aae4516df8ebfac3.jpeg'),
                // ],
              ))
            ],
          ),
          Column(
            children: [
              InkWell(
                onTap: () {
                  _statisticsController.fetchType1CountData();
                  Get.toNamed(Routes.statistics);
                  _workTypeController.fetchTypeList();

                  _statisticsController.fetchType2CountData(1);
                },
                child: const ListTile(
                  leading: CircleAvatar(
                    child: Icon(Icons.analytics),
                  ),
                  title: Text('图表'),
                ),
              ),

              const Divider(), //横线

              InkWell(
                onTap: () {
                  // 进入主页
                  _commonController.isSearchPage.value = false;

                  Get.toNamed(Routes.home);
                },
                child: const ListTile(
                  leading: CircleAvatar(
                    child: Icon(Icons.description),
                  ),
                  title: Text('日志浏览'),
                ),
              ),

              const Divider(),
              InkWell(
                onTap: () {
                  _statisticsController.fetchType1CountData();
                  Get.toNamed(Routes.setting);
                },
                child: const ListTile(
                  leading: CircleAvatar(
                    child: Icon(Icons.settings),
                  ),
                  title: Text('字典设置'),
                ),
              ),

              const Divider(),
            ],
          ),
        ],
      ),
    );
  }
}
