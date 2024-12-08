package ru.draen.hps.file.config;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import io.rsocket.core.Resume;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.draen.hps.common.core.exception.NotFoundException;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.core.model.EUserRole;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.common.webflux.config.auth.RequestApplier;
import ru.draen.hps.common.webflux.saga.SagaStep;
import ru.draen.hps.common.webflux.utils.SagaUtils;
import ru.draen.hps.file.controller.dto.FileBriefDto;
import ru.draen.hps.file.controller.dto.FileDto;
import ru.draen.hps.file.producer.FileUploadedProducer;
import ru.draen.hps.file.service.FileService;

import java.time.Duration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "File Service",
                description = "File Service",
                version = "1.0",
                contact = @Contact(
                        name = "ThatDraenGuy"
                )
        ),
        servers = {
                @Server(url = "/file-service", description = "Gateway server")
        }
)
public class AppConfiguration {
    @Bean
    public RequestApplier requestApplier(@Value("${api.prefix}") String apiPrefix,
                                         @Value("${springdoc.api-docs.path}") String docsPath) {
        return http -> http
                .pathMatchers(apiPrefix + "/files/**").hasAuthority(EUserRole.OPERATOR.name())
                .pathMatchers(docsPath).permitAll()
                .anyExchange().authenticated();
    }

    @Bean
    public RSocketServerCustomizer rSocketResume() {
        Resume resume =
                new Resume()
                        .retry(
                                Retry.fixedDelay(5, Duration.ofSeconds(1)));
        return rSocketServer -> rSocketServer.resume(resume);
    }

    @Bean
    public IMap<Long, File> fileCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("files");
    }

    @Bean
    public SagaStep<FileDto, FileBriefDto> fileSagaStep(
            FileService fileService,
            IMapper<File, FileBriefDto> fileBriefMapper,
            IMapper<File, FileDto> fileMapper,
            FileUploadedProducer fileUploadedProducer
    ) {
        return SagaUtils.createStartStep(
                FileDto::getFileId,
                fileDto -> fileService.create(fileMapper.toEntity(fileDto)).map(fileBriefMapper::toDto),
                FileBriefDto::getFileId,
                fileDto -> fileService.delete(fileDto.getFileId())
                        .flatMap(result -> result ? Mono.empty() : Mono.error(NotFoundException::new)),
                fileUploadedProducer::send,
                SagaUtils::noOnError
        );
    }
}
