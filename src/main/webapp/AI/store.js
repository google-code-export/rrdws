;(function ($) {

  var annotations = []

  $.mockjax({
    url: 'http://rrd.llabor.co.cc/api.jsp?we=qwe&',
    contentType: 'text/json',
    responseText: annotations
  })


})(jQuery)

