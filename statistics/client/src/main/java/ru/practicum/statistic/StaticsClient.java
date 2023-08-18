package ru.practicum.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.EndpointDtoIn;
import ru.practicum.baseClient.BaseClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StaticsClient extends BaseClient {

    @Autowired
    public StaticsClient(@Value("${st.server.address}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveStats(EndpointDtoIn endpointDtoIn) {
        return post("/hit", endpointDtoIn);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start,
                                           LocalDateTime end,
                                           boolean unique,
                                           List<String> uris) {
        return get(start,end,unique,uris);

    }
}
