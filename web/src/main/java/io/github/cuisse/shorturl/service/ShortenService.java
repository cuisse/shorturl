package io.github.cuisse.shorturl.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;

@Service
public class ShortenService {

    @Value("${api.url}")
    private String apiURL;

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.secret}")
    private String apiSecret;

    public String create(String longURL) {
        return sendRequest("/api/v1/create?url=" + longURL, HttpMethod.POST);
    }

    public String find(String shortURL) {
        return sendRequest("/api/v1/find/" + shortURL, HttpMethod.GET);
    }

    private String sendRequest(String url, HttpMethod method) {
        try {
            WebClient.RequestHeadersUriSpec<?> request = WebClient.create().method(method);
            request.uri(apiURL + url);
            request.header(apiKey, apiSecret);
            return request.exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(String.class);
                } else {
                    return Mono.error(new ResponseStatusException(response.statusCode()));
                }
            }).block();
        } catch (WebClientException error) {
            if (error.getMessage().contains("Connection refused")) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "API server is down", error);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "API server error", error);
            }
        }
    }

}
