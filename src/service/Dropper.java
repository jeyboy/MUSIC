package service;

import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * This class makes it easy to drag and drop files from the operating
 * system to a Java program. Any <tt>java.awt.Component</tt> can be
 * dropped onto, but only <tt>javax.swing.JComponent</tt>s will indicate
 * the drop event with a changed border.
 * <p/>
 * To use this class, construct a new <tt>FileDrop</tt> by passing
 * it the target component and a <tt>Listener</tt> to receive notification
 * when file(s) have been dropped. Here is an example:
 * <p/>
 * <code><pre>
 *      JPanel myPanel = new JPanel();
 *      new FileDrop( myPanel, new FileDrop.Listener()
 *      {   public void filesDropped( java.io.File[] files )
 *          {   
 *              // handle file drop
 *              ...
 *          }   // end filesDropped
 *      }); // end FileDrop.Listener
 * </pre></code>
 * <p/>
 * You can specify the border that will appear when files are being dragged by
 * calling the constructor with a <tt>javax.swing.border.Border</tt>. Only
 * <tt>JComponent</tt>s will show any indication with a border.
 * <p/>
 * You can turn on some debugging features by passing a <tt>PrintStream</tt>
 * object (such as <tt>System.out</tt>) into the full constructor. A <tt>null</tt>
 * value will result in no extra debugging information being output.
 * <p/>
 */
public class Dropper
{
    private transient java.awt.dnd.DropTargetListener dropListener;
    
    
    /** Discover if the running JVM is modern enough to have drag and drop. */
    private static Boolean supportsDnD;
    
