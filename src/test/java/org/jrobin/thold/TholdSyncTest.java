package org.jrobin.thold; 

public class TholdSyncTest extends TholdTest {

	@Override
	protected void setUp() throws Exception {
	
		super.setUp(); 
		AlertCaptain.getInstance().setAsync(false);
	
	}
 
}
