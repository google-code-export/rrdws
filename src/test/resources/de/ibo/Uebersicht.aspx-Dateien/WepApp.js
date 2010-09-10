$(function () {
    $("#ctl00_ctl00_BottomContent_PageContent_datepicker").datepicker({ showOn: 'button', buttonImage: '../img/calendar_year.png', buttonImageOnly: true });
    $("#ctl00_ctl00_BottomContent_PageContent_datepickerVon").datepicker({ showOn: 'button', buttonImage: '../img/calendar_year.png', buttonImageOnly: true });
    $("#ctl00_ctl00_BottomContent_PageContent_datepickerBis").datepicker({ showOn: 'button', buttonImage: '../img/calendar_year.png', buttonImageOnly: true });
});

function getIEVersion() {
    var rv = -1;
    if (navigator.appName == 'Microsoft Internet Explorer') {
        var ua = navigator.userAgent;
        var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
        if (re.exec(ua) != null) {
            rv = parseFloat(RegExp.$1);
        }
    }
    return rv;
};

function getDocumentVersion() {
    // ab IE8
    return document.documentMode;
}

// string2date(string)
//
// string => TT.MM.YYYY HH:MM:SS
// Das Trennzeichen kann ein: "./-" sein
// prüft einen String auf ein gültiges Datum
// gibt ein Datum Objekt oder null zurück
// Quelle: http://javascript.jstruebig.de/javascript/39/
function string2date(string) {
    if (!string)
        return null;

    string += '';
    string = string.replace(/[-\/]/g, '.'); // Trennzeichen normalisieren
    string = string.replace(/[^0-9.: ]/g, ''); // ungültige Zeichen entfernen
    string = string.replace(/ +/g, ' '); // doppelte Leerzeichen entfernen
    var split = string.split(".");
    var day = parseInt(split[0], 10);
    var month = parseInt(split[1] || 0, 10);
    var year = parseInt(split[2] || 0, 10);

    var check = new Date(year, month - 1, day);

    var day2 = check.getDate();
    var year2 = getFullYear(check);
    var month2 = check.getMonth() + 1;

    return (year2 == year && month == month2 && day == day2) ? check : null;
}

function getFullYear(date) {
    if (!date)
        date = new Date();
    if (typeof date.getFullYear != 'undefined')
        return date.getFullYear();
    var year = date.getYear();
    if (year < 1000)
        year += 2000;
    return year;
}

function getDatum(date) {
    var result = '';
    if (date.getDate() < 10)
        result += '0';
    result += date.getDate();
    result += '.';
    if (date.getMonth() + 1 < 10)
        result += '0';
    result += date.getMonth() + 1;
    result += '.';
    result += getFullYear(date);
    return result;
}

function getCSS(element) {
    if (window.getComputedStyle) { // Standardkonforme Browser
        return window.getComputedStyle(element, null);
    } else if (element.currentStyle) { // IE
        return element.currentStyle;
    }
}

function showToolTip(txt, x, y) {
    if (txt != "" && txt != null) {
        $("#toolTip").removeClass();
        $("#toolTip").addClass("toolTipVisible");
        $('#toolTip').html(txt);
        $('#toolTip').css("left", x + 20);
        $('#toolTip').css("top", y + 10);
    }
    else {
        hideToolTip();
    }
}

function hideToolTip() {
    $("#toolTip").removeClass();
    $("#toolTip").addClass("toolTipHidden");
}