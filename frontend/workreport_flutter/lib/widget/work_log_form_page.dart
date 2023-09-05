import 'package:WorkReport/common/log.dart';
import 'package:WorkReport/common/parser_time.dart';
import 'package:WorkReport/module/type.dart';
import 'package:WorkReport/module/work_log.dart';
import 'package:WorkReport/state/workLog.dart';
import 'package:WorkReport/state/workType.dart';
import 'package:WorkReport/widget/box.dart';
import 'package:WorkReport/widget/button.dart';
import 'package:WorkReport/widget/icon.dart';
import 'package:dropdown_search/dropdown_search.dart';
import 'package:flutter/material.dart';
import 'package:flutter_picker/flutter_picker.dart';
import 'package:get/get.dart';

typedef DefaultSelectedItemFunction = Typ? Function();

class WorkLogFormPageWidget extends StatelessWidget {
  WorkLogFormPageWidget({super.key}) {
    _init();
  }

  final WorkLogController _workLogController = Get.find<WorkLogController>();
  final TextEditingController _contentController = TextEditingController();
  final TextEditingController _dateController = TextEditingController();
  final WorkTypeController _workTypeController = Get.find<WorkTypeController>();
  final _formKey = GlobalKey<FormState>();

  _init() {
    if (!_workLogController.isEdit.value) {
      _dateController.text =
          parseUnixTime2Date(DateTime.now().millisecondsSinceEpoch ~/ 1000);
      return;
    }

    _contentController.text = _workLogController.editWorkLogData.value.content!;
    _dateController.text =
        parseUnixTime2Date(_workLogController.editWorkLogData.value.date!);
  }

