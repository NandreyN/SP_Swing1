import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class StudentReader {
    private static final  String FILE = "students.txt";
    public static List<Student> getStudents() throws FileNotFoundException {
        List<Student> students = new ArrayList<>();
        Scanner in = new Scanner(new File(FILE));
        while (in.hasNextLine())
        {
            String[] line = in.nextLine().split("\\s+");
            students.add(new Student(line[0], Integer.parseInt(line[1]),Integer.parseInt(line[2])));
        }

        return students;
    }
}
