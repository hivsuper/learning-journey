package org.lxp.springboot.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lxp.springboot.BaseTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class EmailServiceTest extends BaseTest {
    private final String toAddress = "to";
    private final String subject = "subject";
    private final String text = "text";
    private final String attachment = "git-test.properties";
    private String attachmentPath;
    @Inject
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        URL url = EmailServiceTest.class.getClassLoader().getResource("");
        Assert.assertNotNull(url);
        File file = new File(url.getFile());
        final String absolutePath = file.getAbsolutePath() + File.separator;
        attachmentPath = absolutePath + attachment;
    }

    @Test
    void testSend() {
        assertThat(emailService.send(toAddress, subject, text)).isTrue();
    }

    @Test
    void returnFalseWhenFailToSend() {
        assertThat(emailService.send(null, subject, text)).isFalse();
    }

    @Test
    void testSendWithAttachment() {
        assertThat(emailService.send(toAddress, toAddress, text, attachmentPath)).isTrue();
    }

    @Test
    void returnFalseWhenFailToSendWithAttachment() {
        assertThat(emailService.send(null, toAddress, text, attachmentPath)).isFalse();
    }
}