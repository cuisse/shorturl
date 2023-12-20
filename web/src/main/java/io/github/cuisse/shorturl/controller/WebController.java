package io.github.cuisse.shorturl.controller;

import io.github.cuisse.shorturl.properties.CaptchaProperties;
import io.github.cuisse.shorturl.service.CaptchaService;
import io.github.cuisse.shorturl.service.ShortenService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.CompletableFuture;

@Controller
public class WebController {

    private final CaptchaProperties captchaProperties;
    private final CaptchaService captchaService;
    private final ShortenService service;

    @Autowired
    public WebController(CaptchaProperties captchaProperties, CaptchaService captchaService, ShortenService service) {
        this.captchaProperties = captchaProperties;
        this.captchaService = captchaService;
        this.service = service;
    }

    @GetMapping("/favicon.ico")
    public String favicon() {
        return "forward:/resources/favicon.ico";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("captcha_site", captchaProperties.site());
        model.addAttribute("data_completed", "false");
        return "home";
    }

    @GetMapping("/{url}")
    public ModelAndView redirect(@PathVariable String url) {
        return new ModelAndView("redirect:" + service.find(url));
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<String>> create(@RequestParam("url") String url, @RequestParam(value = "g-recaptcha-response") String captcha, HttpServletRequest request, Model model) {
        return CompletableFuture.supplyAsync(() -> {
            captchaService.validate(captcha, request.getRemoteAddr());
            return ResponseEntity.ok(
                    "{\"url\": \"" + service.create(url) + "\"}"
            );
        });
    }

}
