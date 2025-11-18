import java.util.*;
class Student
{
    private String uid;
    private String name;
    private double total;
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
        if (marks.containsKey(subjectID))
            // remove previous mark to update
            total -= getMark(subjectID);
        total += mark;
        marks.put(subjectID, mark);
    }

    double getTotalMarks()
    {
        return total;
    }

    int getSubCount()
    {
        return marks.size();
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
