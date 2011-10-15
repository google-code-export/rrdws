

var Browser=new Object();
var ua=navigator.userAgent.toLowerCase();
Browser.isMozilla=(typeof document.implementation!='undefined')&&(typeof document.implementation.createDocument!='undefined')&&(typeof HTMLDocument!='undefined');
Browser.isIE=window.ActiveXObject?true:false;
Browser.isFirefox=(ua.indexOf("firefox")!=-1);
Browser.isSafari=(ua.indexOf("Browser.isSafari")!=-1);
Browser.isOpera=(typeof window.opera!='undefined');

if(Browser.isMozilla)
{
    HTMLElement.prototype.removeNode=function(){this.parentNode.removeChild(this);}
}


function $$(e){return document.createElement(e);}
function $(id){if (document.getElementById) return document.getElementById(id); else if (document.all) return document.all[id]; else return null;}
function $hide(e){if(typeof e=="object")e.style.visibility="hidden";else if($(e)!=null)$(e).style.visibility="hidden";}
function $visible(e){if(typeof e=="object")e.style.visibility="";else if($(e)!=null)$(e).style.visibility="";}
function T(e,text){var item;if(typeof e=="object")item=e;else item=$(e);if(Browser.isFirefox)item.textContent=text;else item.innerText=text;}
function $T(e){var item;if(typeof e=="object")item=e;else item=$(e);if(Browser.isFirefox)return item.textContent;else return item.innerText;}
function $trimlast(s) { return s.substr(0, s.length - 1); }

function $event(obj,name,f)
{
    if(window.attachEvent) obj.attachEvent(name, f);
    if(window.addEventListener) obj.addEventListener(name, f, false);
}

function getOptionSelected(sel)
{
  var els;
  if (typeof sel=="object") els=sel; else els = $(sel);
  var i;
  for (i = els.length - 1; i>=0; i--) {
    if (els.options[i].selected) {
      return els.options[i].value;
    }
  }
  return null;
}

function selectOption(sel,v)
{
  var els;
  if (typeof sel=="object") els=sel; else els = $(sel);
  var i;
  for (i = els.length - 1; i>=0; i--) {
    if (els.options[i].value==v) {
      els.options[i].selected=true;
      break;
    }
  }
}

function fixEvent(ev) {
    if (!ev) ev = window.event;
    if (ev.target) {
        if (ev.target.nodeType == 3)
            ev.target = ev.target.parentNode;
    }
    else if (ev.srcElement) {
        ev.target = ev.srcElement;
    }
    return ev;
}

function deleteChildren(e) {
    var item; if (typeof e == "object") item = e; else item = $(e);
    while (item.childNodes.length) {
        item.removeChild(item.firstChild)
    }
}
//property
function getProperty(olist, n) {
    var cnt = 0;
    for (var propertyName in olist) {
        if (cnt == n) return olist[propertyName];
        cnt++;
    }
    return null;
}
function propCount(olist) {
    var cnt = 0;
    for (var propertyName in olist)
        cnt++;
    return cnt;
}


function getMouseX(e) {
    var posx = 0;
    if (e.pageX || e.pageY) posx = e.pageX;
    else if (e.clientX || e.clientY)
        posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
    return posx;
}

function getMouseY(e) {
    var posy = 0;
    if (e.pageX || e.pageY) posy = e.pageY;
    else if (e.clientX || e.clientY)
        posy = e.clientY + document.body.scrollTop
			+ document.documentElement.scrollTop;
    return posy;
}

