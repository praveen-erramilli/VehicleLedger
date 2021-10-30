package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties("user")
@Data
public class UserProperties {
    Map<String, UserConfig> configs = new HashMap<>();
}

