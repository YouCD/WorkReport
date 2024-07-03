package utils

import (
	"strconv"
	"time"
)

func IntToStr(i int) string {
	return strconv.Itoa(i)
}
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

func Date2Timestamp(date string) int64 {
	loc, _ := time.LoadLocation("Local")
	theTime, err := time.ParseInLocation("2006-01-02 15:04:05", date+" 00:00:00", loc)
	if err == nil {
		return theTime.Unix()
	}
	return 0
}
