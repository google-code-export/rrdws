function zoom(datei,breit,hoch)
{
	big = window.open(datei, breit+"zoomed"+hoch, "width="+breit+",height="+hoch+",resizable=no,menubar=no,status=no,locationbar=no,dependent=yes,scrollbars=no");
	big.focus(); 
}

function zoom_scroll(datei,breit,hoch)
{
	big = window.open(datei, breit+"zOOmed"+hoch, "width="+breit+",height="+hoch+",resizable=no,menubar=no,status=no,locationbar=no,dependent=yes,scrollbars=yes");
	big.focus(); 
}