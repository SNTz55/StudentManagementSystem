import java.io.*;
import java.util.*;
public class StudentManager
{
    private TreeMap<String, Student> students;
    private String datafile = "students.txt";
    public StudentManager()
    {
        students = new TreeMap<>();
        loadFromFile();
    }

    void addStudent(Student student)
    {
        students.put(student.getUID(), student);
    }

    Student getStudent(String uid)
    {
        return students.get(uid);
    }

    Collection<Student> getAllStudents()
    {
        return students.values();
    }

    public void saveToFile()
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(datafile)))
        {
            for (Student student : getAllStudents())
            {
                StringBuilder line = new StringBuilder();
                line.append(student.getUID()).append(",");
                line.append(student.getName());

                for (int subject : student.getSubjects())
                {
                    line.append(",");
                    line.append(subject).append(":").append(student.getMark(subject));
                }
                writer.println(line.toString());
            }
        }
        catch (IOException err)
        {
            System.out.println("Error: " + err.getMessage());
        }
    }

    public void loadFromFile()
    {
        students.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(datafile)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String parts[] = line.split(",");
                Student student = new Student(parts[0], parts[1]);
                for (int i = 2; i < parts.length; i++)
                {
                    String subparts[] = parts[i].split(":");
                    student.addMark(Integer.parseInt(subparts[0]), Double.parseDouble(subparts[1]));
                }
                addStudent(student);
            }
        }
        catch (IOException err)
        {
            System.out.println("Error: " + err.getMessage());
        }
    }

    
}
