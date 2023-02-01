package com.example.myprojectstore.services;

import com.example.myprojectstore.models.Image;
import com.example.myprojectstore.models.Product;
import com.example.myprojectstore.models.User;
import com.example.myprojectstore.repositories.ImageRepository;
import com.example.myprojectstore.repositories.ProductRepository;
import com.example.myprojectstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    public void saveProduct(Principal principal,Product product, MultipartFile file1,
                            MultipartFile file2, MultipartFile file3) throws IOException {//Multi - для загрузки фото
       product.setUser(getUserByPrincipal(principal));

        Image image1;
       Image image2;
       Image image3;
       if(file1.getSize()!=0){
           image1=toImageEntity(file1);
           image1.setPreviewImage(true);
           product.addImageToProduct(image1);
       }
        if(file2.getSize()!=0){
            image2=toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if(file3.getSize()!=0){
            image3=toImageEntity(file3);
            product.addImageToProduct(image3);
        }

        try {

            Product productFromDB=productRepository.save(product);
            productFromDB.setPreviewImageId(productFromDB.getImages().get(0).getId());
        }catch (Exception e){
            log.info("NO SAVE Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());
        }
        //productRepository.save(product);

    }

    public User getUserByPrincipal(Principal principal) {
        if(principal==null) return new User();
        return userRepository.findByEmail(principal.getName());
    }
    public Long getIdUserOfProduct(Product product){
        User user = product.getUser();
        return user.getId();
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;

    }
    public void deleteProduct(User user, Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            if (product.getUser().getId().equals(user.getId()) | user.getRoles().toString().equals("[ROLE_ADMIN]")) {
                productRepository.deleteById(id);


                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }

    }


    public Product getProductById(Long id) {

        return productRepository.findById(id).orElse(null); //если товар не найден вернет null
    }


}
