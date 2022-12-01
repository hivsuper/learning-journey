package org.lxp.java8.map;

import org.junit.Test;
import org.lxp.vo.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

/**
 * @see https://www.cnblogs.com/hiver/p/9156147.html
 */
public class CollectionToMapTest {
    private List<Student> fakeStudent() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("1", "name1", false, 2));
        students.add(new Student("2", "name2", false, 2));
        students.add(new Student("3", "name2", null, 2));
        students.add(new Student("4", "name4", true, 2));
        students.add(new Student(null, "name5", true, 2));
        return students;
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenGroupingByNullKey() {
        fakeStudent().stream().collect(Collectors.groupingBy(Student::getStudentNo));
    }

    @Test
    public void shouldReturnMapWhenToMapNullKey() {
        Map<String, Student> map = fakeStudent().stream()
                .collect(Collectors.toMap(Student::getStudentNo, Function.identity()));
        assertEquals("{null=Student [studentNo=null, name=name5, gender=true, age=2], "
                + "1=Student [studentNo=1, name=name1, gender=false, age=2], "
                + "2=Student [studentNo=2, name=name2, gender=false, age=2], "
                + "3=Student [studentNo=3, name=name2, gender=null, age=2], "
                + "4=Student [studentNo=4, name=name4, gender=true, age=2]}", map.toString());
    }

    @Test
    public void shouldThrowNPEWhenToMapNullValue() {
        assertThatThrownBy(() -> fakeStudent().stream().collect(Collectors.toMap(Student::getStudentNo, Student::getGender))).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldReturnMapWhenPartitioningByNullKey() {
        assertThatThrownBy(() -> fakeStudent().stream().collect(Collectors.partitioningBy(Student::getGender))).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenToMapDuplicateKey() {
        assertThatThrownBy(() -> fakeStudent().stream().collect(Collectors.toMap(Student::getName, Function.identity())))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("Student [studentNo=2, name=name2, gender=false, age=2]");
    }

    @Test
    public void shouldReturnMapWhenToMapDuplicateKey() {
        Map<String, Student> map = fakeStudent().stream()
                .collect(Collectors.toMap(Student::getName, Function.identity(), (student1, student2) -> student1));
        assertEquals("{name5=Student [studentNo=null, name=name5, gender=true, age=2], "
                + "name4=Student [studentNo=4, name=name4, gender=true, age=2], "
                + "name2=Student [studentNo=2, name=name2, gender=false, age=2], "
                + "name1=Student [studentNo=1, name=name1, gender=false, age=2]}", map.toString());
    }

    @Test
    public void shouldReturnMapWhenCollectDuplicateKey() {
        Map<String, Student> map = fakeStudent().stream().collect(HashMap::new, (m, v) -> m.put(v.getName(), v),
                HashMap::putAll);
        assertEquals("{name5=Student [studentNo=null, name=name5, gender=true, age=2], "
                + "name4=Student [studentNo=4, name=name4, gender=true, age=2], "
                + "name2=Student [studentNo=3, name=name2, gender=null, age=2], "
                + "name1=Student [studentNo=1, name=name1, gender=false, age=2]}", map.toString());
    }

    @Test
    public void shouldReturnSameMapWhenGroupingByAndPartitioningBy() {
        List<Student> students = fakeStudent().stream().filter(student -> student.getGender() != null)
                .collect(Collectors.toList());
        Map<Boolean, List<Student>> groupingByMap = students.stream()
                .collect(Collectors.groupingBy(Student::getGender));
        Map<Boolean, List<Student>> partitioningByMap = students.stream()
                .collect(Collectors.partitioningBy(Student::getGender));
        assertEquals("{false=[Student [studentNo=1, name=name1, gender=false, age=2], "
                + "Student [studentNo=2, name=name2, gender=false, age=2]], "
                + "true=[Student [studentNo=4, name=name4, gender=true, age=2], "
                + "Student [studentNo=null, name=name5, gender=true, age=2]]}", groupingByMap.toString());
        assertEquals(groupingByMap.toString(), partitioningByMap.toString());
    }

    @Test
    public void shouldReturnDifferentMapWhenGroupingByAndPartitioningBy() {
        Function<Student, Boolean> function = student -> student.getAge() > 3;
        List<Student> students = fakeStudent();
        Map<Boolean, List<Student>> groupingByMap = students.stream().collect(Collectors.groupingBy(function));
        Map<Boolean, List<Student>> partitioningByMap = students.stream()
                .collect(Collectors.partitioningBy(function::apply));
        assertEquals("{false=[Student [studentNo=1, name=name1, gender=false, age=2], "
                + "Student [studentNo=2, name=name2, gender=false, age=2], "
                + "Student [studentNo=3, name=name2, gender=null, age=2], "
                + "Student [studentNo=4, name=name4, gender=true, age=2], "
                + "Student [studentNo=null, name=name5, gender=true, age=2]]}", groupingByMap.toString());
        assertEquals(
                "{false=[Student [studentNo=1, name=name1, gender=false, age=2], "
                        + "Student [studentNo=2, name=name2, gender=false, age=2], "
                        + "Student [studentNo=3, name=name2, gender=null, age=2], "
                        + "Student [studentNo=4, name=name4, gender=true, age=2], "
                        + "Student [studentNo=null, name=name5, gender=true, age=2]], true=[]}",
                partitioningByMap.toString());
    }
}
