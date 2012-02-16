/*<script type="text/javascript">*/

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

/*</script>*/
