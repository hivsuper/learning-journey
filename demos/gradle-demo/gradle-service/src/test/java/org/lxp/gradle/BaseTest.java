package org.lxp.gradle;

import org.lxp.gradle.config.MySQLContainerInitializer;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = {MySQLContainerInitializer.class})
public class BaseTest {
}
