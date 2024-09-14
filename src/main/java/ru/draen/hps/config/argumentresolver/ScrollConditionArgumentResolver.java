package ru.draen.hps.config.argumentresolver;

import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.draen.hps.common.model.ScrollCondition;

import java.util.Optional;

@Component
public class ScrollConditionArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String OFFSET_PARAM = "offset";
    public static final String LIMIT_PARAM = "limit";

    public static final int OFFSET_DEFAULT = 0;
    public static final int LIMIT_DEFAULT = 50;

    public static final SortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER = new SortHandlerMethodArgumentResolver();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ScrollCondition.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String offsetStr = webRequest.getParameter(OFFSET_PARAM);
        String limitStr = webRequest.getParameter(LIMIT_PARAM);

        Sort sort = DEFAULT_SORT_RESOLVER.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        return new ScrollCondition(
                parseNum(offsetStr).orElse(OFFSET_DEFAULT),
                parseNum(limitStr).orElse(LIMIT_DEFAULT),
                sort);
    }

    private Optional<Integer> parseNum(@Nullable String param) {
        if (StringUtils.isBlank(param)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Math.max(0, Integer.parseInt(param)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
