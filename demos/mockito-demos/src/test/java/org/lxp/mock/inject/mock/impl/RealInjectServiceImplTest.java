package org.lxp.mock.inject.mock.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lxp.mock.inject.mock.InjectService;
import org.lxp.mock.inject.mock.RealInjectService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RealInjectServiceImplTest {
    @Mock
    private InjectService injectService;
    @InjectMocks
    private final RealInjectService realService = new RealInjectServiceImpl();

    @BeforeEach
    public void setUp() {
        Mockito.when(injectService.getName()).thenReturn("Super Li");
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals("Real Name is Super Li", realService.getName());
    }

}
