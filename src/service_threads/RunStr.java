package service_threads;

public class RunStr extends BaseThread {
	int wait_time = 24;
	String str_object="";
	
    public RunStr() {
		this.setDaemon(true);
    	start();
    }

    synchronized public void run() { routing(); }
    
    public void addElem(String str) {
    	str_object = str;
    }
    
    public void stopRoutind() { wait_time = -100000; }
    
    void routing() {
    	while(!closeRequest()) {                      
            if (wait_time > 0) {
            	
            }
    	}
    }
}



//import java.awt.Cursor;
//import java.awt.Font;
//import java.awt.FontMetrics;
//import javax.swing.JLabel;
// 
//public class move extends Thread{
//private javax.swing.JLabel Lab = null;
//private int x=0;
//private int y=0;
//private int StringSize=0;
// 
//private void LabMouseClicked(java.awt.event.MouseEvent evt) {
// 
//System.out.println("вы кликнули по бегущей строке");
//}
//public move() {
//}
// 
//public move(javax.swing.JFrame a, String x) {
//super();
//Lab = new JLabel(x);
//Lab.setFont(new Font(null,0,14));
//FontMetrics fm = Lab.getFontMetrics(Lab.getFont());
//StringSize = fm.stringWidth(Lab.getText());
//Lab.setSize(StringSize,fm.getHeight());
//Lab.setCursor(new Cursor(Cursor.HAND_CURSOR));
//a.add(Lab);
//Lab.setVisible(true);
//y=Lab.getY();
//Lab.addMouseListener(new java.awt.event.MouseAdapter() {
//public void mouseClicked(java.awt.event.MouseEvent evt) {
//LabMouseClicked(evt);
//}
//});
//start();
//}
//public void run()
//{
//x=Lab.getParent().getWidth();
//for(;;)
//{
//if((x + StringSize)<0) x=Lab.getParent().getWidth();
//Lab.setLocation(x--,y);
//try {
//Thread.sleep(24);
//} catch (InterruptedException ex) {
//ex.printStackTrace();
//}
//}
//}
//}