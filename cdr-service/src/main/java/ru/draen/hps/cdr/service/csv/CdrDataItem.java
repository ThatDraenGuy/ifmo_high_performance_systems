package ru.draen.hps.cdr.service.csv;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.draen.hps.common.csv.converter.LocalDateTimeConverter;
import ru.draen.hps.common.r2dbcdao.domain.ECallDirection;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CdrDataItem {
    @CsvBindByPosition(required = true, position = 0)
    private ECallDirection callDirection;

    @CsvBindByPosition(required = true, position = 1)
    private String phoneNumber;

    @CsvCustomBindByPosition(required = true, position = 2, converter = LocalDateTimeConverter.class)
    private LocalDateTime startTime;

    @CsvCustomBindByPosition(required = true, position = 3, converter = LocalDateTimeConverter.class)
    private LocalDateTime endTime;
}
