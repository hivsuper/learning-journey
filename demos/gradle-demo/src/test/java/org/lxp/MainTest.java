package org.lxp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class MainTest {
    @Test
    void main(CapturedOutput capturedOutput) {
        Main.main(new String[]{});
        assertThat(capturedOutput).containsPattern("Hello world!");
    }
}