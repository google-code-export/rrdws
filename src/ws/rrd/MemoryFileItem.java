package ws.rrd;
/** 
 * <b>Description:TODO</b>
 * @author      xco5015<br>
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
import java.io.UnsupportedEncodingException;
import java.util.Date;


import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


import org.apache.commons.fileupload.FileItem;


import com.google.appengine.api.datastore.Blob;




@PersistenceCapable(identityType = IdentityType.UNSPECIFIED)//APPLICATION
public class MemoryFileItem implements FileItem {


        /**
         * 
         */
        private static final long serialVersionUID = 6873943621309250882L;
        
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Long id;
    
    @NotPersistent
        private String fieldName;
    
    @Persistent
    protected String contentType;
    
    @Persistent
    protected String fileName;
    
    @NotPersistent
        private boolean isFormField;
        
    @NotPersistent
        private ByteArrayOutputStream content_out;
    
    @Persistent
    protected Blob content;
    
    @Persistent
    protected Date date_created;
        
        public MemoryFileItem(String fieldName, String contentType, boolean isFormField,
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
                return (content != null) ? content.getBytes() : content_out.toByteArray(); 
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
                        bais = new ByteArrayInputStream(content_out.toByteArray());
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
                return content_out;
        }


        public long getSize() {
                return (content != null) ? content.getBytes().length : content_out.size();
        }


        public String getString() {
                return (content != null) ? new String(content.getBytes()) : new String(content_out.toByteArray());
        }


        public String getString(String arg0) throws UnsupportedEncodingException {
                return (content != null) ? new String(content.getBytes(), arg0) : new String(content_out.toByteArray(), arg0);
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
                // Unimplemented - can't use FileWriter in GAE.
        }


        public boolean commit(){
                
                content = new Blob(get());
                 
                  return true;
        }


        public Date getDate() {
                return date_created;
        }
        
}

 