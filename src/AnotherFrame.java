import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AnotherFrame extends JFrame {
    AnotherFrame() {
        int width = 700;
        setTitle("No Name");
        setSize(width, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Boolean start = true;
        JPanel listPane = new JPanel();
        JPanel inListPane = new JPanel();
        JPanel PanewithList=new JPanel();
        listPane.setLayout(new BorderLayout());
        inListPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        PanewithList.setLayout(new BorderLayout());

        JPanel jp2 = new JPanel();
        jp2.setLayout(new BorderLayout());
        JPanel jp1 = new JPanel();
        jp1.setLayout(new BorderLayout());

        String[] dataAL = {"— I can't help feeling something is off over here...", "— Cross it out, it's only an assumption"};
        DefaultListModel<CheckboxListItem> listModel = new DefaultListModel<>();
        DefaultListModel<CheckboxListItem> standartName = new DefaultListModel<>();
        JList<CheckboxListItem> list = new JList<>(listModel);
        for (String e : dataAL){
            listModel.addElement(new CheckboxListItem(e));
        }
        for (int i=0; i<listModel.getSize(); i++) {
            CheckboxListItem item = listModel.getElementAt(i);
            standartName.addElement(item);
        }

        listModel.set(0, new CheckboxListItem("<strike>— I can't help feeling something is off over here...</strike>"));
        listModel.firstElement().setSelected(true);
        list.setCellRenderer(new CheckboxListCellRenderer());
        PanewithList.add(list, BorderLayout.PAGE_START);
        list.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        listPane.add(PanewithList, "North");
        Button b = new Button("New Task");
        b.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        b.setPreferredSize(new Dimension(200, 30));
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int count=0;
                for (Component c : listPane.getComponents()){
                    if (c instanceof JTextField){
                        if (!((JTextField) c).getText().isEmpty() & !((JTextField) c).getText().equals("Write your task here...")) {
                            listModel.addElement(new CheckboxListItem(((JTextField) c).getText()));
                            standartName.addElement(new CheckboxListItem(((JTextField) c).getText()));
                            listPane.remove(count);
                            listPane.revalidate();
                            listPane.repaint();
                        }
                        return;
                    }
                    count+=1;
//                    count+=1;
                }
                JTextField jtf = new JTextField("Write your task here...");
                jtf.setSize(100, 100);
                listPane.revalidate();
                listPane.repaint();
                jtf.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (jtf.getText().equals("Write your task here...")) {
                            jtf.setText("");
                            jtf.setForeground(Color.BLACK);
                        }else{
                            jtf.setForeground(Color.BLACK);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (jtf.getText().isEmpty()){
                            jtf.setText("Write your task here...");
                            jtf.setForeground(Color.GRAY);
                        } else{
                            jtf.setForeground(Color.GRAY);
                        }
                    }
                });
                jtf.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!jtf.getText().isEmpty()) {
                            listModel.addElement(new CheckboxListItem(jtf.getText()));
                            standartName.addElement(new CheckboxListItem(jtf.getText()));
                            listPane.remove(jtf);
                            listPane.revalidate();
                            listPane.repaint();
                        }
                    }
                });
                listPane.add(jtf);
                listPane.revalidate();
                listPane.repaint();
            }
        });
        list.addMouseListener(new PopUpClickListener(list, listPane, listModel, standartName));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int index = list.locationToIndex(e.getPoint());
                    CheckboxListItem cli = listModel.get(index);
                    cli.setSelected(!cli.isSelected());
                    list.revalidate();
                    list.repaint();

                    if (cli.isSelected()) {
                        listModel.set(index, new CheckboxListItem("<strike>" + cli.getName() + "</strike>", true));
                        cli.setSelected(false);
                    } else {
                        listModel.set(index, standartName.get(index));
                    }
                }
            }
        });
        inListPane.add(b);
        inListPane.setBackground(Color.WHITE);
        listPane.add(inListPane, "South");

        jp2.add(listPane, "North");
        jp2.setBackground(Color.WHITE);
        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jp1, jp2);
        jsp.setDividerLocation(width/5);
        setContentPane(jsp);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private class CheckboxListItem {
        private String name;
        private boolean selected;

        public CheckboxListItem(String name) {
            this.name = name;
            this.selected = false;
        }
        public CheckboxListItem(String name, Boolean selected) {
            this.name = name;
            this.selected = selected;
        }

        public String getName() {
            return name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            JCheckBox jcb=new JCheckBox();
            jcb.setSelected(true);
        }
    }
    private class CheckboxListCellRenderer extends JCheckBox implements ListCellRenderer<CheckboxListItem> {
        public Component getListCellRendererComponent(JList<? extends CheckboxListItem> list, CheckboxListItem value, int index, boolean isSelected, boolean cellHasFocus) {
            setComponentOrientation(list.getComponentOrientation());
            setSelected(value.isSelected());
            setFont(list.getFont());
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setText("<html>"+value.getName()+"</html>");
            return this;
        }
}
class PopUpList extends JPopupMenu {
    JMenuItem Remove, Rename;

    public  PopUpList(int index, DefaultListModel<CheckboxListItem> list, DefaultListModel<CheckboxListItem> stn, JList<CheckboxListItem> jl) {
        Remove = new JMenuItem("Remove");
        Remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.remove(index);
                stn.remove(index);
            }
        });
        add(Remove);

        Rename  = new JMenuItem("Rename");
        Rename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = JOptionPane.showInputDialog("Enter new name:");
                if (newName != null && !newName.isEmpty()) {
                    list.set(index, new CheckboxListItem(newName));
                    stn.set(index, new CheckboxListItem(newName));
                }
            }
        });
        add(Rename);
    }
}

class PopUpClickListener extends MouseAdapter{
    private final DefaultListModel<CheckboxListItem> stdN;
    private JList<CheckboxListItem> list;
    private DefaultListModel<CheckboxListItem> dlm;
    public PopUpClickListener(JList<CheckboxListItem> list, JPanel parentComponent, DefaultListModel<CheckboxListItem> dlm, DefaultListModel<CheckboxListItem> stdN) {
        this.list = list;
        this.dlm=dlm;
        this.stdN=stdN;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            PopUp(e);
    }
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            PopUp(e);
    }
    private void PopUp(MouseEvent e){
        if (e.getButton()==MouseEvent.BUTTON3){
            int index = list.locationToIndex(e.getPoint());
            PopUpList pul = new PopUpList(index, dlm, stdN, list);
            pul.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    }
}
