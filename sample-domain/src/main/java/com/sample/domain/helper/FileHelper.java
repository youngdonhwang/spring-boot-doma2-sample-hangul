package com.sample.domain.helper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import com.sample.domain.exception.FileNotFoundException;

/**
 * 파일 업로드
 */
@Component
public class FileHelper {

    /**
     * 파일 목록을 취득한다.
     *
     * @param location
     * @return
     */
    public Stream<Path> listAllFiles(Path location) {
        try {
            return Files.walk(location, 1).filter(path -> !path.equals(location)).map(location::relativize);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to list uploadfiles. ", e);
        }
    }

    /**
     * 파일을 읽어들인다.
     *
     * @param location
     * @param filename
     * @return
     */
    public Resource loadFile(Path location, String filename) {
        try {
            Path file = location.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            }

            throw new FileNotFoundException("could not read file. " + filename);

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "malformed Url resource. [location=" + location.toString() + ", filename=" + filename + "]", e);
        }
    }
}