    /**
     * Constructs a {@link Dropper} with a default light-blue border
     * and, if <var>c</var> is a {@link java.awt.Container}, recursively
     * sets all elements contained within as drop targets, though only
     * the top level container will change borders.
     *
     * @param c Component on which files will be dropped.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public Dropper(
    final java.awt.Component c,
    final Listener listener )
    {   this( c,     // Drop target
              true, // Recursive
              listener );
    }   // end constructor
     
    /**
     * Constructor with a default border and the option to recursively set drop targets.
     * If your component is a <tt>java.awt.Container</tt>, then each of its children
     * components will also listen for drops, though only the parent will change borders.
     *
     * @param c Component on which files will be dropped.
     * @param recursive Recursively set children as drop targets.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public Dropper(
    final java.awt.Component c,
    final boolean recursive,
    final Listener listener )
    {   
        if( supportsDnD() )
        {   // Make a drop listener
            dropListener = new java.awt.dnd.DropTargetListener()
            {   public void dragEnter( java.awt.dnd.DropTargetDragEvent evt )
                {
                    // Is this an acceptable drag event?
                    if( isDragOk(evt) )
                    {
                        // Acknowledge that it's okay to enter
                        //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                        evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY );
                    }   // end if: drag ok
                    else 
                    {   // Reject the drag event
                        evt.rejectDrag();
                    }   // end else: drag not ok
                }   // end dragEnter

                public void dragOver( java.awt.dnd.DropTargetDragEvent evt ) 
                {   // This is called continually as long as the mouse is
                    // over the drag target.
                }   // end dragOver

                public void drop( java.awt.dnd.DropTargetDropEvent evt ) {
                	try
                    {   // Get whatever was dropped
                        java.awt.datatransfer.Transferable tr = evt.getTransferable();
                        // Is it a file list?
                        if (tr.isDataFlavorSupported (java.awt.datatransfer.DataFlavor.javaFileListFlavor))
                        {
                            // Say we'll take it.
                            //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                            evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY );

                            // Get a useful list
                            java.util.List<?> fileList = (java.util.List<?>) tr.getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor);
                            fileList.iterator();

                            // Convert list to array
                            java.io.File[] filesTemp = new java.io.File[ fileList.size() ];
                            fileList.toArray( filesTemp );
                            final java.io.File[] files = filesTemp;

                            // Alert listener to drop.
                            if( listener != null )
                          		listener.filesDropped( new Event( files, evt.isLocalTransfer()) );

                            // Mark that drop is completed.
                            evt.getDropTargetContext().dropComplete(true);
                        }   // end if: file list
                        else // this section will check for a reader flavor.
                        {
                            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                            DataFlavor[] flavors = tr.getTransferDataFlavors();
                            boolean handled = false;
                            for (int zz = 0; zz < flavors.length; zz++) {
                                if (flavors[zz].isRepresentationClassReader()) {
                                    // Say we'll take it.
                                    //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                                    evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);

                                    Reader reader = flavors[zz].getReaderForText(tr);
                                    BufferedReader br = new BufferedReader(reader);
                                    
                                    if(listener != null) {
                                        if( listener != null )
                                      		listener.filesDropped( new Event( createFileArray(br), evt.isLocalTransfer()) );
                                    }
                                    
                                    // Mark that drop is completed.
                                    evt.getDropTargetContext().dropComplete(true);
                                    handled = true;
                                    break;
                                }
                            }
                            if(!handled){
                                evt.rejectDrop();
                            }
                            // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                        }   // end else: not a file list
                    }   // end try
                    catch ( java.io.IOException io) {
                    	Errorist.printLog(io);
                        evt.rejectDrop();
                    }   // end catch IOException
                    catch (java.awt.datatransfer.UnsupportedFlavorException ufe) {
                    	Errorist.printLog(ufe);
                        evt.rejectDrop();
                    }   // end catch: UnsupportedFlavorException
                }   // end drop

                public void dragExit( java.awt.dnd.DropTargetEvent evt ) {
                    // If it's a Swing component, reset its border
                }   // end dragExit

                public void dropActionChanged( java.awt.dnd.DropTargetDragEvent evt ) {
                    // Is this an acceptable drag event?
                    if( isDragOk( evt ) )
                    {   //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                        evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY );
                    }   // end if: drag ok
                    else 
                    {   evt.rejectDrag();
                    }   // end else: drag not ok
                }   // end dropActionChanged
            }; // end DropTargetListener

            // Make the component (and possibly children) drop targets
            makeDropTarget( c, recursive );
        }   // end if: supports dnd
        else
        {
        }   // end else: does not support DnD
    }   // end constructor

    
    private static boolean supportsDnD()
    {   // Static Boolean
        if( supportsDnD == null )
        {   
            boolean support = false;
            try
            {   Class.forName( "java.awt.dnd.DnDConstants" );
                support = true;
            }   // end try
            catch( Exception e )
            {   support = false;
            	Errorist.printLog(e);
            }   // end catch
            supportsDnD = new Boolean( support );
        }   // end if: first time through
        return supportsDnD.booleanValue();
    }   // end supportsDnD
    
    
     // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
     private static String ZERO_CHAR_STRING = "" + (char)0;
     private static File[] createFileArray(BufferedReader bReader)
     {
        try { 
            java.util.List<File> list = new java.util.ArrayList<File>();
            java.lang.String line = null;
            while ((line = bReader.readLine()) != null) {
                try {
                    // kde seems to append a 0 char to the end of the reader
                    if(ZERO_CHAR_STRING.equals(line)) continue; 
                    
                    java.io.File file = new java.io.File(new java.net.URI(line));
                    list.add(file);
                } catch (Exception ex) { Errorist.printLog(ex); }
            }

            return (java.io.File[]) list.toArray(new File[list.size()]);
        } catch (IOException ex) { Errorist.printLog(ex); }
        return new File[0];
     }
     // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
     
    
    private void makeDropTarget(final java.awt.Component c, boolean recursive )
    {
        // Make drop target
        final java.awt.dnd.DropTarget dt = new java.awt.dnd.DropTarget();
        try
        {   dt.addDropTargetListener( dropListener );
        }   // end try
        catch( java.util.TooManyListenersException e )
        { Errorist.printLog(e); }   // end catch
        
        // Listen for hierarchy changes and remove the drop target when the parent gets cleared out.
        c.addHierarchyListener( new java.awt.event.HierarchyListener()
        {   public void hierarchyChanged( java.awt.event.HierarchyEvent evt ) {
                java.awt.Component parent = c.getParent();
                if( parent == null )
                	c.setDropTarget( null );
                else new java.awt.dnd.DropTarget(c, dropListener);
            }   // end hierarchyChanged
        }); // end hierarchy listener
        if( c.getParent() != null )
            new java.awt.dnd.DropTarget(c, dropListener);
        
        if( recursive && (c instanceof java.awt.Container ) )
        {   
            // Get the container
            java.awt.Container cont = (java.awt.Container) c;
            
            // Get it's components
            java.awt.Component[] comps = cont.getComponents();
            
            // Set it's components as listeners also
            for( int i = 0; i < comps.length; i++ )
                makeDropTarget(comps[i], recursive );
        }   // end if: recursively set components as listener
    }   // end dropListener
    
    
    
    /** Determine if the dragged data is a file list. */
    private boolean isDragOk(final java.awt.dnd.DropTargetDragEvent evt ) {   
    	boolean ok = false;
        
        // Get data flavors being dragged
        java.awt.datatransfer.DataFlavor[] flavors = evt.getCurrentDataFlavors();
        
        // See if any of the flavors are a file list
        int i = 0;
        while( !ok && i < flavors.length )
        {   
            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            // Is the flavor a file list?
            final DataFlavor curFlavor = flavors[i];
            if( curFlavor.equals( java.awt.datatransfer.DataFlavor.javaFileListFlavor ) ||
                curFlavor.isRepresentationClassReader()){
                ok = true;
            }
            // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            i++;
        }   // end while: through flavors
        
        return ok;
    }   // end isDragOk
    
