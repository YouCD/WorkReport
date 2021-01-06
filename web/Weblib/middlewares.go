package Weblib

import (


	"github.com/gin-gonic/gin"

	"net/http"
)

//func InitMiddleware(services ...[]interface{}) gin.HandlerFunc {
//	return func(ctx *gin.Context) {
//		ctx.Keys = make(map[string]interface{})
//		for _, v := range services[0] {
//			switch ser := v.(type) {
//			case Services.SealHandlerService:
//				ctx.Keys["sealService"] = ser
//			case Services.UserService:
//				ctx.Keys["userService"] = ser
//			case Services.JWTAuthService:
//				ctx.Keys["jwtAuthService"] = ser
//			case Services.SecretService:
//				ctx.Keys["secretService"] = ser
//			case Services.RbacHandlerService:
//				ctx.Keys["rbacService"] = ser
//			}
//		}
//		ctx.Next()
//	}
//
//}

//func JWTAuthMiddleware(authService Services.JWTAuthService) gin.HandlerFunc {
//	return func(ctx *gin.Context) {
//		jwtStr := ctx.Request.Header.Get("jwt")
//		h := model.SysLogMgr(utils.GetDB())
//		result, err := h.GetFromToken(jwtStr)
//		if err != nil {
//			log.Println("token错误,err:", err.Error())
//			errrsp.Msg = "无效的Token"
//			ctx.JSON(401, errrsp)
//			ctx.Abort()
//			return
//		}
//		if result.IsOnline==0{
//			var req Services.AuthRequest
//			req.JwtToken = jwtStr
//			resp, err := authService.Auth(context.Background(), &req)
//			if err != nil {
//				log.Println("token错误,err:", err.Error())
//				errrsp.Msg = "无效的Token"
//				ctx.JSON(401, errrsp)
//				ctx.Abort()
//				return
//			}
//
//			if resp.IsLogin {
//				h := model.SysLogMgr(utils.GetDB())
//				res, err := h.GetFromToken(jwtStr)
//				if err != nil {
//					errrsp.Msg = "无效的Token,请重新登入"
//					ctx.JSON(401, errrsp)
//					ctx.Abort()
//					return
//				}
//				if res.Token != "" {
//					ctx.Keys["uid"] = resp.UserID
//					ctx.Keys["roles"] = resp.Roles
//					ctx.Next()
//				} else {
//					errrsp.Msg = "无效的Token,请重新登入"
//					ctx.JSON(401, errrsp)
//					ctx.Abort()
//					return
//				}
//			} else {
//				errrsp.Msg = "无效的Token"
//				ctx.JSON(401, errrsp)
//				ctx.Abort()
//				return
//			}
//		}else {
//			errrsp.Msg = "无效的Token"
//			ctx.JSON(401, errrsp)
//			ctx.Abort()
//			return
//		}
//
//	}
//}

//处理跨域
func CorsMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		method := c.Request.Method
		c.Header("Access-Control-Allow-Origin", "*")
		c.Header("Access-Control-Allow-Headers", "Content-Type,AccessToken,X-CSRF-Token, Authorization, jwt")
		c.Header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
		c.Header("Access-Control-Expose-Headers", "Content-Length, Access-Control-Allow-Origin, Access-Control-Allow-Headers, Content-Type")
		c.Header("Access-Control-Allow-Credentials", "true")

		//放行所有OPTIONS方法
		if method == "OPTIONS" {
			c.AbortWithStatus(http.StatusNoContent)
		}
		// 处理请求
		c.Next()
	}
}

//Rbac拦截器
//func Authorize(e *casbin.Enforcer) gin.HandlerFunc {
//
//	return func(ctx *gin.Context) {
//
//		//获取请求的URI
//		obj := ctx.Request.URL.Path
//		//获取请求方法
//		act := ctx.Request.Method
//		//获取用户的角色
//		roles, _ := ctx.Get("roles")
//		sub := roles.(string)
//		//判断策略中是否存在
//		if ok := e.Enforce(sub, obj, act); ok {
//			ctx.Next()
//		} else {
//			errrsp.Msg = "权限不足"
//			ctx.JSON(402, errrsp)
//			ctx.Abort()
//			return
//			ctx.Abort()
//		}
//	}
//}
//
//func ApiAuthMiddleware(authService Services.JWTAuthService) gin.HandlerFunc {
//	return func(ctx *gin.Context) {
//		jwtStr := ctx.Request.Header.Get("jwt")
//
//		//model.Sec
//
//		var req Services.AuthRequest
//		req.JwtToken = jwtStr
//		rsp, err := authService.Auth(context.Background(), &req)
//		if err != nil {
//			log.Println("token错误,err:", err.Error())
//			errrsp.Msg = "无效的Token"
//			ctx.JSON(401, errrsp)
//			ctx.Abort()
//			return
//		}
//
//		if rsp.IsLogin {
//			h := model.SysLogMgr(utils.GetDB())
//			res, err := h.GetFromToken(jwtStr)
//			if err != nil {
//				errrsp.Msg = "无效的Token,请重新登入"
//				ctx.JSON(401, errrsp)
//				ctx.Abort()
//				return
//			}
//			if res.Token != "" {
//				ctx.Keys["uid"] = rsp.UserID
//				ctx.Keys["roles"] = rsp.Roles
//				ctx.Next()
//			} else {
//				errrsp.Msg = "无效的Token,请重新登入"
//				ctx.JSON(401, errrsp)
//				ctx.Abort()
//				return
//			}
//
//		} else {
//			errrsp.Msg = "无效的Token"
//			ctx.JSON(401, errrsp)
//			ctx.Abort()
//			return
//		}
//	}
//}
