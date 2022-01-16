package com.sample.web.base.view;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.sample.common.util.EncodeUtils;

import lombok.val;

/**
 * Excel뷰
 */
public class ExcelView extends AbstractXlsxView {

    protected String filename;

    protected Collection<?> data;

    protected Callback callback;

    /**
     * 생성자
     */
    public ExcelView() {
        setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8;");
    }

    /**
     * 생성자
     *
     * @param callback
     * @param data
     * @param filename
     */
    public ExcelView(Callback callback, Collection<?> data, String filename) {
        this();
        this.callback = callback;
        this.data = data;
        this.filename = filename;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // 파일명에 한국어를 포함해도 문자 깨짐이 발생하지 않도록 UTF-8로 인코딩한다
        val encodedFilename = EncodeUtils.encodeUtf8(filename);
        val contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFilename);
        response.setHeader(CONTENT_DISPOSITION, contentDisposition);

        // Excel 통합 문서를 구축한다
        callback.buildExcelWorkbook(model, this.data, workbook);
    }

    public interface Callback {

        /**
         * Excel 통합 문서를 구축한다
         *
         * @param model
         * @param data
         * @param workbook
         */
        void buildExcelWorkbook(Map<String, Object> model, Collection<?> data, Workbook workbook);
    }
}
