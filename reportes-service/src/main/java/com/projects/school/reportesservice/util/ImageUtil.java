package com.projects.school.reportesservice.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class ImageUtil {

    public String loadImageAsBase64(String classpathResource) {
        try {
            ClassPathResource resource = new ClassPathResource(classpathResource);
            byte[] bytes = resource.getInputStream().readAllBytes();
            String extension = classpathResource.substring(classpathResource.lastIndexOf('.') + 1);
            String mimeType = "image/" + (extension.equals("jpg") ? "jpeg" : extension);
            return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            return "";
        }
    }
}
