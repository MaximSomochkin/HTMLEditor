import com.javarush.task.task32.task3209.listeners.FrameListener;
import com.javarush.task.task32.task3209.listeners.TabbedPaneChangeListener;
import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {

  private  Controller controller;

  private JTabbedPane tabbedPane = new JTabbedPane();
  private JTextPane htmlTextPane = new JTextPane();
   private JEditorPane plainTextPane = new JEditorPane();
   private UndoManager undoManager= new UndoManager();
   private UndoListener undoListener = new UndoListener(undoManager);

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public View() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            ExceptionHandler.log(e);
            // handle exception
        }
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Новый": controller.createNewDocument();
            break;
            case "Открыть": controller.openDocument();
            break;
            case "Сохранить": controller.saveDocument();
            break;
            case "Сохранить как...": controller.saveDocumentAs();
            break;
            case "Выход": exit();
            break;
            case "О программе": showAbout();
        }


    }

    public void init(){
        initGui();
        FrameListener frameListener = new FrameListener(this);
       super.addWindowListener(frameListener);
       super.setVisible(true);
    }

    public void exit(){
        controller.exit();
    }

    public void initMenuBar(){

        JMenuBar menuBar = new JMenuBar();

        MenuHelper.initFileMenu(this, menuBar);
        MenuHelper.initEditMenu(this, menuBar);
        MenuHelper.initStyleMenu(this, menuBar);
        MenuHelper.initAlignMenu(this, menuBar);
        MenuHelper.initColorMenu(this, menuBar);
        MenuHelper.initFontMenu(this, menuBar);
        MenuHelper.initHelpMenu(this, menuBar);

        getContentPane().add(menuBar,BorderLayout.NORTH) ;
    }

    public void initEditor(){
        htmlTextPane.setContentType("text/html");
        JScrollPane jScrollPaneHtml = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML", jScrollPaneHtml);
        JScrollPane jScrollPane1Text = new JScrollPane(plainTextPane);
        tabbedPane.addTab("Текст", jScrollPane1Text);
        //tabbedPane.setLayout(new FlowLayout());
        tabbedPane.setPreferredSize(null);
        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void initGui(){
        initMenuBar();
        initEditor();
        this.pack();
    }

    public void selectedTabChanged() {

        if (tabbedPane.getSelectedIndex()==0)
            controller.setPlainText(plainTextPane.getText());
        else if (tabbedPane.getSelectedIndex()==1)
           plainTextPane.setText( controller.getPlainText());

        resetUndo();

    }

    public boolean canUndo(){
        return undoManager.canUndo();
    }

    public boolean canRedo() {return undoManager.canRedo();  }

    public void undo(){
        try{
            undoManager.undo();
        }catch (Exception e){ExceptionHandler.log(e);}
    }

    public void redo(){
        try{
            undoManager.redo();

        }catch (Exception e){ExceptionHandler.log(e);}

    }

    public void resetUndo(){
        undoManager.discardAllEdits();
    }

    public boolean isHtmlTabSelected(){
        return tabbedPane.getSelectedIndex()==0;
    }

   public void selectHtmlTab(){
        tabbedPane.setSelectedIndex(0);
        this.resetUndo();
    }

    public void update(){
       htmlTextPane.setDocument(controller.getDocument());
    }

    public void showAbout(){

        JOptionPane.showMessageDialog(getContentPane(),"Information","Info",JOptionPane.INFORMATION_MESSAGE);
    }




}
