package ws.rdd.jdo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class RRD_JDOHelper {
	private static final RRD_JDOHelper me = new RRD_JDOHelper();
	PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	PersistenceManager pm = PMF.getPersistenceManager();
	
	public static RRD_JDOHelper getInstance(){
		return me;
	}

	public PersistenceManagerFactory getPMF() {
		return PMF;
	}

	public PersistenceManager getPm() {
		return pm;
	}
}
