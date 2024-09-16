package ru.draen.hps.config.argumentresolver;

import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.draen.hps.I18n;
import ru.draen.hps.common.exception.BadRequestException;
import ru.draen.hps.common.label.ILabelService;
import ru.draen.hps.common.model.PageCondition;

import java.util.Optional;

@Component
public class PageConditionArgumentResolver implements HandlerMethodArgumentResolver {
    private static final ILabelService lbs = I18n.getLabelService();
    public static final String PAGE_PARAM = "page";
    public static final String SIZE_PARAM = "size";

    private final Integer PAGE_DEFAULT = 0;
    @Value("${pagination.default-page-size}")
    private Integer SIZE_DEFAULT;
    @Value("${pagination.max-page-size}")
    private Integer SIZE_MAX;

    public static final SortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER = new SortHandlerMethodArgumentResolver();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PageCondition.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        int page = Optional.ofNullable(webRequest.getHeader("X-Page"))
                .or(() -> Optional.ofNullable(webRequest.getParameter(PAGE_PARAM)))
                .flatMap(this::parseNum)
                .filter(this::validPageOrThrow)
                .orElse(PAGE_DEFAULT);

        int size = Optional.ofNullable(webRequest.getHeader("X-Per-Page"))
                .or(() -> Optional.ofNullable(webRequest.getParameter(SIZE_PARAM)))
                .flatMap(this::parseNum)
                .filter(this::validSizeOrThrow)
                .orElse(SIZE_DEFAULT);

        Sort sort = DEFAULT_SORT_RESOLVER.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        return new PageCondition(page, size, sort);
    }

    private boolean validPageOrThrow(Integer page) {
        if (page < 0) {
            throw new BadRequestException(lbs.msg("BadRequestException.PageCondition.page.negative", page));
        }
        return true;
    }

    private boolean validSizeOrThrow(Integer size) {
        if (size <= 0) {
            throw new BadRequestException(lbs.msg("BadRequestException.PageCondition.size.nonPositive", size));
        }
        if (size > SIZE_MAX) {
            throw new BadRequestException(lbs.msg("BadRequestException.PageCondition.size.aboveMax", size, SIZE_MAX));
        }
        return true;
    }

    private Optional<Integer> parseNum(@Nullable String param) {
        if (StringUtils.isBlank(param)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.parseInt(param));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