    /**
     * Removes the drag-and-drop hooks from the component and optionally
     * from the all children. You should call this if you add and remove
     * components after you've set up the drag-and-drop.
     * This will recursively unregister all components contained within
     * <var>c</var> if <var>c</var> is a {@link java.awt.Container}.
     *
     * @param c The component to unregister as a drop target
     * @since 1.0
     */
    public static boolean remove( java.awt.Component c)
    {   return remove( null, c, true );
    }   // end remove
    
    
    
    /**
     * Removes the drag-and-drop hooks from the component and optionally
     * from the all children. You should call this if you add and remove
     * components after you've set up the drag-and-drop.
     *
     * @param out Optional {@link java.io.PrintStream} for logging drag and drop messages
     * @param c The component to unregister
     * @param recursive Recursively unregister components within a container
     * @since 1.0
     */
    public static boolean remove( java.io.PrintStream out, java.awt.Component c, boolean recursive )
    {   // Make sure we support dnd.
        if( supportsDnD() ) {
            c.setDropTarget( null );
            if( recursive && ( c instanceof java.awt.Container ) )
            {   java.awt.Component[] comps = ((java.awt.Container)c).getComponents();
                for( int i = 0; i < comps.length; i++ )
                    remove( out, comps[i], recursive );
                return true;
            }   // end if: recursive
            else return false;
        }   // end if: supports DnD
        else return false;
    }   // end remove
    
    

    
/* ********  I N N E R   I N T E R F A C E   L I S T E N E R  ******** */    
    
    
    /**
     * Implement this inner interface to listen for when files are dropped. For example
     * your class declaration may begin like this:
     * <code><pre>
     *      public class MyClass implements FileDrop.Listener
     *      ...
     *      public void filesDropped( java.io.File[] files )
     *      {
     *          ...
     *      }   // end filesDropped
     *      ...
     * </pre></code>
     *
     * @since 1.1
     */
    public static interface Listener {
       
        /**
         * This method is called when files have been successfully dropped.
         *
         * @param files An array of <tt>File</tt>s that were dropped.
         * @since 1.0
         */
        public abstract void filesDropped( Event event );
        
        
    }   // end inner-interface Listener
    
    
/* ********  I N N E R   C L A S S  ******** */    
    
    
    /**
     * This is the event that is passed to the
     * {@link FileDropListener#filesDropped filesDropped(...)} method in
     * your {@link FileDropListener} when files are dropped onto
     * a registered drop target.
     */
    public static class Event extends java.util.EventObject {

        /**
		 * 
		 */
		private static final long serialVersionUID = -2628373286251503684L;
		private java.io.File[] files;

