package com.sample.web.base.view;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.AbstractView;

import com.sample.common.util.EncodeUtils;

import lombok.val;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 * PDF뷰
 */
public class PdfView extends AbstractView {

    protected String report;

    protected Collection<?> data;

    protected String filename;

    /**
     * 생성자
     *
     * @param report
     * @param data
     * @param filename
     */
    public PdfView(String report, Collection<?> data, String filename) {
        super();
        this.setContentType("application/pdf");
        this.report = report;
        this.data = data;
        this.filename = filename;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // IE의 경우는 Content-Length헤더가 지정되어 있지 않으면 다운로드에 실패하므로,
        // 사이즈를 취득하기 위한 일시적인 바이트 배열 스트림에 컨텐츠를 출력하도록 한다
        val baos = createTemporaryOutputStream();

        // 서류 레이아웃
        val report = loadReport();

        // 데이터의 설정
        val dataSource = new JRBeanCollectionDataSource(this.data);
        val print = JasperFillManager.fillReport(report, model, dataSource);

        val exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();

        // 파일명으로 한국어를 포함해도 문자 깨짐이 발생하지 않도록 UTF-8로 인코딩한다
        val encodedFilename = EncodeUtils.encodeUtf8(filename);
        val contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFilename);
        response.setHeader(CONTENT_DISPOSITION, contentDisposition);

        writeToResponse(response, baos);
    }

    /**
     * 서류 레이아웃을 로딩한다
     *
     * @return
     */
    protected final JasperReport loadReport() {
        val resource = new ClassPathResource(this.report);

        try {
            val fileName = resource.getFilename();
            if (fileName.endsWith(".jasper")) {
                try (val is = resource.getInputStream()) {
                    return (JasperReport) JRLoader.loadObject(is);
                }
            } else if (fileName.endsWith(".jrxml")) {
                try (val is = resource.getInputStream()) {
                    JasperDesign design = JRXmlLoader.load(is);
                    return JasperCompileManager.compileReport(design);
                }
            } else {
                throw new IllegalArgumentException(
                        ".jasper 또는 .jrxml 의 서류 포맷을 지정해주세요 [" + fileName + "] must end in either ");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to load report. " + resource, e);
        } catch (JRException e) {
            throw new IllegalArgumentException("failed to parse report. " + resource, e);
        }
    }
}
