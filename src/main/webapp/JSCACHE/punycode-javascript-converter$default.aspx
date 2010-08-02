<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><div id='__ASProxyOriginalURL' dir='ltr' style='display:block;font-family:verdana;color:black;font-size:11px;padding:2px 5px 2px 5px;margin:0;position:absolute;left:0px;top:0px;width:98%;background:whitesmoke none;border:solid 2px black;overflow: visible;z-index:999999999;visibility:hidden;text-align:left;'></div>
<html>
<head><title>
	ASProxy: Blog@CodeXpress  &raquo; Blog Archive   &raquo; Punycode Javascript Converter
</title>
<script src="scripts/base64encoder.js" type="text/javascript"></script>
<!-- Surf the web invisibly using ASProxy power. A Powerfull web proxy is in your hands. -->
<style type="text/css">
#ASProxyMain{width:99.5%;display:block;padding:1px;margin:0px;border:2px solid #000000;text-align: center;}
#ASProxyMainForm{display:block;padding:1px;margin:0px; width:auto; height:auto;}
#ASProxyMain table{margin:0; padding:0px;}
#ASProxyMainTable{color:black;padding:0px;margin:0px;border:1px solid #C0C0C0;background-color:#f8f8f8;background-image:none;font-weight:normal;font-style:normal;line-height:normal;visibility:visible;table-layout:auto;white-space:normal;word-spacing:normal;}
#ASProxyMainTable td{margin:0; padding:0px; border-width:0px;color:black;font-family: Tahoma, sans-serif;font-size: 8pt;text-align: center;float:none;background-color:#f8f8f8;}
#ASProxyMainTable .Button{background-color: #ECE9D8;border:2px outset;float:none;}
#ASProxyMainTable .Sides{width: 140px;}
#ASProxyMainTable a,#ASProxyMainTable a:hover,#ASProxyMainTable a:visited,#ASProxyMainTable a:active{font:normal normal normal 100% Tahoma;font-family: Tahoma, sans-serif; color: #000099; text-decoration:underline;}
#ASProxyMainTable input{width:auto !important; font-size: 10pt;border:solid 1px silver;background-color: #FFFFFF;margin:1px 2px 1px 2px;}
#ASProxyMainTable span input,
.ASProxyCheckBox input{margin:0px;background-color:#F8F8F8;display:inline;border-width: 0px;float:none;height:auto !important;}
#ASProxyMainTable span label,
.ASProxyCheckBox label{margin:0px 2px;padding:0px; vertical-align:baseline;float:none;color:Black;height:auto !important;font:normal normal normal 100% Tahoma;display:inline;border-width:0px;background-color:#F8F8F8;}
.ASProxyCheckBox{margin:0px 2px;padding:0px;float:none;color:Black;height:auto !important;font:normal normal normal 100% Tahoma;display:inline;border-width:0px;background-color:#F8F8F8;}
</style></head><body>
<script language="javascript" type="text/javascript">
var _ASProxyVersion="5.2";
function toggleOpt(lnk){var trMoreOpt=document.getElementById('trMoreOpt'); if (trMoreOpt.style.display=='none'){trMoreOpt.style.display='';lnk.innerHTML='Options...<small>&lt;</small>';
}else{trMoreOpt.style.display='none';lnk.innerHTML='Options...<small>&gt;</small>';}}
</script>

<script type="text/javascript">
_PgOpt={};
_XPage={};
var _Page_B64Unknowner="B64Coded!";
var _Page_CookieName="ASProxyConfig";

function _Page_Initialize(){
	_XNav='default.aspx';
	_XPage.UrlBox =document.getElementById('txtUrl');
	_XPage.ProcessLinks =document.getElementById('chkProcessLinks');
	_XPage.DisplayImages =document.getElementById('chkDisplayImages');
	_XPage.Forms =document.getElementById('chkForms');
	_XPage.Compression =document.getElementById('chkCompression');
	_XPage.Cookies =document.getElementById('chkCookies');
	_XPage.TempCookies =document.getElementById('chkTempCookies');
	_XPage.OrginalUrl =document.getElementById('chkOrginalUrl');
	_XPage.Frames =document.getElementById('chkFrames');
	_XPage.PageTitle =document.getElementById('chkPageTitle');
	_XPage.UTF8 =document.getElementById('chkUTF8');
	_XPage.RemoveScripts =document.getElementById('chkRemoveScripts');
}

function _Page_SetOptions(){
	_XPage.ProcessLinks.checked =true;
	_XPage.DisplayImages.checked =true;
	_XPage.Forms.checked =true;
	_XPage.Compression.checked =false;
	_XPage.Cookies.checked =true;
	_XPage.TempCookies.checked =false;
	_XPage.OrginalUrl.checked =true;
	_XPage.Frames.checked =true;
	_XPage.PageTitle.checked =true;
	_XPage.UTF8.checked =false;
	_XPage.RemoveScripts.checked =false;
	_PgOpt.EncodeUrl=true;
}
function _Page_SaveOptions(){
	var cookieOpt=_Page_CookieName+"=";
	cookieOpt+="&Links="+_XPage.ProcessLinks.checked;
	cookieOpt+="&Images="+_XPage.DisplayImages.checked;
	cookieOpt+="&SubmitForms="+_XPage.Forms.checked;
	cookieOpt+="&HttpCompression="+_XPage.Compression.checked;
	cookieOpt+="&Cookies="+_XPage.Cookies.checked;
	cookieOpt+="&TempCookies="+_XPage.TempCookies.checked;
	cookieOpt+="&OrginalUrl="+_XPage.OrginalUrl.checked;
	cookieOpt+="&Frames="+_XPage.Frames.checked;
	cookieOpt+="&PageTitle="+_XPage.PageTitle.checked;
	cookieOpt+="&ForceEncoding="+_XPage.UTF8.checked;
	cookieOpt+="&RemoveScripts="+_XPage.RemoveScripts.checked;
	//cookieOpt+="&EncodeUrl="+_XPage.EncodeUrl.checked;
	//cookieOpt+="&EmbedObjects="+_XPage.EmbedObjects.checked;
	
	var dt=new Date();
	dt.setYear(dt.getFullYear()+1);
	
	cookieOpt+=" ;Path=/ ;expires="+dt.toString();
	document.cookie=cookieOpt;
}
function _Page_HandleTextKey(ev){
	var IE=false;
	if(window.event) {ev=window.event;IE=true;}
	if(ev.keyCode==13 || ev.keyCode==10){
		var loc=_XPage.UrlBox.value.toLowerCase();
		if(loc.lastIndexOf('.com')== -1 && loc.lastIndexOf('.net')== -1 && loc.lastIndexOf('.org')== -1){
		if(ev.ctrlKey && ev.shiftKey)
			_XPage.UrlBox.value+='.org';
		else if(ev.ctrlKey)
			_XPage.UrlBox.value+='.com';
		else if(ev.shiftKey)
			_XPage.UrlBox.value+='.net';
		}
		_Page_SubmitForm();		
	}
	return true;
}
function _Page_SubmitForm(){
	_Page_SaveOptions();
	var url=_XPage.UrlBox.value;
	if(url!='') {_Page_Navigate(url); return true;}
	else {alert('Please enter a url.'); return false;}
}

function _Page_Navigate(url){
	var navUrl=_XNav;
	if(_PgOpt.EncodeUrl){
		navUrl+='?dec='+'1'+'&url=';
	    navUrl+=_Base64_encode(_XPage.UrlBox.value)+_Page_B64Unknowner;
	} else {
		navUrl+='?dec='+'0'+'&url=';
	    navUrl+=_XPage.UrlBox.value;
	}
	document.location=navUrl;
}
function _PageOnSubmit(){
	_Page_SubmitForm();
	return false;
}
</script>

<form asproxydone="2" onsubmit="return _PageOnSubmit();" method="post" id="ASProxyMainForm">
<div id="ASProxyMain" dir="ltr">
<table id="ASProxyMainTable" style="width: 100%; ">
<tr><td style="padding:0px; margin:0px;"><table style="width: 100%;border-width:0px;" cellpadding="0" cellspacing="0">
<tr><td class="Sides"><a href="." asproxydone="2">ASProxy 5.2</a></td><td style="font-size:small;"><strong>Surf the web with ASProxy</strong></td><td class="Sides">powered by SalarSoft</td></tr>
</table></td></tr><tr><td><!--This is ASProxy powered by SalarSoft. --><input name="url" type="text" size="60" id="txtUrl" dir="ltr" style="width:450px;" onkeyup="_Page_HandleTextKey(event)" value="http://web.archive.org/web/20080422224810/http://blog.codexpress.cn/javascript/punycode-javascript-converter/"/>
<input type="submit" value="Display" id="btnASProxyDisplayButton" class="Button" style="height: 22px"/>&nbsp;<br />
<span class="ASProxyCheckBox"><input id="chkUTF8" type="checkbox" onclick="_Page_SaveOptions()"/><label for="chkUTF8">Force UTF-8</label></span>&nbsp;<span class="ASProxyCheckBox"><input id="chkOrginalUrl" type="checkbox" checked="checked" onclick="_Page_SaveOptions()"/><label for="chkOrginalUrl">Original URLs</label></span>&nbsp;<span class="ASProxyCheckBox"><input id="chkRemoveScripts" type="checkbox" onclick="_Page_SaveOptions()"/><label for="chkRemoveScripts">Remove scripts</label></span>&nbsp;<span class="ASProxyCheckBox"><input id="chkDisplayImages" type="checkbox" checked="checked" onclick="_Page_SaveOptions()"/><label for="chkDisplayImages">Images</label></span>&nbsp;<span class="ASProxyCheckBox"><input id="chkCookies" type="checkbox" checked="checked" onclick="_Page_SaveOptions()"/><label for="chkCookies">Cookies</label></span>&nbsp;<span class="ASProxyCheckBox"><input id="chkCompression" type="checkbox" onclick="_Page_SaveOptions()"/><label for="chkCompression">Compress response</label></span>&nbsp;<a asproxydone="2" id="lnkMoreOpt" href="javascript:void(0);" onclick="toggleOpt(this);">Options...<small>&gt;</small></a></td>
</tr><tr id="trMoreOpt" style="display: none;"><td id="tdMoreOpt"><span class="ASProxyCheckBox"><input id="chkFrames" type="checkbox" checked="checked" onclick="_Page_SaveOptions()"/><label for="chkFrames">Frames</label></span>&nbsp;<span class="ASProxyCheckBox"><input id="chkPageTitle" type="checkbox" checked="checked" onclick="_Page_SaveOptions()"/><label for="chkPageTitle">Display page title</label></span>&nbsp;<span class="ASProxyCheckBox"><input id="chkForms" type="checkbox" checked="checked" onclick="_Page_SaveOptions()"/><label for="chkForms">Process forms</label></span>&nbsp;<span class="ASProxyCheckBox"><input id="chkProcessLinks" type="checkbox" checked="checked" onclick="_Page_SaveOptions()"/><label for="chkProcessLinks">Links</label></span>&nbsp;<span class="ASProxyCheckBox"><input id="chkTempCookies" type="checkbox" checked="checked" onclick="_Page_SaveOptions()"/><label for="chkTempCookies">Save Cookies as Temp</label></span></td></tr>
<tr><td><a asproxydone="2" href="cookieman.aspx" target="_blank">Cookie Manager</a>&nbsp;&nbsp;<a asproxydone="2" href="download.aspx" target="_blank">Download Tool</a>&nbsp;&nbsp;Have your own <a asproxydone="2" href="?decode=1&amp;url=aHR0cDovL2FzcHJveHkuc291cmNlZm9yZ2UubmV0B64Coded!" target="_blank">ASProxy</a>. It's free.&nbsp;&nbsp;<span id="lblVersionNotifier"></span></td>
</tr></table>
<script type="text/javascript">
_Page_Initialize();
_Page_SetOptions();
</script>

<span title="Error message" style="color:Red; font-weight:bold; font-family:Tahoma; font-size:10pt;"></span>

<noscript style="color:Maroon;font-weight:bold; font-family:Tahoma; font-size:11pt;">Warning, the JavaScript is disabled so this form will not perform any more.<br />Please enable JavaScript or <a asproxydone="2" href="noscript.aspx">use this page</a>.</noscript>
</div></form>
</body></html>

<div style="position: relative; left: 0px; top: 5px; width: 100%; height:auto;">
<script  asproxydone='2'  language='javascript' src='scripts/ajaxwrapper.js'type='text/javascript'></script><script  asproxydone='2'  language='javascript' type='text/javascript'>_userConfig={EncodeUrl:true, OrginalUrl:true, Links:true, Images:true, Forms:true, Frames:true, Cookies:true, RemScripts:false };_reqInfo={pageUrl:"http://web.archive.org/web/20080422224810/http://blog.codexpress.cn/javascript/punycode-javascript-converter/", pageUrlNoQuery:"http://web.archive.org/web/20080422224810/http://blog.codexpress.cn/javascript/punycode-javascript-converter/", pagePath:"http://blog.codexpress.cn.wstub.archive.org/javascript/punycode-javascript-converter/", rootUrl:"http://blog.codexpress.cn.wstub.archive.org/", cookieName:'web.archive.org_Cookie', ASProxyUrl:"https://trust.peras-pm.com/ASProxy/default.aspx", ASProxyPath:"https://trust.peras-pm.com/ASProxy/", ASProxyRoot:"https://trust.peras-pm.com", ASProxyPageName:'default.aspx', UrlUnknowner:'B64Coded!'};_reqInfo.location={ Hash:'', Host:"web.archive.org", Hostname:"web.archive.org", Pathname:"/web/20080422224810/http://blog.codexpress.cn/javascript/punycode-javascript-converter/", Search:"", Port:'80', Protocol:'http' };</script><script  asproxydone='2'  language='javascript' src='scripts/base64encoder.js'type='text/javascript'></script><script  asproxydone='2'  language='javascript' src='scripts/asproxyencoder.js'type='text/javascript'></script><script language='javascript' type='text/javascript'>var _wparent=window.top ? window.top : window.parent;_wparent=_wparent ? _wparent : window;var _document=_wparent.document;var ASProxyOriginalURL=_document.getElementById('__ASProxyOriginalURL');ASProxyOriginalURL.Freeze=false; ASProxyOriginalURL.CurrentUrl=''; var ASProxyUnvisibleHide;function ORG_Position_(){if(!ASProxyOriginalURL)return;var topValue='0';topValue=_document.body.scrollTop+'';if(topValue=='0' || topValue=='undefined')topValue=_wparent.scrollY+'';if(topValue=='0' || topValue=='undefined')topValue=_document.documentElement.scrollTop+'';if(topValue!='undefined')ASProxyOriginalURL.style.top=topValue+'px';}function ORG_IN_(obj){if(!ASProxyOriginalURL || ASProxyOriginalURL.Freeze)return;ORG_Position_();var attrib=obj.attributes['originalurl'];if(attrib!=null)attrib=attrib.value; else attrib=null;if(attrib!='undefined' && attrib!='' && attrib!=null){_wparent.clearTimeout(ASProxyUnvisibleHide);ASProxyOriginalURL.CurrentUrl=''+attrib;ASProxyOriginalURL.innerHTML='URL: <span style="color:maroon;">'+attrib+'</span>';ASProxyOriginalURL.style.visibility='visible';}}function ORG_OUT_(){if(!ASProxyOriginalURL || ASProxyOriginalURL.Freeze)return;ASProxyOriginalURL.innerHTML='URL: ';ASProxyOriginalURL.CurrentUrl='';_wparent.clearTimeout(ASProxyUnvisibleHide);ASProxyUnvisibleHide=_wparent.setTimeout(ORG_HIDE_IT,500);}function ORG_HIDE_IT(){if(ASProxyOriginalURL.Freeze)return;ASProxyOriginalURL.style.visibility='hidden';ASProxyOriginalURL.innerHTML='';}_wparent.onscroll=ORG_Position_;_ASProxy.AttachEvent(document,'keydown',function(aEvent){var ev = window.event ? window.event : aEvent;if(ev.ctrlKey && ev.shiftKey && ev.keyCode==88){if(ASProxyOriginalURL.Freeze){ASProxyOriginalURL.Freeze=false;ORG_HIDE_IT();}else if(ASProxyOriginalURL.CurrentUrl!=''){ASProxyOriginalURL.Freeze=true;ASProxyOriginalURL.innerHTML=ASProxyOriginalURL.innerHTML+"<br /><span style='color:navy;'>Press Ctrl+Shift+X again to unfreeze this bar.<span/>";}}});</script><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
   <BASE HREF="">
</HEAD>


<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Blog@CodeXpress  &raquo; Blog Archive   &raquo; Punycode Javascript Converter</title>

<meta name="generator" content="WordPress 2.3.1" /> <!-- leave this for stats -->

<link rel="stylesheet" href="getany.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTAvaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3RoZW1lcy9pdGhlbWUvc3R5bGUuY3NzB64Coded!" type="text/css" media="all" />
<link rel="stylesheet" href="getany.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTAvaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3RoZW1lcy9pdGhlbWUvcHJpbnQuY3NzB64Coded!" type="text/css" media="print" />

<!-- Sidebar docking boxes (dbx) by Brothercake - http://www.brothercake.com/ -->
<script type="text/javascript" src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3RoZW1lcy9pdGhlbWUvZGJ4LmpzB64Coded!"></script>
<script type="text/javascript" src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3RoZW1lcy9pdGhlbWUvZGJ4LWtleS5qcw==B64Coded!"></script>
<link rel="stylesheet" type="text/css" href="getany.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTAvaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3RoZW1lcy9pdGhlbWUvZGJ4LmNzcw==B64Coded!" media="screen, projection" />

<!--[if lt IE 7]>
<link rel="stylesheet" href="getany.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTAvaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3RoZW1lcy9pdGhlbWUvaWUtZ2lmLmNzcw==B64Coded!" type="text/css" />
<![endif]-->

<link rel="alternate" type="application/rss+xml" title="Blog@CodeXpress RSS Feed" href="getany.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTAvaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9mZWVkLw==B64Coded!" />
<link rel="pingback" href="getany.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTAvaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi94bWxycGMucGhwB64Coded!" />

	<link rel="EditURI" type="application/rsd+xml" title="RSD" href="getany.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTAvaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi94bWxycGMucGhwP3JzZA==B64Coded!" />
 <link rel="wlwmanifest" type="application/wlwmanifest+xml" href="getany.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTAvaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1pbmNsdWRlcy93bHdtYW5pZmVzdC54bWw=B64Coded!" /> <link rel="stylesheet" href="getany.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTAvaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3BsdWdpbnMvY29vbGNvZGUvY29vbGNvZGUuY3NzB64Coded!" />
<script type="text/javascript" src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3BsdWdpbnMvY29vbGNvZGUvY29vbGNvZGUuanM=B64Coded!"></script>
<script src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL21hcHMuZ29vZ2xlLmNvbS9tYXBzP2ZpbGU9YXBpJnY9MiZrZXk9QUJRSUFBQUExajg2dG5VREZ2OE9BdEM4ZFpWdEtSU25RN0MxX1h0Z1pTV3lyNWRpZHNUZm92LWNGUlFvYnJnV0RnUUM5eVNiVG40NXZsdW4xTnRRQUE=B64Coded!" type="text/javascript"></script>
<script type='text/javascript'>
/* <![CDATA[ */
function ImageZoomer(id, zoom, url, max, w, h){
if (GBrowserIsCompatible()){
var copyright = new GCopyright(100, new GLatLngBounds(new GLatLng(-90, -180), new GLatLng(90, 180)), 0, "&copy;2007 ");
var copyrightCollection = new GCopyrightCollection("<a href='http://blog.codexpress.cn' rel='external'>CodeXpress.CN</a>");
copyrightCollection.addCopyright(copyright);
var zoomerTile = new GTileLayer(copyrightCollection, 0, max, {isPng:false, tileUrlTemplate: url});
var tilelayers = [zoomerTile];
var zoomerType = new GMapType(tilelayers, G_NORMAL_MAP.getProjection(), "Power Thumbnail", {errorMessage:"N/A"});
var anchor = document.getElementById(id);
anchor.innerHTML = "";
var style = "";
if(anchor.parentNode.tagName.toLowerCase()=='p'){
	var align = anchor.parentNode.align.toLowerCase();
	style = anchor.parentNode.style.cssText;
	anchor.parentNode.style.cssText = 'display:none;';
	anchor = anchor.parentNode;
}
var canvas = document.createElement('div');
canvas.style.cssText = style;
canvas.style.width = w+"px";
canvas.style.height = h+"px";
switch(align){
case 'center':canvas.style.marginLeft = "auto";
canvas.style.marginRight = "auto";
break;
case 'right':canvas.style.marginLeft = "auto";
canvas.style.marginRight = "0px";
break;
case 'left':canvas.style.marginLeft = "0px";
canvas.style.marginRight = "auto";
break;
}
canvas.style.textAlign = align;
anchor.parentNode.insertBefore(canvas, anchor);
var map = new GMap2(canvas, {mapTypes: [zoomerType]});
map.setCenter(new GLatLng(0, 0), zoom);
map.addControl(new GSmallZoomControl());
GEvent.addDomListener(window, 'unload', function(){GUnload()});
}
}
/* ]]> */
</script>
<!-- WP GuestMap 1.8 -->
<script src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL2oubWF4bWluZC5jb20vYXBwL2dlb2lwLmpzB64Coded!" type="text/javascript"></script>
<script type="text/javascript">
//<![CDATA[
function setCookie(name,value,expireHours){
	var cookieString=name+"="+value;
	if(expireHours>0){
		var date=new Date();
		date.setTime(date.getTime+expireHours*3600*1000);
		cookieString=cookieString+"; expire="+date.toGMTString();
	}
	document.cookie=__CookieSet(cookieString+"; path=/;");
}
setCookie("GeoInfo", [geoip_latitude(), geoip_longitude(), geoip_city(), geoip_country_name(), geoip_region(), geoip_country_code()].join("|" ), "", "/");
document.write("<scr"+"ipt src=\"http://blog.codexpress.cn/wp-content/plugins/wp-guestmap/stats.php\" type=\"text/javascript\"></scr"+"ipt>");
//]]>
</script><style type="text/css">.recentcomments a{display:inline !important;padding: 0 !important;margin: 0 !important;}</style>
</head>
<body>
<div id="page">
  <div id="wrapper">
    <div id="header">
      <h1><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbg==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn" >Blog@CodeXpress</a></h1>
      <div class="description">Simple</div>
      <form method="POST" id="searchform" action="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi8=B64Coded!&amp;method=GET" asproxydone=1  encodedurl="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi8=B64Coded!&amp;method=GET" methodorginal=POST >
<div><input type="text" value="Search" name="s" id="s" onfocus="if (this.value == 'Search') {this.value = '';}" onblur="if (this.value == '') {this.value = 'Search';}" />
<input type="submit" id="searchsubmit" value="Go" />
</div>
</form>
    </div><!-- /header -->

    <div id="left-col">
      <div id="nav">
        <ul>
          <li class="page_item "><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/"  title="Home">Home</a></li>
          <li class="page_item page-item-16"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9hcmNoaXZlLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/archive/"  title="Archive">Archive</a></li>
<li class="page_item page-item-56"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9kb2N4Mmh0bWwtY29udmVydGVyLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/docx2html-converter/"  title="DocX2Html Converter">DocX2Html Converter</a></li>
<li class="page_item page-item-29"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9ndWVzdG1hcC8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/guestmap/"  title="GuestMap">GuestMap</a></li>
          <li class="page_item"><a href="#" onmousedown="this.href = 'mailto:' + ['kuan.jiang', 'gmail.com'].join('@') + '?subject=%5BFrom+Blog%5D'">Contact Me</a></li>
        </ul>

      </div><!-- /nav -->

    	  <div id="content">
  
  <div class="post-nav"> <span class="previous"></span> <span class="next"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9taXNjZWxsYW5lb3VzL2Fub3RoZXItdHJpY2staW4tZ21haWwtYWRkcmVzcy8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/miscellaneous/another-trick-in-gmail-address/" >Another Trick in GMail Address &#8212; NOT Dot-Blindness</a></span></div>
  
  
        <div class="post" id="post-3">
		  <div class="date"><span>Aug</span> 04</div>
		  <div class="title">
          <h2><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L3B1bnljb2RlLWphdmFzY3JpcHQtY29udmVydGVyLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/punycode-javascript-converter/"  rel="bookmark" title="Permanent Link to Punycode Javascript Converter">Punycode Javascript Converter</a> </h2>
          <div class="postdata"><span class="category"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS9qYXZhc2NyaXB0Lw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/javascript/"  title="View all posts in Javascript" rel="category tag">Javascript</a></span> <span class="right mini-add-comment"><a href="#respond">Add comments</a></span></div>

		  </div>


          <div class="entry">
<div style="margin:5px"><strong>Tags: <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi90YWcvYXNjaWkvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/tag/ascii/"  rel="tag">ascii</a>, <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi90YWcvY29udmVydGVyLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/tag/converter/"  rel="tag">converter</a>, <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi90YWcvZGVjb2Rlci8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/tag/decoder/"  rel="tag">decoder</a>, <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi90YWcvZW5jb2Rlci8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/tag/encoder/"  rel="tag">encoder</a>, <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi90YWcvaWRuLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/tag/idn/"  rel="tag">idn</a>, <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi90YWcvaW50ZXJuYXRpb25hbC1kb21haW4tbmFtZS8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/tag/international-domain-name/"  rel="tag">international domain name</a>, <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi90YWcvamF2YXNjcmlwdC8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/tag/javascript/"  rel="tag">Javascript</a>, <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi90YWcvcHVueWNvZGUvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/tag/punycode/"  rel="tag">punycode</a></strong></div>
<p style="text-align:right;">
<script type="text/javascript">
  addthis_url    = 'http%3A%2F%2Fblog.codexpress.cn%2Fjavascript%2Fpunycode-javascript-converter%2F';
  addthis_title  = 'Punycode+Javascript+Converter';
  addthis_pub    = 'kukukuan';
</script><script type="text/javascript" src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL3M3LmFkZHRoaXMuY29tL2pzL2FkZHRoaXNfd2lkZ2V0LnBocD92PTEyB64Coded!" ></script></p>
<p>This javascript class(Punycode Javascript Converter, PJC) is a punycode encoder and decoder for non-ASCII characters. Also, it provides methods to convert between punycode and International Domain Name(IDN). It is written in pure javascript, no server-side support needed; that is to say, no page reload, no AJAX call are needed, everything done on browsers.</p>
<p>If you don&#8217;t know what is punycode, click <a href="#what_is_punycode">here</a></p>
<p><strong>Demonstration</strong><br />
<script type="text/javascript" src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9wcm9kdWN0L3BqYy9qa29kZV9wamMuY29tcHJlc3NlZC5qcw==B64Coded!"></script></p>
<form action="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi53c3R1Yi5hcmNoaXZlLm9yZy9qYXZhc2NyaXB0L3B1bnljb2RlLWphdmFzY3JpcHQtY29udmVydGVyLw==B64Coded!&amp;method=GET" onsubmit="return false;" method=POST  asproxydone=1  encodedurl="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi53c3R1Yi5hcmNoaXZlLm9yZy9qYXZhc2NyaXB0L3B1bnljb2RlLWphdmFzY3JpcHQtY29udmVydGVyLw==B64Coded!&amp;method=GET" methodorginal=POST >
<div style="text-align:center">
<label for="edIDN">Please put IDN below<br />
<input id="edIDN" name="edIDN" type="text" style="width:200px" />
<input type="button" value="Convert to Punycode" style="width:150px" onclick="var punycode =new Punycode(); edPunycode.value=punycode.EncodeDomain(edIDN.value)" /></label><br />
<label for="edPunycode">Please put Punycode below<br />
<input id="edPunycode" name="edPunycode" type="text" style="width:200px" />
<input type="button" value="Convert to IDN" style="width:150px" onclick="var punycode =new Punycode(); edIDN.value=punycode.DecodeDomain(edPunycode.value)" /></label></div>
</form>
<blockquote><p>You may try the domains below or choose your own:<br />
<a href="javascript:void(0)" onclick="document.getElementById('edIDN').value='ä¸­æ–‡.cn'">ä¸­æ–‡.cn</a> and <a href="javascript:void(0)" onclick="document.getElementById('edPunycode').value='xn--fiq228c.cn'">xn--fiq228c.cn</a><br />
<a href="javascript:void(0)" onclick="document.getElementById('edIDN').value='ä¸­æ–‡.ä¸­å›½'">ä¸­æ–‡.ä¸­å›½</a> and <a href="javascript:void(0)" onclick="document.getElementById('edPunycode').value='xn--fiq228c.xn--fiqs8s'">xn--fiq228c.xn--fiqs8s</a><br />
(The two domain names in the same pair is identical. You can copy and paste them to your browser. It&#8217;s best to test it on IE7, and you will find something interesting)
</p></blockquote>
<p><strong>Before you download</strong></p>
<blockquote><p><strong>FOR COMMERCIAL USE:</strong><br />
      Please contact: <a href="mailto:codexpress.cn@gmail.com">codexpress.cn@gmail.com</a></p>
<p><strong>FOR NON-COMMERCIAL USE:</strong><br />
      COMMON CREATIVE (Attribution-NonCommercial-NoDerivs 3.0 Unported)<br />
You are free:<br />
    * to Share - to copy, distribute and transmit the work<br />
    * to Remix - to adapt the work<br />
Under the following conditions:<br />
    * Attribution.<br />
       You must attribute the work in the manner specified by the author or licensor<br />
       (but not in any way that suggests that they endorse you or your use of the work).<br />
    * Noncommercial.<br />
       You may not use this work for commercial purposes.<br />
    * No Derivative Works.<br />
       You may not alter, transform, or build upon this work.<br />
For more details, see<br />
<a href="default.aspx?dec=1&amp;url=aHR0cDovL2NyZWF0aXZlY29tbW9ucy5vcmcvbGljZW5zZXMvYnktbmMtbmQvMy4wLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://creativecommons.org/licenses/by-nc-nd/3.0/"  target="_blank">http://creativecommons.org/licenses/by-nc-nd/3.0/</a></p>
<p>NOTICE:<br />
  * Any business concerned with domain name selling will not be considered<br />
NON-COMMERCIAL.<br />
  * For non-commercial use, please KEEP ALL ABOVE UNTOUCHED.</p></blockquote>
<p><strong>Download</strong></p>
<blockquote><p><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9wcm9kdWN0L3BqYy9qa29kZV9wamMuY29tcHJlc3NlZC5qcw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/product/pjc/jkode_pjc.compressed.js" ><strong>Download Now!</strong></a><br />
(To obtain uncompressed file, please contact <a href="mailto:codexpress.cn@gmail.com">codexpress.cn@gmail.com</a>)</p>
<p><strong>For commercial users, click the BUTTON below. The class will be sent to you after your payment.</strong></p>
<form action="default.aspx?dec=1&amp;url=aHR0cHM6Ly93d3cucGF5cGFsLmNvbS9jZ2ktYmluL3dlYnNjcg==B64Coded!&amp;method=POST" method="POST" asproxydone=1  encodedurl="default.aspx?dec=1&amp;url=aHR0cHM6Ly93d3cucGF5cGFsLmNvbS9jZ2ktYmluL3dlYnNjcg==B64Coded!&amp;method=POST" methodorginal=POST >
<input type="hidden" name="cmd" value="_s-xclick">
<input type="image" src="https://www.paypal.com/en_US/i/btn/x-click-butcc.gif" border="0" name="submit" alt="Make payments with PayPal - it's fast, free and secure!"><img alt="" border="0" src="images.ashx?dec=1&amp;url=aHR0cHM6Ly93d3cucGF5cGFsLmNvbS9lbl9VUy9pL3Njci9waXhlbC5naWY=B64Coded!" width="1" height="1"><br />
<input type="hidden" name="encrypted" value="-----BEGIN PKCS7-----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-----END PKCS7-----"></form>
</blockquote>
<p><strong>Documentation</strong></p>
<blockquote><p>Class Name: Punycode<br />
Class Methods: only four(Encode, Decode, EncodeDomain, DecodeDomain)<br />
    <ins datetime="2007-08-03T19:23:05+00:00">Encode</ins>: pure encoding function, just convert non-ASCII characters to punycode without &#8220;xn--&#8221; prefix.<br />
    <ins datetime="2007-08-03T19:23:05+00:00">Decode</ins>: pure decoding function, just convert punycode non-ASCII characters. It cannot deal with punycode &#8220;xn--&#8221; prefix. If fail, <em>null</em> is returned.<br />
    <ins datetime="2007-08-03T19:23:05+00:00">EncodeDomain</ins>: IDN-to-Punycode function. Of course, the prefix is added.<br />
    <ins datetime="2007-08-03T19:23:05+00:00">DecodeDomain</ins>: Punycode-to-IDN function. If fail, <em>null</em> is returned.</p></blockquote>
<p><strong>How to use</strong></p>
<blockquote><p>First, include the class library first, then create a new instance of Punycode class, and call its member methods. Here is an example:<br />
<coolcode lang="html"><script type="text/javascript" src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L3B1bnljb2RlLWphdmFzY3JpcHQtY29udmVydGVyL2prb2RlX3BqYy5jb21wcmVzc2VkLmpzB64Coded!"></script><br />
<script type="text/javascript">
var pc =new Punycode();</p>
<p>// The result of Encode() does not contain "xn--" prefix
alert(pc.Encode("ä¸­æ–‡"));
// Decode() does not support punycode with "xn--" prefix
alert(pc.Decode("fiqs8s"));</p>
<p>alert(pc.EncodeDomain ("æµ·ä¿¡.cn"));
alert(pc.DecodeDomain ("xn--efv938c.com"));</p>
<p></script>  </coolcode></p></blockquote>
<div id="what_is_punycode"><strong>What is punycode</strong></div>
<blockquote><p>Punycode, defined in RFC, is a self-proclaimed &#8220;Bootstring encoding&#8221; of Unicode strings into the limited character set supported by the Domain Name System. The encoding is used as part of IDNA, which is a system enabling the use of internationalized domain names in all languages supported by Unicode, where the burden of translation lies entirely with the user application (for example, the Web browser).  (From <a href="default.aspx?dec=1&amp;url=aHR0cDovL2VuLndpa2lwZWRpYS5vcmcvd2lraS9QdW55Y29kZQ==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://en.wikipedia.org/wiki/Punycode"  target="_blank">Wikipedia.org</a>)</p></blockquote>
<p style="text-align:left;"><script type="text/javascript">
  addthis_url    = 'http%3A%2F%2Fblog.codexpress.cn%2Fjavascript%2Fpunycode-javascript-converter%2F';
  addthis_title  = 'Punycode+Javascript+Converter';
  addthis_pub    = 'kukukuan';
</script><script type="text/javascript" src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL3M3LmFkZHRoaXMuY29tL2pzL2FkZHRoaXNfd2lkZ2V0LnBocD92PTEyB64Coded!" ></script></p>
<div style="text-align:left;">
<h3>Related Posts</h3><ul class="related_post"><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L3N0cmFuZ2UtYnVncy1vbi1pZTYvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/strange-bugs-on-ie6/"  title="Strange Bugs On IE6">Strange Bugs On IE6</a></li><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L3N3YXAtdHdvLXZhcmlhYmxlcy13aXRob3V0LXVzaW5nLWEtdGVtcG9yYXJ5LXZhcmlhYmxlLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/swap-two-variables-without-using-a-temporary-variable/"  title="Swap Two Variables Without Using A Temporary Variable ">Swap Two Variables Without Using A Temporary Variable </a></li><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L21ha2UtZnVsbC11c2Utb2YtaXNuYW4vB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/make-full-use-of-isnan/"  title="Make Full Use Of isNaN">Make Full Use Of isNaN</a></li><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L2F3ZnVsLXJlZ3VsYXItZXhwcmVzc2lvbnMvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/awful-regular-expressions/"  title="Awful Regular Expressions">Awful Regular Expressions</a></li><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9waHAvY29udmVydC13b3JkLTIwMDctZG9jdW1lbnQtaW50by1odG1sLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/php/convert-word-2007-document-into-html/"  title="Convert Word 2007 Document Into HTML">Convert Word 2007 Document Into HTML</a></li><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L2Z1bGwtcGVybXV0YXRpb24tcmVjdXJzaXZlLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/full-permutation-recursive/"  title="Full Permutation (recursive)">Full Permutation (recursive)</a></li><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L2EtcHVwaWxzLWFyaXRobWV0aWMtcHJvYmxlbS8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/a-pupils-arithmetic-problem/"  title="A Pupil&#8217;s Arithmetic Problem">A Pupil&#8217;s Arithmetic Problem</a></li><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L3VuY29tbW9uLXVzZS1vZi1qYXZhc2NyaXB0LWZ1bmN0aW9ucy8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/uncommon-use-of-javascript-functions/"  title="Uncommon Use Of Javascript Functions">Uncommon Use Of Javascript Functions</a></li><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L2phdmFzY3JpcHQtYXJyYXktY2xvbmUvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/javascript-array-clone/"  title="Javascript Array Clone">Javascript Array Clone</a></li><li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9qYXZhc2NyaXB0L2phdmFzY3JpcHQtdGhlLWNvZGluZy1zdHlsZS8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/javascript/javascript-the-coding-style/"  title="Javascript: The Coding Style?">Javascript: The Coding Style?</a></li></ul>
</div>

			
          </div><!--/entry -->

<div style="width:100%;height:20px"></div>
	
		
<!-- You can start editing here. -->


   
		<!-- If comments are open, but there are no comments. -->

	 


<h3 id="respond">Leave a Reply</h3>


<form action="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb21tZW50cy1wb3N0LnBocA==B64Coded!&amp;method=POST" method="POST" id="commentform" asproxydone=1  encodedurl="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb21tZW50cy1wb3N0LnBocA==B64Coded!&amp;method=POST" methodorginal=POST >

<p><input type="text" name="author" id="author" value="" size="22" tabindex="1" />
<label for="author"><strong>Name</strong> (required)</label></p>

<p><input type="text" name="email" id="email" value="" size="22" tabindex="2" />
<label for="email"><strong>Mail</strong> (will not be published) (required)</label></p>

<p><input type="text" name="url" id="url" value="" size="22" tabindex="3" />
<label for="url"><strong>Website</strong></label></p>


<p><textarea name="comment" id="comment" cols="100%" rows="10" tabindex="4"></textarea></p>

<p><input name="submit" type="submit" id="submit" tabindex="5" value="Submit Comment" />
<input type="hidden" name="comment_post_ID" value="3" />
</p>

</form>


		
			
	</div><!--/post -->

  </div><!--/content -->
  
  <div id="footer"><a href="default.aspx?dec=1&amp;url=aHR0cDovL3d3dy5uZGVzaWduLXN0dWRpby5jb20vcmVzb3VyY2VzL3dwLXRoZW1lcy8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://www.ndesign-studio.com/resources/wp-themes/" >WP Theme</a> &amp; <a href="default.aspx?dec=1&amp;url=aHR0cDovL3d3dy5uZGVzaWduLXN0dWRpby5jb20vc3RvY2staWNvbnMvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://www.ndesign-studio.com/stock-icons/" >Icons</a> by <a href="default.aspx?dec=1&amp;url=aHR0cDovL3d3dy5uZGVzaWduLXN0dWRpby5jb20=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://www.ndesign-studio.com" >N.Design Studio</a></div>
</div><!--/left-col -->

<div class="dbx-group" id="sidebar">

        <!--sidebox start -->
      <div id="meta" class="dbx-box">
        <h3 class="dbx-handle">Meta</h3>
        <div class="dbx-content">
          <ul>
              <li class="rss"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9mZWVkLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/feed/" >Entries (RSS)</a></li>
              <li class="rss"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jb21tZW50cy9mZWVkLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/comments/feed/" >Comments (RSS)</a></li>
              <li class="login"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1sb2dpbi5waHA=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/wp-login.php" >Login</a></li>
              <li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1sb2dpbi5waHA/YWN0aW9uPXJlZ2lzdGVyB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/wp-login.php?action=register" >Register</a></li>              <li class="wordpress"><a href="default.aspx?dec=1&amp;url=aHR0cDovL3d3dy53b3JkcHJlc3Mub3JnB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://www.wordpress.org"  title="Powered by WordPress">WordPress</a></li>
          </ul>
        </div>
      </div>
      <!--sidebox end -->

		<!--sidebox start --><div id="text-2" class="dbx-box widget_text">			<h3 class="dbx-handle">Online Users</h3><div class="dbx-content">			<div class="textwidget"><iframe id="wpgm_frame" src="gethtml.ashx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3BsdWdpbnMvd3AtZ3Vlc3RtYXAvb25saW5lLXRyYWNrZXIucGhwP210PTImZGM9MzAsMCZ6bD0wB64Coded!" asproxydone=1  onload=_ASProxy.Enc.EncodeFrames()  scrolling="no" style="margin:0px" width="190" height="200" frameborder="0"></iframe></div>
		</div></div><!--sidebox end --><!--sidebox start --><div id="categories-1" class="dbx-box widget_categories"><h3 class="dbx-handle">Categories</h3><div class="dbx-content">		<ul>
			<li class="cat-item cat-item-8"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS9maXJlZm94Lw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/firefox/"  title="View all posts filed under Firefox">Firefox</a>
</li>
	<li class="cat-item cat-item-7"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS9mdW5ueS8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/funny/"  title="View all posts filed under Funny">Funny</a>
</li>
	<li class="cat-item cat-item-10"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS9nb29nbGUtbWFwcy8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/google-maps/"  title="View all posts filed under Google Maps">Google Maps</a>
</li>
	<li class="cat-item cat-item-2"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS9qYXZhc2NyaXB0Lw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/javascript/"  title="View all posts filed under Javascript">Javascript</a>
</li>
	<li class="cat-item cat-item-4"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS9taXNjZWxsYW5lb3VzLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/miscellaneous/"  title="View all posts filed under Miscellaneous">Miscellaneous</a>
</li>
	<li class="cat-item cat-item-84"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS9waGlsb3NvcGh5Lw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/philosophy/"  title="View all posts filed under Philosophy">Philosophy</a>
</li>
	<li class="cat-item cat-item-6"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS9waHAvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/php/"  title="View all posts filed under PHP">PHP</a>
</li>
	<li class="cat-item cat-item-1"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS91bmNhdGVnb3JpemVkLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/uncategorized/"  title="View all posts filed under Uncategorized">Uncategorized</a>
</li>
	<li class="cat-item cat-item-9"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS93b3JkcHJlc3MvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/wordpress/"  title="View all posts filed under WordPress">WordPress</a>
</li>
	<li class="cat-item cat-item-88"><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9jYXRlZ29yeS94aHRtbC8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/category/xhtml/"  title="View all posts filed under XHTML">XHTML</a>
</li>
		</ul>
</div></div><!--sidebox end -->		<!--sidebox start --><div id="recent-posts" class="dbx-box widget_recent_entries">			<h3 class="dbx-handle">Recent Posts</h3><div class="dbx-content">			<ul>
						<li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi91bmNhdGVnb3JpemVkL3Bvc3RzLWZyb20tZmxvY2svB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/uncategorized/posts-from-flock/" >Posts from Flock </a></li>
						<li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9mdW5ueS9kby15b3Uta25vdy10aGVpci1uYW1lLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/funny/do-you-know-their-name/" >Do you know their name? </a></li>
						<li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9taXNjZWxsYW5lb3VzL3llc3RlcmRheS1vbmNlLW1vcmUvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/miscellaneous/yesterday-once-more/" >Yesterday Once More </a></li>
						<li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9nb29nbGUtbWFwcy9nYnV0dG9uLWNvbnRyb2wvB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/google-maps/gbutton-control/" >GButton Control </a></li>
						<li><a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93b3JkcHJlc3Mvd3AtZ3Vlc3RtYXAtaTE4bi8=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/wordpress/wp-guestmap-i18n/" >WP GuestMap I18N </a></li>
						</ul>
		</div></div><!--sidebox end -->
		<!--sidebox start --><div id="recent-comments" class="dbx-box widget_recent_comments">			<h3 class="dbx-handle">Recent Comments</h3><div class="dbx-content">			<ul id="recentcomments"><li class="recentcomments">Smalls on <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9maXJlZm94L2ZpcmVmb3gtZXh0ZW5zaW9uLWp0cmFuc2xhdG9yLw==B64Coded!#comment-5044" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/firefox/firefox-extension-jtranslator/#comment-5044" >[Firefox Extension] JTranslator - A Text Translator</a></li><li class="recentcomments"><a href='default.aspx?dec=1&amp;url=aHR0cDovL3d3dy5pa2hpd2EuY29tB64Coded!' asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://www.ikhiwa.com"  rel='external nofollow'>Frank</a> on <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9waHAvd29yZHByZXNzLXBsdWdpbi13cC1ndWVzdG1hcC8=B64Coded!#comment-4955" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/php/wordpress-plugin-wp-guestmap/#comment-4955" >[WordPress Plugin] WP GuestMap</a></li><li class="recentcomments"><a href='default.aspx?dec=1&amp;url=aHR0cDovL3d3dy53ZWJyZWdhcmQuZGUvbmV3cy90YWctY2xvdWQtaW4td29yZHByZXNzLXNlbGJzdC1nZW1hY2h0Lw==B64Coded!' asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://www.webregard.de/news/tag-cloud-in-wordpress-selbst-gemacht/"  rel='external nofollow'>Tag Cloud in Wordpress selbst gemacht | Webregard - Watch the Web</a> on <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9waHAvd29yZHByZXNzLXBsdWdpbi1pbmxpbmUtcGhwLw==B64Coded!#comment-4923" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/php/wordpress-plugin-inline-php/#comment-4923" >[Wordpress Plugin] Inline-PHP</a></li><li class="recentcomments"><a href='default.aspx?dec=1&amp;url=aHR0cDovL3ZpcnR1YWwtbGFiLnJ1LzUzLWRlbGFlbS1zYXRlbGxpdHVyb2stMy8=B64Coded!' asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://virtual-lab.ru/53-delaem-satelliturok-3/"  rel='external nofollow'>Делаем сателлит(Урок 3) - Virtual-lab</a> on <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9waHAvd29yZHByZXNzLXBsdWdpbi1pbmxpbmUtcGhwLw==B64Coded!#comment-4888" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/php/wordpress-plugin-inline-php/#comment-4888" >[Wordpress Plugin] Inline-PHP</a></li><li class="recentcomments"><a href='default.aspx?dec=1&amp;url=aHR0cDovL3d3dy53ZWJyZWdhcmQuZGUvcGVyc29lbmxpY2gvd29yZHByZXNzLXBsdWdpbnMtZHJpdHRlLXJ1bmRlLw==B64Coded!' asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://www.webregard.de/persoenlich/wordpress-plugins-dritte-runde/"  rel='external nofollow'>Wordpress Plugins dritte Runde | Webregard - Watch the Web</a> on <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi9waHAvd29yZHByZXNzLXBsdWdpbi1pbmxpbmUtcGhwLw==B64Coded!#comment-4871" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/php/wordpress-plugin-inline-php/#comment-4871" >[Wordpress Plugin] Inline-PHP</a></li></ul>
		</div></div><!--sidebox end --><!--sidebox start --><div id="archives" class="dbx-box widget_archives"><h3 class="dbx-handle">Archives</h3><div class="dbx-content">		<ul>
			<li><a href='default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi8yMDA4LzAxLw==B64Coded!' asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/2008/01/"  title='January 2008'>January 2008</a></li>
	<li><a href='default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi8yMDA3LzExLw==B64Coded!' asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/2007/11/"  title='November 2007'>November 2007</a></li>
	<li><a href='default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi8yMDA3LzEwLw==B64Coded!' asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/2007/10/"  title='October 2007'>October 2007</a></li>
	<li><a href='default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi8yMDA3LzA5Lw==B64Coded!' asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/2007/09/"  title='September 2007'>September 2007</a></li>
	<li><a href='default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi8yMDA3LzA4Lw==B64Coded!' asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn/2007/08/"  title='August 2007'>August 2007</a></li>
		</ul>
</div></div><!--sidebox end -->		<!--sidebox start --><div id="text-1" class="dbx-box widget_text">			<h3 class="dbx-handle">Collectons</h3><div class="dbx-content">			<div class="textwidget"><div style="text-align:center;">
<a href="default.aspx?dec=1&amp;url=aHR0cDovL3ZhbGlkYXRvci53My5vcmcvY2hlY2s/dXJpPXJlZmVyZXI=B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://validator.w3.org/check?uri=referer" ><img
        src="images.ashx?dec=1&amp;url=aHR0cDovL3d3dy53My5vcmcvSWNvbnMvdmFsaWQteGh0bWwxMC1ibHVlB64Coded!"
        alt="Valid XHTML 1.0 Transitional" height="31" width="88" /></a>
<a href="default.aspx?dec=1&amp;url=aHR0cDovL2ZlZWR2YWxpZGF0b3Iub3JnL2NoZWNrLmNnaT91cmw9aHR0cCUzQS8vZmVlZHMuZmVlZGJ1cm5lci5jb20vY29kZXhwcmVzcw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://feedvalidator.org/check.cgi?url=http%3A//feeds.feedburner.com/codexpress" ><img src="images.ashx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi53c3R1Yi5hcmNoaXZlLm9yZy92YWxpZC1yc3MucG5nB64Coded!" alt="[Valid RSS]" title="Validate my RSS feed" /></a>
<a href="default.aspx?dec=1&amp;url=aHR0cDovL3d3dy5ibG9nY2F0YWxvZy5jb20vZGlyZWN0b3J5B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://www.blogcatalog.com/directory"  title="Internet Blogs - Blog Catalog Blog Directory"><img style="border: 0;" src="images.ashx?dec=1&amp;url=aHR0cDovL3d3dy5ibG9nY2F0YWxvZy5jb20vaW1hZ2VzL2J1dHRvbnMvYmxvZ2NhdGFsb2c1LmdpZg==B64Coded!" alt="Internet Blogs - Blog Catalog Blog Directory"  /></a>

<a href="default.aspx?dec=1&amp;url=aHR0cDovL3d3dy5ibG9nYXJhbWEuY29tB64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://www.blogarama.com" ><img src="images.ashx?dec=1&amp;url=aHR0cDovL3d3dy5ibG9nYXJhbWEuY29tL2ltYWdlcy9idXR0b25fc21fMi5naWY=B64Coded!" border="0" alt="Blogarama - The Blog Directory" /></a>

</div>
<hr />
<div style="text-align:left;">
If no other copyright declaration found,
all the articles in this BLOG are under <a href="default.aspx?dec=1&amp;url=aHR0cDovL2NyZWF0aXZlY29tbW9ucy5vcmcvbGljZW5zZXMvYnktbmMtc2EvMy4wLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://creativecommons.org/licenses/by-nc-sa/3.0/" >Common Creative</a> License.
<div style="text-align:center;"><a rel="license" href="default.aspx?dec=1&amp;url=aHR0cDovL2NyZWF0aXZlY29tbW9ucy5vcmcvbGljZW5zZXMvYnktbmMtc2EvMy4wLw==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://creativecommons.org/licenses/by-nc-sa/3.0/" >
<img alt="Creative Commons License" style="border-width:0" src="images.ashx?dec=1&amp;url=aHR0cDovL2NyZWF0aXZlY29tbW9ucy5vcmcvaW1hZ2VzL3B1YmxpYy9zb21lcmlnaHRzMjAucG5nB64Coded!" title="This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License" />
</a></div>
</div>
<div style="text-align:center;">
Copyright &copy; 2007 <a href="default.aspx?dec=1&amp;url=aHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbg==B64Coded!" asproxydone=1  onmouseout=ORG_OUT_() onmouseover=ORG_IN_(this) originalurl="http://blog.codexpress.cn" >CodeXpress.CN</a>
</div>

</div>
		</div></div><!--sidebox end -->
</div><!--/sidebar -->  
    <hr class="hidden" />

  </div><!--/wrapper -->

</div><!--/page -->

<!-- Google Analytics Tracking by Google Analyticator: http://cavemonkey50.com/code/google-analyticator/ -->
	<script src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL3d3dy5nb29nbGUtYW5hbHl0aWNzLmNvbS91cmNoaW4uanM=B64Coded!" type="text/javascript"></script>
	<script src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL2Jsb2cuY29kZXhwcmVzcy5jbi93cC1jb250ZW50L3BsdWdpbnMvZ29vZ2xlLWFuYWx5dGljYXRvci9nYV9leHRlcm5hbC1saW5rcy5qcw==B64Coded!" type="text/javascript"></script>
	<script type="text/javascript">
		urchin = new urchin();
		urchin.trackDownload = "zip|rar|pdf|jpg|png|gif|jpeg";
		urchin.trackLinks();
		_uacct="UA-284389-8";  urchinTracker();
	</script>
<script src="getjs.ashx?dec=1&amp;url=aHR0cDovL3dlYi5hcmNoaXZlLm9yZy93ZWIvMjAwODA0MjIyMjQ4MTBqc18vaHR0cDovL2ZlZWRzLmZlZWRidXJuZXIuY29tL35zL2NvZGV4cHJlc3M/aT1odHRwOi8vYmxvZy5jb2RleHByZXNzLmNuL3dvcmRwcmVzcy93cC1ndWVzdG1hcC1pMThuLw==B64Coded!" type="text/javascript" charset="utf-8"></script>
</body>

<!-- SOME SCRIPT SRC'S ON THIS PAGE HAVE BEEN REWRITTEN BY THE WAYBACK MACHINE
OF THE INTERNET ARCHIVE IN ORDER TO PRESERVE THE TEMPORAL INTEGRITY OF THE SESSION. -->


<!-- SOME LINK HREF'S ON THIS PAGE HAVE BEEN REWRITTEN BY THE WAYBACK MACHINE
OF THE INTERNET ARCHIVE IN ORDER TO PRESERVE THE TEMPORAL INTEGRITY OF THE SESSION. -->


<SCRIPT language="Javascript">
<!--

// FILE ARCHIVED ON 20080422224810 AND RETRIEVED FROM THE
// INTERNET ARCHIVE ON 20100728090315.
// JAVASCRIPT APPENDED BY WAYBACK MACHINE, COPYRIGHT INTERNET ARCHIVE.
// ALL OTHER CONTENT MAY ALSO BE PROTECTED BY COPYRIGHT (17 U.S.C.
// SECTION 108(a)(3)).

   var sWayBackCGI = "http://web.archive.org/web/20080422224810/";

   function xResolveUrl(url) {
      var image = new Image();
      image.src = url;
      return image.src;
   }
   function xLateUrl(aCollection, sProp) {
      var i = 0;
      for(i = 0; i < aCollection.length; i++) {
         var url = aCollection[i][sProp];         if (typeof(url) == "string") { 
          if (url.indexOf("mailto:") == -1 &&
             url.indexOf("javascript:") == -1
             && url.length > 0) {
            if(url.indexOf("http") != 0) {
                url = xResolveUrl(url);
            }
            url = url.replace('.wstub.archive.org','');
            aCollection[i][sProp] = sWayBackCGI + url;
         }
         }
      }
   }

   xLateUrl(document.getElementsByTagName("IMG"),"src");
   xLateUrl(document.getElementsByTagName("A"),"href");
   xLateUrl(document.getElementsByTagName("AREA"),"href");
   xLateUrl(document.getElementsByTagName("OBJECT"),"codebase");
   xLateUrl(document.getElementsByTagName("OBJECT"),"data");
   xLateUrl(document.getElementsByTagName("APPLET"),"codebase");
   xLateUrl(document.getElementsByTagName("APPLET"),"archive");
   xLateUrl(document.getElementsByTagName("EMBED"),"src");
   xLateUrl(document.getElementsByTagName("BODY"),"background");
   xLateUrl(document.getElementsByTagName("TD"),"background");
   xLateUrl(document.getElementsByTagName("INPUT"),"src");
   var forms = document.getElementsByTagName("FORM");
   if (forms) {
       var j = 0;
       for (j = 0; j < forms.length; j++) {
              f = forms[j];
              if (typeof(f.action)  == "string") {
                 if(typeof(f.method)  == "string") {
                     if(typeof(f.method) != "post") {
                        f.action = sWayBackCGI + f.action;
                     }
                  }
              }
        }
    }


//-->
</SCRIPT>

</html>
</div>
<script type="text/javascript" src="scripts/versionchecker.js"></script>
