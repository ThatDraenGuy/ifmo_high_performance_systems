package ru.draen.hps.app.cdrfile.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.draen.hps.app.cdrdata.controller.dto.CdrDataCondition;
import ru.draen.hps.app.cdrdata.controller.dto.CdrDataDto;
import ru.draen.hps.app.cdrdata.service.CdrDataService;
import ru.draen.hps.app.cdrfile.controller.dto.CdrFileDto;
import ru.draen.hps.app.cdrfile.controller.dto.ParseCdrRequest;
import ru.draen.hps.app.cdrfile.service.CdrFileService;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.CdrData;
import ru.draen.hps.domain.CdrFile;

@RestController
@RequestMapping(value = "/cdr-files", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CdrFileController {
    private final CdrFileService cdrFileService;
    private final CdrDataService cdrDataService;
    private final IMapper<CdrFile, CdrFileDto> cdrFileMapper;
    private final IMapper<CdrData, CdrDataDto> cdrDataMapper;

    @PostMapping("/parse")
    public ResponseEntity<CdrFileDto> parse(@Validated @RequestBody ParseCdrRequest request) {
        return ResponseEntity.ok(
                cdrFileMapper.toDto(
                        cdrFileService.parseData(request.getFileId())));
    }

    @GetMapping("/{id}/records/paged")
    public ResponseEntity<PageResponse<CdrDataDto>> findRecordsPage(@PathVariable("id") Long id, @Validated CdrDataCondition condition,
                                                    PageCondition pageCondition) {
        return ResponseEntity.of(cdrFileService.findById(id).map(file -> {
            condition.setCdrFileId(file.getId());
            return cdrDataService.findAll(condition, pageCondition).map(cdrDataMapper::toDto);
        }));
    }

    @GetMapping("/{id}/records")
    public ResponseEntity<ScrollResponse<CdrDataDto>> findRecords(@PathVariable("id") Long id, @Validated CdrDataCondition condition,
                                                                      ScrollCondition scrollCondition) {
        return ResponseEntity.of(cdrFileService.findById(id).map(file -> {
            condition.setCdrFileId(file.getId());
            return cdrDataService.findAll(condition, scrollCondition).map(cdrDataMapper::toDto);
        }));
    }
}
