package ru.practicum.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "endpoint")
@EnableJpaRepositories(basePackages = "ru.practicum")
@Getter
@Setter
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "app",nullable = false)
    private  String app;
    @Column(name = "uri",nullable = false)
    private  String uri;
    @Column(name = "ip",nullable = false)
    private  String ip;
    @Column(name = "created",nullable = false)
    private LocalDateTime timestamp;

    public EndpointHit(String app, String uri, String ip, LocalDateTime timestamp) {
        this.app=app;
        this.uri=uri;
        this.ip=ip;
        this.timestamp=timestamp;
    }

}
