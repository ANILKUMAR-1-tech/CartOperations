package com.eCommerce.CartOperations.service.image;

import com.eCommerce.CartOperations.dto.ImageDto;
import com.eCommerce.CartOperations.exceptions.ResourceNotFoundException;
import com.eCommerce.CartOperations.model.Image;
import com.eCommerce.CartOperations.model.Product;
import com.eCommerce.CartOperations.repository.ImageRepository;
import com.eCommerce.CartOperations.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final IProductService productService;
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No Image found with:"+id));
    }

    @Override
    public void deleteImageById(Long id) {
         imageRepository.findById(id).ifPresentOrElse(imageRepository ::delete,()-> {
             throw new ResourceNotFoundException("No Image found with:"+id);
         });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> file, Long productId) {
        Product product=productService.getProductById(productId);
        List<ImageDto> savedImageDto=new ArrayList<>();
        for (MultipartFile file1 : file){
            try{
                 Image image=new Image();
                 image.setFilename(file1.getOriginalFilename());
                 image.setFiletype(file1.getContentType());
                 image.setImage(new SerialBlob(file1.getBytes()));
                 image.setProduct(product);
                 String downloadImage="/api/images/image/download/";
                 String downloadUrl=downloadImage +image.getId();
                 image.setDownloadUrl(downloadUrl);
                Image savedImage= imageRepository.save(image);
                 savedImage.setDownloadUrl(downloadImage+savedImage.getId());
                 imageRepository.save(savedImage);
                 ImageDto imageDto=new ImageDto();
                 imageDto.setImageId(savedImage.getId());
                 imageDto.setImageName(savedImage.getFilename());
                 imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                 savedImageDto.add(imageDto);

            } catch (IOException | SQLException e){
                 throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void update(MultipartFile file, Long imageId) {
        Image image=getImageById(imageId);
        try {
            image.setFilename(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }


    }
}