  @override
  Widget build(BuildContext context) {
    return BoxWidget(
      content: Form(
        key: _formKey,
        child: Column(
          children: [
            /// 选择日期
            Container(
                decoration: BoxDecoration(
                  color: const Color(0xFFF0EEF0),
                  borderRadius: BorderRadius.circular(15.0),
                ),
                child: GestureDetector(
                  onTap: () => showPickerDateCustom(context),
                  child: AbsorbPointer(
                    child: TextFormField(
                      controller: _dateController,
                      validator: (v) {
                        if (v == null || v.isEmpty) {
                          return '请填写日期';
                        }
                        return null;
                      },
                      onSaved: (v) {
                        _workLogController.addData.value.date =
                            parseDate2UnixTime(v!);
                      },
                      readOnly: true,
                      decoration: const InputDecoration(
                        errorStyle: TextStyle(fontSize: 8),

                        contentPadding: EdgeInsets.symmetric(
                          horizontal: 20,
                          vertical: 20,
                        ),
                        // 以下属性可用来去除TextField的边框
                        disabledBorder: InputBorder.none,
                        enabledBorder: InputBorder.none,
                        focusedBorder: InputBorder.none,

                        hintText: '请选择日期',
                        prefixIcon: Icon(
                          Icons.date_range,
                          size: 14,
                        ),
                      ),
                    ),
                  ),
                )),

            const SizedBox(height: 20),

            /// 工作大类 选择
            Container(
              padding: const EdgeInsets.only(top: 5, bottom: 5),
              decoration: BoxDecoration(
                color: const Color(0xFFF0EEF0),
                borderRadius: BorderRadius.circular(15.0),
              ),
              child: buildDropdownSearch(
                "请选择工作大类",
                "工作大类",
                _workTypeController.type1List,
                type1Change,
                type1Save,
                defaultSelectedItem: defaultType1Item,
                labelText: "请选择工作大类",
              ),
            ),
            const SizedBox(height: 20),

            /// 工作子类 选择
            Container(
              padding: const EdgeInsets.only(top: 5, bottom: 5),
              decoration: BoxDecoration(
                color: const Color(0xFFF0EEF0),
                borderRadius: BorderRadius.circular(15.0),
              ),
              child: buildDropdownSearch(
                "请选择工作子类",
                "工作子类",
                _workTypeController.type2List,
                type2Change,
                type2Save,
                defaultSelectedItem: defaultType2Item,
                labelText: "请选择工作子类",
              ),
            ),

            const SizedBox(height: 20),

            /// 工作内容 输入
            Container(
              padding: const EdgeInsets.only(top: 5, bottom: 5),
              decoration: BoxDecoration(
                color: const Color(0xFFF0EEF0),
                borderRadius: BorderRadius.circular(15.0),
              ),
              child: TextFormField(
                validator: (v) {
                  if (v == null || v.isEmpty) {
                    return '请输入工作内容';
                  }
                  return null;
                },
                onSaved: (v) {
                  _workLogController.addData.value.content = v;
                },
                controller: _contentController,
                decoration: const InputDecoration(
                  contentPadding: EdgeInsets.symmetric(
                    horizontal: 20,
                    vertical: 20,
                  ),
                  // 以下属性可用来去除TextField的边框
                  disabledBorder: InputBorder.none,
                  enabledBorder: InputBorder.none,
                  focusedBorder: InputBorder.none,
                  errorStyle: TextStyle(fontSize: 8),
                  hintText: '请输入工作内容',
                  prefixIcon: Icon(Icons.title),
                ),
              ),
            ),
            const SizedBox(height: 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Button(
                  "提交",
                  onTap: submit,
                  icon: Icons.add,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  void type1Save(v) {
    /// 判断是新增还是编辑
    if (_workLogController.isEdit.value) {
      _workLogController.editWorkLogData.value =
          _workLogController.editWorkLogData.value.copyWith(type1: v!.id);
      return;
    }
    _workLogController.addData.value.type1 = v!.id;
  }

  void type1Change(Typ? value) async {
    await _workTypeController.getType2ByPid(value!.id!);
  }

  void type2Save(v) {
    /// 判断是新增还是编辑
    if (_workLogController.isEdit.value) {
      _workLogController.editWorkLogData.value =
          _workLogController.editWorkLogData.value.copyWith(type2: v!.id);
      return;
    }
    _workLogController.addData.value.type2 = v!.id;
  }

  void type2Change(Typ? value) {}

  void submit() async {
    /// 校验数据
    if (!_formKey.currentState!.validate()) {
      return;
    }

    /// 保存数据
    _formKey.currentState!.save();

    /// 判断是新增还是编辑
    if (_workLogController.isEdit.value) {
      _workLogController.editWorkLogData.value = _workLogController
          .editWorkLogData.value
          .copyWith(content: _contentController.text);
      bool flag = await _workLogController.editWorkLog();
      if (flag) {
        _workLogController.isEdit.value = false;
        _workLogController.editWorkLogData.value = WorkLogItem();
        _contentController.text = "";
        Get.back();
      }
      return;
    }

    /// 添加数据
    _workLogController.addWorkLog();

    ///  重置表单的内容
    _workLogController.addData.value.content = null;
    _contentController.text = "";
    return;
  }

  showPickerDateCustom(BuildContext context) {
    Picker(
        confirmText: "确认",
        cancelText: "取消",
        hideHeader: true,
        adapter: DateTimePickerAdapter(
          // yearSuffix: "年",
          // monthSuffix: "月",
          // daySuffix: "日",
          isNumberMonth: true,
          customColumnType: [
            0,
            1,
            2,
          ],
        ),
        title: const Text("选择日期"),
        selectedTextStyle: const TextStyle(color: Colors.blue),
        onConfirm: (Picker picker, List value) {
          int unixTimeStamp = (picker.adapter as DateTimePickerAdapter)
                  .value!
                  .millisecondsSinceEpoch ~/
              1000;

          /// 设置 日期输入框的值
          _dateController.text = parseUnixTime2Date(unixTimeStamp);
        }).showDialog(context);
  }

  Typ? defaultType1Item() {
    if (!_workLogController.isEdit.value) {
      return null;
    }
    for (var element in _workTypeController.type1List) {
      if (element.id == _workLogController.editWorkLogData.value.type1) {
        logger.i(
            "${element.id},${_workLogController.editWorkLogData.value.type1}");
        return element;
      }
    }
  }

  Typ? defaultType2Item() {
    if (!_workLogController.isEdit.value) {
      return null;
    }

    _workTypeController
        .getType2ByPid(_workLogController.editWorkLogData.value.type1!);

    for (var element in _workTypeController.type2List) {
      if (element.id == _workLogController.editWorkLogData.value.type2) {
        logger.i(
            "${element.id},${_workLogController.editWorkLogData.value.type2}");

        return element;
      }
    }
  }
}

// 定义自定义的compareFn函数
bool customCompare(Typ? selectedItem, Typ? item) {
  // 判断两个项是否相等
  return selectedItem?.id == item?.id;
}

/// 下拉框构建
DropdownSearch<Typ> buildDropdownSearch(
  String errorMsg,
  String hintText,
  List<Typ> items,
  Function? onChanged,
  Function? onSaved, {
  DefaultSelectedItemFunction? defaultSelectedItem,
  double? padding = 12,
  String? labelText,
}) {
  return DropdownSearch<Typ>(
    validator: (v) => v == null ? errorMsg : null,
    compareFn: customCompare,
    selectedItem: defaultSelectedItem != null ? defaultSelectedItem() : null,
    // popupProps:  PopupProps.menu(
    //   /// 设置fit
    //   fit: FlexFit.loose,
    //   menuProps:const MenuProps(
    //     backgroundColor: Colors.transparent,
    //     elevation: 0,
    //   ),
    //   showSelectedItems: true,
    //     containerBuilder: (ctx, popupWidget) {
    //       return Container(
    //           child: popupWidget,
    //           decoration: const  BoxDecoration(
    //             // color:  Color(0xFFF0EEF0),
    //             color:  Colors.white,
    //           borderRadius: BorderRadius.all(Radius.circular(15)),
    //       ));}
    // ),
    // popupProps: PopupProps.dialog(
    //     fit: FlexFit.loose,
    //     dialogProps: const DialogProps(
    //       backgroundColor: Colors.transparent,
    //       elevation: 0,
    //     ),
    //     containerBuilder: (ctx, popupWidget) {
    //       return Container(
    //           decoration: const BoxDecoration(
    //
    //             // color:  Color(0xFFF0EEF0),
    //             color: Colors.white,
    //             borderRadius: BorderRadius.all(Radius.circular(15)),
    //           ),
    //           child: popupWidget);
    //     }),
    popupProps: PopupProps.bottomSheet(
        fit: FlexFit.loose,
        bottomSheetProps: const BottomSheetProps(
          backgroundColor: Colors.transparent,
          elevation: 0,
        ),
        containerBuilder: (ctx, popupWidget) {
          return Container(
              height: 300,
              decoration: const BoxDecoration(
                // color:  Color(0xFFF0EEF0),
                color: Colors.white,
                borderRadius: BorderRadius.only(
                    topRight: Radius.circular(20),
                    topLeft: Radius.circular(20)),
              ),
              child: popupWidget);
        }),
    items: items,
    itemAsString: (Typ? u) => u?.description ?? '',

    // 用了就不显示 hintText
    // dropdownBuilder: (context, item) {
    //   return Text(item?.description ?? 'dsadsa');
    // },
    onSaved: (value) => onSaved!(value),
    dropdownDecoratorProps: DropDownDecoratorProps(
      dropdownSearchDecoration: InputDecoration(
        prefixIcon: const Icon(
          IconFont.icon_type1,
          size: 14,
        ),
        labelText: labelText,
        hintText: hintText,
        contentPadding: EdgeInsets.symmetric(
          horizontal: padding!,
          vertical: padding!,
        ),
        errorStyle: const TextStyle(fontSize: 8),
        disabledBorder: InputBorder.none,
        enabledBorder: InputBorder.none,
        focusedBorder: InputBorder.none,
      ),
    ),
    onChanged: (value) => onChanged!(value),
  );
}
