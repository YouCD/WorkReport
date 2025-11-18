package common

import (
	"testing"
)

func TestGetRelease(t *testing.T) {
	release := GetRelease()
	t.Log(release)
}
