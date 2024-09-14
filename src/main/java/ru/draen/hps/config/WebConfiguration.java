package ru.draen.hps.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.draen.hps.config.argumentresolver.PageConditionArgumentResolver;
import ru.draen.hps.config.argumentresolver.ScrollConditionArgumentResolver;

import java.util.List;

@Configuration
@AllArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {
    private final PageConditionArgumentResolver pageConditionArgumentResolver;
    private final ScrollConditionArgumentResolver scrollConditionArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(pageConditionArgumentResolver);
        resolvers.add(scrollConditionArgumentResolver);
    }
}
