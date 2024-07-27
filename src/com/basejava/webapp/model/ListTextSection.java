package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListTextSection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<String> texts = new ArrayList<>();

    public ListTextSection() {
    }

    public ListTextSection(List<String> texts) {
        this.texts.addAll(texts);
    }

    public List<String> getTexts() {
        return new ArrayList<>(texts);
    }

    public void setTexts(List<String> texts) {
        this.texts.clear();
        this.texts.addAll(texts);
    }

    public void add(String text) {
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
        return Objects.equals(texts, that.texts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(texts);
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
}
