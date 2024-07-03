package weblib

import (
	"time"

	"github.com/dgrijalva/jwt-go"
)

type MyAuth struct {
	Username string `json:"username"`
	jwt.StandardClaims
}

const TokenExpireDuration = time.Hour * 24 * 8

var MySecret = []byte("@c3)#n&w6Tvfv$564xg4*3dv7i_vdqwdwqdqj56r758421fd9707)(%$@DCVSw537")

// GenerateToken 生成JWT
func GenerateToken(username string) (string, error) {
	// 创建一个我们自己的声明
	c := MyAuth{
		username, // 自定义字段
		// roles,
		jwt.StandardClaims{
			ExpiresAt: time.Now().Add(TokenExpireDuration).Unix(), // 过期时间
			Issuer:    "WorkLog System",                           // 签发人
		},
	}
	// 使用指定的签名方法创建签名对象
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, c)
	// 使用指定的secret签名并获得完整的编码后的字符串token
	//nolint:wrapcheck
	return token.SignedString(MySecret)
}

// ParseToken 解析JWT
func ParseToken(tokenString string) (*MyAuth, bool) {
	// 解析token
	token, err := jwt.ParseWithClaims(tokenString, &MyAuth{}, func(_ *jwt.Token) (interface{}, error) {
		return MySecret, nil
	})
	if err != nil {
		return nil, false
	}

	if claims, ok := token.Claims.(*MyAuth); ok && token.Valid { // 校验token
		return claims, true
	}
	return nil, false
}
