import javax.swing.tree.DefaultMutableTreeNode;

public class Student{
    private String name;
    private int term;
    private int group;

    public Student(String name , int term ,int group)
    {
        this.setName(name);
        this.setGroup(group);
        this.setTerm(term);
    }

    public Student (Student s)
    {
        this.setName(s.getName());
        this.setGroup(s.getGroup());
        this.setTerm(s.getTerm());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    @Override
    public  String toString()
    {
        return name;
    }
}
