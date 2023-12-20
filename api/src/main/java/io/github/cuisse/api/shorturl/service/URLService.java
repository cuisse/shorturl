package io.github.cuisse.api.shorturl.service;

import io.github.cuisse.api.shorturl.exception.APIException;
import io.github.cuisse.api.shorturl.model.URL;
import io.github.cuisse.api.shorturl.repository.URLRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Instant;
import java.util.Date;

@Service
public class URLService {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLService.class);

    private final int URL_LENGTH = 8;
    private final String BASE_62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final URLRepository repository;

    public URLService(URLRepository repository) {
        this.repository = repository;
    }

    public String create(String url) {
        String sanitizedURL = sanitize(url);
        return repository.save(
                new URL(shorten(sanitizedURL), sanitizedURL, Date.from(Instant.now()))
        ).getHash();
    }

    public String find(String url) {
        return repository.findById(url).orElseThrow(() -> {
            throw new APIException(HttpStatus.NOT_FOUND, "URL not found: " + url);
        }).getUrl();
    }

    public void delete(String url) {
        repository.deleteById(url);
    }

    private String shorten(String url) {
        int attempts = 1;
        var sb = new StringBuilder();
        while (true) {
            if (attempts++ > 10) {
                LOGGER.error("Failed to generate a normal URL for: " + url);
                throw new RuntimeException("Failed to generate a normal URL for: " + url);
            }
            for (int i = 0; i < URL_LENGTH; i++) {
                sb.append(BASE_62.charAt(
                        (int) (Math.random() * BASE_62.length())
                ));
            }
            if (false == repository.existsById(sb.toString())) {
                return sb.toString();
            } else {
                sb.setLength(0);
            }
        }
    }

    public String sanitize(String url) {
        try {
            URI.create(url).toURL();
            return url;
        } catch (Exception error) {
            if (error.getMessage().contains("is not absolute")) {
                return sanitize(("http://").concat(url));
            } else {
                throw new IllegalArgumentException("Invalid URL: " + url);
            }
        }
    }

}
