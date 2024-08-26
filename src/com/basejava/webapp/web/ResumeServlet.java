package com.basejava.webapp.web;

import com.basejava.webapp.Config;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.*;
import com.basejava.webapp.storage.Storage;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
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
                case ACHIEVEMENTS, QUALIFICATIONS -> new ListTextSection(List.of(value.trim().split("\\n")));
                case EXPERIENCE, EDUCATION -> null;
            };

            if (type == SectionType.EXPERIENCE || type == SectionType.EDUCATION) {
                continue;
            }

            if (value != null && !value.trim().isEmpty()) {
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
