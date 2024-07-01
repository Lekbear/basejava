package com.basejava.webapp;

import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.SortedArrayStorage;
import com.basejava.webapp.storage.Storage;

public class MainTestSortedArrayStorage {
    static final Storage SORTED_ARRAY_STORAGE = new SortedArrayStorage();
    static final String UUID0 = "uuid0";
    static final String UUID1 = "uuid1";
    static final String UUID2 = "uuid2";
    static final String UUID3 = "uuid3";

    public static void main(String[] args) {
        Resume r1 = new Resume(UUID1, "");
        Resume r2 = new Resume(UUID2, "");
        Resume r3 = new Resume(UUID3, "");

        SORTED_ARRAY_STORAGE.save(r2);
        SORTED_ARRAY_STORAGE.save(r1);
        SORTED_ARRAY_STORAGE.save(r3);

        System.out.println("Get r1: " + SORTED_ARRAY_STORAGE.get(UUID1));
        System.out.println("Size: " + SORTED_ARRAY_STORAGE.size());
        System.out.println("Get dummy: " + SORTED_ARRAY_STORAGE.get("dummy"));
        printAll();

        System.out.println("\nDelete: " + UUID2);
        SORTED_ARRAY_STORAGE.delete(UUID2);
        printAll();

        System.out.println("\nSave: " + UUID0);
        Resume r0 = new Resume(UUID0, "");
        SORTED_ARRAY_STORAGE.save(r0);
        printAll();

        System.out.println("\nDelete: " + UUID3);
        SORTED_ARRAY_STORAGE.delete(UUID3);
        printAll();

        System.out.println("\nClear All");
        SORTED_ARRAY_STORAGE.clear();
        System.out.println("Size: " + SORTED_ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : SORTED_ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}
