package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.entity.Uploader;
import com.travelvcommerce.uploadservice.repository.UploaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload-service/test")
public class TestController {
    @Autowired
    UploaderRepository uploaderRepository;

    @PostMapping("/uploaders")
    public String createTestUploader(@RequestPart(value = "sellerLogo") MultipartFile sellerLogoFile,
                                     @RequestPart(value = "sellerName") String sellerName) throws IOException {
        String sellerId = UUID.randomUUID().toString();

        byte[] sellerLogo = sellerLogoFile.getInputStream().readAllBytes();

        Uploader uploader = new Uploader();
        uploader.setSellerId(sellerId);
        uploader.setSellerLogo(sellerLogo);
        uploader.setSellerName(sellerName);

        uploaderRepository.save(uploader);

        return "createTestUploader";
    }
}
