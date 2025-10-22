package config

import (
	"os"

	"github.com/spf13/viper"
	"gopkg.in/yaml.v3"
)

type LLM struct {
	ApiKey  string `json:"apiKey,omitempty" yaml:"apiKey"`
	BaseURL string `json:"baseURL,omitempty" yaml:"baseURL"`
	Model   string `json:"model,omitempty" yaml:"model"`
	Prompt  string `json:"prompt,omitempty" yaml:"prompt"`
}
type DB struct {
	Host string `json:"host,omitempty" yaml:"host"`
	User string `json:"user,omitempty" yaml:"user"`
	Pwd  string `json:"pwd,omitempty" yaml:"pwd"`
	Port string `json:"port,omitempty" yaml:"port"`
	Name string `json:"name,omitempty" yaml:"name"`
}
type Email struct {
	Host string `json:"host,omitempty" yaml:"host"`
	Port string `json:"port,omitempty" yaml:"port"`
	User string `json:"user,omitempty" yaml:"user"`
	Pwd  string `json:"pwd,omitempty" yaml:"pwd"`

	To          []string               `json:"to,omitempty" yaml:"to"`                   // 收件人
	Cc          []string               `json:"cc,omitempty" yaml:"cc"`                   // 抄送人
	SubjectTpl  string                 `json:"subjectTpl,omitempty" yaml:"subjectTpl"`   // 邮件主题
	SubjectData map[string]interface{} `json:"subjectData,omitempty" yaml:"subjectData"` // 邮件主题数据
	ContentTpl  string                 `json:"contentTpl,omitempty" yaml:"contentTpl"`   // 邮件内容模板
}
type Global struct {
	LogLevel string `json:"logLevel,omitempty" yaml:"logLevel"`
	Port     string `json:"port,omitempty" yaml:"port"`
	Token    string `json:"token,omitempty" yaml:"token"`
}
type config struct {
	LLM    LLM    `json:"llm" yaml:"llm"`
	DB     DB     `json:"db" yaml:"db"`
	Global Global `json:"global" yaml:"global"`
	Email  Email  `json:"email" yaml:"email"`
}

var (
	Cfg config
)

func ParserConfig(configFile string) {
	v := viper.New()
	v.SetConfigFile(configFile)
	v.SetConfigType("yaml")
	if err := v.ReadInConfig(); err != nil {
		panic(err)
	}
	err := v.Unmarshal(&Cfg)
	if err != nil {
		panic(err)
	}
	file, _ := os.ReadFile(configFile)
	var data map[string]interface{}
	err = yaml.Unmarshal(file, &data)
	if err != nil {
		panic(err)
	}
	subjectData := data["email"].(map[string]interface{})["subjectData"].(map[string]interface{})
	Cfg.Email.SubjectData = subjectData
}
