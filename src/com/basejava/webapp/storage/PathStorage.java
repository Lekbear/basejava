package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final StreamSerializable streamSerializable;
    private final Path directory;

    protected PathStorage(Path path, StreamSerializable streamSerializable) {
        directory = path;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(path + " is not directory or is not writable");
        }
        Objects.requireNonNull(streamSerializable, "streamSerializable must not be null");
        this.streamSerializable = streamSerializable;
    }

    @Override
    protected boolean isExisting(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void clearAll() {
        try (Stream<Path> s = Files.list(directory)) {
            s.forEach(this::deleteResume);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    protected void saveResume(Resume resume, Path path) {
        try {
            streamSerializable.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(Files.createFile(path))));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void setResume(Resume resume, Path path) {
        try {
            streamSerializable.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume getResume(Path path) {
        try {
            return streamSerializable.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void deleteResume(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Failed to delete", path.getFileName().toString());
        }
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> resumes;

        try (Stream<Path> s = Files.list(directory)) {
            resumes = s.map(this::getResume).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }

        return resumes;
    }

    @Override
    protected int getSize() {
        try (Stream<Path> s = Files.list(directory)) {
            return (int) s.count();
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }
}
