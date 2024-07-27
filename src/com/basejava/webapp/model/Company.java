package com.basejava.webapp.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public final class Company implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String website;
    private List<Period> periods;

    public Company() {
    }

    public Company(String name, String website, List<Period> periods) {
        this.name = name;
        this.website = website;
        this.periods = periods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        if (this.periods == null) {
            this.periods = periods;
        } else {
            this.periods.clear();
            this.periods.addAll(periods);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;
        return Objects.equals(name, company.name) && Objects.equals(website, company.website)
                && Objects.equals(periods, company.periods);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(website);
        result = 31 * result + Objects.hashCode(periods);
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
}