        /**
         * Constructs an {@link Event} with the array
         * of files that were dropped and the
         * {@link Dropper} that initiated the event.
         *
         * @param files The array of files that were dropped
         * @source The event source
         * @since 1.1
         */
        public Event( java.io.File[] files, Object source ) {
            super( source );
            this.files = files;
        }   // end constructor

        /**
         * Returns an array of files that were dropped on a
         * registered drop target.
         *
         * @return array of files that were dropped
         * @since 1.1
         */
        public java.io.File[] getFiles() {
            return files;
        }   // end getFiles
    
    }   // end inner class Event
    
    
/* ********  I N N E R   C L A S S  ******** */
    

    /**
     * At last an easy way to encapsulate your custom objects for dragging and dropping
     * in your Java programs!
     * When you need to create a {@link java.awt.datatransfer.Transferable} object,
     * use this class to wrap your object.
     * For example:
     * <pre><code>
     *      ...
     *      MyCoolClass myObj = new MyCoolClass();
     *      Transferable xfer = new TransferableObject( myObj );
     *      ...
     * </code></pre>
     * Or if you need to know when the data was actually dropped, like when you're
     * moving data out of a list, say, you can use the {@link TransferableObject.Fetcher}
     * inner class to return your object Just in Time.
     * For example:
     * <pre><code>
     *      ...
     *      final MyCoolClass myObj = new MyCoolClass();
     *
     *      TransferableObject.Fetcher fetcher = new TransferableObject.Fetcher()
     *      {   public Object getObject(){ return myObj; }
     *      }; // end fetcher
     *
     *      Transferable xfer = new TransferableObject( fetcher );
     *      ...
     * </code></pre>
     *
     * The {@link java.awt.datatransfer.DataFlavor} associated with 
     * {@link TransferableObject} has the representation class
     * <tt>net.iharder.dnd.TransferableObject.class</tt> and MIME type
     * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
     * This data flavor is accessible via the static
     * {@link #DATA_FLAVOR} property.
     *
     *
     * <p>I'm releasing this code into the Public Domain. Enjoy.</p>
     * 
     * @author  Robert Harder
     * @author  rob@iharder.net
     * @version 1.2
     */
    public static class TransferableObject implements java.awt.datatransfer.Transferable
    {
        /**
         * The MIME type for {@link #DATA_FLAVOR} is 
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public final static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";


        /**
         * The default {@link java.awt.datatransfer.DataFlavor} for
         * {@link TransferableObject} has the representation class
         * <tt>net.iharder.dnd.TransferableObject.class</tt>
         * and the MIME type 
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public final static java.awt.datatransfer.DataFlavor DATA_FLAVOR = 
            new java.awt.datatransfer.DataFlavor( Dropper.TransferableObject.class, MIME_TYPE );


        private Fetcher fetcher;
        private Object data;

        private java.awt.datatransfer.DataFlavor customFlavor; 



        /**
         * Creates a new {@link TransferableObject} that wraps <var>data</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class,
         * this creates a custom data flavor with a representation class 
         * determined from <code>data.getClass()</code> and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @param data The data to transfer
         * @since 1.1
         */
        public TransferableObject( Object data )
        {   this.data = data;
            this.customFlavor = new java.awt.datatransfer.DataFlavor( data.getClass(), MIME_TYPE );
        }   // end constructor



        /**
         * Creates a new {@link TransferableObject} that will return the
         * object that is returned by <var>fetcher</var>.
         * No custom data flavor is set other than the default
         * {@link #DATA_FLAVOR}.
         *
         * @see Fetcher
         * @param fetcher The {@link Fetcher} that will return the data object
         * @since 1.1
         */
        public TransferableObject( Fetcher fetcher )
        {   this.fetcher = fetcher;
        }   // end constructor



