import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class StudentGUI extends JFrame
{
    private StudentManager manager;
    private JTextField idField, nameField, marksField;
    private JTextArea displayArea;
    private JComboBox<String> studentDropdown, subjectDropdown;
    private int subID[] = {1, 2, 3, 4, 5};
    private String subs[] = {"Physics",
                             "Chemistry",
                             "Maths",
                             "Computer",
                             "Biology"};

    public StudentGUI()
    {
        manager = new StudentManager();
        setupGUI();
        viewAllStudents();
    }

    private void setupGUI()
    {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,500);
        setLocationRelativeTo(null);

        // create componets
        idField = new JTextField(10);
        nameField = new JTextField(15);
        subjectDropdown = new JComboBox<>();
        marksField = new JTextField(5);
        displayArea = new JTextArea(20, 50);
        displayArea.setEditable(false);
        studentDropdown = new JComboBox<>();
        updateDropdown();

        JButton addStudentBtn = new JButton("Add Student");
        JButton addMarksBtn = new JButton("Add Marks");
        JButton viewAllBtn = new JButton("View All");
        JButton loadStudentBtn = new JButton("Load Student");

        // layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 5, 5));

        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Subject:"));
        
        inputPanel.add(subjectDropdown);
        subjectDropdown.addItem("-- Select Subject --");
        for (int i = 0; i < subID.length; i++)
            subjectDropdown.addItem(subID[i] + " - " + subs[i]);
        
        inputPanel.add(new JLabel("Marks /100: "));
        inputPanel.add(marksField);
        inputPanel.add(new JLabel("Existing Students:"));
        
        inputPanel.add(studentDropdown);
        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel(""));

        inputPanel.add(addStudentBtn);
        inputPanel.add(loadStudentBtn);
        inputPanel.add(addMarksBtn);
        inputPanel.add(viewAllBtn);

        // display area
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Students List"));

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // button actions
        addStudentBtn.addActionListener(e -> addStudent());
        addMarksBtn.addActionListener(e -> addMarks());
        viewAllBtn.addActionListener(e -> viewAllStudents());
        loadStudentBtn.addActionListener(e -> loadSelectedStudent());

        setVisible(true);
        JButton butarr[] = {addStudentBtn, loadStudentBtn, addMarksBtn, viewAllBtn};
        applyStyling(inputPanel, butarr);
    }

    private Color darken(Color color, float factor)
    {
        int r = Math.max(0, (int)(color.getRed() * factor));
        int g = Math.max(0, (int)(color.getGreen() * factor));
        int b = Math.max(0, (int)(color.getBlue() * factor));
        return new Color(r, g, b);
    }

    private void setupButtonHover(JButton button, Color c1, Color c2)
    {
        button.setBackground(c1);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e)
               {
                    button.setBackground(c2);
               }

               @Override
               public void mouseExited(MouseEvent e)
               {
                   button.setBackground(c1);
               }
        });
    }
    
    private void applyStyling(JPanel inpanel, JButton[] buttons)
    {
        Color primary = new Color(70, 130, 180);
        Color darker = darken(primary, 0.8f);
        Color bg = new Color(210, 210, 210);
        Color bg2 = new Color(245, 245, 245);

        getContentPane().setBackground(bg);
        inpanel.setBackground(bg2);
        inpanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Student Information"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        for (JButton btn : buttons)
        {
            setupButtonHover(btn, primary, darker);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        displayArea.setBackground(new Color(250, 250, 250));
    }

    private void updateDropdown()
    {
        studentDropdown.removeAllItems();
        studentDropdown.addItem("-- Select Student --");
        for (Student student : manager.getAllStudents())
            studentDropdown.addItem(student.getUID() + " - " + student.getName());
    }

    private void addStudent()
    {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || id.length() > 10 || name.length() > 10)
        {
            JOptionPane.showMessageDialog(this, "Please enter valid UID and Name");
            return;
        }
        if (!(manager.getStudent(id) == null))
        {
            JOptionPane.showMessageDialog(this, "ID already exists!");
            return;
        }

        Student student = new Student(id, name);
        manager.addStudent(student);
        updateDropdown();
        JOptionPane.showMessageDialog(this, "Student added successfully!!");
        viewAllStudents();
    }

    private void addMarks()
    {
        String id = idField.getText().trim();
        String subjectStr = (String)subjectDropdown.getSelectedItem();
        String marksStr = marksField.getText().trim();

        if (id.isEmpty() || subjectStr == null || subjectStr.equals("-- Select Subject --") || marksStr.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }
        int subject = Integer.parseInt(subjectStr.split(" - ")[0]);

        try
        {
            double marks = Double.parseDouble(marksStr);
            if (marks < 0 || marks > 100)
                throw new NumberFormatException("Illegal Marks");
            Student student = manager.getStudent(id);
            if (student != null)
            {
                student.addMark(subject, marks);
                manager.saveToFile();
                JOptionPane.showMessageDialog(this, "Marks added!!");
            }
            else
                JOptionPane.showMessageDialog(this, "Student not found!!");
        }
        catch (NumberFormatException err)
        {
            JOptionPane.showMessageDialog(this, "Please enter valid marks");
        }
        viewAllStudents();
    }

    private void viewAllStudents()
    {
        displayArea.setText("UID:\tName:\t\t");
        for (String s : subs)
            displayArea.append(s + "\t");
        displayArea.append("\n");
        for (Student student : manager.getAllStudents())
        {
            displayArea.append(student.getUID() + "\t" +
                               student.getName() + "\t\t");
            for (int subject : subID)
            {
                boolean in = false;
                for (int i : student.getSubjects())
                {
                    if (i == subject)
                    {
                        displayArea.append(student.getMark(i) + "");
                        in = true;
                        break;
                    }
                }
                if (!in)
                    displayArea.append(" - ");
                displayArea.append("\t");
            }
            displayArea.append("\n");
        }
    }

    private void loadSelectedStudent()
    {
        String selected = (String)studentDropdown.getSelectedItem();
        if (selected != null && !selected.equals("-- Select Student --"))
        {
            String id = selected.split(" - ")[0];
            Student student = manager.getStudent(id);
            if (student != null)
            {
                idField.setText(student.getUID());
                nameField.setText(student.getName());
                displaySelectedStudent(student);
            }
        }
    }

    private void displaySelectedStudent(Student student)
    {
        displayArea.setText("UID: " + student.getUID() + "\tName: " + student.getName() + "\n");
        displayArea.append("Marks:\n");
        for (int subject : student.getSubjects())
        {
            displayArea.append("    " + subs[subject - 1] + "\t:    " + student.getMark(subject) + "\n");
        }
    }

    private void clearFields()
    {
        idField.setText("");
        nameField.setText("");
        marksField.setText("");
    }

    public static void main(String argz[])
    {
        SwingUtilities.invokeLater(() -> new StudentGUI());
    }
}
