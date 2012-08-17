package service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.Enumeration;

import javax.swing.JOptionPane;

public class JarModifier {

    File jarFile; // the original jar file
    File tempFile;// the temp file for the jar entries
    Map<String, File> filesForReplacment;// contains the strings from a file
    JarOutputStream jos;// output to temp jar file
    ZipEntry ze;
 
    public JarModifier(String fName, Map<String, File> v) {
        jarFile = new File(fName);
        filesForReplacment = v;
    }
 
    public void run() {
        try{
            JarFile jf = new JarFile(jarFile);
            tempFile = new File("temp.tmp");
            jos = new JarOutputStream(new FileOutputStream(tempFile));
            for(Enumeration<JarEntry> en = jf.entries(); en.hasMoreElements(); ) {
                ze = new ZipEntry((ZipEntry)en.nextElement());
                if(filesForReplacment.containsKey(ze.getName())) {
                	ze = new ZipEntry(ze.getName());
                    readWrite(new FileInputStream(filesForReplacment.get(ze.getName())));
                }
                else readWrite(jf.getInputStream(ze));
            }
            jos.close();
            jf.close();
 
            //replace the current jar file with the new file at JVM exit
            Runtime r = Runtime.getRuntime();
            r.addShutdownHook(new Thread(){
                public void run() {
                	JOptionPane.showMessageDialog(null, "JVM close");
                    boolean b = jarFile.delete();
                    JOptionPane.showMessageDialog(null, "deleting");
                    boolean bb = tempFile.renameTo(jarFile);
                    
                    JOptionPane.showMessageDialog(null, "Delete : " + (b ? "true" : "false") + " Rename : " + (bb ? "true" : "false"));
                }
            });
        }
        catch(Exception e){ JOptionPane.showMessageDialog(null, e.getMessage()); }
    }
 
    // the method to read the jar file and write the bytes to the temp file
    void readWrite(InputStream in){
        try{
            jos.putNextEntry(ze);
            int i;
            byte [] b = new byte[2048];
            while((i=in.read(b)) > 0) {
                jos.write(b,0,i);
                jos.flush();
            }
            in.close();
        }
        catch(IOException ie) { ie.printStackTrace(); }
    }
}


//File jarFile; // the original jar file
//File tempFile;// the temp file for the jar entries
//File temp;//the temp file for the names vector
//Vector outputNames;// contains the strings from a file
//JarOutputStream jos;// output to temp jar file
//ZipEntry ze;
//
//public JarReader(String fName, Vector v) {
//    jarFile = new File(fName);
//    outputNames = v;
//}
//
//public void run(){
//    try{
//        // write the names to a temp file
//        temp = File.createTempFile("names", null);
//        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
//        Enumeration enum = outputNames.elements();
//        while(enum.hasMoreElements()){
//            out.write(enum.nextElement().toString());
//            out.newLine();
//        }
//        out.close();
//    }
//    catch(Exception fe){
//        fe.printStackTrace();
//    }
//
//    // write the jar files
//    try{
//        JarFile jf = new JarFile(jarFile);
//        tempFile = new File("temp.tmp");
//        jos = new JarOutputStream(new FileOutputStream(tempFile));
//        for(Enumeration enum = jf.entries(); enum.hasMoreElements(); ){
//            ze = new ZipEntry((ZipEntry)enum.nextElement());
//            if(!(ze.getName().equals("names.dat"))){// exclude names.dat
//                readWrite(jf.getInputStream(ze));
//            }
//        }
//        ZipEntry ze = new ZipEntry("names.dat");
//        readWrite(new FileInputStream(temp));
//        jos.close();
//        jf.close();// needed to allow correct renaming on exit
//
//        // replace the current jar file with the new file at JVM exit
//        Runtime r = Runtime.getRuntime();r.addShutdownHook(new Thread(){
//            public void run() {
//                jarFile.delete();
//                tempFile.renameTo(jarFile);
//            }
//        });
//
//
//    }
//    catch(Exception e){
//        e.printStackTrace();
//    }
//}
//
//// the method to read the jar file and write the bytes to the temp file
//void readWrite(InputStream in){
//    try{
//        jos.putNextEntry(ze);
//        int i;
//        while((i = in.read()) != -1){
//            jos.write(i);
//            jos.flush();
//        }
//        in.close();
//    }
//    catch(IOException ie){
//        ie.printStackTrace();
//    }
//}
//}