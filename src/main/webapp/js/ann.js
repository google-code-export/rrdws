//javascript:(
var APP_BASE = "http://c0824ce0:8080/examples";
//var xxxxx =  function(options, window, document, jQuery, undefined) {
var body = document.body,
  head = document.getElementsByTagName("head")[0],
  bookmarklet = {},
  notification;
notification = function() {
  var element = document.createElement("div"),
    transition = "top 0.4s ease-out",
    styles = {
      display: "block",
      position: "absolute",
      fontFamily: '"Helvetica Neue", Arial, Helvetica, sans-serif',
      fontSize: "14px",
      color: "#fff",
      top: "-54px",
      left: 0,
      width: "100%",
      zIndex: 9999,
      lineHeight: "50px",
      fontSize: "14px",
      textAlign: "center",
      backgroundColor: "#000",
      borderBottom: "4px solid",
      WebkitTransition: transition,
      MozTransition: transition,
      OTransition: transition,
      transition: transition
    },
    property;
  element.className = "annotator-bm-status";
  for (property in styles) if (styles.hasOwnProperty(property)) element.style[property] = styles[property];
  element.style.position = "fixed";
  element.style.backgroundColor = "rgba(0, 0, 0, 0.9)";
  return {
    status: {
      INFO: "#d4d4d4",
      SUCCESS: "#3665f9",
      ERROR: "#ff7e00"
    },
    element: element,
    show: function(message, status) {
      this.message(message, status);
      element.style.display = "block";
      element.style.visibility = "visible";
      element.style.top = "0";
      return this
    },
    hide: function() {
      element.style.top = "-54px";
      setTimeout(function() {
        element.style.display = "none";
        element.style.visibility = "hidden"
      }, 400);
      return this
    },
    message: function(message, status) {
      status = status || this.status.INFO;
      element.style.borderColor = status;
      element.innerHTML = message;
      return this
    },
    append: function() {
      body.appendChild(element);
      return this
    },
    remove: function() {
      var parent = element.parentNode;
      if (parent) parent.removeChild(element);
      return this
    }
  }.append()
}();
bookmarklet = {
  notification: notification,
  keypath: function(object, path, fallback) {
    var keys = (path || "").split("."),
      key;
    while (object && keys.length) {
      key = keys.shift();
      if (object.hasOwnProperty(key)) {
        object = object[key];
        if (keys.length === 0 && object !== undefined) return object
      } else break
    }
    return fallback == null ? null : fallback
  },
  config: function(path, fallback) {
    var value = this.keypath(options, path, fallback);
    if (value === null) {
      notification.show("Sorry there was an error reading the bookmarklet setting for key: " + path, notification.status.ERROR);
      setTimeout(notification.hide, 3E3)
    }
    return value
  },
  loadjQuery: function() {
    var script = document.createElement("script"),
      fallback = //"http://c0824ce0:8080/rrdsaas/F/h_t_t_p_://c0824ce0:8080/rrdsaas/F/h_t_t_p_s_://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.js";
      APP_BASE + "/js/jquery.js";
    script.src = this.config("externals.jQuery", fallback);
    script.onload = function() {
      jQuery = window.jQuery;
      body.removeChild(script);
      bookmarklet.load(function() {
        jQuery.noConflict(true);
        bookmarklet.setup()
      })
    };
    body.appendChild(script)
  },
  load: function(callback) {
    head.appendChild(jQuery("<link />", {
      rel: "stylesheet",
      href: this.config("externals.styles")
    })[0]);
    jQuery.getScript(this.config("externals.source"), callback)
  },
  storeOptions: function() {
    var uri = location.href.split(/#|\?/).shift();
    return {
      prefix: this.config("store.prefix"),
      annotationData: {
        "uri": uri
      },
      loadFromSearch: {
        "uri": uri,
        "all_fields": 1
      }
    }
  },
  permissionsOptions: function() {
    return jQuery.extend({}, {
      userId: function(user) {
        return user && user.id ? user.id : ""
      },
      userString: function(user) {
        return user && user.name ? user.name : ""
      }
    }, this.config("permissions"))
  },
  setup: function() {
    var annotator = new Annotator(options.target || body);
    annotator.addPlugin("Unsupported").addPlugin("Store", this.storeOptions()).addPlugin("Permissions", this.permissionsOptions()).element.data("annotator:headers", this.config("auth.headers"));
    if (this.config("tags") === true) annotator.addPlugin("Tags");
    jQuery.extend(window._annotator, {
      jQuery: jQuery,
      element: body,
      instance: annotator,
      Annotator: Annotator.noConflict()
    });
    notification.message("Annotator is ready!", notification.status.SUCCESS);
    setTimeout(function() {
      notification.hide();
      setTimeout(notification.remove, 800)
    }, 3E3)
  },
  init: function() {
    if (window._annotator.instance) window._annotator.Annotator.showNotification("Annotator is already loaded. Try highlighting some text to get started");
    else {
      notification.show("Loading Annotator into page");
      if (window.jQuery === undefined || !window.jQuery.sub) this.loadjQuery();
      else {
        jQuery = window.jQuery.sub();
        this.load(jQuery.proxy(this.setup, this))
      }
    }
  }
};
if (!window._annotator) window._annotator = {
  bookmarklet: bookmarklet
};
if (!options.test) bookmarklet.init()
})({
  "externals": {
    //"jQuery": "http://c0824ce0:8080/rrdsaas/F/h_t_t_p_://c0824ce0:8080/rrdsaas/F/h_t_t_p_s_://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.js",
    "jQuery": APP_BASE + "/js/jquery.js",
    //"source": "http://c0824ce0:8080/rrdsaas/F/h_t_t_p_://c0824ce0:8080/rrdsaas/F/h_t_t_p_://assets.annotateit.org/annotator/v1.0.0rc3/annotator-full.min.js",
    "source": APP_BASE + "/js/annotator-full.min.js",
    //"styles": "http://c0824ce0:8080/rrdsaas/F/h_t_t_p_://c0824ce0:8080/rrdsaas/F/h_t_t_p_://assets.annotateit.org/annotator/v1.0.0rc3/annotator.min.css"
    "styles": APP_BASE + "/js/annotator.min.css"
  },
  "auth": {
    "headers": {
      "X-Annotator-Account-Id": "39fc339cf058bd22176771b3e3153e6a",
      "X-Annotator-User-Id": "39fc339cf058bd22176771b3e3153e6a",
      "X-Annotator-Auth-Token": "04ad004a6ead3dc45ab82ece11b61252410cd12248226c5c4027104b91a2760b"
    }
  },
  "tags": true,
  "store": {
    //"prefix": "http://c0824ce0:8080/rrdsaas/F/h_t_t_p_://c0824ce0:8080/rrdsaas/F/h_t_t_p_://annotateit.org/api"
    "prefix": APP_BASE + "/api.jsp"
  },
  "permissions": {
    "showViewPermissionsCheckbox": true,
    "showEditPermissionsCheckbox": true,
    "user": {
      "id": "39fc339cf058bd22176771b3e3153e6a",
      "name": "vasja"
    },
    "permissions": {
      "read": [],
      "update": ["39fc339cf058bd22176771b3e3153e6a"],
      "delete": ["39fc339cf058bd22176771b3e3153e6a"],
      "admin": ["39fc339cf058bd22176771b3e3153e6a"]
    }
  }
}
//, this, this.document, this.jQuery);