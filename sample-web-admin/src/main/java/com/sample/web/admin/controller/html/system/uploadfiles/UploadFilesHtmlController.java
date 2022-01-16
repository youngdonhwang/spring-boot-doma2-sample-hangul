package com.sample.web.admin.controller.html.system.uploadfiles;

import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Paths;

import com.sample.web.base.util.MultipartFileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.common.util.FileUtils;
import com.sample.domain.helper.FileHelper;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.util.MultipartFileUtils;
import com.sample.web.base.view.FileDownloadView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/system/uploadfiles")
@Slf4j
public class UploadFilesHtmlController extends AbstractHtmlController implements InitializingBean {

    @Value("${application.fileUploadLocation:#{systemProperties['java.io.tmpdir']}}") // 設定ファイルに定義されたアップロード先を取得する
    String fileUploadLocation;

    @Autowired
    FileHelper fileHelper;

    @Override
    public String getFunctionName() {
        return "A_FileUpload";
    }

    /**
     * 리스트 화면
     *
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/list")
    public String listFiles(Model model) throws IOException {
        // 파일명의 리스트를 작성한다
        val location = Paths.get(fileUploadLocation);
        val stream = fileHelper.listAllFiles(location);
        val filenames = stream.map(path -> path.getFileName().toString()).collect(toList());

        model.addAttribute("filenames", filenames);

        return "modules/system/uploadfiles/list";
    }

    /**
     * 파일 내용 표시
     * 
     * @param filename
     * @return
     */
    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ModelAndView serveFile(@PathVariable String filename) {
        // 파일을 읽어들인다
        val resource = fileHelper.loadFile(Paths.get(fileUploadLocation), filename);

        // 응답을 설정한다
        val view = new FileDownloadView(resource);
        view.setAttachment(false);
        view.setFilename(filename);

        return new ModelAndView(view);
    }

    /**
     * 파일 다운로드
     *
     * @param filename
     * @return
     */
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ModelAndView downloadFile(@PathVariable String filename) {
        // 파일을 읽어들인다
        val resource = fileHelper.loadFile(Paths.get(fileUploadLocation), filename);

        // 응답을 설정한다
        val view = new FileDownloadView(resource);
        view.setFilename(filename);

        return new ModelAndView(view);
    }

    /**
     * 파일 업로드
     * 
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        // 파일을 보관한다
        MultipartFileUtils.saveFile(Paths.get(fileUploadLocation), file);

        // 리다이렉트할 곳에 완료 메시지를 표시한다
        redirectAttributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage("uploadfiles.upload.success"));

        return "redirect:/system/uploadfiles/list";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 업로드 디렉터리
        val location = Paths.get(fileUploadLocation);

        // 디렉터리가 없는 경우는 작성한다
        FileUtils.createDirectories(location);
    }
}
