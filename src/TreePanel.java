import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.List;

class StudentCellRenderer extends DefaultTreeCellRenderer {
    private static final int IMAGE_SIZE = 20;
    private JLabel label;
    private ImageIcon stIcon, termIcon, groupIcon;

    StudentCellRenderer() {
        stIcon = getScaledImage(new ImageIcon("TreeIcons\\leaf.png"), IMAGE_SIZE, IMAGE_SIZE);
        termIcon = getScaledImage(new ImageIcon("TreeIcons\\root.png"), IMAGE_SIZE, IMAGE_SIZE);
        groupIcon = getScaledImage(new ImageIcon("TreeIcons\\branch.png"), IMAGE_SIZE, IMAGE_SIZE);
        label = new JLabel();
    }

    @Override
    public Color getBackgroundNonSelectionColor() {
        return (null);
    }

    @Override
    public Color getBackgroundSelectionColor() {
        return Color.GREEN;
    }

    @Override
    public Color getBackground() {
        return (null);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof Student) {
            label.setIcon(stIcon);
            label.setText(((Student) o).getName());
        } else if (o instanceof Term) {
            label.setText(String.valueOf(((Term) o).getTermNumber()));
            label.setIcon(termIcon);
        } else if (o instanceof Group) {
            label.setText(String.valueOf(((Group) o).getGroupNumber()));
            label.setIcon(groupIcon);
        } else {
            label.setText(o.toString());
            label.setIcon(null);
        }

        if (selected)
        {
            label.setForeground(getBackgroundSelectionColor());
        }
        else
        {
            label.setForeground(getBackgroundNonSelectionColor());
        }
        return label;
    }

    private ImageIcon getScaledImage(ImageIcon imageIcon, int w, int h) {
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(newimg);
    }
}

class TreePanel extends JPanel {
    private List<Student> students;
    private DefaultMutableTreeNode rootNode;
    private Student lastSelectedStudent;

    private JScrollPane scroll;
    private JTree tree;

    private JPanel editPanel;
    private JButton applyButton;
    private JButton addButton, deleteButton;
    private JTextField term, group, name;


    TreePanel() throws FileNotFoundException {
        applyButton = new JButton("Apply");
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        term = new JTextField("Term");
        group = new JTextField("Group");
        name = new JTextField("Name");
        editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(1, 6));
        editPanel.add(term);
        editPanel.add(group);
        editPanel.add(name);
        editPanel.add(addButton);
        editPanel.add(applyButton);
        editPanel.add(deleteButton);

        students = StudentReader.getStudents();
        rootNode = new DefaultMutableTreeNode(new Faculty("FAMCS"));

        students.forEach(this::addStudentToTree);
        tree = new JTree(rootNode);
        tree.setCellRenderer(new StudentCellRenderer());
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (node == null)
                //Nothing is selected.
                return;
            if (node.isLeaf()) {
                if (node.getUserObject() instanceof Student) {
                    lastSelectedStudent = (Student) node.getUserObject();
                    updateNodeInfo(lastSelectedStudent);
                }
            }
        });

        scroll = new JScrollPane(tree);
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(editPanel, BorderLayout.NORTH);

        addButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student student = new Student(name.getText(), Integer.parseInt(term.getText()), Integer.parseInt(group.getText()));
                addStudentToTree(student);
                tree.updateUI();
            }
        });

        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStudentFromTree(lastSelectedStudent);
                tree.updateUI();
            }
        });

        applyButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student student = new Student(name.getText(), Integer.parseInt(term.getText()), Integer.parseInt(group.getText()));
                removeStudentFromTree(lastSelectedStudent);
                addStudentToTree(student);
                tree.updateUI();
            }
        });
    }

    private void addStudentToTree(Student s) {
        DefaultMutableTreeNode term = null;
        DefaultMutableTreeNode group = null;

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode localTerm = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            Term termEntity = (Term) localTerm.getUserObject();
            if (termEntity.getTermNumber() == s.getTerm()) {
                term = localTerm;
                break;
            }
        }
        if (term == null) {
            term = new DefaultMutableTreeNode(new Term(s.getTerm()));
            rootNode.add(term);
        }

        for (int i = 0; i < term.getChildCount(); i++) {
            DefaultMutableTreeNode localGroup = (DefaultMutableTreeNode) term.getChildAt(i);
            Group groupEntity = (Group) localGroup.getUserObject();
            if (groupEntity.getGroupNumber() == s.getGroup()) {
                group = localGroup;
                break;
            }
        }
        if (group == null) {
            group = new DefaultMutableTreeNode(new Group(s.getGroup()));
            term.add(group);
        }

        group.add(new DefaultMutableTreeNode(s));
    }

    private void removeStudentFromTree(Student s) {
        DefaultMutableTreeNode term = null;
        DefaultMutableTreeNode group = null;

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode localTerm = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            Term termEntity = (Term) localTerm.getUserObject();
            if (termEntity.getTermNumber() == s.getTerm()) {
                term = localTerm;
                break;
            }
        }
        if (term == null)
            return;

        for (int i = 0; i < term.getChildCount(); i++) {
            DefaultMutableTreeNode localGroup = (DefaultMutableTreeNode) term.getChildAt(i);
            Group groupEntity = (Group) localGroup.getUserObject();
            if (groupEntity.getGroupNumber() == s.getGroup()) {
                group = localGroup;
                break;
            }
        }
        if (group == null)
            return;

        for (int i = 0; i < group.getChildCount(); i++) {
            Student c = (Student) ((DefaultMutableTreeNode) group.getChildAt(i)).getUserObject();
            if (c.getTerm() == s.getTerm() && c.getName().equals(s.getName()) && c.getGroup() == s.getGroup()) {
                group.remove(i);
                if (group.getChildCount() == 0) {
                    term.remove(group);
                    if (term.getChildCount() == 0)
                        rootNode.remove(term);
                }
                return;
            }
        }
    }

    private void updateNodeInfo(Student s) {
        term.setText(String.valueOf(s.getTerm()));
        group.setText(String.valueOf(s.getGroup()));
        name.setText(s.getName());
    }
}
