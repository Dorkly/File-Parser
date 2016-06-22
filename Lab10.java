import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

public class Lab10 extends JPanel implements ActionListener
{
  private int width, height;
  private JButton open,open2;
  private JRadioButton alpha;
  private TextArea textarea,textarea2;
  private JLabel label ,label2;
  private JButton match;
  private SuperString[] array,array0,array1;
  private JTextField input;
  public Lab10()
  {
    width = 800;
    height = 900;
    match = new JButton("Compute match");
    match.addActionListener(this);
    alpha = new JRadioButton("Alphabetical",false);
    open = new JButton("Open File");
    open2 = new JButton("Open Second File");
    open.addActionListener(this);
    open2.addActionListener(this);
    label = new JLabel("no file selected");
    label2 = new JLabel("no file selected");
    input = new JTextField("2");
    textarea = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
    textarea2 = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
    GridLayout lay = new GridLayout(4,1,10,10);
    setLayout(new BorderLayout());
    
    JPanel upperPanel = new JPanel();
    input.addActionListener(this);
    textarea.setBackground(new Color(200,85,25));
    textarea2.setBackground(new Color(70,60,170));
    
    upperPanel.add(open);
    upperPanel.add(label);
    upperPanel.add(input);
    upperPanel.add(alpha);
    upperPanel.add(open2);
    upperPanel.add(label2);
    upperPanel.add(match);
    
    add(textarea2,BorderLayout.EAST);
    add(upperPanel,BorderLayout.NORTH);
    add(textarea,BorderLayout.WEST);
    upperPanel.setLayout(lay);
    setPreferredSize(new Dimension(width,height));
  }
  //action listener for loading files
  public void actionPerformed(ActionEvent event)
  {
     if(event.getSource() == match)
    {
      System.err.println(MatchTools.match(array0,array1));
      return;
    }
    array = null;
    String name = loadfile();
    System.err.println(name+" "+array.length);
    if( name == null)
      return;
    if(!alpha.isSelected())
      Arrays.sort(array, new SuperStringComparator());
    if(event.getSource() == open)
    {
      System.err.println("inside 0");
      array0 = array;
      label.setText(name);
      textarea.setText("");
      StringBuilder sb = new StringBuilder();
      for(SuperString token : array0)
        sb.append(token+"\n");
      textarea.setText(sb.toString());
    }
      
    if(event.getSource() == open2)
    {
      array1 = array;
      label2.setText(name);
      textarea2.setText("");
       StringBuilder sb = new StringBuilder();
      for(SuperString token : array1)
        sb.append(token+"\n");
      textarea2.setText(sb.toString());
    }
  }
  
  private String loadfile()
  {
    String filename;
    JFileChooser chooser = new JFileChooser("../Text");
    int returnValue = chooser.showOpenDialog(null);
    if(returnValue != JFileChooser.APPROVE_OPTION)
    return null;
    BinaryCountTree<SuperString> tree = new BinaryCountTree<SuperString>();
    try
    {
      File file = chooser.getSelectedFile();
      filename = file.getName();
      Scanner scan = new Scanner(file);
      int n = Integer.parseInt(input.getText());
      
      String[] count = new String[n];
      for(int i =1 ;i<count.length;i++)
      {
      count[i] = scan.next().toLowerCase().replace(".","").
      replace("'","").replace(",","").replace("-","");
      }
      
      while(scan.hasNext())
      {
        if (!(count.length == 1)){
        for(int i = 1;i<count.length;i++)
        {
        count[i-1] = count[i];
        }
        count[count.length-1] = scan.next().toLowerCase().replace(".","").
        replace("'","").replace(",","").replace("-","");
        }
      else{
        count[0] = scan.next().toLowerCase().replace(".","").
        replace("'","").replace(",","");
          }
      tree.add(new SuperString(count));
      }
      scan.close();
    }
    catch(FileNotFoundException err)
    {
      System.err.println("File read error");
      return null;
    }
    array = new SuperString[tree.size()];
    int i=0;
    for(SuperString token: tree)
      {array[i] = token;
        i++;
      }
      return filename;
    }
    
  public static void main(String[] arg)
  {
    JFrame frame = new JFrame("Super List");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Lab10());
    frame.pack();
    frame.setVisible(true);
  }
}