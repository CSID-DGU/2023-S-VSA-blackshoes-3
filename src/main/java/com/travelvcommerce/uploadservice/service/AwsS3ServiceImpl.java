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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service{

    private final AmazonS3Client amazonS3Client;


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Override
    public String uploadEncodedVideo(String fileName, String filePath) {
        try {
            Files.walk(Path.of(filePath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        String key = "videos/" + fileName + "/" + file.getFileName().toString();
                        amazonS3Client.putObject(new PutObjectRequest(bucket, key, file.toFile()).withCannedAcl(CannedAccessControlList.PublicRead));
                    });
            String s3Url = amazonS3Client.getUrl(bucket, "videos/" + fileName).toString();

            deleteFolder(filePath);

            return s3Url;

        } catch (Exception e) {
            log.error(e.getMessage());
            deleteFolder(filePath);
            throw new RuntimeException("Encoded video upload error");
        }
    }

    private static void deleteFolder(String filePath) {
        File encodingPathFile = new File(filePath.toString());
        File[] encodingPathFileList = encodingPathFile.listFiles();
        for (File file : encodingPathFileList) {
            file.delete();
        }
        encodingPathFile.delete();
    }

    @Override
    public String uploadThumbnail(String fileName, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("file must not be empty");
        }
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            String fileExtension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            String key = "videos/" + fileName + "/" + fileName + "." + fileExtension;
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, multipartFile.getInputStream(), objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));

            String s3Url = amazonS3Client.getUrl(bucket, key).toString();
            return s3Url;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("thumbnail upload error");
        }
    }
}
