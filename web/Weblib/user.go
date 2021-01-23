package Weblib

import (
	"WorkReport/web/model"
	"WorkReport/web/model/utils"
	"bytes"
	"encoding/json"
	"github.com/gin-gonic/gin"
	"io/ioutil"
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
	data, err := ioutil.ReadAll(ctx.Request.Body)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
	}
	ctx.Request.Body = ioutil.NopCloser(bytes.NewBuffer(data))
	user := User{}
	err = json.Unmarshal(data, &user)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}

	h := model.UserTableMgr(utils.GetDB())
	u, err := h.GetFromUserName(user.Username)
	if err != nil {
		errrsp.Msg = ErrToMsg(err)
		ctx.JSON(500, errrsp)
		return
	}

	if PasswordVerify(user.Password, u.Password) {
		jwtToken, err := GenerateToken(u.UserName)
		if err != nil {
			errrsp.Msg = ErrToMsg(err)
			ctx.JSON(500, errrsp)
			return
		}
		var rsp JwtRespData
		rsp.Token = jwtToken
		rsp.Uid = u.UserName
		suRsp.Data = rsp
		suRsp.Msg = "登入成功"
		ctx.JSONP(200, suRsp)
	} else {
		errrsp.Msg = "登入出错"
		ctx.JSON(500, errrsp)
		return
	}
}
