import 'package:WorkReport/common/log.dart';
import 'package:WorkReport/module/response.dart';
import 'package:WorkReport/state/local_storage.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_smart_dialog/flutter_smart_dialog.dart';
import 'package:get/get.dart';

class ApiService {
  final Dio _dio;
  final LocalStorage _localStorage = Get.find<LocalStorage>();

  ApiService() : _dio = Dio() {
    // 在这里可以添加一些公共配置，如请求超时时间、拦截器等
    _dio.options.baseUrl = 'https://work.youcd.online';
    // _dio.options.baseUrl = 'http://192.168.1.110:8080';
    _dio.options.connectTimeout = const Duration(seconds: 5); // 设置请求超时时间为5秒
    _dio.interceptors.add(
      InterceptorsWrapper(
        onRequest: (options, handler) {
          // 在请求被发送之前做一些事情
          // 在请求头中添加 Token
          final token = _localStorage.getData(jwtName); // 获取 Token 的方法，根据实际情况替换
          options.headers[jwtName] = token;
          options.headers['Content-Type'] = "application/json";

          return handler.next(options);
          // 如果你想完成请求并返回一些自定义数据，可以resolve一个Response对象 `handler.resolve(response)`。
          // 如果你想终止请求并返回一些自定义数据，可以reject一个DioError对象 `handler.reject(dioError)`
        },
        onResponse: (response, handler) {
          // 在返回响应数据之前做一些预处理
          return handler.next(response); // continue
          // 如果你想完成请求并返回一些自定义数据，可以resolve一个Response对象 `handler.resolve(response)`。
          // 如果你想终止请求并返回一些自定义数据，可以reject一个DioError对象 `handler.reject(dioError)`
        },
        onError: (DioError e, handler) {
          // 当请求失败时做一些预处理
          return handler.next(e); //continue
          // 如果你想完成请求并返回一些自定义数据，可以resolve一个Response对象 `handler.resolve(response)`。
          // 如果你想终止请求并返回一些自定义数据，可以reject一个DioError对象 `handler.reject(dioError)`
        },
      ),
    );
  }

  Future<Resp> get(String path, {Map<String, dynamic>? params}) async {
    try {
      final response = await _dio.get(path, queryParameters: params);
      final resp = Resp.fromJson(response.data);
      return resp;
    } catch (e) {
      SmartDialog.showToast(
        e.toString(),
        alignment: Alignment.topCenter,
      );
      throw Exception('网络请求失败');
    }
  }

  Future<Resp> post(String path, {Object? data}) async {
    try {
      final response = await _dio.post(path, data: data);
      final resp = Resp.fromJson(response.data);
      return resp;
    } catch (e) {
      SmartDialog.showToast(
        e.toString(),
        alignment: Alignment.topCenter,
      );
      throw Exception('网络请求失败');
    }
  }

  Future<Resp> put(String path, {Object? data}) async {
    try {
      final response = await _dio.put(path, data: data);
      final resp = Resp.fromJson(response.data);
      return resp;
    } catch (e) {
      SmartDialog.showToast(
        e.toString(),
        alignment: Alignment.topCenter,
      );
      throw Exception('网络请求失败');
    }
  }

  Future<Resp> del(String path, {Map<String, dynamic>? params}) async {
    logger.i(
      "接口 $path",
    );
    try {
      final response = await _dio.delete(path, queryParameters: params);
      final resp = Resp.fromJson(response.data);
      return resp;
    } catch (e) {
      SmartDialog.showToast(
        e.toString(),
        alignment: Alignment.topCenter,
      );
      throw Exception('网络请求失败');
    }
  }

//
// Future<dio.Response> putData(String path, {Map<String, dynamic>? params}) async {
//   try {
//     final response = await _dio.put(path, queryParameters: params);
//     return response;
//   } catch (e) {
//     throw Exception('网络请求失败');
//   }
// }
}
