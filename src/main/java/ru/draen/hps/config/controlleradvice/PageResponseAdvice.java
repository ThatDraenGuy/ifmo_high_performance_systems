package ru.draen.hps.config.controlleradvice;

import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import ru.draen.hps.common.model.PageResponse;

@ControllerAdvice
public class PageResponseAdvice implements ResponseBodyAdvice<PageResponse<?>> {
    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return PageResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public PageResponse<?> beforeBodyWrite(PageResponse<?> body, @NonNull MethodParameter returnType,
                                           @NonNull MediaType selectedContentType,
                                           @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                           @NonNull ServerHttpRequest request,
                                           @NonNull ServerHttpResponse response) {
        if (body != null) {
            response.getHeaders().add("X-Total-Count", body.listInfo().count().toString());
        }
        return body;
    }
}
