package org.lxp.gradle.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.gradle.dto.TestTableDto;
import org.lxp.gradle.entity.TestTable;
import org.lxp.gradle.repository.TestTableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestTableRepository testTableRepository;

    @Operation(summary = "Add an entry", description = "Return the added entry id.")
    @PostMapping(value = "/add")
    public ResponseEntity<Integer> add(@RequestParam @NotEmpty String name) {
        return ResponseEntity.ok(testTableRepository.save(TestTable.builder().name(name).build()).getId());
    }

    @Operation(summary = "Rename an entry")
    @PatchMapping(value = "/rename")
    public ResponseEntity<TestTable> rename(@Valid @RequestBody TestTableDto testTableDto) {
        final var testTable = testTableRepository.findById(testTableDto.getId()).orElseThrow(RuntimeException::new);
        testTable.setName(testTableDto.getName());
        return ResponseEntity.ok(testTableRepository.save(testTable));
    }

    @Operation(summary = "Query entries")
    @GetMapping(value = "/findAll")
    public ResponseEntity<Page<TestTable>> list(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(testTableRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNumber)));
    }
}
