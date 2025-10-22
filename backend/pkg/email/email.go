package email

import (
	"WorkReport/internal/config"
	"WorkReport/pkg/tools"
	"bytes"
	"crypto/tls"
	"net/smtp"
	"text/template"
	"time"

	"github.com/jordan-wright/email"
	"github.com/youcd/toolkit/log"
)

func SenEmail(content string) error {
	e := email.NewEmail()
	e.From = config.Cfg.Email.User
	e.To = config.Cfg.Email.To // 收件地址

	e.Cc = config.Cfg.Email.Cc //抄送地址
	// 1️⃣ 从配置读取模板和数据
	subjectTpl := config.Cfg.Email.SubjectTpl
	subjectData := config.Cfg.Email.SubjectData

	// 2️⃣ 渲染模板
	var buf bytes.Buffer
	tpl, err := template.New("subject").Funcs(template.FuncMap{
		"weekStart": func() time.Time { return tools.GetFirstDateOfWeek() },
		"weekEnd":   func() time.Time { return tools.GetFirstDateOfWeek().AddDate(0, 0, 4) },
		"dateFormat": func(t time.Time, layout string) string {
			return t.Format("20060102")
		},
	}).
		Parse(subjectTpl)
	if err != nil {
		log.Error("解析主题模板失败:", err)
		return err
	}
	if err := tpl.Execute(&buf, subjectData); err != nil {
		log.Error("渲染主题模板失败:", err)
		return err
	}
	e.Subject = buf.String()

	e.Text = []byte(content)

	// 使用正确的SMTP配置
	port := config.Cfg.Email.Port
	address := config.Cfg.Email.Host + ":" + port

	// 确保auth参数正确
	auth := smtp.PlainAuth("", config.Cfg.Email.User, config.Cfg.Email.Pwd, config.Cfg.Email.Host)

	return e.SendWithTLS(address, auth, &tls.Config{
		ServerName:         config.Cfg.Email.Host,
		InsecureSkipVerify: true,
	})
}
