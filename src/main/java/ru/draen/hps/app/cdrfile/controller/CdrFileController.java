package ru.draen.hps.app.cdrfile.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.app.cdrfile.controller.dto.CdrFileDto;
import ru.draen.hps.app.cdrfile.controller.dto.ParseCdrRequest;
import ru.draen.hps.app.cdrfile.service.CdrFileService;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.domain.CdrFile;

@RestController
@RequestMapping(value = "/cdrFiles", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CdrFileController {
    private final CdrFileService cdrFileService;
    private final IMapper<CdrFile, CdrFileDto> cdrFileMapper;

    @PostMapping("/parse")
    public ResponseEntity<CdrFileDto> parse(@Validated ParseCdrRequest request) {
        return ResponseEntity.ok(
                cdrFileMapper.toDto(
                        cdrFileService.parseData(request.getFileId())));
    }
}
