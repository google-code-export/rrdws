

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>
	ibo Web-Application
</title><link rel="stylesheet" type="text/css" href="../css/main.css" />
    <!--[if IE 6]>
        <style type="text/css">@import url(../css/ie6fix.css);</style>
    <![endif]-->
    <link rel="shortcut icon" type="image/x-icon" href="../favicon.ico" /><link type="text/css" href="../jquery/jquery-ui-1.8.custom.css" rel="stylesheet" />
    <script type="text/javascript" src="../jquery/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="../jquery/jquery-ui-1.8.custom.min.js"></script>
    <script type="text/javascript" src="../jquery/jquery.ui.datepicker-de.js"></script>
    <script type="text/javascript" src="../javascript/WepApp.js"></script>
    <script type="text/javascript" src="../javascript/overlib_mini.js">    </script>
    <script type="text/javascript">
        function myMouseOver(ele) {
            if (ele.id != 'current') {
                ele.className = "menuItemHover";
            }
        };

        function myMouseOut(ele) {
            if (ele.id != 'current') {
                ele.className = "menuItemNormal";
            }
        };

        function myMouseClick(url) {
            window.location.href = url;
        };

        $(function () {
            $("button, input:submit, input:button", "#posMasterWithCopyright").button();
            $(".menuItem").buttonset();
        });
        
    </script>
</head>
<body>
    <div id="overDiv" style="position: absolute; visibility: hidden; z-index: 1000;">
    </div>
    <br />
    <form name="aspnetForm" method="post" action="Uebersicht.aspx" onkeypress="javascript:return WebForm_FireDefaultButton(event, 'ctl00_btnSubmit')" id="aspnetForm">
<div>
<input type="hidden" name="__EVENTTARGET" id="__EVENTTARGET" value="" />
<input type="hidden" name="__EVENTARGUMENT" id="__EVENTARGUMENT" value="" />
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwUKMTE4NTQ0Nzg4OA9kFgJmD2QWAgIDD2QWBAIBD2QWCAIHDw8WAh4EVGV4dAUjR3V0ZW4gVGFnLCA8Yj5HZW5uYWR5IEJlcmV6aG5veTwvYj5kZAIVD2QWBgIBDw8WAh4ISW1hZ2VVcmwFFH4vaW1nL3dhcm5pbmdfMTYucG5nZGQCAw8PFgIfAAVTRXMgaXN0IGVpbmUgUmVnZWwgZGVzIEFid2VzZW5oZWl0c2Fzc2lzdGVudGVuIGFrdGl2LCBzb2xsIGRpZXNlIGRlYWt0aXZpZXJ0IHdlcmRlbj9kZAIFD2QWBGYPDxYCHwEFDX4vaW1nL3llcy5wbmdkZAICDw8WBB8ABQJKYR4HVG9vbFRpcAUCSmFkZAIXD2QWCgIBDw8WAh4HVmlzaWJsZWhkFgJmD2QWAgIBD2QWAgIFD2QWCAIBDw8WAh8ABQJKYWRkAgMPDxYCHwAFBE5laW5kZAIFDw8WAh8ABQJPS2RkAgcPDxYCHwAFCUFiYnJlY2hlbmRkAgMPZBYGZg8PFgIfAAUMTWVpbiBQZWdhc3VzZGQCAQ8PFgIfAQURfi9pbWcvUGVnYXN1cy5wbmdkZAICDw8WAh8ABVFJbiAiTWVpbiBQZWdhc3VzIiBrw7ZubmVuIFNpZSBSZXBvcnRzIGF1ZnJ1ZmVuLCBiZWlzcGllbHN3ZWlzZSBJaHJlIFBlcnNvbmFsYWt0ZS5kZAIFD2QWCGYPDxYCHwAFF2libyBNZWluZSBaZWl0ZXJmYXNzdW5nZGQCAQ8PFgIfAQUXfi9pbWcvWmVpdGVyZmFzc3VuZy5wbmdkZAICDw8WAh8ABXpJbiAiTWVpbmUgWmVpdGVyZmFzc3VuZyIga8O2bm5lbiBTaWUgSWhyZSBaZWl0ZW4gYnVjaGVuLCBLb3JyZWt0dXJlbiBkdXJjaGbDvGhyZW4gdW5kIElociBha3R1ZWxsZXMgWmVpdGd1dGhhYmVuIGVpbnNlaGVuLmRkAgMPZBYMAgEPDxYCHwAFCklociBTdGF0dXNkZAIDDw8WAh8ABSVJaHIgYWt0dWVsbGVyIFN0YXR1czogPGI+QW53ZXNlbmQ8L2I+ZGQCBQ8PFgIfAAUGS29tbWVuZGQCBw8PFgIfAAUwU2llIHNpbmQgYW0gMDguMDkuMjAxMCB1bSAxNjoxNToxMCBVaHIgZ2Vrb21tZW4uZGQCCQ8PFgIfAAUFR2VoZW5kZAILDw8WAh8AZWRkAgcPZBYGZg8PFgIfAAUPaWJvIE1laW4gVXJsYXViZGQCAQ8PFgIfAQUQfi9pbWcvVXJsYXViLnBuZ2RkAgIPDxYCHwAFL0luICJNZWluIFVybGF1YiIga8O2bm5lbiBTaWUgVXJsYXViIGJlYW50cmFnZW4uZGQCCQ8PFgIfA2hkFgZmDw8WAh8ABRJpYm8gTWVpbmUgU2VtaW5hcmVkZAIBDw8WAh8BBRF+L2ltZy9TZW1pbmFyLnBuZ2RkAgIPDxYCHwAFNERhcyBNb2R1bCBcIk1laW5lIFNlbWluYXJlXCIgd3VyZGUgbmljaHQgbGl6ZW56aWVydC5kZAIZDw8WAh8ABR0mY29weTsgMjAxMCBpYm8gU29mdHdhcmUgR21iSGRkAgMPZBYCAgEPDxYCHwEFD34vaW1nL2RlYnVnLnBuZ2RkZCs9hPZ5eqPvMJp6mJ3IySy9PxQz" />
</div>

