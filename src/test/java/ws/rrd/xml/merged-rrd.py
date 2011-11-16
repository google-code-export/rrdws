#!/usr/bin/env python2
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
    
def getXmlDict(oxml):
    """Reads in rrd xml and returns a dictionary of lists
    
    The dictionary key is "<cf><pdp-per-row>". e.g.: AVERAGE1 or MAX6
    The list is the database entries for this <rra>
    """
    lines = oxml.split("\n")
    rrd_d = {}
    #    <cf> AVERAGE </cf>
    #    <pdp_per_row> 288 </pdp_per_row> <!-- 86400 seconds -->

    # parse xml file
    key = ""
    rows = []  
    for line in lines:
        if (reMatchCF(line)):
            cf = line.split()[1]
            key += cf
        if (reMatchPDP(line)):
            pdp = line.split()[1]
            key += pdp
        if (reMatchRow(line)):
            ele = line.split()
            time = ele[5]
            val = ele[8]
            rows.append([time,val,line])
        # end of rra is reached, store to dict and rest vals
        if (reMatchDBEnd(line) and key and rows):
            rrd_d[key] = rows
            key = ""
            rows = []
    return rrd_d

def mergeRRD(opath, npath, mpath):
    """Combines old and cur rrd to create new one"""  
    # read in xml dump of old and new rrds
    ocmd = "rrdtool dump %s"%(opath,)
    ncmd = "rrdtool dump %s"%(npath,)
    (status, oxml) = commands.getstatusoutput(ocmd)
    (status, nxml) = commands.getstatusoutput(ncmd)
    
    odict = getXmlDict(oxml)
    mxmlf = open(mpath+".xml",'w')
    
    new_lines = []
    clines = nxml.split("\n")
    
    # look for matches to replace and upate current file
    key = ""
    
    for line in clines:
        replace = False
        replace_line = ""
        if (reMatchCF(line)):
            cf = line.split()[1]
            key += cf
        if (reMatchPDP(line)):
            pdp = line.split()[1]
            key += pdp
        if (reMatchRow(line)):
            ele = line.split()
            time = ele[5]
            val = ele[8]
            crow = [time,val]
            # check if any old entries has same time
            # and replace if current rrd has no values!
            if (key in odict.keys()):
                rows = odict[key]
                for row in rows:
                    if (row[0] == crow[0] and crow[1] == "NaN"):
                        #print "found replace:"+row[2]
                        replace = True
                        replace_line = row[2]
                        break
        # end of rra is reached, reset key
        if (reMatchDBEnd(line)):
            key = ""
        if(replace):
            mxmlf.write(replace_line+"\n")
        else:
            mxmlf.write(line+"\n")
    mxmlf.close()     

    #create new rrds
    rcmd = "rrdtool restore %s %s"%(os.path.join(mpath+".xml"),mpath)
    (status, query_res) = commands.getstatusoutput(rcmd)
    #remove tmp.xml
    os.unlink(mpath+".xml")

### start of script ###
if (len(sys.argv) != 4):
    print "merge-rrd.py take 3 arguments"
    printUsage()
    sys.exit(1)
    
old_path = sys.argv[1]
new_path = sys.argv[2]
mer_path = sys.argv[3]

if (mer_path == old_path):
    print "WARN: you are trying to overwrite your old rrd:%s"%(old_path,)
    printUsage()
    sys.exit(1)
if (mer_path == new_path):
    print "WARN: you are trying to overwrite your new rrd:%s"%(new_path,)
    printUsage()
    sys.exit(1)
if (os.path.exists(mer_path)):
    print "WARN: your merged rrd already exits.  Please remove it first:%s"%(mer_path,)
    printUsage()
    sys.exit(1)
    
print "merging old:%s to new:%s. creating merged rrd: %s"%(old_path,new_path,mer_path)
mergeRRD(old_path, new_path, mer_path)
    

    
        
     

