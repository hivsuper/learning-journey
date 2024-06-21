package org.lxp.powermock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class PowerMockHelperTest {

    @Test
    public void getFullName(CapturedOutput capturedOutput) {
        assertThat(PowerMockHelper.getFullName("11", "bb")).isEqualTo("11 bb");
        assertThat(capturedOutput).containsPattern(
                "INFO(.+)org.lxp.powermock.PowerMockHelper(.+)Full name is 11 bb");
    }
}