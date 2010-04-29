package ws.rrd;
/** 
 * <b>Description:TODO</b>
 * @author      v.i.p.<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  13.04.2010::19:31:22<br> 
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date; 
import org.apache.commons.fileupload.FileItem;
 
 

 
public class MemoryFileItem implements FileItem, Serializable {
 
	private static final long serialVersionUID = -7492370404074144424L;
 
    protected Long id;
     
    private String fieldName; 
    protected String contentType; 
    protected String fileName; 
    private boolean isFormField; 
    volatile private Object content_out; 
    protected Blob content; 
    protected Date date_created;
        
        MemoryFileItem(String fieldName, String contentType, boolean isFormField,
                        String fileName, int maxSize){
                this.fieldName = fieldName;
                this.contentType = contentType;
                this.isFormField = isFormField;
                this.fileName = fileName;
                this.content = null;
                this.content_out = (maxSize > 0) ? new ByteArrayOutputStream(maxSize) : new ByteArrayOutputStream();
                this.date_created = new Date();
        }
        
        public Long getId() {
                return id;
        }


        public void delete() {
                //Unimplemented - memory only, no cleaning up needed.
        }


        public byte[] get() {
                return (content != null) ? content.getBytes() : ((ByteArrayOutputStream)content_out).toByteArray(); 
        }


        public String getContentType() {
                return contentType;
        }


        public String getFieldName() {
                return fieldName;
        }


        public InputStream getInputStream() throws IOException {
                ByteArrayInputStream bais;
                if(content != null){
                        bais = new ByteArrayInputStream(content.getBytes());
                } else {
                        bais = new ByteArrayInputStream(((ByteArrayOutputStream)content_out).toByteArray());
                }
                return bais;
        }


        public String getName() {
                return fileName;
        }


        /**
         * This method can't be used if object was fetched from datastore.
         * */
        public OutputStream getOutputStream() throws IOException {
                return (ByteArrayOutputStream)content_out;
        }


        public long getSize() {
                return (content != null) ? content.getBytes().length : ((ByteArrayOutputStream )content_out).size();
        }


        public String getString() {
                return (content != null) ? new String(content.getBytes()) : new String(((ByteArrayOutputStream)content_out).toByteArray());
        }


        public String getString(String arg0) throws UnsupportedEncodingException {
                return (content != null) ? new String(content.getBytes(), arg0) : new String(((ByteArrayOutputStream)content_out).toByteArray(), arg0);
        }


        public boolean isFormField() {
                return isFormField;
        }


        public boolean isInMemory() {
                return true;
        }


        public void setFieldName(String arg0) {
                this.fieldName = arg0;
        }


        public void setFormField(boolean arg0) {
                this.isFormField = arg0;
        }


        public void write(File arg0) throws Exception {
        	if(1==1)throw new RuntimeException("Unimplemented - can't use FileWriter in GAE.   File::"+arg0);
        }

        /** 
         * finalize write-enable-mode and flush data into content-Blob.
         * 
         * @return
         * @throws IOException 
         */
        public synchronized boolean flush() throws IOException{
        		((ByteArrayOutputStream)content_out).flush();
        		((ByteArrayOutputStream)content_out).close(); 
                content = new Blob(get());  
                content_out = null;
                return true;
        }


        public Date getDate() {
                return date_created;
        }
        
}

 