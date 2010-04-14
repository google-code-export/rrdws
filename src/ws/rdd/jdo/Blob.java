package ws.rdd.jdo;
 
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.DATASTORE )
public class Blob {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private com.google.appengine.api.datastore.Blob data;
    @Persistent
    private String name;
    @Persistent
    private Date createDate = new Date();

    @Persistent
    private Date updateDate;    

    public Blob( byte[] data) {
		super(); 
		this.setData( data );
	}
	
    // Accessors for the fields.  JDO doesn't use these, but your application does.

    public Key getKey() {
        return key;
    }
    
    
	public byte[] getData() {
			 return this.data.getBytes() ;
	}
	

	private void update() {
		setUpdateDate(new Date () );
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		update();
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setData(byte[] buffer) {
		this.data = new com.google.appengine.api.datastore.Blob(buffer);
		this.update();		
	}    

}
