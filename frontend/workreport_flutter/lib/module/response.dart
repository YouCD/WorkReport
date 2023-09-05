class Resp {
  final int code;
  final String msg;
  final dynamic data;
  final bool flag;

  Resp({
    required this.code,
    required this.msg,
    required this.data,
    required this.flag,
  });

  factory Resp.fromJson(Map<String, dynamic> json) {
    return Resp(
      code: json['code'],
      msg: json['msg'],
      data: json['data'],
      flag: json['flag'],
    );
  }
}
