package com.sample.web.base.view;

import static com.fasterxml.jackson.dataformat.csv.CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS;
import static com.sample.common.util.ValidateUtils.isNotEmpty;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sample.common.util.EncodeUtils;

import lombok.Setter;
import lombok.val;

/**
 * CSV뷰
 */
public class CsvView extends AbstractView {

    protected static final CsvMapper csvMapper = createCsvMapper();

    protected Class<?> clazz;

    protected Collection<?> data;

    @Setter
    protected String filename;

    @Setter
    protected List<String> columns;

    /**
     * CSV맵퍼를 생성한다.
     *
     * @return
     */
    static CsvMapper createCsvMapper() {
        CsvMapper mapper = new CsvMapper();
        mapper.configure(ALWAYS_QUOTE_STRINGS, true);
        mapper.findAndRegisterModules();
        return mapper;
    }

    /**
     * 생성자
     *
     * @param clazz
     * @param data
     * @param filename
     */
    public CsvView(Class<?> clazz, Collection<?> data, String filename) {
        setContentType("application/octet-stream; charset=Windows-31J;");
        this.clazz = clazz;
        this.data = data;
        this.filename = filename;
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // 파일명에 한국어를 포함해도 문자 깨짐이 발생하지 않도록 UTF-8로 인코딩한다
        val encodedFilename = EncodeUtils.encodeUtf8(filename);
        val contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFilename);

        response.setHeader(CONTENT_TYPE, getContentType());
        response.setHeader(CONTENT_DISPOSITION, contentDisposition);

        // CSV헤더를 객체로부터 작성한다
        CsvSchema schema = csvMapper.schemaFor(clazz).withHeader();

        if (isNotEmpty(columns)) {
            // 컬럼이 지정된 경우는, 스키마를 재구축한다
            val builder = schema.rebuild().clearColumns();
            for (String column : columns) {
                builder.addColumn(column);
            }
            schema = builder.build();
        }

        // 출력한다
        val outputStream = createTemporaryOutputStream();
        try (Writer writer = new OutputStreamWriter(outputStream, "Windows-31J")) {
            csvMapper.writer(schema).writeValue(writer, data);
        }
    }
}
