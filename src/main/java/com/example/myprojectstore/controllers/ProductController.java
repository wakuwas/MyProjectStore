package com.example.myprojectstore.controllers;

import com.example.myprojectstore.models.Product;
import com.example.myprojectstore.repositories.ProductRepository;
import com.example.myprojectstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor //вместо инициализации productService
@Controller

public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @ModelAttribute("productCreate")
    public Product product(){
        return new Product();
    }

    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title, Principal principal, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id,Principal principal, Model model) {


        model.addAttribute("user", productService.getUserByPrincipal(principal));
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-info";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, @ModelAttribute("productCreate") @Valid Product product,
                                BindingResult bindingResult,
                                Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
           return "redirect:/";
        }
        else {
            productService.saveProduct(principal, product, file1, file2, file3);
        }
        return "redirect:/";
    }



    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        String roleString = productService.getUserByPrincipal(principal).getRoles().toString();
        Long idUser =productService.getUserByPrincipal(principal).getId();
        Long idCostumer = productService.getIdUserOfProduct(productService.getProductById(id));
        try {
            if (idUser.equals(idCostumer) |
            roleString.equals("[ROLE_ADMIN]")) {
                productService.deleteProduct(productService.getUserByPrincipal(principal), id);

                return "redirect:/";
            } else
                System.out.println(productService.getUserByPrincipal(principal).getId());
                return "redirect:/login";
        }catch (Exception e){

            return "redirect:/login";
        }
    }
}
