package org.lxp.gradle;

import org.lxp.gradle.config.PostgreSQLContainerInitializer;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
public class BaseTest {
}
