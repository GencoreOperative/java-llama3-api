package uk.co.gencoreoperative.runner;

public enum ModelType {
    LLAMA3("llama-3"),
    QWEN2("qwen2");

    private final String searchString;

    ModelType(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchString() {
        return searchString;
    }
}
