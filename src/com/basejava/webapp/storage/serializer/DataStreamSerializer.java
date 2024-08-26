package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            writeWithException(resume.getContacts().entrySet(), dos, entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            writeWithException(resume.getSections().entrySet(), dos, entry -> {
                dos.writeUTF(entry.getKey().name());
                switch (entry.getKey()) {
                    case PERSONAL, OBJECTIVE -> {
                        dos.writeUTF(((TextSection) entry.getValue()).getText());
                    }
                    case ACHIEVEMENTS, QUALIFICATIONS -> {
                        writeWithException(((ListTextSection) entry.getValue()).getTexts(), dos, dos::writeUTF);
                    }
                    case EXPERIENCE, EDUCATION -> {
                        writeWithException(((CompanySection) entry.getValue()).getCompanies(), dos, company -> {
                            dos.writeUTF(company.getName());
                            dos.writeUTF(company.getWebsite());
                            writeWithException(company.getPeriods(), dos, period -> {
                                dos.writeUTF(period.getStartDate().toString());
                                dos.writeUTF(period.getEndDate().toString());
                                dos.writeUTF(period.getTitle());
                                dos.writeUTF(period.getDescription());
                            });
                        });
                    }
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());

            resume.putAllContact(readMapWithException(dis, () ->
                    new AbstractMap.SimpleEntry<>(ContactType.valueOf(dis.readUTF()), dis.readUTF())));

            resume.putAllSection(readMapWithException(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                return new AbstractMap.SimpleEntry<>(sectionType, switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> new TextSection(dis.readUTF());
                    case ACHIEVEMENTS, QUALIFICATIONS -> new ListTextSection(readListWithException(dis, dis::readUTF));
                    case EXPERIENCE, EDUCATION -> new CompanySection(readListWithException(dis, () ->
                            new Company(dis.readUTF(), dis.readUTF(), readListWithException(dis, () ->
                                    new Period(LocalDate.parse(dis.readUTF()), LocalDate.parse(dis.readUTF()),
                                            dis.readUTF(), dis.readUTF())))));
                });
            }));

            return resume;
        }
    }

    @FunctionalInterface
    public interface CustomConsumer<T> {
        void accept(T t) throws IOException;
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos,
                                        CustomConsumer<? super T> action) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            action.accept(t);
        }
    }

    @FunctionalInterface
    public interface ReadListCustomConsumer<T> {
        T accept() throws IOException;
    }

    private <T> ArrayList<T> readListWithException(DataInputStream dis, ReadListCustomConsumer<T> action)
            throws IOException {
        int size = dis.readInt();
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(action.accept());
        }
        return list;
    }

    @FunctionalInterface
    public interface ReadMapCustomConsumer<K, V> {
        Map.Entry<K, V> accept() throws IOException;
    }

    private <K, V> HashMap<K, V> readMapWithException(DataInputStream dis, ReadMapCustomConsumer<K, V> action)
            throws IOException {
        int size = dis.readInt();
        HashMap<K, V> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            Map.Entry<K, V> entry = action.accept();
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
