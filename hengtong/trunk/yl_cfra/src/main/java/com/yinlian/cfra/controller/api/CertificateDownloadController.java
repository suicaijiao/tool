package com.yinlian.cfra.controller.api;

import com.yinlian.cfra.commons.global.result.ResponseResult;
import com.yinlian.cfra.service.CertificateDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-06-08 15:10
 **/
@RestController
@RequestMapping("/api")
public class CertificateDownloadController {

    @Value("${custom.image_src}")
    private String imageSrc;

    @Autowired
    private CertificateDownloadService certificateDownloadService;

    @PostMapping("/certificate/{idCard}")
    public ResponseResult certificate(@PathVariable("idCard") String idCard) {
        String result = certificateDownloadService.getCertificateUrg(idCard);
        return ResponseResult.success(result);
    }

    @GetMapping("/download-file")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {

        String realPath = imageSrc + "certificate.png";
        File file = new File(realPath);

        try {
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(realPath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
