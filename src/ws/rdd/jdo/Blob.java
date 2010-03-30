package ws.rdd.jdo;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Blob {
    public static final String ENCODING = "UTF-8";
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

    public Blob( String data) {
		super(); 
		this.setData( data );
	}
	
    // Accessors for the fields.  JDO doesn't use these, but your application does.

    public Key getKey() {
        return key;
    }
    
    
	public String getData() {
		try {
			return new String(this.data.getBytes(), ENCODING);
		} catch (UnsupportedEncodingException e) {
			return new String(this.data.getBytes() );
		}
	}
	public void setData(String data) {
		byte[] buffer;
		try {
			buffer = data.getBytes(ENCODING);
		} catch (UnsupportedEncodingException e) {
			buffer = data.getBytes( );
		}
		this.setData(buffer);
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
