package llm

import (
	"WorkReport/internal/config"
	"WorkReport/pkg/types"
	"context"
	"encoding/json"
	"fmt"

	"github.com/sashabaranov/go-openai"
	"github.com/youcd/toolkit/log"
)

func WorkTypes(ctx context.Context, content, workTypes string) (*types.LlmType, error) {
	conf := openai.DefaultAnthropicConfig(config.Cfg.LLM.ApiKey, config.Cfg.LLM.BaseURL)
	conf.APIType = openai.APITypeOpenAI
	client := openai.NewClientWithConfig(conf)
	resp, err := client.CreateChatCompletion(
		ctx,
		openai.ChatCompletionRequest{
			Model: config.Cfg.LLM.Model,
			Messages: []openai.ChatCompletionMessage{
				{Role: "system", Content: fmt.Sprintf(promptA, workTypes)},
				{Role: "user", Content: content},
			},
			ResponseFormat: &openai.ChatCompletionResponseFormat{
				Type: openai.ChatCompletionResponseFormatTypeJSONObject,
			},
		},
	)
	if err != nil {
		log.Error("解码响应时发生错误:", err)
		return nil, err
	}

	Content := resp.Choices[0].Message.Content

	var t types.LlmType

	err = json.Unmarshal([]byte(Content), &t)
	if err != nil {
		return nil, err
	}

	return &t, nil
}
func WeekWorkLog(ctx context.Context, contents string) (*types.LlmContents, error) {
	conf := openai.DefaultAnthropicConfig(config.Cfg.LLM.ApiKey, config.Cfg.LLM.BaseURL)
	conf.APIType = openai.APITypeOpenAI
	client := openai.NewClientWithConfig(conf)
	resp, err := client.CreateChatCompletion(
		ctx,
		openai.ChatCompletionRequest{
			Model: config.Cfg.LLM.Model,
			Messages: []openai.ChatCompletionMessage{
				{Role: "system", Content: config.Cfg.LLM.Prompt},
				{Role: "user", Content: "工作内容: " + contents},
			},
			ResponseFormat: &openai.ChatCompletionResponseFormat{
				Type: openai.ChatCompletionResponseFormatTypeJSONObject,
			},
		},
	)
	if err != nil {
		log.Error("解码响应时发生错误:", err)
		return nil, err
	}

	Content := resp.Choices[0].Message.Content
	log.Debug(Content)
	var t types.LlmContents
	err = json.Unmarshal([]byte(Content), &t)
	if err != nil {
		return nil, err
	}

	return &t, nil
}
