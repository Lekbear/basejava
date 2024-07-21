package com.basejava.webapp.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanySection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<Company> companies = new ArrayList<>();

    public CompanySection(List<Company> companies) {
        checkCompaniesNoNull(companies);
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
        checkCompaniesNoNull(companies);
        this.companies.clear();
        this.companies.addAll(companies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;
        return companies.equals(that.companies);
    }

    @Override
    public int hashCode() {
        return companies.hashCode();
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

    private void checkCompaniesNoNull(List<Company> companies) {
        Objects.requireNonNull(companies, "companies must not be null");
        for (Company company : companies) {
            Objects.requireNonNull(company, "company must not be null");
        }
    }
}
