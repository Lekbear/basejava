package com.basejava.webapp.model;

import java.util.List;
import java.util.Objects;

public final class Company {
    private String name;
    private String website;
    private final List<Period> periods;

    public Company(String name, String website, List<Period> periods) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(website, "website must not be null");
        checkPeriodsNoNull(periods);
        this.name = name;
        this.website = website;
        this.periods = periods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        Objects.requireNonNull(website, "website must not be null");
        this.website = website;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        checkPeriodsNoNull(periods);
        this.periods.clear();
        this.periods.addAll(periods);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;
        return name.equals(company.name) && website.equals(company.website) && periods.equals(company.periods);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + website.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        ans.append(name);
        ans.append(" ");
        ans.append(website);
        for (Period period : periods) {
            ans.append("\n\t\t\t");
            ans.append(period);
        }
        return ans.toString();
    }

    private void checkPeriodsNoNull(List<Period> periods) {
        Objects.requireNonNull(periods, "periods must not be null");
        for (Period period : periods) {
            Objects.requireNonNull(period, "period must not be null");
        }
    }
}
