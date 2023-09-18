package htwb.ai.apigateway.gatewayfilterfactories;

import htwb.ai.apigateway.exception.AuthorizationException;
import htwb.ai.apigateway.util.JwtUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class AuthorizationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AuthorizationGatewayFilterFactory.Config> {

    @NoArgsConstructor @Getter @Setter
    public static class Config {
        private boolean setSubjectHeader;
    }

    public AuthorizationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String userId = authorizeRequest(request.getHeaders());

            if (! config.isSetSubjectHeader())
                return chain.filter(exchange);

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("Subject", userId)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }, 1);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("setSubjectHeader");
    }

    private String authorizeRequest(HttpHeaders headers) throws AuthorizationException {
        String token = headers.getFirst(AUTHORIZATION);
        if (token == null)
            throw new AuthorizationException("Access Token required");

        return JwtUtils.getUserIdFromToken(token);   // throws AuthorizationException
    }
}
