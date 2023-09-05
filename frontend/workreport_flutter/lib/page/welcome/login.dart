import 'package:WorkReport/router/router.dart';
import 'package:WorkReport/state/user.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Login extends StatelessWidget {
  Login({super.key});

  final RxBool obscureTextFlag = true.obs;
  final UserController _userController = Get.put(UserController());

  final _userInputController = TextEditingController();
  final _pwdInputController = TextEditingController();
  final _formKey = GlobalKey<FormState>();

  final TextStyle baseStyle = const TextStyle(color: Colors.white);
  var user = ''.obs;
  var password = ''.obs;

  void togglePasswordVisibility() {
    obscureTextFlag.toggle();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color.fromRGBO(47, 79, 79, 50),
      body: Center(
        child: SingleChildScrollView(
          child: Form(
              key: _formKey,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center, // 垂直方向居中对齐
                children: [
                  const Text(
                    "WorkReport",
                    style: TextStyle(
                      fontSize: 35,
                      fontWeight: FontWeight.bold,
                      color: Colors.white,
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.symmetric(
                        horizontal: 24.0, vertical: 16.0),
                    child: TextFormField(
                      validator: (v) {
                        if (v == null || v.isEmpty) {
                          return '账户不能为空';
                        }
                        return null;
                      },
                      onSaved: (value) =>
                          user.value = _userInputController.text,
                      style: baseStyle,
                      controller: _userInputController,
                      decoration: InputDecoration(
                        prefixIcon: const Icon(
                          Icons.person,
                          color: Colors.white,
                        ),
                        labelText: "账户 *",
                        hintText: "请输入账户",
                        labelStyle: baseStyle,
                        hintStyle: baseStyle,
                        enabledBorder: const UnderlineInputBorder(
                          borderSide: BorderSide(color: Colors.white),
                        ),
                      ),
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.symmetric(
                        horizontal: 24.0, vertical: 16.0),
                    child: Obx(() => TextFormField(
                          validator: (v) {
                            if (v == null || v.isEmpty) {
                              return '密码不能为空';
                            }
                            return null;
                          },
                          onSaved: (value) =>
                              password.value = _pwdInputController.text,
                          style: const TextStyle(color: Colors.white),
                          controller: _pwdInputController,
                          obscureText: obscureTextFlag.value,
                          decoration: InputDecoration(
                            prefixIcon: const Icon(
                              Icons.lock,
                              color: Colors.white,
                            ),
                            suffixIcon: InkWell(
                              onTap: () {
                                togglePasswordVisibility();
                              },
                              child: Icon(
                                obscureTextFlag.value
                                    ? Icons.remove_red_eye
                                    : Icons.visibility_off,
                                color: Colors.white,
                              ),
                            ),
                            labelText: "密码 *",
                            hintText: "请输入密码",
                            labelStyle: baseStyle,
                            hintStyle: baseStyle,
                            enabledBorder: const UnderlineInputBorder(
                              borderSide: BorderSide(color: Colors.white),
                            ),
                          ),
                        )),
                  ),
                  const SizedBox(
                    height: 52.0,
                  ),
                  SizedBox(
                    width: MediaQuery.of(context).size.width - 48.0,
                    height: 48.0,
                    child: ElevatedButton(
                      style: ButtonStyle(
                        backgroundColor:
                            MaterialStateProperty.resolveWith<Color?>(
                          (Set<MaterialState> states) {
                            if (states.contains(MaterialState.pressed)) {
                              return const Color(0xff02D35F); // 按下时的背景颜色
                            }
                            return const Color(0xff02D35F); // 默认的背景颜色
                          },
                        ),
                      ),
                      onPressed: () async {
                        await submit();
                      },
                      child: const Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(Icons.login),
                          SizedBox(
                            width: 10.0,
                          ),
                          Text("登入"),
                        ],
                      ),
                    ),
                  ),
                ],
              )),
        ),
      ),
    );
  }

  Future<void> submit() async {
    /// 校验数据
    if (!_formKey.currentState!.validate()) {
      return;
    }

    /// 保存数据
    _formKey.currentState!.save();

    /// 要做登入失败消息渲染
    bool flag = await _userController.login(user.value, password.value);
    if (flag) {
      Get.offNamed(Routes.home);
      return;
    }
  }
}
