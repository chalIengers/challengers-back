package org.knulikelion.challengers_backend.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/")
public class FileUploadController {
    private final AmazonS3 amazonS3Client;

    @Autowired
    public FileUploadController(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    private static final String BUCKET_NAME = "challengers";

    @PostMapping("/upload/image")
    public BaseResponseDto uploadToS3(@RequestParam("file") MultipartFile file) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,
                    file.getOriginalFilename(), file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3Client.putObject(putObjectRequest);

            baseResponseDto.setSuccess(true);
            baseResponseDto.setMsg(amazonS3Client.getUrl(BUCKET_NAME, file.getOriginalFilename()).toString());

            return baseResponseDto;
        } catch (Exception e) {
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg(HttpStatus.INTERNAL_SERVER_ERROR.toString());

            return baseResponseDto;
        }
    }
}
