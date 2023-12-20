package io.github.cuisse.api.shorturl.controller;

import io.github.cuisse.api.shorturl.service.URLService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/*")
public class URLController {

    private final URLService service;

    public URLController(URLService service) {
        this.service = service;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> create(@RequestParam("url") String url) {
        return ResponseEntity.ok(
                service.create(url)
        );
    }

    @RequestMapping(path = "find/{url}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> find(@PathVariable("url") String url) {
        return ResponseEntity.ok(
                service.find(url)
        );
    }

}
