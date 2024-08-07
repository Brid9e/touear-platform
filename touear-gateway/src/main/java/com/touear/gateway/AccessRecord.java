package com.touear.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessRecord implements Serializable {
    private MultiValueMap<String, String> formData;
    private HttpHeaders headers;
    private String remoteAddress;
    private String targetUri;
    private String method;
    private String scheme;
    private String path;
    private String body;
}
