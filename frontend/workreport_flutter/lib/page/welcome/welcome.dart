import 'dart:async';

import 'package:WorkReport/router/router.dart';
import 'package:WorkReport/state/local_storage.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Welcome extends StatefulWidget {
  const Welcome({Key? key}) : super(key: key);

  @override
  _WelcomeState createState() => _WelcomeState();
}

class _WelcomeState extends State<Welcome> with SingleTickerProviderStateMixin {
  late double _bottom = -200;
  final LocalStorage _localStorage = Get.find<LocalStorage>();
  late AnimationController _animationController;
  late Animation<double> _animation;

  @override
  void initState() {
    Timer(const Duration(milliseconds: 1000), () {
      setState(() {
        _bottom = 160;
      });
    });
    super.initState();

    _animationController = AnimationController(
      vsync: this,
      duration: const Duration(seconds: 3),
    );
    _animation =
        Tween<double>(begin: 0.0, end: 1.0).animate(_animationController);

    // 启动动画
    _animationController.forward();
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color.fromRGBO(47, 79, 79, 50),
      body: Stack(
        children: [
          Positioned(
            left: -73,
            child: Container(
              width: 200,
              height: 200,
              decoration: const BoxDecoration(
                color: Color.fromRGBO(2, 211, 95, 0.45),
                shape: BoxShape.circle,
              ),
            ),
          ),
          Positioned(
            left: 0,
            top: -100,
            child: Container(
              width: 200,
              height: 200,
              decoration: const BoxDecoration(
                color: Color.fromRGBO(2, 211, 95, 0.45),
                shape: BoxShape.circle,
              ),
            ),
          ),
          // Positioned(
          //     bottom: 200, // 图像的高度 + 文本和间距的高度
          //     left: -10,
          //     child: Transform.rotate(
          //       angle: 45 * 3.1415927 / 360, // 角度转弧度
          //       child: Image.asset(
          //         width: 450,
          //         'assets/images/devops1.png',
          //         fit: BoxFit.contain,
          //       ),
          //     )),
          Positioned(
              top: 100,
              right: 150,
              child: Transform.rotate(
                angle: 45 * 3.1415927 / 360, // 角度转弧度
                child: Image.asset(
                  width: 80,
                  'assets/images/Kubernetes.png',
                  fit: BoxFit.contain,
                ),
              )),
          Positioned(
              top: 180,
              right: 30,
              child: Transform.rotate(
                angle: 45 * 3.1415927 / 360, // 角度转弧度
                child: Image.asset(
                  width: 50,
                  'assets/images/prometheus1.png',
                  fit: BoxFit.contain,
                ),
              )),
          Positioned(
              top: 100,
              right: 30,
              child: Transform.rotate(
                angle: 45 * 3.1415927 / 360, // 角度转弧度
                child: Image.asset(
                  width: 50,
                  'assets/images/jenkins.png',
                  fit: BoxFit.contain,
                ),
              )),
          Positioned(
              bottom: 50,
              left: 30,
              child: Transform.rotate(
                angle: 45 * 3.1415927 / 360, // 角度转弧度
                child: Image.asset(
                  width: 80,
                  'assets/images/etcd.png',
                  fit: BoxFit.contain,
                ),
              )),
          Positioned(
              bottom: 50,
              right: 30,
              child: Transform.rotate(
                angle: 45 * 3.1415927 / 360, // 角度转弧度
                child: Image.asset(
                  width: 80,
                  'assets/images/Istio.png',
                  fit: BoxFit.contain,
                ),
              )),
          Positioned(
              top: 20, // 图像的高度 + 文本和间距的高度
              right: 30,
              child: Transform.rotate(
                angle: 45 * 3.1415927 / 360, // 角度转弧度
                child: Image.asset(
                  width: 100,
                  'assets/images/docker.png',
                  fit: BoxFit.contain,
                ),
              )),

          Center(
            child: Container(
                alignment: Alignment.center,
                child: AnimatedBuilder(
                    animation: _animationController,
                    builder: (context, child) {
                      return Stack(
                        clipBehavior: Clip.none,
                        alignment: Alignment.center,
                        children: [
                          Positioned(
                              top: -30, // 图像的高度 + 文本和间距的高度
                              // right: 30,
                              child: Text(
                                "WorkReport",
                                style: TextStyle(
                                  fontSize: 20 + 20 * _animation.value,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.white,
                                ),
                              )),

                          Image.asset(
                            'assets/images/devops1.png',
                            width: 100 + 800 * _animation.value,
                            fit: BoxFit.contain,
                          ),

                          // TweenAnimationBuilder(
                          //   tween: Tween<double>(begin: 100.0, end: 900),
                          //   duration: const Duration(seconds: 3),
                          //   builder: (context, value, child) {
                          //     return Image.asset(
                          //       width: value,
                          //       'assets/images/devops1.png',
                          //       fit: BoxFit.contain,
                          //     );
                          //   },
                          // ),
                          // TweenAnimationBuilder(
                          //   tween: Tween<double>(begin: 10, end: 30),
                          //   duration: const Duration(seconds: 3),
                          //   builder: (context, value, child) {
                          //     return Text(
                          //       "WorkReport",
                          //       style: TextStyle(
                          //           fontSize: value,
                          //           fontWeight: FontWeight.bold,
                          //           color: Colors.white),
                          //     );
                          //   },
                          // ),
                        ],
                      );
                    })),
          ),
          AnimatedPositioned(
            duration: const Duration(milliseconds: 1000),
            bottom: _bottom,
            // 从下往上移动
            left: 0,
            right: 0,
            child: Container(
              width: 300,
              alignment: Alignment.center,
              child: ElevatedButton(
                onPressed: () {
                  /// 设置 欢迎页 标志位
                  _localStorage.setIsFirstOpenFlag();
                  Get.offNamed(Routes.login);
                },
                style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.all<Color>(
                    const Color(0xff02D35F),
                  ),
                ),
                child: const SizedBox(
                  width: 100,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [Text("登入"), Icon(Icons.login)],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
