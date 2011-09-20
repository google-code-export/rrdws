;(function ($) {

  var annotations = []

  $.mockjax({
    url: 'http://c0824ce0:8080/examples/api.jsp?we=qwe&',
    contentType: 'text/json',
    responseText: annotations
  })


})(jQuery)

