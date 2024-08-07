package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.serializer.StreamSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final StreamSerializer streamSerializer;
    private final File directory;

    protected FileStorage(File directory, StreamSerializer streamSerializer) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(streamSerializer, "streamSerializable must not be null");

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }

        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }

        this.streamSerializer = streamSerializer;
        this.directory = directory;
    }

    @Override
    protected boolean isExisting(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void clearAll() {
        for (File file : getNoNullListFiles()) {
            deleteResume(file);
        }
    }

    @Override
    protected void saveResume(Resume resume, File file) {
        try {
            if (file.createNewFile()) {
                streamSerializer.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
            }
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void setResume(Resume resume, File file) {
        try {
            streamSerializer.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Resume getResume(File file) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void deleteResume(File file) {
        if (!file.delete()) {
            throw new StorageException("Failed to delete", file.getName());
        }
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> resumes = new ArrayList<>();

        for (File file : getNoNullListFiles()) {
            resumes.add(getResume(file));
        }

        return resumes;
    }

    @Override
    protected int getSize() {
        return getNoNullListFiles().length;
    }

    private File[] getNoNullListFiles() {
        File[] listFiles = directory.listFiles();

        if (listFiles == null) {
            throw new StorageException("isn't a directory or an I/O error", directory.getName());
        }

        return listFiles;
    }
}
