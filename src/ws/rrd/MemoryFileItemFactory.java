package ws.rrd;
/** 
 * <b>Description:TODO</b>
 * @author      xco5015<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  13.04.2010::19:30:48<br> 
 */
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;


public class MemoryFileItemFactory implements FileItemFactory {


        private int maxSize;
        
        public MemoryFileItemFactory(){
                maxSize = 4*1024*1024;
        }
        public MemoryFileItemFactory(int size){
                maxSize = size;
        }
        
        public FileItem createItem(String fieldName, String contentType, boolean isFormField,
                        String fileName) {


                MemoryFileItem item = new MemoryFileItem(fieldName,contentType,isFormField,fileName,maxSize);
                return item;
        }


        public void setMaxSize(int size){
                maxSize = size;
        }
}
 