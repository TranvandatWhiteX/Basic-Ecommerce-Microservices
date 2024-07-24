package com.dattran.productservice.domain.callbacks;

import java.util.List;

public interface UploadCallback {
    void onSuccess(List<String> result);
    void onError(Throwable throwable);
}
