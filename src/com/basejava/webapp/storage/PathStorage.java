package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.serializer.StreamSerializer;

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
    private final StreamSerializer streamSerializer;
    private final Path directory;

    protected PathStorage(Path path, StreamSerializer streamSerializer) {
        directory = path;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(path + " is not directory or is not writable");
        }
        Objects.requireNonNull(streamSerializer, "streamSerializable must not be null");
        this.streamSerializer = streamSerializer;
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
            throw new StorageException("IO error", e);
        }
    }

    @Override
    protected void saveResume(Resume resume, Path path) {
        try {
            streamSerializer.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(Files.createFile(path))));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void setResume(Resume resume, Path path) {
        try {
            streamSerializer.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume getResume(Path path) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(Files.newInputStream(path)));
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
        try (Stream<Path> s = Files.list(directory)) {
            return s.map(this::getResume).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("IO error", e);
        }
    }

    @Override
    protected int getSize() {
        try (Stream<Path> s = Files.list(directory)) {
            return (int) s.count();
        } catch (IOException e) {
            throw new StorageException("IO error", e);
        }
    }
}
