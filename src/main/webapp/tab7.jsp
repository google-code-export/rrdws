<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<link rel="shortcut icon" type="image/ico" href="http://www.sprymedia.co.uk/media/images/favicon.ico" />
		
		<title>DataTables example</title>
		<style type="text/css" title="currentStyle">
			@import "table/demo_page.css";
			@import "table/demo_table.css";
		</style>
		<script type="text/javascript" language="javascript" src="table/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="table/jquery.dataTables.js"></script> 
		<script type="text/javascript" charset="utf-8">
			var oTable;
			var giRedraw = false;
			$(document).ready(function() {
			
				/* Add a click handler to the rows - this could be used as a callback */
				$("#example tbody").click(function(event) {
					$(oTable.fnSettings().aoData).each(function (){
						$(this.nTr).removeClass('row_selected');
					});
					$(event.target.parentNode).addClass('row_selected');
				});
				
				/* Add a click handler for the delete row */
				$('#delete').click( function() {
					var anSelected = fnGetSelected( oTable );
					oTable.fnDeleteRow( anSelected[0] );
				} );
				
				/* Init the table */			
				oTable = $('#example').dataTable( {
					"bProcessing": true,
					"sAjaxSource": 'listasjson1.jsp'
				} );
			} );
			
			/* Get the rows which are currently selected */
			function fnGetSelected( oTableLocal )
			{
				var aReturn = new Array();
				var aTrs = oTableLocal.fnGetNodes();
				
				for ( var i=0 ; i<aTrs.length ; i++ )
				{
					if ( $(aTrs[i]).hasClass('row_selected') )
					{
						aReturn.push( aTrs[i] );
					}
				}
				return aReturn;
			}
		</script>
	</head>
	<body id="dt_example">
		<div id="container"> 
			<p><a href="javascript:void(0)" id="delete">Delete selected row</a></p>
			 
			<div id="dynamic">
<table cellpadding="0" cellspacing="0" border="0" class="display" id="example">
	<thead>
		<tr>
			<th width="1%">C</th>
			<th width="1%">#</th>
			<th width="1%">DB-name</th>

			<th width="95%">xpath</th>
			<th width="1%">rrd</th>
		</tr>
	</thead>
	<tbody>
		
	</tbody>
	<tfoot>
		<tr>

			<th>Rendering engine</th>
			<th>Browser</th>
			<th>Platform(s)</th>
			<th>Engine version</th>
			<th>CSS grade</th>
		</tr>

	</tfoot>
</table>
			</div>
			 
	</body>
</html>