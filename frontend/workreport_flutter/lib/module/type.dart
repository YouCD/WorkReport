import 'dart:convert';

class Typ {
  final int? id;
  final String description;
  final int pid;
  final int type;

  Typ({
    this.id,
    required this.description,
    required this.pid,
    required this.type,
  });

  Typ copyWith({
    int? id,
    String? description,
    int? pid,
    int? type,
  }) =>
      Typ(
        id: id ?? this.id,
        description: description ?? this.description,
        pid: pid ?? this.pid,
        type: type ?? this.type,
      );

  factory Typ.fromRawJson(String str) => Typ.fromJson(json.decode(str));

  String toRawJson() => json.encode(toJson());

  factory Typ.fromJson(Map<String, dynamic> json) => Typ(
        id: json["id"],
        description: json["description"],
        pid: json["pid"],
        type: json["type"],
      );

  static List<Typ> fromJsonList(List<dynamic> jsonList) {
    return jsonList.map((json) => Typ.fromJson(json)).toList();
  }

  Map<String, dynamic> toJson() => {
        "id": id,
        "description": description,
        "pid": pid,
        "type": type,
      };
}
