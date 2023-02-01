package com.example.myprojectstore.models;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Size(min = 2,max = 40,message = "Название должны быть в диапозоне 2-40")
    @Column(name = "title")
    private String title;

    @Size(min = 5,message = "Описание должно состоять более чем из 5 символов")
    @Column(name = "description",columnDefinition = "text")//вместо varchar,чтобы повысить вместительность символов
    private String description;

    @Min(value = 1,message = "цена должна быть больше 0")
    @Column(name = "price")
    private int price;

    @NotEmpty(message = "Необходимо ввести название города")
    @Column(name = "city")
    private String city;



    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "product") // lazy потому что не нужно подгружать все фото
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn
    private User user;


    public void addImageToProduct(Image image){
        image.setProduct(this);
        images.add(image);
    }
}
