package org.lxp.mock.inject.mock.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lxp.mock.inject.mock.InjectService;
import org.lxp.mock.inject.mock.RealInjectService;
import org.lxp.mock.inject.mock.impl.RealInjectServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RealInjectServiceImplTest {
    @Mock
    private InjectService injectService;
    @InjectMocks
    private RealInjectService realService = new RealInjectServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(injectService.getName()).thenReturn("Super Li");
    }

    @Test
    public void testGetName() throws Exception {
        Assert.assertEquals("Real Name is Super Li", realService.getName());
    }

}
