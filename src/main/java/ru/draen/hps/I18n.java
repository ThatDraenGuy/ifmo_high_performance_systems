package ru.draen.hps;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.draen.hps.common.label.ILabelService;
import ru.draen.hps.common.label.ResourceBundleLabelService;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class I18n {
    @Getter
    private final static ILabelService labelService = ResourceBundleLabelService.build(builder ->
            builder.dispositionMarker(I18n.class));
}
