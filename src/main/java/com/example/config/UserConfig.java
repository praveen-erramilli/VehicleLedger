package com.example.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserConfig {
    private String name;
    private String pemPath;
    private String networkConfigPath;
}
