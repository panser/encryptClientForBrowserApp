package ua.org.gostroy;

import javafx.fxml.FXML;

import javax.jnlp.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URL;

/**
 * Created by Sergey on 11/7/2015.
 */
public class Controller {

    PersistenceService ps;
    BasicService bs;
    private ClipboardService cs;

    @FXML
    private void operUrl() throws Exception{

        URL url = new URL("http","www.i.ua",80,"/");
        try {
            // Lookup the javax.jnlp.BasicService object
            bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
            // Invoke the showDocument method
            bs.showDocument(url);
        } catch(UnavailableServiceException ue) {
            ue.printStackTrace();
        }

    }

    @FXML
    private void getCookie() {

        try {
            ps = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
            bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
        } catch (UnavailableServiceException e) {
            ps = null;
            bs = null;
        }

        if (ps != null && bs != null) {

            try {
                // find all the muffins for our URL
                URL codebase = bs.getCodeBase();
                URL url = new URL("http", "localhost", 63342, "/");
                String[] muffins = ps.getNames(url);

                // get the attributes (tags) for each of these muffins.
                // update the server's copy of the data if any muffins
                // are dirty
                int[] tags = new int[muffins.length];
                URL[] muffinURLs = new URL[muffins.length];
                for (int i = 0; i < muffins.length; i++) {
                    muffinURLs[i] = new URL(codebase.toString() + muffins[i]);
                    tags[i] = ps.getTag(muffinURLs[i]);
                    // update the server if anything is tagged DIRTY
                    if (tags[i] == PersistenceService.DIRTY) {
//                        doUpdateServer(muffinURLs[i]);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void getFromClipboard() {

        try {
            cs = (ClipboardService)ServiceManager.lookup("javax.jnlp.ClipboardService");
        } catch (UnavailableServiceException e) {
            cs = null;
        }

        if (cs != null) {
            // set the system clipboard contents to a string selection
            StringSelection ss = new StringSelection("Java Web Start!");
            cs.setContents(ss);
            // get the contents of the system clipboard and print them
            Transferable tr = cs.getContents();
            if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    String s = (String)tr.getTransferData(DataFlavor.stringFlavor);
                    System.out.println("Clipboard contents: " + s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