<script type="text/javascript">
//<![CDATA[
var theForm = document.forms['aspnetForm'];
if (!theForm) {
    theForm = document.aspnetForm;
}
function __doPostBack(eventTarget, eventArgument) {
    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {
        theForm.__EVENTTARGET.value = eventTarget;
        theForm.__EVENTARGUMENT.value = eventArgument;
        theForm.submit();
    }
}
//]]>
</script>


<script src="/WebUrlaub27/WebResource.axd?d=djzOs5Wm2a9qkk2OppjyVA2&amp;t=634177642261875000" type="text/javascript"></script>


<script src="/WebUrlaub27/WebResource.axd?d=QFHj9ij_I5XR8TrutIeZUA2&amp;t=634177642261875000" type="text/javascript"></script>
<div>

	<input type="hidden" name="__PREVIOUSPAGE" id="__PREVIOUSPAGE" value="d6KZXk_iEPRaqfIcqN_K-n5zZT-QhY54G-dvuP_PyRGk0JK9FGcxS0sS4ybd7vMF0" />
	<input type="hidden" name="__EVENTVALIDATION" id="__EVENTVALIDATION" value="/wEWCQK1s+/vBALS09ydCwLYlYPIBwLTqtvKDwLJx4WUCgKjpZfCAQK6jpn1BgKo1umeAwLkgZ2bBR0kY8E+jcNkYTjGlUkr7THJYA9c" />
</div>
    <input type="submit" name="ctl00$btnSubmit" value="Lorem Ipsum" id="ctl00_btnSubmit" class="hidden" />
    <input type="button" name="ctl00$btnForCrossPagePostBack" value="Lorem Ipsum" onclick="javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(&quot;ctl00$btnForCrossPagePostBack&quot;, &quot;&quot;, false, &quot;&quot;, &quot;../Login.aspx&quot;, false, true))" id="ctl00_btnForCrossPagePostBack" class="hidden" />
    <div id="posMasterWithCopyright">
        <div id="posMaster">
            <table style="height: 30px">
                <tr class="bgWhite">
                    <td>
                        
                        &nbsp;
                        <span id="ctl00_lblHeadline">Guten Tag, <b>Gennady Berezhnoy</b></span>
                    </td>
                    <td align="right">
                        
                        <a id="ctl00_lnkBtnPass" title="Passwort ändern" href="javascript:__doPostBack('ctl00$lnkBtnPass','')">
                            &nbsp; &nbsp;
                            <img id="ctl00_imgPass" class="imgButton" src="../img/key.png" style="border-width:0px;" />&nbsp;
                            <span id="ctl00_lblPass">Passwort ändern</span></a>
                        <a id="ctl00_lnkBtnEinstellungen" title="Einstellungen" href="javascript:__doPostBack('ctl00$lnkBtnEinstellungen','')">
                            &nbsp; &nbsp;
                            <img id="ctl00_imgEinstellungen" class="imgButton" src="../img/settings.png" style="border-width:0px;" />&nbsp;
                            <span id="ctl00_lblEinstellungen">Einstellungen</span></a>
                        
                        <a id="ctl00_lnkBtnInfo" title="Information" href="javascript:__doPostBack('ctl00$lnkBtnInfo','')">
                            &nbsp; &nbsp;
                            <img id="ctl00_imgInfo" class="imgButton" src="../img/information.png" style="border-width:0px;" />&nbsp;
                            <span id="ctl00_lblInfo">Information</span></a>
                        <a id="ctl00_lnkBtnExit" title="Logout" href="javascript:__doPostBack('ctl00$lnkBtnExit','')">
                            &nbsp; &nbsp;
                            <img id="ctl00_imgExit" class="imgButton" src="../img/door.png" style="border-width:0px;" />&nbsp;
                            <span id="ctl00_lblExit">Logout</span></a>
                    </td>
                </tr>
            </table>
            <div id="menuLine">
            </div>
            <br />
            <div style="clear: both">
            </div>
            
            
    
    <div id="posMasterBottomSingle">
        <div style="clear: both">
        </div>
        <div id="ctl00_BottomContent_pnlPegasus">
	
            <table class="myTable">
                <tr class="rowFirst">
                    <td colspan="2">
                        <span id="ctl00_BottomContent_lblPegasus">Mein Pegasus</span>
                    </td>
                </tr>
                <tr class="rowAlternate1" onmouseout="this.className='rowAlternate1'" onmouseover="this.className='rowHover'" onclick="myMouseClick('../MeinPegasus/Reports.aspx')" style="cursor: pointer;" >
                    <td style="width: 100px;">
                        <img id="ctl00_BottomContent_imgPegasus" src="../img/Pegasus.png" style="border-width:0px;" />
                    </td>
                    <td>
                        <span id="ctl00_BottomContent_lblPegasusTxt">In "Mein Pegasus" können Sie Reports aufrufen, beispielsweise Ihre Personalakte.</span>
                    </td>
                </tr>
            </table>
            <br />
        
