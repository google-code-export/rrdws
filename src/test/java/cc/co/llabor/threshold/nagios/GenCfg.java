package cc.co.llabor.threshold.nagios;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  17.02.2012::20:23:21<br> 
 */
public class GenCfg extends Cfg {

	private String cfgName;

	public GenCfg(String cfgName) {
		this.cfgName = cfgName;
	}

	@Override
	String getCfgName() { 
			return cfgName; 
	}

}


 