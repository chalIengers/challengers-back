package org.knulikelion.challengers_backend.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.apache.tika.Tika;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/file")
public class FileUploadController {
    private final AmazonS3 amazonS3Client;

    @Autowired
    public FileUploadController(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    private static final String BUCKET_NAME = "challengers";

    @PostMapping("/upload")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto uploadToS3(@RequestParam("file") MultipartFile file) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try {
            // 이미지 파일인지 검증
            Tika tika = new Tika();
            String mimeType = tika.detect(file.getBytes());

            if (!(mimeType.equals("image/gif") || mimeType.equals("image/jpeg") || mimeType.equals("image/png") || mimeType.equals("image/bmp"))) {
                baseResponseDto.setSuccess(false);
                baseResponseDto.setMsg("GIF, JPEG, PNG 또는 BMP 이미지 파일만 업로드할 수 있습니다.");
                return baseResponseDto;
            }

            // 용량 제한 검증 (10MB)
            long sizeInMb = file.getSize() / (1024 * 1024);

            if(sizeInMb > 10) {
                baseResponseDto.setSuccess(false);
                baseResponseDto.setMsg("파일 용량은 10MB를 초과할 수 없습니다.");
                return baseResponseDto;
            }

            // Generate unique name for the file using UUID
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,
                    newFileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3Client.putObject(putObjectRequest);

            baseResponseDto.setSuccess(true);
            baseResponseDto.setMsg(amazonS3Client.getUrl(BUCKET_NAME,newFileName).toString());

        } catch (Exception e) {
            e.printStackTrace();
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }

        return baseResponseDto;
    }
}
