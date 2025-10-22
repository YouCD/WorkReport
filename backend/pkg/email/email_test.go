package email

import (
	"WorkReport/internal/config"
	"testing"

	"github.com/youcd/toolkit/log"
)

func init() {
	config.ParserConfig("/home/ycd/self_data/source_code/WorkReport/backend/internal/config/config.local.yaml")
	log.Init(nil)
}
func TestSenEmail(t *testing.T) {
	//
	//	SenEmail(`一、本周工作内容：
	//      1、SSCRA：
	//          * SSCRA v1.6.2 编排测试（90%）
	//      2、smartflex：
	//          * 部署工具支持一键拉起服务和更新服务（100%）
	//          * 处理本地测试Kafka集群故障（85%）
	//
	//二、问题：
	//    无
	//
	//三、建议：
	//    无
	//
	//四、下周工作计划：
	//    1、远程技术支持：SSCRA、XDR、柔性等相关业务（持续）
	//    2、处理本地测试Kafka集群故障（持续）
	//    3、SSCRA v1.6.2 编排测试（持续）
	//五、Bug处理情况：
	//    无
	//`)
}
