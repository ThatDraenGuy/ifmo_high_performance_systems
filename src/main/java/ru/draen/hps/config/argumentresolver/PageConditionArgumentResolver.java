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
import ru.draen.hps.common.model.PageCondition;

import java.util.Optional;

@Component
public class PageConditionArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String PAGE_PARAM = "page";
    public static final String SIZE_PARAM = "size";

    public static final int PAGE_DEFAULT = 0;
    public static final int SIZE_DEFAULT = 50;

    public static final SortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER = new SortHandlerMethodArgumentResolver();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PageCondition.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String pageStr = webRequest.getParameter(PAGE_PARAM);
        String sizeStr = webRequest.getParameter(SIZE_PARAM);

        Sort sort = DEFAULT_SORT_RESOLVER.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        return new PageCondition(
                parseNum(pageStr).orElse(PAGE_DEFAULT),
                parseNum(sizeStr).orElse(SIZE_DEFAULT),
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