        /**
         * Creates a new {@link TransferableObject} that will return the
         * object that is returned by <var>fetcher</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class,
         * this creates a custom data flavor with a representation class <var>dataClass</var>
         * and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @see Fetcher
         * @param dataClass The {@link java.lang.Class} to use in the custom data flavor
         * @param fetcher The {@link Fetcher} that will return the data object
         * @since 1.1
         */
        public TransferableObject( Class<?> dataClass, Fetcher fetcher )
        {   this.fetcher = fetcher;
            this.customFlavor = new java.awt.datatransfer.DataFlavor( dataClass, MIME_TYPE );
        }   // end constructor

        /**
         * Returns the custom {@link java.awt.datatransfer.DataFlavor} associated
         * with the encapsulated object or <tt>null</tt> if the {@link Fetcher}
         * constructor was used without passing a {@link java.lang.Class}.
         *
         * @return The custom data flavor for the encapsulated object
         * @since 1.1
         */
        public java.awt.datatransfer.DataFlavor getCustomDataFlavor()
        {   return customFlavor;
        }   // end getCustomDataFlavor


    /* ********  T R A N S F E R A B L E   M E T H O D S  ******** */    


        /**
         * Returns a two- or three-element array containing first
         * the custom data flavor, if one was created in the constructors,
         * second the default {@link #DATA_FLAVOR} associated with
         * {@link TransferableObject}, and third the
         * {@link java.awt.datatransfer.DataFlavor.stringFlavor}.
         *
         * @return An array of supported data flavors
         * @since 1.1
         */
        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() 
        {   
            if( customFlavor != null )
                return new java.awt.datatransfer.DataFlavor[]
                {   customFlavor,
                    DATA_FLAVOR,
                    java.awt.datatransfer.DataFlavor.stringFlavor
                };  // end flavors array
            else
                return new java.awt.datatransfer.DataFlavor[]
                {   DATA_FLAVOR,
                    java.awt.datatransfer.DataFlavor.stringFlavor
                };  // end flavors array
        }   // end getTransferDataFlavors



        /**
         * Returns the data encapsulated in this {@link TransferableObject}.
         * If the {@link Fetcher} constructor was used, then this is when
         * the {@link Fetcher#getObject getObject()} method will be called.
         * If the requested data flavor is not supported, then the
         * {@link Fetcher#getObject getObject()} method will not be called.
         *
         * @param flavor The data flavor for the data to return
         * @return The dropped data
         * @since 1.1
         */
        public Object getTransferData( java.awt.datatransfer.DataFlavor flavor )
        throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException 
        {   
            // Native object
            if( flavor.equals( DATA_FLAVOR ) )
                return fetcher == null ? data : fetcher.getObject();

            // String
            if( flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor ) )
                return fetcher == null ? data.toString() : fetcher.getObject().toString();

            // We can't do anything else
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }   // end getTransferData




        /**
         * Returns <tt>true</tt> if <var>flavor</var> is one of the supported
         * flavors. Flavors are supported using the <code>equals(...)</code> method.
         *
         * @param flavor The data flavor to check
         * @return Whether or not the flavor is supported
         * @since 1.1
         */
        public boolean isDataFlavorSupported( java.awt.datatransfer.DataFlavor flavor ) 
        {
            // Native object
            if( flavor.equals( DATA_FLAVOR ) )
                return true;

            // String
            if( flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor ) )
                return true;

            // We can't do anything else
            return false;
        }   // end isDataFlavorSupported


    /* ********  I N N E R   I N T E R F A C E   F E T C H E R  ******** */    

        /**
         * Instead of passing your data directly to the {@link TransferableObject}
         * constructor, you may want to know exactly when your data was received
         * in case you need to remove it from its source (or do anyting else to it).
         * When the {@link #getTransferData getTransferData(...)} method is called
         * on the {@link TransferableObject}, the {@link Fetcher}'s
         * {@link #getObject getObject()} method will be called.
         *
         * @author Robert Harder
         * @copyright 2001
         * @version 1.1
         * @since 1.1
         */
        public static interface Fetcher
        {
            /**
             * Return the object being encapsulated in the
             * {@link TransferableObject}.
             *
             * @return The dropped object
             * @since 1.1
             */
            public abstract Object getObject();
        }   // end inner interface Fetcher

    }   // end class TransferableObject
    
}   // end class FileDrop