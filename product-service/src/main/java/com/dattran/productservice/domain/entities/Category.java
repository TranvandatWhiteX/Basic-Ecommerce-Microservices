package com.dattran.productservice.domain.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity {
    @Id
    String id;

    String name;

    Boolean isDeleted;

    Integer level;

    @DBRef
    Category parentCategory;

    @DBRef
    List<Category> subCategories;

    @DBRef
    List<Product> products;

    public int getLevel() {
        int level = 1;
        Category parentCategory = this.getParentCategory();
        while (parentCategory != null) {
            level++;
            parentCategory = parentCategory.getParentCategory();
        }
        return level;
    }
}
