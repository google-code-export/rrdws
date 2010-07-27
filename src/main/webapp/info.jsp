<html><head><title>Test</title></head>
	<script type="text/javascript" src="/js/zoom.js"> </script>
	<script type="text/javascript" src="/I/l.js"> </script>	
	
	<script type="text/javascript">
	  document.write('<h2> document </h2>');
	  dw('<pre>test documentwrite</pre>');
	  
	  dw('<h2> navigator </h2>');
	  _1('Ihr Browser hat den Spitznamen: <b>'   + navigator.appCodeName + '</b><br>');
      _1('Ihr Browser hat den vollen Namen: <b>'    + navigator.appName + '</b><br>');     
      _1('Ihr Browser hat die folgenden'     + ' Versionsinformationen: <b>'   + navigator.appVersion + '</b><br>');
      _1('Ihr Browser hat aktuell die folgende'    + ' Spracheinstellung: <b>'    + navigator.language + '</b><br>');
      if (navigator.appName == "Microsoft Internet Explorer") {
        _1('Die Sprache im Browser ist: <b>'     + navigator.browserLanguage + '</b><br>'); 
      }
      _1('Ihr Browser läuft auf der Plattform: <b>'   + navigator.platform + '</b><br>');
      _1('Ihr Browser sendet folgende Daten'    + ' im WWW mit: <b>'    + navigator.userAgent + '</b><br>');
      _1('Ihr Browser hat Java aktiviert: <b>'   + navigator.javaEnabled() + '</b><br>');


      _1('<h2> window </h2>');
      _1('Name des Browserfensters: <b>'   + window.name  + '</b><br>');
      _1('Der Text in der Statusleiste des Browserfensters : <b>'   + window.status  + '</b><br>');
	   
      _1('<h2> location </h2>');
      _1('Das verwendete Protokoll: <b>'   + location.protocol  + '</b><br>');
      _1('Name des Servers, von dem die Datei angefordert wurde : <b>'   + location.hostname  + '</b><br>');
      _1('Eine Eigenschaft, die hostname und port in einer Zeichenkette zusammenfasst.: <b>'   + location.host  + '</b><br>');
      _1('Pfad des Dokuments im Verzeichnisbaum des Servers: <b>'   + location.pathname  + '</b><br>');
      _1('Der Port des Servers: <b>'   + location.port  + '</b><br>');
      _1('Alle hinter dem eigentlichen Dateinamen folgenden Parameter: <b>'   + location.search  + '</b><br>');
      _1('Angabe eines möglicherweise übertragenen Ankerverweises mit einem einleitenden #-Zeichen.: <b>'   + location.hash  + '</b><br>');
      _1('Der komplette URL: <b>'   + location.href  + '</b><br>');
      

    </script>
    <body>
    	<a href="javascript:zoom('/info.htm',550,250)">Ansehen</a></strong>
    </body>
</html>