package com.travelvcommerce.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.travelvcommerce.uploadservice.vo.S3Video;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service{

    @Autowired
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;
    @Value("${cloud.aws.cloudfront.distribution-domain}")
    private String DISTRIBUTION_DOMAIN;

    @Override
    public S3Video uploadEncodedVideo(String fileName, String filePath) {
        try {
            Files.walk(Path.of(filePath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        String key = "videos/" + fileName + "/" + file.getFileName().toString();
                        amazonS3Client.putObject(new PutObjectRequest(BUCKET, key, file.toFile()).withCannedAcl(CannedAccessControlList.PublicRead));
                    });
            String hlsKey = "videos/" + fileName + "/" + fileName + ".m3u8";
            String directoryKey = "videos/" + fileName;
            String s3Url = amazonS3Client.getUrl(BUCKET, directoryKey).toString();
            String cloudFrontUrl = DISTRIBUTION_DOMAIN + "/" + hlsKey;

            deleteFolder(filePath);

            S3Video s3Video = new S3Video();

            s3Video.setS3Url(s3Url);
            s3Video.setCloudFrontUrl(cloudFrontUrl);

            return s3Video;

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
            String key = "videos/" + fileName + "/" + fileName + fileExtension;
            amazonS3Client.putObject(new PutObjectRequest(BUCKET, key, multipartFile.getInputStream(), objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));

            String cloudFrontUrl = DISTRIBUTION_DOMAIN + "/" + key;

            return cloudFrontUrl;

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("thumbnail upload error");
        }
    }
}
