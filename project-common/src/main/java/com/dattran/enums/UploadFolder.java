package com.dattran.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UploadFolder {
    AVATARS("avatars"),
    PRODUCT_IMAGES("product-images"),
    BRAND_IMAGES("brand-images");
    private final String val;
}
