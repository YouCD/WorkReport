package types

type LlmType struct {
	Type1   string `json:"type1"`
	Type2   string `json:"type2"`
	Content string `json:"content"`
}
type LlmContents struct {
	Contents map[string][]string `json:"contents"`
	Todo     []string            `json:"todo"`
}
