package com.sample.common.util;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    /**
     * 디렉터리가 없는 경우는 작성한다.
     *
     * @param location
     */
    public static void createDirectory(Path location) {
        try {
            Files.createDirectory(location);
        } catch (FileAlreadyExistsException ignore) {
            // ignore
        } catch (IOException e) {
            throw new IllegalArgumentException("could not create directory. " + location.toString(), e);
        }
    }

    /**
     * 부모 디렉터리를 포함하여 디렉터리가 없는 경우는 작성한다.
     *
     * @param location
     */
    public static void createDirectories(Path location) {
        try {
            Files.createDirectories(location);
        } catch (FileAlreadyExistsException ignore) {
            // ignore
        } catch (IOException e) {
            throw new IllegalArgumentException("could not create directory. " + location.toString(), e);
        }
    }
}
