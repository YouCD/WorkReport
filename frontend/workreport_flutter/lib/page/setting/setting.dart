import 'package:WorkReport/common/log.dart';
import 'package:WorkReport/module/type.dart';
import 'package:WorkReport/page/home/menu.dart';
import 'package:WorkReport/state/workType.dart';
import 'package:WorkReport/widget/app_bar.dart';
import 'package:WorkReport/widget/box.dart';
import 'package:WorkReport/widget/button.dart';
import 'package:WorkReport/widget/work_log_form_page.dart';
import 'package:animated_tree_view/animated_tree_view.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Setting extends StatelessWidget {
  Setting({super.key}) {
    genTreeData();
  }

  bool showSnackBar = false;
  bool expandChildrenOnReady = true;
  final WorkTypeController _workTypeController = Get.find<WorkTypeController>();

  var type1 = ''.obs;
  var type2 = ''.obs;
  var pid = 0.obs;
  final _formKey = GlobalKey<FormState>();
  final _type1Key = GlobalKey<FormState>();
  final RxList<Color?> focusColor = [Colors.blue[700], Colors.grey].obs;

  TreeViewController? _controller;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        drawer: Menu(),
        body: LayoutBuilder(
          builder: (context, constraints) {
            double treeHeight = constraints.biggest.height - 220;
            return CustomScrollView(
              slivers: [
                AppBarWidget(title: '字典设置'),
                // 以下是将SliverFixedExtentList放入CustomScrollView
                SliverFixedExtentList(
                  delegate: SliverChildListDelegate([
                    /// 工作类别树
                    buildWorkTypeTree()
                  ]),
                  itemExtent: treeHeight, // 每个列表项的高度为50
                ),
                SliverFixedExtentList(
                    itemExtent: 220,
                    delegate: SliverChildListDelegate(
                      [
                        BoxWidget(
                            content: Column(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            /// 状态点
                            buildSateIndexObx(),
                            const SizedBox(height: 10),

                            /// 添加工作类别
                            Expanded(
                              child: Form(
                                key: _formKey,
                                child: PageView(
                                  onPageChanged: (index) {
                                    if (index == 0) {
                                      focusColor[0] = Colors.blue[700];
                                      focusColor[1] = Colors.grey;
                                    } else {
                                      focusColor[0] = Colors.grey;
                                      focusColor[1] = Colors.blue[700];
                                    }
                                  },
                                  scrollDirection: Axis.horizontal,
                                  children: [
                                    /// 添加子类
                                    buildAddType2Row(),

                                    ///  添加大类
                                    buildAddType1Row(),
                                  ],
                                ),
                              ),
                            ),
                          ],
                        ))
                      ],
                    ))
              ],
            );
          },
        ));
  }

  /// 工作类别树
  BoxWidget buildWorkTypeTree() {
    return BoxWidget(
        content: TreeView.simple(
            tree: sampleTree,
            showRootNode: false,
            expansionIndicatorBuilder: (context, node) =>
                ChevronIndicator.rightDown(
                  tree: node,
                  color: Colors.blue[700],
                ),
            indentation: const Indentation(style: IndentStyle.squareJoint),
            onItemTap: (item) {
              logger.i(item.key);
            },
            onTreeReady: (controller) {
              // _controller = controller;
              // if (expandChildrenOnReady) controller.expandAllChildren(
              //     sampleTree);
            },
            builder: (context, node) => Container(
                  padding: const EdgeInsets.only(left: 5),
                  width: 200,
                  height: 30,
                  child: Text(node.key),
                )));
  }

  /// 状态点
  Obx buildSateIndexObx() {
    return Obx(
      () => Row(
        mainAxisSize: MainAxisSize.min,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Container(
            width: 10,
            height: 10,
            decoration: BoxDecoration(
                color: focusColor[0], borderRadius: BorderRadius.circular(5)),
          ),
          const SizedBox(width: 5),
          Container(
            width: 10,
            height: 10,
            decoration: BoxDecoration(
                color: focusColor[1], borderRadius: BorderRadius.circular(5)),
          ),
        ],
      ),
    );
  }

  /// 添加工作大类
  Row buildAddType1Row() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        /// 大类 输入
        Expanded(
          child: Container(
            margin: const EdgeInsets.only(right: 10),
            alignment: Alignment.center,
            height: 78,
            decoration: BoxDecoration(
              color: const Color(0xFFF0EEF0),
              borderRadius: BorderRadius.circular(15.0),
            ),
            child: Form(
              key: _type1Key,
              child: TextFormField(
                validator: (v) {
                  if (v == null || v.isEmpty) {
                    return '请输入大类';
                  }
                  return null;
                },
                onSaved: (v) {
                  type1.value = v!;
                },
                decoration: const InputDecoration(
                  disabledBorder: InputBorder.none,
                  enabledBorder: InputBorder.none,
                  focusedBorder: InputBorder.none,
                  errorStyle: TextStyle(fontSize: 8),
                  hintText: '请输入大类',
                  prefixIcon: Icon(Icons.title),
                ),
              ),
            ),
          ),
        ),

        Button(
          "添加",
          onTap: submitTyp1,
          icon: Icons.add,
        ),
      ],
    );
  }

  /// 添加工作子类
  Row buildAddType2Row() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Expanded(
          child: Column(
            children: [
              /// 大类 输入
              Container(
                margin: const EdgeInsets.only(right: 10),
                alignment: Alignment.center,
                height: 78,
                decoration: BoxDecoration(
                  color: const Color(0xFFF0EEF0),
                  borderRadius: BorderRadius.circular(15.0),
                ),
                child: buildDropdownSearch(
                  "请选择工作大类",
                  "请选择工作大类",
                  _workTypeController.type1List,
                  type1Change,
                  type1Save,
                  padding: 5,
                  labelText: "工作大类",
                ),
              ),
              const SizedBox(height: 5),

              /// 子类 输入
              Container(
                margin: const EdgeInsets.only(right: 10),
                alignment: Alignment.center,
                height: 78,
                decoration: BoxDecoration(
                  color: const Color(0xFFF0EEF0),
                  borderRadius: BorderRadius.circular(15.0),
                ),
                child: TextFormField(
                  style: const TextStyle(fontSize: 12, color: Colors.black),
                  validator: (v) {
                    if (v == null || v.isEmpty) {
                      return '请输入子类';
                    }
                    return null;
                  },
                  onSaved: (v) {
                    type2.value = v!;
                  },
                  decoration: const InputDecoration(
                    disabledBorder: InputBorder.none,
                    enabledBorder: InputBorder.none,
                    focusedBorder: InputBorder.none,
                    errorStyle: TextStyle(fontSize: 8),
                    hintText: '请输入子类',
                    labelText: "工作子类",
                    prefixIcon: Icon(Icons.title),
                  ),
                ),
              ),
            ],
          ),
        ),
        Button(
          "添加",
          onTap: submitTyp2,
          icon: Icons.add,
        ),
      ],
    );
  }

  /// 添加大类
  void type1Change(Typ? value) async {
    pid.value = value!.id!;
  }

  void type1Save(v) {}

  submitTyp1() {
    /// 校验数据
    if (!_type1Key.currentState!.validate()) {
      return;
    }

    /// 保存数据
    _type1Key.currentState!.save();
    _workTypeController.createWorkType1(type1.value);
  }

  /// 添加子类
  submitTyp2() {
    /// 校验数据
    if (!_formKey.currentState!.validate()) {
      return;
    }

    /// 保存数据
    _formKey.currentState!.save();

    _workTypeController.createWorkType2(type2.value, pid.value);
  }

  TreeNode sampleTree = TreeNode.root();

  Future<TreeNode> genTreeData() async {
    await _workTypeController.fetchTypeList();
    for (var element in _workTypeController.type1List) {
      TreeNode p = TreeNode(key: element.description, data: element);

      sampleTree.add(p);
      await _workTypeController.getType2ByPid(element.id!);
      final List<Typ>? t2 = _workTypeController.type2ListMap[element.id!];
      if (t2 == null) {
        continue;
      }

      for (var tt2 in t2) {
        p.add(TreeNode(key: tt2.description, data: tt2));
      }
    }

    return sampleTree;
  }
}
