package io.github.cuisse.shorturl.service;

import io.github.cuisse.shorturl.properties.CaptchaProperties;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CaptchaService {

    private final CaptchaProperties captchaProperties;

    public CaptchaService(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    public void validate(String captcha, String clientIP) {
        WebClient.RequestHeadersUriSpec<?> request = WebClient.create().get();
        request.uri(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", captchaProperties.secret(), captcha, clientIP));
        String response = request.exchangeToMono(res -> res.bodyToMono(String.class)).block();
        if (response == null || false == response.contains("\"success\": true")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid captcha");
        }
    }

}
