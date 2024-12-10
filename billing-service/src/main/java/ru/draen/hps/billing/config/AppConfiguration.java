package ru.draen.hps.billing.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.billing.app.billing.service.BillingService;
import ru.draen.hps.billing.producer.BillingPerformedProducer;
import ru.draen.hps.billing.producer.CdrFileCancelProducer;
import ru.draen.hps.common.core.model.EUserRole;
import ru.draen.hps.common.webflux.config.auth.RequestApplier;
import ru.draen.hps.common.webflux.saga.SagaStep;
import ru.draen.hps.common.webflux.utils.SagaUtils;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Billing Service",
                description = "Billing Service",
                version = "1.0",
                contact = @Contact(
                        name = "ThatDraenGuy"
                )
        ),
        servers = {
                @Server(url = "${app.openapi.prefix}/billing-service", description = "Gateway server")
        }
)
public class AppConfiguration {
    @Bean
    public RequestApplier requestApplier(@Value("${api.prefix}") String apiPrefix,
                                         @Value("${springdoc.api-docs.path}") String docsPath) {
        return http -> http
                .pathMatchers(apiPrefix + "/billing/**").hasAuthority(EUserRole.OPERATOR.name())
                .pathMatchers(docsPath).permitAll()
                .anyExchange().authenticated();
    }

    @Bean
    public SagaStep<BillingRequest, Void> billingSagaStep(
            BillingService billingService,
            BillingPerformedProducer billingPerformedProducer,
            CdrFileCancelProducer cdrFileCancelProducer
    ) {
        return SagaUtils.createStep(
                BillingRequest::cdrFileId,
                billingService::perform,
                SagaUtils::noRollback,
                billingPerformedProducer::send,
                (req, e) -> cdrFileCancelProducer.send(req.cdrFileId(), e)
        );
    }
}
