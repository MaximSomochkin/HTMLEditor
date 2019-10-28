import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {

    private View view;
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view) {
        this.view = view;
    }

    public static void main(String[] args) {



       View view = new View();
       Controller controller = new Controller(view);
       view.setController(controller);
       view.init();
       controller.init();

    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void init(){createNewDocument();}

    public void exit(){
        System.exit(0);
    }

    public void resetDocument(){
        if(document!=null) 
            document.removeUndoableEditListener(view.getUndoListener());

            document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();

            document.addUndoableEditListener(view.getUndoListener());
            view.update();

    }

    public void setPlainText(String text){
        resetDocument();

        try {
            new HTMLEditorKit().read( new StringReader(text),document,0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }

    }

   public  String getPlainText() {

       StringWriter sw  = new StringWriter();
       try {
            new HTMLEditorKit().write(sw, document, 0, document.getLength());
       } catch (Exception e) {
           ExceptionHandler.log(e);
       }
       return sw.toString();
   }

    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        view.resetUndo();
        currentFile=null;
    }

    public void openDocument() {
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new HTMLFileFilter());
        int returnVal= fileChooser.showOpenDialog(view);
        if (returnVal==JFileChooser.APPROVE_OPTION){
            currentFile=fileChooser.getSelectedFile();
            resetDocument();
            view.setTitle(currentFile.getName());
            try {
                FileReader fileReader = new FileReader(currentFile);
                new HTMLEditorKit().read(fileReader,document,0);
                fileReader.close();
                view.resetUndo();
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }
    }

    public void saveDocument() {
        view.selectHtmlTab();
        if(currentFile==null)
        saveDocumentAs();
        else {
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
                fileWriter.close();
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }


    }

    public void saveDocumentAs() {
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new HTMLFileFilter());
       int returnVal= fileChooser.showSaveDialog(view);
        if (returnVal==JFileChooser.APPROVE_OPTION){
            currentFile=fileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            try {
                FileWriter fileWriter=new FileWriter(currentFile);
                new HTMLEditorKit().write(fileWriter,document,0, document.getLength());
                fileWriter.close();
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }

        }
    }

    public void showAbout() {
    }
}
