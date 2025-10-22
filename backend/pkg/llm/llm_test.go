package llm

import (
	"WorkReport/internal/config"
	"context"
	"testing"

	"github.com/youcd/toolkit/db"
	"gorm.io/gorm/logger"
)

var (
	a = `[{"type1":"运维","type2":["k8s","数据库","方案","技术评审","缓存","监控","微服务","CICD","质量管理","组件","基建","IAAS","版本发布","测试","备份与恢复","阿里云","网络","服务器","日志"]},{"type1":"项目管理","type2":["会议","项目规划"]},{"type1":"开发","type2":["工具开发","平台开发"]},{"type1":"其他","type2":["其他"]},{"type1":"技术支持","type2":["技术支持"]}]`
)

func init() {
	config.ParserConfig("/home/ycd/self_data/source_code/WorkReport/backend/internal/config/config.local.yaml")
	db.InitDB(config.Cfg.DB.User, config.Cfg.DB.Pwd, config.Cfg.DB.Host, config.Cfg.DB.Port, config.Cfg.DB.Name, logger.Silent)
}
func TestWorkTypes(t *testing.T) {
	tt, err := WorkTypes(context.Background(), "SSCRA 固件检测环境搭建", a)
	if err != nil {
		t.Error(err)
	}
	t.Log(tt)
}
