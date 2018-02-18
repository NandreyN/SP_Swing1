import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class TreePanel extends JPanel {
    private List<Student> students;
    private DefaultMutableTreeNode[] termNodes, groupNodes;
    private DefaultMutableTreeNode rootNode;
    private Student lastSelectedStudent;

    private JScrollPane scroll;
    private JTree tree;

    private JPanel editPanel;
    private JButton applyButton;
    private JButton addButton, deleteButton;
    private JTextField term, group, name;


    public TreePanel() throws FileNotFoundException {
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

        termNodes = new DefaultMutableTreeNode[5];
        groupNodes = new DefaultMutableTreeNode[12];

        students = StudentReader.getStudents();
        rootNode = new DefaultMutableTreeNode("Students");
        for (Student s : students) {
            DefaultMutableTreeNode termNode = null;
            DefaultMutableTreeNode groupNode = null;
            if (s.getTerm() < termNodes.length && termNodes[s.getTerm()] != null) {
                termNode = termNodes[s.getTerm()];
            } else {
                termNode = new DefaultMutableTreeNode(s.getTerm());
                termNodes[s.getTerm()] = termNode;
                rootNode.add(termNode);
            }

            if (s.getGroup() < groupNodes.length && groupNodes[s.getGroup()] != null) {
                groupNode = groupNodes[s.getGroup()];
            } else {
                groupNode = new DefaultMutableTreeNode(s.getGroup());
                termNode.add(groupNode);
                groupNodes[s.getGroup()] = groupNode;
            }
            groupNode.add(s);
        }
        tree = new JTree(rootNode);
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (node == null)
                //Nothing is selected.
                return;
            if (node.isLeaf()) {
                if (node instanceof Student) {
                    lastSelectedStudent = (Student) node;
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
                add(student);
                tree.updateUI();
            }
        });

        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove();
                tree.updateUI();
            }
        });

        applyButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student student = new Student(name.getText(), Integer.parseInt(term.getText()), Integer.parseInt(group.getText()));
                if (student.getTerm() != lastSelectedStudent.getTerm() || student.getGroup() != lastSelectedStudent.getGroup()) {
                    remove();
                    add(student);
                }
                else
                {
                    lastSelectedStudent.setGroup(Integer.parseInt(group.getText()));
                    lastSelectedStudent.setTerm(Integer.parseInt(term.getText()));
                    lastSelectedStudent.setName(name.getText());
                }
                tree.updateUI();
            }
        });
    }

    private void remove()
    {
        DefaultMutableTreeNode oldGroup = groupNodes[lastSelectedStudent.getGroup()];
        oldGroup.remove(lastSelectedStudent);
        if (!oldGroup.children().hasMoreElements())
        {
            DefaultMutableTreeNode oldParent = termNodes[lastSelectedStudent.getTerm()];
            oldParent.remove(oldGroup);
            oldGroup = null;
            groupNodes[lastSelectedStudent.getGroup()] = null;
            if (!oldParent.children().hasMoreElements())
            {
                rootNode.remove(oldParent);
                termNodes[lastSelectedStudent.getTerm()] = null;
                oldParent = null;
            }
        }
    }

    private void add(Student student)
    {
        int newGroup = student.getGroup();
        int newTerm = student.getTerm();
        DefaultMutableTreeNode termNode = null;
        DefaultMutableTreeNode groupNode = null;

        if (termNodes[newTerm] != null) {
            termNode = termNodes[newTerm];
        } else {
            termNode = new DefaultMutableTreeNode(newTerm);
            rootNode.add(termNode);
        }

        if (groupNodes[newGroup] != null)
        {
            groupNode = groupNodes[newGroup];
        }
        else
        {
            groupNode = new DefaultMutableTreeNode(newGroup);
            termNode.add(groupNode);
        }
        groupNode.add(student);
    }

    private void updateNodeInfo(Student s) {
        term.setText(String.valueOf(s.getTerm()));
        group.setText(String.valueOf(s.getGroup()));
        name.setText(s.getName());
    }
}