</div>
        <div id="ctl00_BottomContent_pnlZeit">
	
            <table class="myTable">
                <tr class="rowFirst">
                    <td colspan="2">
                        <span id="ctl00_BottomContent_lblZeiterfassung">ibo Meine Zeiterfassung</span>
                    </td>
                </tr>
                <tr class="rowAlternate1" onmouseout="this.className='rowAlternate1'" onmouseover="this.className='rowHover'" onclick="myMouseClick('../MeineZeiterfassung/MeineZeiterfassung.aspx')" style="cursor: pointer;" >
                    <td style="width: 100px;">
                        <img id="ctl00_BottomContent_imgZeiterfassung" src="../img/Zeiterfassung.png" style="border-width:0px;" />
                    </td>
                    <td>
                        <span id="ctl00_BottomContent_lblZeiterfassungTxt">In "Meine Zeiterfassung" können Sie Ihre Zeiten buchen, Korrekturen durchführen und Ihr aktuelles Zeitguthaben einsehen.</span>
                        <div id="ctl00_BottomContent_pnlZeitKommenGehen">
		
                            <table class="myTable" style="width: 85%; float: right;">
                                <tr class="rowLast">
                                    <td>
                                        <span id="ctl00_BottomContent_lblZeitIhrStatus">Ihr Status</span>
                                    </td>
                                </tr>
                                <tr class="rowAlternate1">
                                    <td>
                                        <span id="ctl00_BottomContent_lblZeitStatus">Ihr aktueller Status: <b>Anwesend</b></span>
                                    </td>
                                </tr>
                                <tr class="rowAlternate2">
                                    <td>
                                        <input type="submit" name="ctl00$BottomContent$btnZeitKommen" value="Kommen" id="ctl00_BottomContent_btnZeitKommen" style="width:80px;" />
                                        &nbsp;
                                        <span id="ctl00_BottomContent_lblZeitKommen">Sie sind am 08.09.2010 um 16:15:10 Uhr gekommen.</span>
                                    </td>
                                </tr>
                                <tr class="rowAlternate1">
                                    <td>
                                        <input type="submit" name="ctl00$BottomContent$btnZeitGehen" value="Gehen" id="ctl00_BottomContent_btnZeitGehen" style="width:80px;" />
                                        &nbsp;
                                        <span id="ctl00_BottomContent_lblZeitGehen"></span>
                                    </td>
                                </tr>
                            </table>
                        
	</div>
                    </td>
                </tr>
            </table>
            <br />
        
</div>
        <div id="ctl00_BottomContent_pnlUrlaub">
	
            <table class="myTable">
                <tr class="rowFirst">
                    <td colspan="2">
                        <span id="ctl00_BottomContent_lblUrlaub">ibo Mein Urlaub</span>
                    </td>
                </tr>
                <tr class="rowAlternate1" onmouseout="this.className='rowAlternate1'" onmouseover="this.className='rowHover'" onclick="myMouseClick('../MeinUrlaub/MeinUrlaub.aspx')" style="cursor: pointer;" >
                    <td style="width: 100px;">
                        <img id="ctl00_BottomContent_imgUrlaub" src="../img/Urlaub.png" style="border-width:0px;" />
                    </td>
                    <td>
                        <span id="ctl00_BottomContent_lblUrlaubTxt">In "Mein Urlaub" können Sie Urlaub beantragen.</span>
                    </td>
                </tr>
            </table>
            <br />
        
</div>
        
    </div>

            <div style="clear: both">
            </div>
        </div>
        <div id="txtCopyright">
            <span id="ctl00_lblCopyright">&copy; 2010 ibo Software GmbH</span>
        </div>
    </div>
    <div style="clear: both">
    </div>
    

<script type="text/javascript">
//<![CDATA[
WebForm_AutoFocus('btnSubmit');//]]>
</script>
</form>
    
</body>
</html>
