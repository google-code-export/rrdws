package ws.rrd;
/** 
 * <b>Description:TODO</b>
 * @author      v.i.p.<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  13.04.2010::19:32:04<br> 
 */
import java.util.Date;
 
import javax.jdo.PersistenceManagerFactory;

import ws.rdd.jdo.RRD_JDOHelper;


public final class PMF {
 

    public static PersistenceManagerFactory get() {
        return RRD_JDOHelper.getInstance().getPMF();
    }
    
    public static Date getServerDate(){
        return RRD_JDOHelper.getInstance().getPMF().getPersistenceManager().getServerDate();
    }
}

 