package com.travelvcommerce.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
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
    @Value("${video.directory}")
    private String DIRECTORY;

    @Override
    public S3Video uploadEncodedVideo(String userId, String videoId, String filePath) {
        String s3FilePath = userId + "/" + videoId;

        try {
            Files.walk(Path.of(filePath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        String key = DIRECTORY + "/" + s3FilePath + "/" + file.getFileName().toString();
                        amazonS3Client.putObject(new PutObjectRequest(BUCKET, key, file.toFile()).withCannedAcl(CannedAccessControlList.PublicRead));
                    });
            String hlsKey = DIRECTORY + "/" + s3FilePath;
            String directoryKey = DIRECTORY + "/" + s3FilePath;
            String s3Url = amazonS3Client.getUrl(BUCKET, directoryKey).toString();
            String cloudFrontUrl = DISTRIBUTION_DOMAIN + "/" + hlsKey;

            deleteFolder(filePath);

            S3Video s3Video = S3Video.builder()
                    .s3Url(s3Url)
                    .cloudfrontUrl(cloudFrontUrl)
                    .build();

            return s3Video;

        } catch (Exception e) {
            log.error(e.getMessage());
            deleteFolder(filePath);
            throw new RuntimeException("AWS encoded video upload error");
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
    public S3Thumbnail uploadThumbnail(String userId, String videoId, MultipartFile multipartFile) {
        String s3FilePath = userId + "/" + videoId;

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("file must not be empty");
        }

        String key;

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            String fileExtension = multipartFile.getOriginalFilename().
                    substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            key = DIRECTORY + "/" + s3FilePath + "/thumbnail" + fileExtension;

            amazonS3Client.putObject(new PutObjectRequest(BUCKET, key, multipartFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("AWS thumbnail upload error");
        }

        String s3url = amazonS3Client.getUrl(BUCKET, key).toString();
        String cloudFrontUrl = DISTRIBUTION_DOMAIN + "/" + key;

        S3Thumbnail s3Thumbnail = S3Thumbnail.builder()
                .s3Url(s3url)
                .cloudfrontUrl(cloudFrontUrl)
                .build();

        return s3Thumbnail;
    }

    @Override
    public S3Thumbnail updateThumbnail(String s3Url, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("file must not be empty");
        }
        String s3Key = s3Url.substring(s3Url.indexOf(DIRECTORY));
        String newS3Key;

        try {
            if (amazonS3Client.doesObjectExist(BUCKET, s3Key)) {
                amazonS3Client.deleteObject(BUCKET, s3Key);
            }
            newS3Key =
                    s3Key.substring(0, s3Key.lastIndexOf(".")) +
                            multipartFile.getOriginalFilename()
                                    .substring(multipartFile.getOriginalFilename().lastIndexOf("."));

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            amazonS3Client.putObject(new PutObjectRequest(BUCKET, newS3Key, multipartFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("AWS thumbnail update error");
        }

        String s3url = amazonS3Client.getUrl(BUCKET, newS3Key).toString();
        String cloudFrontUrl = DISTRIBUTION_DOMAIN + "/" + newS3Key;

        S3Thumbnail s3Thumbnail = S3Thumbnail.builder()
                .s3Url(s3url)
                .cloudfrontUrl(cloudFrontUrl)
                .build();

        return s3Thumbnail;
    }

    @Override
    public void deleteVideo(String s3Url) {

        String s3Key = s3Url.substring(s3Url.indexOf(DIRECTORY));

        try {
            log.info("delete video : " + s3Key);

            amazonS3Client.listObjects(BUCKET, s3Key).getObjectSummaries()
                    .forEach(objectSummary -> {
                        String key = objectSummary.getKey();
                        amazonS3Client.deleteObject(new DeleteObjectRequest(BUCKET, key));
                    });

            amazonS3Client.deleteObject(BUCKET, s3Key);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("video delete error");
        }
    }
}
