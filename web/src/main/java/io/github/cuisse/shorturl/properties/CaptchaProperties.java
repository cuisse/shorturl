package io.github.cuisse.shorturl.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties("google.recaptcha.key")
public record CaptchaProperties(String site, String secret) {

    @ConstructorBinding
    public CaptchaProperties { }

}
