package Weblib

import (
	"WorkReport/web/model"
	"WorkReport/web/model/utils"
	"bytes"
	"encoding/json"
	"github.com/gin-gonic/gin"
	"io"
)

type User struct {
	Username string `json:"username"`
	Password string `json:"password"`
}
type JwtRespData struct {
	Token string `json:"token"`
	Uid   string `json:"uid"`
}

func Login(ctx *gin.Context) {
	data, err := io.ReadAll(ctx.Request.Body)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}
	ctx.Request.Body = io.NopCloser(bytes.NewBuffer(data))
	user := User{}
	err = json.Unmarshal(data, &user)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}

	h := model.UserTableMgr(utils.GetDB())
	u, err := h.GetFromUserName(user.Username)
	if err != nil {
		ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
		return
	}

	if PasswordVerify(user.Password, u.Password) {
		jwtToken, err := GenerateToken(u.UserName)
		if err != nil {
			ctx.JSON(500, NewEmptyDataErrorResponse(ErrToMsg(err)))
			return
		}
		var rsp JwtRespData
		rsp.Token = jwtToken
		rsp.Uid = u.UserName
		ctx.JSONP(200, NewSuccessResponse("登入成功", rsp))
	} else {
		ctx.JSONP(200, NewEmptyDataErrorResponse("登入出错"))
		return
	}
}
