package com.eCommerce.CartOperations.service.image;

import com.eCommerce.CartOperations.dto.ImageDto;
import com.eCommerce.CartOperations.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto>  saveImage(List<MultipartFile> file, Long productId);
    void update(MultipartFile file, Long imageId);
}
