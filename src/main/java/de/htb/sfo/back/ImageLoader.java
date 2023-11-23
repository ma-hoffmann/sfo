package de.htb.sfo.back;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix="application")
public class ImageLoader {

    private Map<String,String> images;

}
