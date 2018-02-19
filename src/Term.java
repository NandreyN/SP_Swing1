public class Term {
    private int termNumber;
    public Term(int number)
    {
        termNumber = number;
    }

    public int getTermNumber()
    {
        return termNumber;
    }

    @Override
    public String toString()
    {
        return "Term " + getTermNumber();
    }
}
