package com.basejava.webapp.web;

import com.basejava.webapp.Config;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.*;
import com.basejava.webapp.storage.Storage;
import com.basejava.webapp.util.ServletHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getInstance().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "view" -> {
                request.setAttribute("resume", storage.get(uuid));
                request.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(request, response);
            }
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect(request.getContextPath() + "/list");
            }
            case "edit" -> {
                request.setAttribute("resume", storage.get(uuid));
                request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
            }
            case "add" -> {
                request.setAttribute("resume", new Resume());
                request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
            }
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName").trim();

        Resume resume;
        boolean exist;

        try {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
            exist = true;
        } catch (NotExistStorageException e) {
            resume = new Resume(uuid, fullName);
            exist = false;
        }

        if (fullName.isEmpty()) {
            if (exist) {
                response.sendRedirect("list?uuid=" + uuid + "&action=edit");
            } else {
                response.sendRedirect("list?action=add");
            }
            return;
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                resume.putContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            Section section = switch (type) {
                case PERSONAL, OBJECTIVE -> new TextSection(value);
                case ACHIEVEMENTS, QUALIFICATIONS -> new ListTextSection(Arrays.stream(value.trim().split("\\n"))
                        .filter(text -> text != null && !text.trim().isEmpty())
                        .map(String::trim)
                        .toList());
                case EXPERIENCE, EDUCATION -> {
                    List<Company> companies = new ArrayList<>();
                    int i = 0;
                    int j = 0;
                    while (true) {
                        String companyName = request.getParameter(type.name() + "_company_name_" + i + "_" + j);
                        String companyUrl = request.getParameter(type.name() + "_company_url_" + i + "_" + j);

                        if (companyName == null || companyUrl == null) {
                            break;
                        }

                        List<Period> periods = new ArrayList<>();
                        while (true) {
                            String periodStartDate = request.getParameter(type.name() + "_period_start_date_" + i + "_" + j);
                            String periodEndDate = request.getParameter(type.name() + "_period_end_date_" + i + "_" + j);
                            String periodTitle = request.getParameter(type.name() + "_period_title_" + i + "_" + j);
                            String periodDescription = request.getParameter(type.name() + "_period_description_" + i + "_" + j);
                            if (periodStartDate == null || periodEndDate == null || periodTitle == null || periodDescription == null) {
                                break;
                            }
                            LocalDate startDate = ServletHelper.parseLocalDate(periodStartDate);
                            LocalDate endDate = ServletHelper.parseLocalDate(periodEndDate);
                            if (ServletHelper.validateText(periodTitle) || ServletHelper.validateText(periodDescription) ||
                                    startDate != null || endDate != null) {
                                periods.add(new Period(startDate, endDate, periodTitle.trim(), periodDescription.trim()));
                            }
                            j++;
                        }
                        if (ServletHelper.validateText(companyName)) {
                            companies.add(new Company(companyName.trim(), companyUrl.trim(), periods));
                        }
                        i++;
                        j = 0;
                    }

                    yield companies.isEmpty() ? null : new CompanySection(companies);
                }
            };

            if (ServletHelper.validateText(value) || section != null &&
                    (type == SectionType.EXPERIENCE || type == SectionType.EDUCATION)) {
                resume.putSection(type, section);
            } else {
                resume.getSections().remove(type);
            }
        }

        if (exist) {
            storage.update(resume);
        } else {
            storage.save(resume);
        }

        response.sendRedirect(request.getContextPath() + "/list");
    }
}
