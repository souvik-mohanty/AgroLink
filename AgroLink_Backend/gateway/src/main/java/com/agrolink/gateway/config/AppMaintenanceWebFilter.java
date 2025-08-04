package com.agrolink.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AppMaintenanceWebFilter implements WebFilter {

    @Value("${app.maintenanceMode:false}")
    private boolean maintenanceMode;

    @Value("#{'${app.maintenancePaths:/api/orders,/api/admin}'.split(',')}")
    private List<String> maintenancePaths;

    @Value("${app.originRestrictionEnabled:false}")
    private boolean originRestrictionEnabled;

    @Value("${app.restrictedOrigin:http://localhost:5173}")
    private String restrictedOrigin;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        String origin = exchange.getRequest().getHeaders().getFirst("Origin");

        boolean blockAll = maintenanceMode;
        boolean blockPath = maintenancePaths.stream().anyMatch(uri::startsWith);
        boolean blockOrigin = originRestrictionEnabled && origin != null && origin.equals(restrictedOrigin);

        if (blockAll || blockPath || blockOrigin) {
            exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String responseBody = "{\"message\": \"Service temporarily unavailable due to maintenance [AgroLink].\"}";
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        return chain.filter(exchange);
    }
}