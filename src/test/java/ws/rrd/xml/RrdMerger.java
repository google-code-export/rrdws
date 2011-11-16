package ws.rrd.xml;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jrobin.cmd.RrdCommander;
import org.jrobin.core.RrdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/** 
 * <b>Description:DraftUndDirty Python2Java reimplementation of http://oss.oetiker.ch/rrdtool/pub/contrib/merge-rrd.tgz </b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  16.11.2011::14:50:49<br> 
 */
public class RrdMerger {
public static final String ANY_WHITE_SPACE_CHAR = " \n\t";
/*
 * 
 * #!/usr/bin/env python2
"""
    merge-rrd.py
    
    This script takes rrds with the same structure and merges them 
    into a new rrd with the combined data.
    
"""

import sys, commands, os, re

reMatchCF = re.compile(".*<cf>").match
reMatchPDP = re.compile(".*<pdp_per_row>").match
reMatchDBStart = re.compile(".*<database>").match
reMatchDBEnd = re.compile(".*</database>").match
reMatchRow = re.compile(".*<row><v>").match
*/
	String reMatchCF =  ".*<cf>";
	String reMatchPDP =  ".*<pdp_per_row>";
	String reMatchDBStart =  ".*<database>";
	String reMatchDBEnd =  ".*</database>";
	String reMatchRow =  ".*<row><v>";
	private static final Logger log = LoggerFactory.getLogger(RrdMerger.class .getName());
	
/*	
def printUsage():
    """Prints help information"""
    help = """
    Usage: merge-rrd.py <old rrd> <new rrd> <merged rrd>
    
    merge-rrd.py is a python script that merges that data found in rrd
    files.  This assumes that the two rrds are have the same data structure.
    
    The script creates the merged rrd by copying the entries from the new rrd.
    If the new rrd has database entries with missing data, then the records
    of the old rrd are used instead.  This mean that data from the new rrd
    will always take precedence over the data in the old rrd.
    
    Please report any bug at:
     minhong.tsai@gmail.com                                    4-11-2005
    """
    print help
 */
	
private static void printUsage(){
	String help = 
			"\n\t   Usage: merge-rrd.py <old rrd> <new rrd> <merged rrd>" +
			"\n\t   merge-rrd.py is a python script that merges that data found in rrd" +
			"\n\t   files.  This assumes that the two rrds are have the same data structure." +
			"\n\t"+
			"\n\t"+
			"\n\t   The script creates the merged rrd by copying the entries from the new rrd." +
			"\n\t   If the new rrd has database entries with missing data, then the records" +
			"\n\t   of the old rrd are used instead.  This mean that data from the new rrd" +
			"\n\t   will always take precedence over the data in the old rrd." +
			"\n\t"+
			"\n\t"+
			"\n\t"+
			"\n\t   Please report any bug at:" +
			"\n\t    vasiliij.pupkin att gmail.com                                      " +
			""
		;
	System.out.println(help );
}
	
//def getXmlDict(oxml):
Map<String, List<Row>> getXmlDict(String oxml){
//    """Reads in rrd xml and returns a dictionary of lists
//    
//    The dictionary key is "<cf><pdp-per-row>". e.g.: AVERAGE1 or MAX6
//    The list is the database entries for this <rra>
//    """
//    lines = oxml.split("\n")
	  String[] lines = oxml.split("\n");
//    rrd_d = {}
	  Map<String,List<Row>> rrd_d = new HashMap<String, List<Row>>();
//    #    <cf> AVERAGE </cf>
//    #    <pdp_per_row> 288 </pdp_per_row> <!-- 86400 seconds -->
//
//    # parse xml file
//    key = ""
	  String key = "";
//    rows = []  
	  List<Row> rows = new ArrayList<Row>();
//    for line in lines:
	  for (String line:lines){
//        if (reMatchCF(line)):
//            cf = line.split()[1]
//            key += cf
		  if (line.matches(reMatchCF)){
			  key +=  line.split(reMatchCF)[1];
		  }
//        if (reMatchPDP(line)):
//            pdp = line.split()[1]
//            key += pdp
		  if (line.matches(reMatchPDP)){
			  key +=  line.split(reMatchPDP)[1];
		  }

		//        if (reMatchRow(line)):
//            ele = line.split()
//            time = ele[5]
//            val = ele[8]
//            rows.append([time,val,line])
		  if (line.matches(reMatchRow)){
			 
			  Row row = new Row(line);
			  rows.add( row);
		  }
			  		  
//        # end of rra is reached, store to dict and rest vals
//        if (reMatchDBEnd(line) and key and rows):
//            rrd_d[key] = rows
//            key = ""
//            rows = []
		  if (line.matches(reMatchDBEnd) && key.length()>0 && rows.size()>0 ){
			  rrd_d.put (key ,rows);
		  }
	  }
//    return rrd_d
	  return rrd_d ;
}
	
 
//def mergeRRD(opath, npath, mpath):
void mergeRRD(String opath,String npath,String mpath) throws IOException, RrdException{
//    """Combines old and cur rrd to create new one"""  
//    # read in xml dump of old and new rrds
//    ocmd = "rrdtool dump %s"%(opath,)
//    ncmd = "rrdtool dump %s"%(npath,)
		
	String ocmd = "rrdtool dump "+opath ;
	String ncmd = "rrdtool dump "+npath ;
	
//    (status, oxml) = commands.getstatusoutput(ocmd)
//    (status, nxml) = commands.getstatusoutput(ncmd)
	
	String oxml = null; 
	
	oxml = existAsXml(opath)?
			readFully(opath)
			:(String) RrdCommander.execute(ocmd); 
	
	
	String nxml = null;
	
	nxml =existAsXml(npath)?
			readFully(npath): 
		(String) RrdCommander.execute(ncmd); 
		
//    
//    odict = getXmlDict(oxml)
	Map<String,List<Row>> odict = getXmlDict(""+oxml);
//    mxmlf = open(mpath+".xml",'w')
	ByteArrayOutputStream mergedxml = new ByteArrayOutputStream();
	PrintWriter mxmlf = new PrintWriter(mergedxml, true);
//    
//    new_lines = []
//    clines = nxml.split("\n")
	String []clines = nxml.split("\n");
//    
//    # look for matches to replace and upate current file
//    key = ""
	String key = "";
//    
//    for line in clines:
	for (String line:clines){
//        replace = False
		boolean replace = false;
//        replace_line = ""
		 String replace_line = "";
	
//        if (reMatchCF(line)):
//            cf = line.split()[1]
//            key += cf
		 if (line.matches( reMatchCF )){ 
			 key += line.split(ANY_WHITE_SPACE_CHAR)[1];
		 }
//        if (reMatchPDP(line)):
//            pdp = line.split()[1]
//            key += pdp
		 if (line.matches( reMatchPDP )){ 
			 key += line.split(ANY_WHITE_SPACE_CHAR)[1];
		 }		 
//        if (reMatchRow(line)):
//            ele = line.split()
//            time = ele[5]
//            val = ele[8]
//            crow = [time,val]
		  if (line.matches(reMatchRow)){
				 
			  Row crow = new Row(line);
			   
		  		 
//            # check if any old entries has same time
//            # and replace if current rrd has no values!
//            if (key in odict.keys()):
			  if (odict.keySet().contains(key)){
//                rows = odict[key]
				  List<Row> rows = odict.get(key); 
//                for row in rows:
				  for (Row row:rows){
//                    if (row[0] == crow[0] and crow[1] == "NaN"):
					  if (row.getTime() == crow.getTime() && crow.getVal() == Double.NaN){
//                        #print "found replace:"+row[2]
						  System.out.println("found replace:"+row.getVal() );
//                        replace = True
						  replace = true;
//                        replace_line = row[2]
						  replace_line = ""+row.getVal();
//                        break
						  break;
					  }
				  }
			  }
		  }
//        # end of rra is reached, reset key
//        if (reMatchDBEnd(line)):
		  if (line.matches(reMatchDBEnd)){
//            key = ""
			  key = "";
		  }
//        if(replace):
//            mxmlf.write(replace_line+"\n")
		  if(replace){
			  mxmlf.write(replace_line+"\n");
		  }else{
//        else:
//            mxmlf.write(line+"\n")
			  mxmlf.println(line+"\n");
		  }
	}
//    mxmlf.close()     
	 
	
	 // TODO refactor to stream
	mxmlf.flush();
	mxmlf.close();
	// System.out.println(mergedxml.toString());
	mergedxml.writeTo(new FileOutputStream(mpath));
	
//
//    #create new rrds
//    rcmd = "rrdtool restore %s %s"%(os.path.join(mpath+".xml"),mpath)
//    (status, query_res) = commands.getstatusoutput(rcmd)
//    #remove tmp.xml
//    os.unlink(mpath+".xml")
}
	
	
private String readFully(String opath) throws IOException {
	FileInputStream fileInputStream = new FileInputStream(opath);
	BufferedReader rdTmp = new BufferedReader( new InputStreamReader (fileInputStream));
	StringBuffer retval = new StringBuffer();
	for (String lnTmp=rdTmp.readLine();lnTmp!=null;lnTmp=rdTmp.readLine()){
		retval .append( lnTmp);
		retval .append(  "\n");
	}
	return retval.toString();
	
}

private boolean existAsXml(String opath) {
	File fTmp = new File (opath);
	return fTmp.exists();
 
}

//### start of script ###
public static void main(String [] argv) throws IOException, RrdException{
//if (len(sys.argv) != 4):
 if (argv.length != 3){	
//    print "merge-rrd.py take 3 arguments"
	System.out.println("merge-rrd.py take 3 arguments");
//    printUsage()
	printUsage();
//    sys.exit(1)
//    
}else{
//old_path = sys.argv[1]
	String old_path = argv[0];
//new_path = sys.argv[2]
	String new_path = argv[1];
//mer_path = sys.argv[3]
	String mer_path = argv[2];
//
//if (mer_path == old_path):
	if (mer_path == old_path){
//    print "WARN: you are trying to overwrite your old rrd:%s"%(old_path,)
		System.out.println("WARN: you are trying to overwrite your old rrd: "+mer_path );
//    printUsage()
//    sys.exit(1)
	}
//if (mer_path == new_path):
	if (mer_path == new_path){
//    print "WARN: you are trying to overwrite your new rrd:%s"%(new_path,)
//    printUsage()
//    sys.exit(1)
		System.out.println("WARN: you are trying to overwrite your new rrd "+new_path );
		printUsage();
	}
//if (os.path.exists(mer_path)):
	if ((new File( mer_path)).exists()){
//    print "WARN: your merged rrd already exits.  Please remove it first:%s"%(mer_path,)
//    printUsage()
//    sys.exit(1)
		System.out.println("WARN: your merged rrd already exits.  Please remove it first:"+mer_path );
		printUsage();
	}
//    
//print "merging old:%s to new:%s. creating merged rrd: %s"%(old_path,new_path,mer_path)
	log.info("merging old:%s to new:%s. creating merged rrd: %s", new Object[]{old_path,new_path,mer_path} );
//mergeRRD(old_path, new_path, mer_path)
	new RrdMerger().mergeRRD( old_path, new_path, mer_path);
    
}
    
        
     


 }
}


 