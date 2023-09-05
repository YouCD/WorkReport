import 'dart:convert';

class Type1Count {
  final int count;
  final String type1;

  Type1Count({
    required this.count,
    required this.type1,
  });

  Type1Count copyWith({
    int? count,
    String? type1,
  }) =>
      Type1Count(
        count: count ?? this.count,
        type1: type1 ?? this.type1,
      );

  factory Type1Count.fromRawJson(String str) =>
      Type1Count.fromJson(json.decode(str));

  String toRawJson() => json.encode(toJson());

  factory Type1Count.fromJson(Map<String, dynamic> json) => Type1Count(
        count: json["count"],
        type1: json["type1"],
      );

  static List<Type1Count> fromJsonList(List<dynamic> jsonList) {
    return jsonList.map((json) => Type1Count.fromJson(json)).toList();
  }

  Map<String, dynamic> toJson() => {
        "count": count,
        "type1": type1,
      };
}

class Type2Count {
  final int count;
  final String type2;

  Type2Count({
    required this.count,
    required this.type2,
  });

  Type2Count copyWith({
    int? count,
    String? type2,
  }) =>
      Type2Count(
        count: count ?? this.count,
        type2: type2 ?? this.type2,
      );

  factory Type2Count.fromRawJson(String str) =>
      Type2Count.fromJson(json.decode(str));

  String toRawJson() => json.encode(toJson());

  factory Type2Count.fromJson(Map<String, dynamic> json) => Type2Count(
        count: json["count"],
        type2: json["type2"],
      );

  static List<Type2Count> fromJsonList(List<dynamic> jsonList) {
    return jsonList.map((json) => Type2Count.fromJson(json)).toList();
  }

  Map<String, dynamic> toJson() => {
        "count": count,
        "type2": type2,
      };
}
