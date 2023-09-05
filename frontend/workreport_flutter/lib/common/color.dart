import 'dart:math';

import 'package:flutter/material.dart';

const Color daddyLabGreen = Color.fromRGBO(34, 166, 64, 1);

Color randomColor() {
  return Color.fromARGB(255, Random().nextInt(256) + 0,
      Random().nextInt(256) + 0, Random().nextInt(256) + 0);
}

Color colorByType(String type1) {
  switch (type1) {
    case "运维":
      return const Color(0xFF2196F3);
    case "开发":
      return const Color(0xFFFFC300);
    case "其他":
      return const Color(0xFFFF683B);
    case "技术支持":
      return const Color(0xFF3BFF49);
    case "项目管理":
      return const Color(0xFF6E1BFF);
    case "平台开发":
      return const Color(0xFFE80054);
    default:
      return const Color(0xFF50E4FF);
  }
}

