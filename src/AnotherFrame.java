import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Objects;

public class AnotherFrame extends JFrame {
    AnotherFrame() throws InterruptedException {
        int width = 700;
        setTitle("No Name");
        setSize(width, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        String[] dataAL = {"Item 1", "Item 2", "Item 3"};
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        for (String e : dataAL){
            listModel.addElement(e);
        }
//        list.setCellRenderer(new BoxListCellRenderer());
        PanewithList.add(list, BorderLayout.PAGE_START);
        
        list.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        listPane.add(PanewithList, "North");
//        JScrollPane scrollPane = new JScrollPane(list);
//        System.out.println(scrollPane.getViewportBorder());
//        listPane.add(scrollPane, "North");
//        listPane.add(list, "North");
//        JTextField jtf = new JTextField();
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
                            listModel.addElement(((JTextField) c).getText());
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
                            listModel.addElement(jtf.getText());
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
        list.addMouseListener(new PopUpClickListener(list, listPane, listModel));
//        list.addMouseListener(new MouseAdapter() {
//            public void mouseClickedMouseEvent e) {
//                int index = list.locationToIndex(e.getPoint());
//                if (index != -1) {
//                    JCheckBox checkbox = listModel.getElementAt(index);
//                    checkbox.setSelected(!checkbox.isSelected());
//                    list.repaint();
//                }
//            }
//        });
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
//        JLabel l = new JLabel("123");
//        this.add(l);

    }
//    class BoxListCellRenderer extends JCheckBox implements ListCellRenderer<JCheckBox>{
//        @Override
//        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
//            setComponentOrientation(list.getComponentOrientation());
//            setSelected(isSelected);
//            setFont(list.getFont());
//            setText(value);
//            setBackground(list.getBackground());
//            setForeground(list.getForeground());
//            return this;
//        }
//    }
}
class PopUpList extends JPopupMenu {
    JMenuItem anItem;
    public  PopUpList(int index, DefaultListModel<String> list) {
        anItem = new JMenuItem("Remove");
//        System.out.println(get());
        anItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.remove(index);
                System.out.println(e.getClass());
            }
        });
        add(anItem);
    }
}
class PopUpClickListener extends MouseAdapter{
    private JList<String> list;
    private JPanel ParentPanel;
    private DefaultListModel<String> dlm;
    public PopUpClickListener(JList<String> list, JPanel parentComponent, DefaultListModel<String> dlm) {
        this.list = list;
        this.ParentPanel = parentComponent;
        this.dlm=dlm;
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
            PopUpList pul = new PopUpList(index, dlm);
            pul.show(e.getComponent(), e.getX(), e.getY());
        }
    }

}
