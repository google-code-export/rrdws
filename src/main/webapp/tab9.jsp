<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<link rel="shortcut icon" type="image/ico" href="favicon.ico" />
		
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
 
					if ( $(event.target.parentNode).hasClass('row_selected') )
						$(event.target.parentNode).removeClass('row_selected');
					else	{
						$(event.target.parentNode).addClass('row_selected');
						$(selecterRRDs).src=event.target;
					}
				}); 
				/* Add a click handler for the delete row */
				$('#delete').click( function() {
					var anSelected = fnGetSelected( oTable );
					// remove backwards!!
					for ( var i=anSelected.length ; i>0 ; i-- ){
						oTable.fnDeleteRow( anSelected[i-1] );
					}
				} );
	
			

				/* Init the table */			
				oTable = $('#example').dataTable( {
					"oLanguage": {
						"sUrl": "table/de_DE.txt"
					}				, 
					"bProcessing": true,
					"sPaginationType": "full_numbers",
					"sAjaxSource": 'listasjson1.jsp' 				
				} );

 
				
			} );
				

			/*
			 * I don't actually use this here, but it is provided as it might be useful and demonstrates
			 * getting the TR nodes from DataTables
			 */
			function fnGetSelected( oTableLocal )
			{
				var aReturn = new Array();
				var aTrs = oTableLocal.fnGetNodes();
				
				for ( var i=0 ; i<aTrs.length ; i++ )
				{
					if ( $(aTrs[i]).hasClass('row_selected') )
					{
						aReturn.push( i );
					}
				}
				return aReturn;
			}
			
		</script>
	</head>
	<body id="dt_example">
		<div id="container"> 
	 
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
			
			<div id="selecterRRDs">
			<p><a href="javascript:void(0)" id="delete">Delete selected row</a></p>
			</div>
			 
	</body>
</html>