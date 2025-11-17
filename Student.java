import java.util.*;
class Student
{
    private String uid;
    private String name;
    private HashMap<Integer, Double> marks;

    public Student(String uid, String name)
    {
        this.uid = uid;
        this.name = name;
        this.marks = new HashMap<>();
    }

    String getUID()
    {
        return uid;
    }

    String getName()
    {
        return name;
    }

    void addMark(int subjectID, double mark)
    {
        marks.put(subjectID,mark);
    }

    Double getMark(int subjectID)
    {
        return marks.get(subjectID);
    }

    Set<Integer> getSubjects()
    {
        return marks.keySet();
    }
}
