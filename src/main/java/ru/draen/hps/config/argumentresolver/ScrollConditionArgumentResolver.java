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
import ru.draen.hps.common.model.ScrollCondition;

import java.util.Optional;

@Component
public class ScrollConditionArgumentResolver implements HandlerMethodArgumentResolver {
    private static final ILabelService lbs = I18n.getLabelService();
    public static final String OFFSET_PARAM = "offset";
    public static final String LIMIT_PARAM = "limit";

    private final int OFFSET_DEFAULT = 0;
    @Value("${scroll.default-limit}")
    private Integer LIMIT_DEFAULT;
    @Value("${scroll.max-limit}")
    private Integer LIMIT_MAX;

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
                parseNum(offsetStr).filter(this::validOffsetOrThrow).orElse(OFFSET_DEFAULT),
                parseNum(limitStr).filter(this::validLimitOrThrow).orElse(LIMIT_DEFAULT),
                sort);
    }

    private boolean validOffsetOrThrow(Integer offset) {
        if (offset < 0) {
            throw new BadRequestException(lbs.msg("BadRequestException.ScrollCondition.offset.negative", offset));
        }
        return true;
    }

    private boolean validLimitOrThrow(Integer limit) {
        if (limit <= 0) {
            throw new BadRequestException(lbs.msg("BadRequestException.ScrollCondition.limit.nonPositive", limit));
        }
        if (limit > LIMIT_MAX) {
            throw new BadRequestException(lbs.msg("BadRequestException.ScrollCondition.limit.aboveMax", limit, LIMIT_MAX));
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
