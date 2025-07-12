package com.eCommerce.CartOperations.controller;

import com.eCommerce.CartOperations.dto.ImageDto;
import com.eCommerce.CartOperations.exceptions.ResourceNotFoundException;
import com.eCommerce.CartOperations.model.Image;
import com.eCommerce.CartOperations.response.ApiResponse;
import com.eCommerce.CartOperations.service.image.IImageService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final IImageService imageService;



    @PostMapping("image/uploadImage")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> files,@RequestParam Long productId){
        try {
            List<ImageDto> imageDtos=imageService.saveImage(files, productId);
            return ResponseEntity.ok(new ApiResponse("upload success",imageDtos));
        } catch (Exception e) {
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("upload failed",e.getMessage()));
        }
    }

    @Transactional
    @GetMapping("image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image=imageService.getImageById(imageId);
       ByteArrayResource resource=new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
       return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFiletype()))
               .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename\"" +image.getFilename()+"\"")
               .body(resource);

   }

   @PutMapping("/image/{imageId}/update")
   public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,@RequestBody MultipartFile file) {
       try {
           Image image = imageService.getImageById(imageId);
           if (image != null) {
               imageService.update(file, imageId);
               return ResponseEntity.ok(new ApiResponse("update sucess", null));
           }
       } catch (ResourceNotFoundException e) {
         return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
       }
       return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("update failed", INTERNAL_SERVER_ERROR));
   }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("delete sucess", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("delete failed", INTERNAL_SERVER_ERROR));
    }


}
