package service;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CloseDialog extends WindowAdapter {
	final Container wnd;
	public CloseDialog(Container wnd) {this.wnd = wnd;}
    public void windowClosing( WindowEvent e ) {  
//        int option = JOptionPane.showOptionDialog(  
//        		wnd,  
//                "Are you sure you want to quit?",  
//                "Exit Dialog", JOptionPane.YES_NO_OPTION,  
//                JOptionPane.WARNING_MESSAGE, null, null,  
//                null );  
        //if( option == JOptionPane.YES_OPTION ) {
        	components.MainWnd.destroy();
            System.exit(0);  
        //}  
    }  
}
