package org.lxp.java8;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

@RunWith(MockitoJUnitRunner.class)
public class StudyFileTest {
    private File file;
    @Captor
    private ArgumentCaptor<byte[]> byteCaptor;

    @Before
    public void setUp() throws Exception {
        file = File.createTempFile("StudyFileTest", null);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            IntStream.rangeClosed(1, 3).forEach(i -> {
                try {
                    String s = String.valueOf(i);
                    writer.append(s).append("\r\n").append(s).append("\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @After
    public void tearDown() {
        file.delete();
    }

    @Test
    public void testRead() throws Exception {
        assertThat(StudyFile.read(file)).containsExactly("1", "1", "2", "2", "3", "3");
    }

    @Test
    public void testReadDistinct() throws Exception {
        assertThat(Files.list(Paths.get(file.getParent())).anyMatch(f -> f.getFileName().toString().equals(file.getName()))).isTrue();
        assertThat(StudyFile.readDistinct(file)).containsExactly("3", "2", "1");
    }

    @Test
    public void testLines() throws Exception {
        assertThat(StudyFile.lines(file.getPath())).containsExactly("1", "1", "2", "2", "3", "3");
    }

    @Test
    public void testWrite() throws Exception {
        try (MockedStatic<Files> mocked = mockStatic(Files.class)) {
            ArgumentCaptor<Path> pathArgumentCaptor = ArgumentCaptor.forClass(Path.class);
            Path path = Mockito.mock(Path.class);
            mocked.when(() -> Files.write(pathArgumentCaptor.capture(), byteCaptor.capture())).thenReturn(path);
            assertThat(StudyFile.write("asd", "1".getBytes())).isEqualTo(path);
        }
    }
}
