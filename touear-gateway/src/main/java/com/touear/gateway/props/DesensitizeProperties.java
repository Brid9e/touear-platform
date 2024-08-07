package com.touear.gateway.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties("berserker.desensitize")
public class DesensitizeProperties {
    private Map<String, List<DesensitizeField>> configs;

    public Map<String, List<DesensitizeField>> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, List<DesensitizeField>> configs) {
        this.configs = configs;
    }
    @Getter
    @Setter
    public static class DesensitizeField {
        private String name;
        private String type;

        // getters and setters

        @Override
        public String toString() {
            return "DesensitizeField{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

}
