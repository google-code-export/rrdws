package ws.rrd.xml;
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  16.11.2011::15:10:05<br> 
 */
public class Row {

	private String time;
	private Double val;

	public Row(String[] ele) {
//      time = ele[5]
//      val = ele[8]
		this.setTime(ele[5]);
		this.setVal(Double.valueOf( ele[8] ));
	}

	public Row(String line) {
		 this( line.split(RrdMerger.ANY_WHITE_SPACE_CHAR) );
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() { return time;
	}

	public void setVal(Double val) {
		this.val = val;
	}

	public Double getVal() { return val;
	}

}


 