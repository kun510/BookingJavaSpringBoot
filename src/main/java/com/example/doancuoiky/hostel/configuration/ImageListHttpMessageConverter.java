package com.example.doancuoiky.hostel.configuration;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.List;

public class ImageListHttpMessageConverter extends AbstractHttpMessageConverter<List<byte[]>> {
    public ImageListHttpMessageConverter() {
        super(new MediaType("image", "jpeg"), new MediaType("image", "png")); // Đặt kiểu phương tiện tương ứng với kiểu hình ảnh của bạn
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    protected List<byte[]> readInternal(Class<? extends List<byte[]>> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(List<byte[]> imageList, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // Chuyển đổi danh sách hình ảnh thành dữ liệu đầu ra
        for (byte[] imageData : imageList) {
            outputMessage.getBody().write(imageData);
        }
    }
}
