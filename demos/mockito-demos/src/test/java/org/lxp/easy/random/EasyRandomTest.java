package org.lxp.easy.random;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.api.Randomizer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://www.baeldung.com/java-easy-random
 */
public class EasyRandomTest {
    @Test
    public void test() {
        EasyRandom easyRandom = new EasyRandom();
        TestEntity entity = easyRandom.nextObject(TestEntity.class);
        assertThat(entity.getField1()).isNotNull();
        assertThat(entity.getField2()).isIn(TestEnum.values());
    }

    @Test
    public void testEasyRandomParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.setStringLengthRange(new EasyRandomParameters.Range(3, 3));
        parameters.randomize(TestEnum.class, (Randomizer) () -> TestEnum.A);
        EasyRandom easyRandom = new EasyRandom(parameters);
        TestEntity entity = easyRandom.nextObject(TestEntity.class);
        assertThat(entity.getField1()).isNotNull();
        assertThat(entity.getField1()).hasSize(3);
        assertThat(entity.getField2()).isIn(TestEnum.A);
    }
}

enum TestEnum {
    A, B
}

class TestEntity {
    private String field1;
    private TestEnum field2;

    public TestEntity(String field1, TestEnum field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public TestEnum getField2() {
        return field2;
    }

    public void setField2(TestEnum field2) {
        this.field2 = field2;
    }
}