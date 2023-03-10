package devfuel.streamapi;

import devfuel.streamapi.domain.Group;
import devfuel.streamapi.domain.Student;
import devfuel.streamapi.domain.Event;
import devfuel.streamapi.domain.Position;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

public class Streams {
    private List<Student> students = List.of(
            new Student("Tonny", "Watson",   243,  43, Position.AVERAGE_STUDENT),
            new Student("Alexandr",    "Volkov",   523,  40, Position.GOOD_STUDENT),
            new Student("James",    "Bond", 6423, 26, Position.GOOD_STUDENT),
            new Student("Jack",    "Jackson",  5543, 53, Position.BAD_STUDENT),
            new Student("Eric",    "Proudhon", 2534, 22, Position.BAD_STUDENT),
            new Student("Andrew",  "Titled",    3456, 44, Position.BAD_STUDENT),
            new Student("Joe",     "Roman",   723,  30, Position.GOOD_STUDENT),
            new Student("Pedro",    "Pascal", 7423, 35, Position.GOOD_STUDENT),
            new Student("Remy",    "Protean",  7543, 42, Position.BAD_STUDENT),
            new Student("Chloe",    "Jillane", 7534, 31, Position.BAD_STUDENT),
            new Student("Terry",    "Bosh",    7456, 54, Position.BAD_STUDENT),
            new Student("Mark",    "Smith",   123,  41, Position.GOOD_STUDENT),
            new Student("July",    "Sandstone", 1423, 28, Position.GOOD_STUDENT),
            new Student("Morgan",     "Oren",  1543, 52, Position.BAD_STUDENT),
            new Student("Morning",    "Tesla", 1534, 27, Position.BAD_STUDENT),
            new Student("Tesla",    "Rain",    1456, 32, Position.BAD_STUDENT)
    );

    private List<Group> groups = List.of(
            new Group(1, 0, "Main"),
            new Group(2, 1, "West"),
            new Group(3, 1, "East"),
            new Group(4, 2, "Germany"),
            new Group(5, 2, "France"),
            new Group(6, 3, "China"),
            new Group(7, 3, "Japan")
    );

    @Test
    public void creation() throws IOException {
        Stream<String> lines = Files.lines(Paths.get("some.txt"));
        Stream<Path> list = Files.list(Paths.get("./"));
        Stream<Path> walk = Files.walk(Paths.get("./"), 3);

        IntStream intStream = IntStream.of(1, 2, 3, 4);
        DoubleStream doubleStream = DoubleStream.of(1.2, 3.4);
        IntStream range = IntStream.range(10, 100); // 10 .. 99
        IntStream intStream1 = IntStream.rangeClosed(10, 100); // 10 .. 100

        int[] ints = {1, 2, 3, 4};
        IntStream stream = Arrays.stream(ints);

        Stream<String> stringStream = Stream.of("1", "2", "3");
        Stream<? extends Serializable> stream1 = Stream.of(1, "2", "3");

        Stream<String> build = Stream.<String>builder()
                .add("Mike")
                .add("joe")
                .build();

        Stream<Student> stream2 = students.stream();
        Stream<Student> employeeStream = students.parallelStream();

        Stream<Event> generate = Stream.generate(() ->
                new Event(UUID.randomUUID(), LocalDateTime.now(), "")
        );

        Stream<Integer> iterate = Stream.iterate(1950, val -> val + 3);

        Stream<String> concat = Stream.concat(stringStream, build);
    }

    @Test
    public void terminate() {
        Stream<Student> stream = students.stream();
        stream.count();

        students.stream().forEach(student -> System.out.println(student.getAge()));
        students.forEach(student -> System.out.println(student.getAge()));

        students.stream().forEachOrdered(student -> System.out.println(student.getAge()));

        students.stream().collect(Collectors.toList());
        students.stream().toArray();
        Map<Integer, String> collect = students.stream().collect(Collectors.toMap(
                Student::getId,
                emp -> String.format("%s %s", emp.getLastName(), emp.getFirstName())
        ));

        IntStream intStream = IntStream.of(100, 200, 300, 400);
        intStream.reduce((left, right) -> left + right).orElse(0);

        System.out.println(groups.stream().reduce(this::reducer));

        IntStream.of(100, 200, 300, 400).average();
        IntStream.of(100, 200, 300, 400).max();
        IntStream.of(100, 200, 300, 400).min();
        IntStream.of(100, 200, 300, 400).sum();
        IntStream.of(100, 200, 300, 400).summaryStatistics();

        students.stream().max(Comparator.comparingInt(Student::getAge));

        students.stream().findAny();
        students.stream().findFirst();

        students.stream().noneMatch(student -> student.getAge() > 60); // true
        students.stream().anyMatch(student -> student.getPosition() == Position.AVERAGE_STUDENT); // true
        students.stream().allMatch(student -> student.getAge() > 18); // true
    }

    @Test
    public void transform() {
        LongStream longStream = IntStream.of(100, 200, 300, 400).mapToLong(Long::valueOf);
        IntStream.of(100, 200, 300, 400).mapToObj(value ->
                new Event(UUID.randomUUID(), LocalDateTime.of(value, 12, 1, 12, 0), "")
        );

        IntStream.of(100, 200, 300, 400, 100, 200).distinct(); // 100, 200, 300, 400

        Stream<Student> employeeStream = students.stream().filter(student -> student.getPosition() != Position.AVERAGE_STUDENT);

        students.stream()
                .skip(3)
                .limit(5);

        students.stream()
                .sorted(Comparator.comparingInt(Student::getAge))
                .peek(emp -> emp.setAge(18))
                .map(emp -> String.format("%s %s", emp.getLastName(), emp.getFirstName()));

        students.stream().takeWhile(student -> student.getAge() > 30).forEach(System.out::println);
        System.out.println();
        students.stream().dropWhile(student -> student.getAge() > 30).forEach(System.out::println);

        System.out.println();

        IntStream.of(100, 200, 300, 400)
                .flatMap(value -> IntStream.of(value - 50, value))
                .forEach(System.out::println);
    }

    @Test
    public void real() {
        Stream<Student> stud = students.stream()
                .filter(student ->
                        student.getAge() <= 30 && student.getPosition() != Position.BAD_STUDENT
                )
                .sorted(Comparator.comparing(Student::getLastName));

        print(stud);

        Stream<Student> sorted = students.stream()
                .filter(student -> student.getAge() > 40)
                .sorted((o1, o2) -> o2.getAge() - o1.getAge())
                .limit(4);

        print(sorted);

        IntSummaryStatistics statistics = students.stream()
                .mapToInt(Student::getAge)
                .summaryStatistics();

        System.out.println(statistics);
    }

    private void print(Stream<Student> stream) {
        stream
                .map(emp -> String.format(
                        "%4d | %-15s %-10s age %s %s",
                        emp.getId(),
                        emp.getLastName(),
                        emp.getFirstName(),
                        emp.getAge(),
                        emp.getPosition()
                ))
                .forEach(System.out::println);

        System.out.println();
    }

    public Group reducer(Group parent, Group child) {
        if (child.getParent() == parent.getId()) {
            parent.getChild().add(child);
        } else {
            parent.getChild().forEach(subParent -> reducer(subParent, child));
        }

        return parent;
    }
}
