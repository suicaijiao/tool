package com.yinlian.cfra.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.yinlian.cfra.commons.exception.entity.BusinessException;
import com.yinlian.cfra.commons.global.result.ResultCode;
import com.yinlian.cfra.commons.util.RestTemplateUtils;
import com.yinlian.cfra.service.CertificateDownloadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-06-06 09:56
 **/
@Service
public class CertificateDownloadServiceImpl implements CertificateDownloadService {

    @Value("${custom.certificate_download_url}")
    private String certificateDownloadUrl;

    @Value("${custom.image_src}")
    private String imageSrc;

    private static final String IMG_NAME = "certificate.png";

    @Override
    public JSONObject certificateMap(String idCard) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("idcard", idCard);

        String url = certificateDownloadUrl + "api/app/exam/certificate";
        ResponseEntity<JSONObject> responseApplyFileEntity = RestTemplateUtils.post(url, requestBody, JSONObject.class);

        if (responseApplyFileEntity.getStatusCodeValue() == 200) {
            return responseApplyFileEntity.getBody();
        } else {
            throw new BusinessException(ResultCode.SYS_COM_HTTP_ERROR);
        }
    }

    /**
     * 返回合成图片
     *
     * @param idCard
     * @return
     */
    @Override
    public String getCertificateUrg(String idCard) {
        JSONObject certificateMap = this.certificateMap(idCard);
        if (certificateMap != null && Integer.valueOf(certificateMap.get("code").toString()) != 200) {
            throw new BusinessException(ResultCode.SYS_COM_HTTP_ERROR);
        }
        JSONArray array = certificateMap.getJSONArray("data");
        if (array == null || array.size() == 0) {
            throw new BusinessException(ResultCode.SYS_COM_HTTP_ERROR);
        }
        JSONObject data = array.getJSONObject(0);

        String userName = data.getString("realName") + " " + data.getString("pinyin");
        certificateImg(userName);
        return "/upload/" + IMG_NAME;
    }

    /**
     * 图片合成
     *
     * @param userName
     */
    private void certificateImg(String userName) {
        byte[] bytes = null;
        try {
//            Resource res = new ClassPathResource("certificate.png");
            String str = userName;
//            File _file = res.getFile();
            InputStream is = this.getClass().getResourceAsStream("/certificate.png");
            Image src = ImageIO.read(is);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);

            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            g.setColor(Color.darkGray);
            g.setFont(new Font("宋体", Font.PLAIN, 120));
            Font aa = new Font("宋体", Font.PLAIN, 20);
            g.drawString(str, wideth - 1660, height - 2080);
            g.dispose();
            ByteArrayOutputStream out1 = new ByteArrayOutputStream();
            // 保存图片
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out1);
//            encoder.encode(image);

            ImageIO.write(image, "jpeg", out1);
            bytes = out1.toByteArray();
            out1.close();
            FileOutputStream out2 = new FileOutputStream(imageSrc + IMG_NAME);
            out2.write(bytes);
            out2.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.SYS_COM_HTTP_ERROR);
        }
    }


}
