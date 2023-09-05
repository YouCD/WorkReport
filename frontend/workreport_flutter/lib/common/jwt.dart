import 'package:jwt_decoder/jwt_decoder.dart';

class JwtToken {
  final String jwtStr;

  const JwtToken(this.jwtStr);

  // 解析为map
  Map<String, dynamic> decodedToken2Map() {
    return JwtDecoder.decode(jwtStr);
  }

  // 获取userID
  String getUserId() {
    return decodedToken2Map()["user_id"];
  }

  // 获取过期时间
  int getExpirationDate() {
    return decodedToken2Map()["exp"].toInt();
  }
}
