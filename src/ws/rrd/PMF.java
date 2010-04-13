package ws.rrd;
/** 
 * <b>Description:TODO</b>
 * @author      xco5015<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  13.04.2010::19:32:04<br> 
 */
import java.util.Date;


import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;


public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");


    private PMF() {}


    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
    
    public static Date getServerDate(){
        return pmfInstance.getPersistenceManager().getServerDate();
    }
}

 