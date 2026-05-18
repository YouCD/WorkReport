package utils

import (
	"strconv"
)

func StrToInt32(i string) int32 {
	pageindex, _ := strconv.Atoi(i)
	//nolint:gosec
	return int32(pageindex)
}

func StrToInt(i string) int {
	pageindex, _ := strconv.Atoi(i)

	return pageindex
}

func Str2int64(str string) int64 {
	num, _ := strconv.ParseInt(str, 10, 64)
	return num
}
