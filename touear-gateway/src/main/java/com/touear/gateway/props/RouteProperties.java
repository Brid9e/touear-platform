package com.touear.gateway.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

@Data
@RefreshScope
@ConfigurationProperties("berserker.document")
public class RouteProperties {

	private final List<RouteResource> resources = new ArrayList<>();

}