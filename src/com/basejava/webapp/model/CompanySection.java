package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class CompanySection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<Company> companies = new ArrayList<>();

    public CompanySection() {
    }

    public CompanySection(List<Company> companies) {
        this.companies.addAll(companies);
    }

    public void add(Company company) {
        companies.add(company);
    }

    public void delete(int index) {
        Objects.checkIndex(index, companies.size());
        companies.remove(index);
    }

    public List<Company> getCompanies() {
        return new ArrayList<>(companies);
    }

    public void setCompanies(List<Company> companies) {
        this.companies.clear();
        this.companies.addAll(companies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;
        return Objects.equals(companies, that.companies);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(companies);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (Company company : companies) {
            ans.append("\n\t\t");
            ans.append(company);
        }
        return ans.toString();
    }
}
