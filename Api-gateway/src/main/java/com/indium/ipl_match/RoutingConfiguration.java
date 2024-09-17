package com.indium.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("insert-match-data", r -> r.path("/api/matches")
                        .uri("http://localhost:8085"))  // Forward to cricket microservice
                .route("get-matches-by-player", r -> r.path("/api/matches/player/match/{playerName}")
                        .uri("http://localhost:8085"))  // Forward to cricket microservice
                .route("get-cumulative-score", r -> r.path("/api/matches/score")
                        .uri("http://localhost:8085"))  // Forward to cricket microservice
                .route("get-scores-by-date", r -> r.path("/api/matches/match/date")
                        .uri("http://localhost:8085"))  // Forward to cricket microservice
                .route("get-top-batsmen", r -> r.path("/api/matches/top-batsmen")
                        .uri("http://localhost:8085"))  // Forward to cricket microservice
                .route("get-wicket-count-by-bowler", r -> r.path("/api/json-upload/player/wickets")
                        .uri("http://localhost:8085"))  // Forward to cricket microservice
                .build();
    }
}
