package ru.draen.hps.cdr.config;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.rsocket.core.Resume;
import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.loadbalance.RoundRobinLoadbalanceStrategy;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.service.RSocketServiceProxyFactory;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.CdrFileDto;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.ParseCdrRequest;
import ru.draen.hps.cdr.app.cdrfile.service.CdrFileService;
import ru.draen.hps.cdr.client.FileRSocketClient;
import ru.draen.hps.cdr.producer.CdrFileParsedProducer;
import ru.draen.hps.cdr.producer.FileUploadCancelProducer;
import ru.draen.hps.common.core.model.EUserRole;
import ru.draen.hps.common.messaging.model.FileRelatedMsg;
import ru.draen.hps.common.webflux.config.auth.RequestApplier;
import ru.draen.hps.common.webflux.saga.SagaStep;
import ru.draen.hps.common.webflux.utils.SagaUtils;

import java.net.URI;
import java.time.Duration;
import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Cdr Service",
                description = "Cdr Service",
                version = "1.0",
                contact = @Contact(
                        name = "ThatDraenGuy"
                )
        ),
        servers = {
                @Server(url = "/cdr-service", description = "Gateway server")
        }
)
public class AppConfiguration {
    @Bean
    public RequestApplier requestApplier(@Value("${api.prefix}") String apiPrefix,
                                         @Value("${springdoc.api-docs.path}") String docsPath) {
        return http -> http
                .pathMatchers(HttpMethod.GET, apiPrefix + "/cdr-files/**").hasAnyAuthority(
                        EUserRole.OPERATOR.name(), EUserRole.CLIENT.name())
                .pathMatchers(apiPrefix + "/cdr-files/**").hasAuthority(EUserRole.OPERATOR.name())
                .pathMatchers(docsPath).permitAll()
                .anyExchange().authenticated();
    }

    @Bean
    @Lazy
    @SneakyThrows
    public RSocketServiceProxyFactory rSocketServiceProxyFactory(EurekaClient eurekaClient,
                                                                 @Value("${app.socket.file-service-port}") Integer port,
                                                                 Jackson2JsonEncoder encoder, Jackson2JsonDecoder decoder) {
        RSocketRequester.Builder builder = RSocketRequester.builder();
        return RSocketServiceProxyFactory.builder(builder
                        .rsocketConnector(conn -> conn.resume(new Resume()
                                .retry(Retry.fixedDelay(5, Duration.ofMillis(100)))))
                        .rsocketStrategies(strategyBuilder -> strategyBuilder.encoder(encoder).decoder(decoder).build())
                        .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                        .transports(eurekaHosts(eurekaClient, "file-service", port), new RoundRobinLoadbalanceStrategy()))
                .build();
    }

    private Flux<List<LoadbalanceTarget>> eurekaHosts(EurekaClient eurekaClient, String serviceName, int port) {
        return Flux.interval(Duration.ofSeconds(10)).map(index -> {
            try {
                InstanceInfo service = eurekaClient.getNextServerFromEureka(serviceName, false);
                return List.of(LoadbalanceTarget.from(
                        service.getHostName(),
                        WebsocketClientTransport.create(service.getHostName(), port)
                ));
            } catch (Exception e) {
                return List.of();
            }
        });
    }

    @Bean
    @Lazy
    public FileRSocketClient fileRSocketClient(RSocketServiceProxyFactory factory) {
        return factory.createClient(FileRSocketClient.class);
    }

    @Bean
    public IMap<Long, CdrFileDto> cdrFileCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("cdr-files");
    }

    @Bean
    public SagaStep<ParseCdrRequest, CdrFileDto> cdrSagaStep(CdrFileService cdrFileService,
                                                         CdrFileParsedProducer cdrFileParsedProducer,
                                                         FileUploadCancelProducer fileUploadCancelProducer) {
        return SagaUtils.createStep(
                ParseCdrRequest::getFileId,
                req -> cdrFileService.parseData(req.getFileId()),
                req -> cdrFileService.delete(req.getFileId()),
                cdrFileParsedProducer::send,
                (req, e) -> fileUploadCancelProducer.send(req.getFileId(), e)
        );
    }
}
