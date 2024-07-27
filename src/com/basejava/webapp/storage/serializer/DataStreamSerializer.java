package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size());

            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            Map<SectionType, Section> sections = resume.getSections();
            dos.writeInt(sections.size());

            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());

                switch (entry.getKey()) {
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) entry.getValue()).getText());
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> texts = ((ListTextSection) entry.getValue()).getTexts();
                        dos.writeInt(texts.size());

                        for (String text : texts) {
                            dos.writeUTF(text);
                        }
                    }
                    case EXPERIENCE, EDUCATION -> {
                        List<Company> companies = ((CompanySection) entry.getValue()).getCompanies();
                        dos.writeInt(companies.size());

                        for (Company company : companies) {
                            dos.writeUTF(company.getName());
                            dos.writeUTF(company.getWebsite());
                            List<Period> periods = company.getPeriods();
                            dos.writeInt(periods.size());

                            for (Period period : periods) {
                                dos.writeUTF(period.getStartDate().toString());
                                dos.writeUTF(period.getEndDate().toString());
                                dos.writeUTF(period.getTitle());
                                dos.writeUTF(period.getDescription());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());

            int sizeContacts = dis.readInt();
            for (int i = 0; i < sizeContacts; i++) {
                resume.putContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sizeSections = dis.readInt();
            for (int i = 0; i < sizeSections; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                Section section = switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> new TextSection(dis.readUTF());
                    case ACHIEVEMENT, QUALIFICATIONS -> retrieveListTextSection(dis);
                    case EXPERIENCE, EDUCATION -> retrieveCompanySection(dis);
                };
                resume.putSection(sectionType, section);
            }
            return resume;
        }
    }

    private ListTextSection retrieveListTextSection(DataInputStream dis) throws IOException {
        List<String> texts = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            texts.add(dis.readUTF());
        }
        return new ListTextSection(texts);
    }

    private CompanySection retrieveCompanySection(DataInputStream dis) throws IOException {
        List<Company> companies = new ArrayList<>();

        int sizeCompanies = dis.readInt();
        for (int i = 0; i < sizeCompanies; i++) {
            String name = dis.readUTF();
            String webSite = dis.readUTF();

            List<Period> periods = new ArrayList<>();
            int sizePeriods = dis.readInt();
            for (int j = 0; j < sizePeriods; j++) {
                periods.add(new Period(LocalDate.parse(dis.readUTF()), LocalDate.parse(dis.readUTF()), dis.readUTF(), dis.readUTF()));
            }
            companies.add(new Company(name, webSite, periods));
        }
        return new CompanySection(companies);
    }
}
