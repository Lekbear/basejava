package com.basejava.webapp;

import com.basejava.webapp.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumeTestData {
    private static final Map<SectionType, String> TEXTS = new HashMap<>();
    private static final Map<SectionType, List<String>> LISTTEXTS = new HashMap<>();
    private static final Map<SectionType, List<LocalDate>> STARTDATES = new HashMap<>();
    private static final Map<SectionType, List<LocalDate>> ENDDATES = new HashMap<>();
    private static final Map<SectionType, List<String>> TITLES = new HashMap<>();
    private static final Map<SectionType, List<String>> DESCRIPTIONS = new HashMap<>();
    private static final Map<SectionType, List<List<Period>>> PERIODS = new HashMap<>();
    private static final Map<SectionType, List<String>> NAMES = new HashMap<>();
    private static final Map<SectionType, List<String>> WEBSITES = new HashMap<>();
    private static final Map<SectionType, List<Company>> COMPANIES = new HashMap<>();

    public static void main(String[] args) {
        Resume resume = getNewResume("uuid1", "Григорий Кислин");
        System.out.println(resume);
    }

    public static Resume getNewResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        resume.putContact(ContactType.TELEPHONE, "+79218550482");
        resume.putContact(ContactType.SKYPE, "skype:grigory.kislin");
        resume.putContact(ContactType.EMAIL, "gkislin@yandex.ru");

        for (SectionType sectionType : SectionType.values()) {
            Section section = switch (sectionType) {
                case PERSONAL, OBJECTIVE -> retrieveTextSection(sectionType);
                case ACHIEVEMENTS, QUALIFICATIONS -> retrieveListTextSection(sectionType);
                case EXPERIENCE, EDUCATION -> retrieveCompanySection(sectionType);
            };
            resume.putSection(sectionType, section);
        }

        return resume;
    }

    private static Section retrieveTextSection(SectionType sectionType) {
        if (TEXTS.get(sectionType) == null) {
            fillText(sectionType);
        }
        return new TextSection(TEXTS.get(sectionType));
    }

    private static void fillText(SectionType sectionType) {
        if (sectionType == SectionType.PERSONAL) {
            TEXTS.put(sectionType, "Аналитический склад ума, сильная логика, креативность, инициативность. " +
                    "Пурист кода и архитектуры.");
        } else if (sectionType == SectionType.OBJECTIVE) {
            TEXTS.put(sectionType, "Ведущий стажировок и корпоративного обучения по Java Web и " +
                    "Enterprise технологиям");
        }
    }

    private static Section retrieveListTextSection(SectionType sectionType) {
        if (LISTTEXTS.get(sectionType) == null) {
            fillListText(sectionType);
        }
        return new ListTextSection(LISTTEXTS.get(sectionType));
    }

    private static void fillListText(SectionType sectionType) {
        List<String> listTexts = new ArrayList<>();

        if (sectionType == SectionType.ACHIEVEMENTS) {
            listTexts.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: " +
                    "приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов " +
                    "на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для " +
                    "комплексных DIY смет");
            listTexts.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                    "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). " +
                    "Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. " +
                    "Более 3500 выпускников.");
            listTexts.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                    "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        } else if (sectionType == SectionType.QUALIFICATIONS) {
            listTexts.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
            listTexts.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
            listTexts.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        }

        LISTTEXTS.put(sectionType, listTexts);
    }

    private static Section retrieveCompanySection(SectionType sectionType) {
        if (STARTDATES.get(sectionType) == null) {
            fillStartDates(sectionType);
        }

        if (ENDDATES.get(sectionType) == null) {
            fillEndDates(sectionType);
        }

        if (TITLES.get(sectionType) == null) {
            fillTitles(sectionType);
        }

        if (DESCRIPTIONS.get(sectionType) == null) {
            fillDescriptions(sectionType);
        }

        if (PERIODS.get(sectionType) == null) {
            fillPeriods(sectionType);
        }

        if (NAMES.get(sectionType) == null) {
            fillNames(sectionType);
        }

        if (WEBSITES.get(sectionType) == null) {
            fillWebsites(sectionType);
        }

        if (COMPANIES.get(sectionType) == null) {
            fillCompanies(sectionType);
        }

        return new CompanySection(COMPANIES.get(sectionType));
    }

    private static void fillStartDates(SectionType sectionType) {
        List<LocalDate> startDates = new ArrayList<>();

        if (sectionType == SectionType.EXPERIENCE) {
            startDates.add(LocalDate.of(2013, 10, 1));
            startDates.add(LocalDate.of(2014, 10, 1));
            startDates.add(LocalDate.of(2012, 4, 1));
        } else if (sectionType == SectionType.EDUCATION) {
            startDates.add(LocalDate.of(2013, 3, 1));
            startDates.add(LocalDate.of(2011, 3, 1));
            startDates.add(LocalDate.of(2005, 1, 1));
        }

        STARTDATES.put(sectionType, startDates);
    }

    private static void fillEndDates(SectionType sectionType) {
        List<LocalDate> endDates = new ArrayList<>();

        if (sectionType == SectionType.EXPERIENCE) {
            endDates.add(LocalDate.now());
            endDates.add(LocalDate.of(2016, 1, 1));
            endDates.add(LocalDate.of(2014, 10, 1));
        } else if (sectionType == SectionType.EDUCATION) {
            endDates.add(LocalDate.of(2013, 5, 1));
            endDates.add(LocalDate.of(2011, 4, 1));
            endDates.add(LocalDate.of(2005, 4, 1));
        }

        ENDDATES.put(sectionType, endDates);
    }

    private static void fillTitles(SectionType sectionType) {
        List<String> titles = new ArrayList<>();

        if (sectionType == SectionType.EXPERIENCE) {
            titles.add("Автор проекта.");
            titles.add("Старший разработчик (backend)");
            titles.add("Java архитектор");
        } else if (sectionType == SectionType.EDUCATION) {
            titles.add("Functional Programming Principles in Scala' by Martin Odersky");
            titles.add("Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'");
            titles.add("3 месяца обучения мобильным IN сетям (Берлин)");
        }

        TITLES.put(sectionType, titles);
    }

    private static void fillDescriptions(SectionType sectionType) {
        List<String> descriptions = new ArrayList<>();

        if (sectionType == SectionType.EXPERIENCE) {
            descriptions.add("Создание, организация и проведение Java онлайн проектов и стажировок.");
            descriptions.add("Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, " +
                    "Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, " +
                    "авторизация по OAuth1, OAuth2, JWT SSO.");
            descriptions.add("Организация процесса разработки системы ERP для разных окружений: релизная политика, " +
                    "версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование " +
                    "системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка " +
                    "интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, " +
                    "экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера " +
                    "документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring " +
                    "MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via " +
                    "ssh tunnels, PL/Python");
        } else if (sectionType == SectionType.EDUCATION) {
            descriptions.add("");
            descriptions.add("");
            descriptions.add("");
        }

        DESCRIPTIONS.put(sectionType, descriptions);
    }

    private static void fillPeriods(SectionType sectionType) {
        List<List<Period>> periodsList = new ArrayList<>();
        int size = min4(STARTDATES.get(sectionType).size(), ENDDATES.get(sectionType).size(),
                TITLES.get(sectionType).size(), DESCRIPTIONS.get(sectionType).size());

        for (int i = 0; i < size; i++) {
            List<Period> periods = new ArrayList<>();
            periods.add(new Period(STARTDATES.get(sectionType).get(i), ENDDATES.get(sectionType).get(i),
                    TITLES.get(sectionType).get(i), DESCRIPTIONS.get(sectionType).get(i)));
            periodsList.add(periods);
        }
        PERIODS.put(sectionType, periodsList);
    }

    private static int min4(int val1, int val2, int val3, int val4) {
        return Math.min(val1, Math.min(val2, Math.min(val3, val4)));
    }

    private static void fillNames(SectionType sectionType) {
        List<String> names = new ArrayList<>();

        if (sectionType == SectionType.EXPERIENCE) {
            names.add("Java Online Projects");
            names.add("Wrike");
            names.add("RIT Center");
        } else if (sectionType == SectionType.EDUCATION) {
            names.add("Coursera");
            names.add("Luxoft");
            names.add("Siemens AG");
        }

        NAMES.put(sectionType, names);
    }

    private static void fillWebsites(SectionType sectionType) {
        List<String> websites = new ArrayList<>();

        if (sectionType == SectionType.EXPERIENCE) {
            websites.add("https://javaops.ru/");
            websites.add("https://www.wrike.com/");
            websites.add("");
        } else if (sectionType == SectionType.EDUCATION) {
            websites.add("https://www.coursera.org/course/progfun");
            websites.add("https://www.luxoft-training.ru/training/catalog/course.html?ID=22366");
            websites.add("http://www.siemens.ru/");
        }

        WEBSITES.put(sectionType, websites);
    }

    private static void fillCompanies(SectionType sectionType) {
        List<Company> companies = new ArrayList<>();
        int size = min3(NAMES.get(sectionType).size(), WEBSITES.get(sectionType).size(),
                PERIODS.get(sectionType).size());

        for (int i = 0; i < size; i++) {
            companies.add(new Company(NAMES.get(sectionType).get(i), WEBSITES.get(sectionType).get(i),
                    PERIODS.get(sectionType).get(i)));
        }
        COMPANIES.put(sectionType, companies);
    }

    private static int min3(int val1, int val2, int val3) {
        return Math.min(val1, Math.min(val2, val3));
    }
}
