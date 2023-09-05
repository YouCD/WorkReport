import 'dart:convert';

class WorkLog {
  final int id;
  final int date;
  final String type1;
  final int type1Id;
  final String type2;
  final int type2Id;
  final String content;

  WorkLog({
    required this.id,
    required this.date,
    required this.type1,
    required this.type1Id,
    required this.type2,
    required this.type2Id,
    required this.content,
  });

  WorkLog copyWith({
    int? id,
    int? date,
    String? type1,
    int? type1Id,
    String? type2,
    int? type2Id,
    String? content,
  }) =>
      WorkLog(
        id: id ?? this.id,
        date: date ?? this.date,
        type1: type1 ?? this.type1,
        type1Id: type1Id ?? this.type1Id,
        type2: type2 ?? this.type2,
        type2Id: type2Id ?? this.type2Id,
        content: content ?? this.content,
      );

  factory WorkLog.fromRawJson(String str) => WorkLog.fromJson(json.decode(str));

  String toRawJson() => json.encode(toJson());

  factory WorkLog.fromJson(Map<String, dynamic> json) => WorkLog(
        id: json["id"],
        date: json["date"],
        type1: json["type1"],
        type1Id: json["type1_id"],
        type2: json["type2"],
        type2Id: json["type2_id"],
        content: json["content"],
      );

  static List<WorkLog> fromJsonList(List<dynamic> jsonList) {
    return jsonList.map((json) => WorkLog.fromJson(json)).toList();
  }

  Map<String, dynamic> toJson() => {
        "id": id,
        "date": date,
        "type1": type1,
        "type1_id": type1Id,
        "type2": type2,
        "type2_id": type2Id,
        "content": content,
      };

  WorkLogItem toWorkLogItem() {
    return WorkLogItem(
      id: id,
      type1: type1Id,
      type2: type2Id,
      content: content,
      date: date,
    );
  }
}

class WorkLogItem {
  late int? id;
  late int? type1;
  late int? type2;
  late String? content;
  late int? date;

  WorkLogItem({
    this.id,
    this.type1,
    this.type2,
    this.content,
    this.date,
  });

  WorkLogItem copyWith({
    int? id,
    int? type1,
    int? type2,
    String? content,
    int? date,
  }) =>
      WorkLogItem(
        id: id ?? this.id,
        type1: type1 ?? this.type1,
        type2: type2 ?? this.type2,
        content: content ?? this.content,
        date: date ?? this.date,
      );

  factory WorkLogItem.fromRawJson(String str) =>
      WorkLogItem.fromJson(json.decode(str));

  String toRawJson() => json.encode(toJson());

  factory WorkLogItem.fromJson(Map<String, dynamic> json) => WorkLogItem(
        id: json["id"],
        type1: json["type1"],
        type2: json["type2"],
        content: json["content"],
        date: json["date"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "type1": type1,
        "type2": type2,
        "content": content,
        "date": date,
      };
}
