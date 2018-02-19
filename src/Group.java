public class Group {
    private int groupNumber;
    public Group(int number)
    {
        groupNumber = number;
    }
    public int getGroupNumber()
    {
        return groupNumber;
    }

    @Override
    public String toString()
    {
        return "Group " + getGroupNumber();
    }
}
