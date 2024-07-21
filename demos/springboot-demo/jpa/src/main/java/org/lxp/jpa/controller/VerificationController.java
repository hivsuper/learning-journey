package org.lxp.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.lxp.jpa.dto.VerificationCodeDto;
import org.lxp.jpa.dto.VerificationDto;
import org.lxp.jpa.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService verificationService;

    @PostMapping("/generate")
    public ResponseEntity<VerificationCodeDto> generate() {
        return ResponseEntity.ok(verificationService.generate());
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@ModelAttribute VerificationDto verificationDto) {
        verificationService.verify(verificationDto);
        return ResponseEntity.ok().build();
    }
}
