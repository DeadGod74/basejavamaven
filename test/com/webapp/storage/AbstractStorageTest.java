package com.webapp.storage;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webapp.Config;
import com.webapp.exception.ExistStorageException;
import com.webapp.exception.NotExistStorageException;
import com.webapp.model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest  {

    protected final Storage storage;
    //protected static final File FILE_PATH = new File("/Users/deadgod/IdeaProjects/basejava/storage");
    protected static final File FILE_PATH = Config.get().getStorageDir();
    private static final String UUID_1 = UUID.randomUUID().toString();
    private static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();
    private static final String UUID_4 = UUID.randomUUID().toString();;
    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;

    private final String dummy = "dummy";

    static {
        RESUME_1 = new Resume(UUID_1, "Alice Smith");
        RESUME_2 = new Resume(UUID_2, "Bob Johnson");
        RESUME_3 = new Resume(UUID_3, "Charlie Brown");
        RESUME_4 = new Resume(UUID_4, "Diana Prince");

        RESUME_1.setContact(ContactType.MAIL, "alice.smith@example.com");
        RESUME_1.setContact(ContactType.PHONE, "+9876543210");
        RESUME_1.setSection(TypeSection.OBJECTIVE, new TextSection("Seeking a challenging position in software development"));
        RESUME_1.setSection(TypeSection.PERSONAL, new TextSection("Passionate about technology and innovation"));
        RESUME_1.setSection(TypeSection.ACHIEVEMENT, new ListSection("Developed a successful app", "Led a team of engineers", "Published a research paper"));
        RESUME_1.setSection(TypeSection.QUALIFICATIONS, new ListSection("Python", "HTML/CSS", "Project Management"));

        List<Company> experienceCompanies = new ArrayList<>();
        List<Period> periodsA = new ArrayList<>();
        List<Period> periodsB = new ArrayList<>();
        periodsA.add(new Period(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 12, 31), "Работа в компании A", "Описание работы в компании A"));
        periodsB.add(new Period(LocalDate.of(2018, 1, 1), LocalDate.of(2019, 12, 31), "Работа в компании B", "Описание работы в компании B"));
        //System.out.println("вывод периодов: " + periodsB);
        experienceCompanies.add(new Company("Organization13", "http://Organization13.ru", periodsA));
        experienceCompanies.add(new Company("Organization11", "http://Organization11.ru", periodsB));
        //System.out.println("вывод периодов: " + experienceCompanies);
        RESUME_1.setSection(TypeSection.EXPERIENCE, new CompanySection(experienceCompanies));
        //System.out.println("Содержимое секции EXPERIENCE: " + RESUME_1.getSection(TypeSection.EXPERIENCE));
        //System.out.println("вывод периодов: " + RESUME_1);
        //RESUME_1.setSection(TypeSection.EDUCATION, new CompanySection(experienceCompanies));
        //System.out.println("секция " + experienceCompanies.getClass().getSimpleName());
        //Section experienceSection = RESUME_1.getSection(TypeSection.EXPERIENCE);
        //if (experienceSection instanceof CompanySection) {
        //    System.out.println("Секция опыта является CompanySection");
        //} else {
        //    System.out.println("Секция опыта не является CompanySection, а является: {}"+  experienceSection.getClass().getSimpleName());
        //}

/*
        RESUME_2.setSection(TypeSection.EXPERIENCE,
                new CompanySection(
                        new Company("Tech Solutions Inc.", "http://techsolutions.com",
                                new Period(LocalDate.of(2012, Month.FEBRUARY, 1), LocalDate.of(2018, Month.APRIL, 30), "Senior Developer", "Worked on various projects using modern technologies."),
                                new Period(LocalDate.of(2009, Month.JUNE, 1), LocalDate.of(2012, Month.JANUARY, 31), "Junior Developer", "Assisted in developing web applications."))));

        RESUME_2.setSection(TypeSection.EDUCATION,
                new CompanySection(
                        new Company("University of Technology", "http://universityoftechnology.edu",
                                new Period(LocalDate.of(2005, Month.SEPTEMBER, 1), LocalDate.of(2009, Month.MAY, 31), "Bachelor's Degree in Computer Science", null),
                                new Period(LocalDate.of(2010, Month.OCTOBER, 1), LocalDate.of(2012, Month.JUNE, 30), "Master's Degree in Software Engineering", "Graduated with honors"))
                ));

 */
        RESUME_3.setSection(TypeSection.ACHIEVEMENT, new ListSection("Developed a successful app", "Led a team of engineers", "Published a research paper"));
        RESUME_3.setSection(TypeSection.QUALIFICATIONS, new ListSection("Python", "HTML/CSS", "Project Management"));
/*
        RESUME_3.setSection(TypeSection.EXPERIENCE,
                new CompanySection(
                        new Company("Tech Solutions Inc.", "http://techsolutions.com",
                                new Period(LocalDate.of(2012, Month.FEBRUARY, 1), LocalDate.of(2018, Month.APRIL, 30), "Senior Developer", "Worked on various projects using modern technologies."),
                                new Period(LocalDate.of(2009, Month.JUNE, 1), LocalDate.of(2012, Month.JANUARY, 31), "Junior Developer", "Assisted in developing web applications."))));

        RESUME_3.setSection(TypeSection.EDUCATION,
                new CompanySection(
                        new Company("University of Technology", "http://universityoftechnology.edu",
                                new Period(LocalDate.of(2005, Month.SEPTEMBER, 1), LocalDate.of(2009, Month.MAY, 31), "Bachelor's Degree in Computer Science", null),
                                new Period(LocalDate.of(2010, Month.OCTOBER, 1), LocalDate.of(2012, Month.JUNE, 30), "Master's Degree in Software Engineering", "Graduated with honors"))
                ));

 */
    }

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }


    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
        System.out.println("Stored resumes: " + storage.size());
    }
    /*
    @Test
    public void getAllSorted() throws Exception {
        List<Resume> list = storage.getAllSorted();
        System.out.println("Stored resumes: " + list);
        assertEquals(3, list.size());
        List<Resume> sortedResumes = Arrays.asList(RESUME_1, RESUME_2, RESUME_3);
        Collections.sort(sortedResumes);
        assertEquals(sortedResumes.toString().trim(), list.toString().trim());
    }

     */




    private void assertGet(Resume resume) {
        Resume retrievedResume = storage.get(resume.getUuid());
        assertNotNull("Resume should not be null", retrievedResume);
        assertEquals(resume.toString().trim(), retrievedResume.toString().trim());
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

/*

    @Test
    public void get() {
        System.out.println();
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

 */


    @Test
    public void save() {
        //System.out.println(RESUME_4);
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test
    public void delete() {
        storage.delete(RESUME_1.getUuid());
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(RESUME_1.getUuid()));
    }

    @Test
    public void update() throws Exception {
        System.out.println("Storage directory: " + Config.get().getStorageDir());
        Resume newResume = new Resume(UUID_1, "New Name");
        storage.update(newResume);
        assertTrue(newResume.equals(storage.get(UUID_1)));
    }

    @Test
    public void doSave() {
        assertSize(3);
        storage.delete(RESUME_1.getUuid());
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(RESUME_1.getUuid()));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        System.out.println("Current size before saving: " + storage.size());
        storage.save(RESUME_1);
        storage.save(RESUME_1);
    }

    @Test (expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete(dummy);
    }

    @Test (expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get(dummy);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test (expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.get(dummy);
    }

    @Test
    public void clear() throws Exception{
        storage.clear();
        assertSize(0);
    }
}