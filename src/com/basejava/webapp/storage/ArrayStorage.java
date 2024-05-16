package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    private Resume[] storage = new Resume[10000];

    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());

        if (index != -1) {
            System.out.println("Резюме " + resume.getUuid() + " не сохранено, в storage уже есть резюме с таким uuid");
        } else if (size < storage.length ) {
            storage[size++] = resume;
        } else {
            System.out.println("Резюме " + resume.getUuid() + " не сохранено, storage перполнено");
        }
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());

        if (index == -1) {
            System.out.println("Резюме " + resume.getUuid() + " не обновлено, в storage нет резюме с таким uuid");
        } else {
            storage[index] = resume;
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);

        if (index == -1) {
            System.out.println("Резюме " + uuid + " не найдено, в storage нет резюме с таким uuid");
            return null;
        } else {
            return storage[index];
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);

        if (index == -1) {
            System.out.println("Резюме " + uuid + " не удалено, в storage нет резюме с таким uuid");
            return;
        }

        storage[index] = storage[--size];
        storage[size] = null;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }

        return -1;
    }
}
