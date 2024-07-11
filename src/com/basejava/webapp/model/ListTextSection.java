package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListTextSection extends Section {
    private final List<String> texts = new ArrayList<>();

    public ListTextSection(List<String> texts) {
        checkTextsNonNull(texts);
        this.texts.addAll(texts);
    }

    public List<String> getTexts() {
        return new ArrayList<>(texts);
    }

    public void setTexts(List<String> texts) {
        checkTextsNonNull(texts);
        this.texts.clear();
        this.texts.addAll(texts);
    }

    public void add(String text) {
        Objects.requireNonNull(text, "text must not be null");
        texts.add(text);
    }

    public void delete(int index) {
        Objects.checkIndex(index, texts.size());
        texts.remove(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListTextSection that = (ListTextSection) o;
        return texts.equals(that.texts);
    }

    @Override
    public int hashCode() {
        return texts.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (String text : texts) {
            ans.append("\n\t\t");
            ans.append(text);
        }
        return ans.toString();
    }

    private void checkTextsNonNull(List<String> texts) {
        Objects.requireNonNull(texts, "texts must not be null");
        for (String text : texts) {
            Objects.requireNonNull(text, "text must not be null");
        }
    }
}
