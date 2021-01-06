package utils

import (
	"database/sql"
	"encoding/json"
	"time"

	"log"
	"strconv"
)

func IntToStr(i int) string {
	return strconv.Itoa(i)
}
func StrToInt32(i string) int32 {
	pageindex, _ := strconv.Atoi(i)

	return int32(pageindex)
}

func StrToInt(i string) int {
	pageindex, _ := strconv.Atoi(i)

	return pageindex
}


func Int32ToStr(i int32) string {
	return strconv.FormatInt(int64(i), 10)
}

func GetRowsToMap(rows *sql.Rows) (results []map[string]string) {
	//defer rows.Close()
	cols, err := rows.Columns()
	if err != nil {
		log.Fatalln(err)
	}
	//fmt.Println(cols)
	vals := make([][]byte, len(cols))
	scans := make([]interface{}, len(cols))

	for i := range vals {
		scans[i] = &vals[i]
	}

	//var pod model.K8sPod
	//mapInstance := make(map[string]interface{})
	for rows.Next() {
		err = rows.Scan(scans...)
		if err != nil {
			log.Fatalln(err)
		}

		row := make(map[string]string)
		for k, v := range vals {
			key := cols[k]
			row[key] = string(v)
		}
		results = append(results, row)
	}
	return
}

func GetRowsToJson(rows *sql.Rows) (jsonByte []byte) {
	defer rows.Close()
	cols, err := rows.Columns()
	if err != nil {
		log.Fatalln(err)
	}
	vals := make([][]byte, len(cols))
	scans := make([]interface{}, len(cols))

	for i := range vals {
		scans[i] = &vals[i]
	}

	var results = make([]map[string]string, 0)
	for rows.Next() {
		err = rows.Scan(scans...)
		if err != nil {
			log.Fatalln(err)
		}

		row := make(map[string]string)
		for k, v := range vals {
			key := cols[k]
			row[key] = string(v)
		}
		results = append(results, row)
	}
	jsonByte, _ = json.Marshal(results)

	return
}

func Str2int64(str string) int64 {
	num, _ := strconv.ParseInt(str, 10, 64)
	return num
}

func Date2Timestamp(date string) int64 {
	loc, _ := time.LoadLocation("Local")
	the_time, err := time.ParseInLocation("2006-01-02 15:04:05", date+" 00:00:00", loc)
	if err == nil {
		unix_time := the_time.Unix()
		return unix_time
	}
	return 0
}
