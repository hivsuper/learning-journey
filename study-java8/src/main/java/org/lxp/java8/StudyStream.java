package org.lxp.java8;

import static java.util.Comparator.comparingInt;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudyStream {

    public static List<Student> collect(List<Student> list, Predicate<Student> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    public static String findFirst(List<Student> list, Predicate<Student> predicate) {
        return list.stream().filter(predicate).findFirst().get().getName();
    }

    public static Student orElse(List<Student> list, Predicate<Student> predicate) {
        return list.stream().filter(predicate).findFirst().orElse(new Student());
    }

    public static List<String> map(List<Student> list, Function<Student, String> function) {
        return list.stream().map(function).collect(Collectors.toList());
    }

    public static Set<String> map(List<Student> list, Predicate<Student> predicate,
            Function<Student, String> function) {
        return list.stream().filter(predicate).map(function).collect(Collectors.toSet());
    }

    public static List<Student> sort(List<Student> list) {
        list.sort(comparingInt(Student::getAge).thenComparing(Student::getName).reversed());
        return list;
    }

    public static Map<Integer, List<Student>> groupby(List<Student> list) {
        return list.stream().collect(Collectors.groupingBy(Student::getAge));
    }

    static class Student {
        private String studentNo;
        private String name;
        private int gender;
        private int age;

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("Student [studentNo=%s, name=%s, gender=%s, age=%s]", studentNo, name, gender, age);
        }
    }
}
