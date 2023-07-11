package com.travelvcommerce.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service{

    private final AmazonS3Client amazonS3Client;


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Override
    public String uploadFile(String fileType, String fileName, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("file must not be empty");
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String key = fileType + "/" + fileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e) {
            throw new RuntimeException("file upload error");
        }

        String s3Url = amazonS3Client.getUrl(bucket, key).toString();

        return s3Url;
    }
}
