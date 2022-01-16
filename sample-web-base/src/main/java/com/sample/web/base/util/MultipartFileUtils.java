package com.sample.web.base.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.sample.common.util.FileUtils;
import com.sample.domain.dto.common.BZip2Data;
import com.sample.domain.dto.common.MultipartFileConvertible;

import lombok.extern.slf4j.Slf4j;

/**
 * MultipartFile관련 유틸리티
 */
@Slf4j
public class MultipartFileUtils {

    /**
     * MultipartFileConvertible에 값을 채워넣는다
     * 
     * @param from
     * @param to
     */
    public static void convert(MultipartFile from, MultipartFileConvertible to) {
        to.setFilename(from.getName());
        to.setOriginalFilename(from.getOriginalFilename());
        to.setContentType(from.getContentType());

        try {
        	// 바이트 배열을 세트한다
            to.setContent(BZip2Data.of(from.getBytes()));
        } catch (IOException e) {
            log.error("failed to getBytes", e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 파일을 보관한다
     *
     * @param location
     * @param file
     *            보관처 디렉터리
     */
    public static void saveFile(Path location, MultipartFile file) {
        Assert.notNull(file, "file can't be null");
        String filename = file.getOriginalFilename();

        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("cloud not save empty file. " + filename);
            }

            // 디렉터리가 없는 경우는 작성한다
            FileUtils.createDirectories(location);

            // INPUT스트림을 파일에 기록한다
            Files.copy(file.getInputStream(), location.resolve(filename));

        } catch (IOException e) {
            throw new IllegalStateException("failed to save file. " + filename, e);
        }
    }
}
