package my.javalab.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetMinMaxFromObjectList {

    public static class Student {

        private String name;
        private int age;

        public  Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static void main(String[] args) {

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Pea", 20));
        studentList.add(new Student("Ni", 35));
        studentList.add(new Student("Pang", 30));
        studentList.add(new Student("Tum", 25));
        studentList.add(new Student("Sombut", 22));

        Student student =  Collections.max(studentList, Comparator.comparing(s -> s.getAge()));
        System.out.println("The older student is : " + student.getName());

        student =  Collections.min(studentList, Comparator.comparing(s -> s.getAge()));
        System.out.println("The youngest student is : " + student.getName());
        
        //using stream 
        Student maxAgeStudent = studentList.stream().max(Comparator.comparingInt(s -> s.getAge())).get();
        System.out.println("The older student is : " + maxAgeStudent.getName());


    }
}
