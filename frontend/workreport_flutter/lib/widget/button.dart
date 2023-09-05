import 'package:flutter/material.dart';

class Button extends StatelessWidget {
  final GestureTapCallback onTap;

  final String title;
  final IconData? icon;

  const Button(
    this.title, {
    Key? key,
    required this.onTap,
    this.icon,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.center,
      decoration: const BoxDecoration(
        color: Colors.blue,
        borderRadius: BorderRadius.all(Radius.circular(20.0)),
      ),
      width: 80,
      height: 35,
      child: InkWell(
          onTap: onTap,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              if (icon != null)
                Icon(
                  icon,
                  color: Colors.white,
                ),
              Text(
                title,
                style: const TextStyle(color: Colors.white),
              )
            ],
          )),
    );
    ;
  }
}
