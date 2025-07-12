package com.eCommerce.CartOperations.dto;

import lombok.Data;

import java.security.PrivateKey;

@Data
public class ImageDto {
   private Long imageId;
   private String imageName;
   private String downloadUrl;
}
