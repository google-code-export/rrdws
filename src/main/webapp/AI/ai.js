function initAnno() {
  //alert("annotation will be started for "+ window.location.href+"  ...");
  (function(i, e, f, h, c) {
    var d = f.body,
      g = f.getElementsByTagName("head")[0],
      a = {},
      b;
    b = (function() {
      var j = f.createElement("div"),
        m = "top 0.4s ease-out",
        k = {
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
          WebkitTransition: m,
          MozTransition: m,
          OTransition: m,
          transition: m
        },
        l;
      j.className = "annotator-bm-status";
      for (l in k) {
        if (k.hasOwnProperty(l)) {
          j.style[l] = k[l]
        }
      }
      j.style.position = "fixed";
      j.style.backgroundColor = "rgba(0, 0, 0, 0.9)";
      return {
        status: {
          INFO: "#d4d4d4",
          SUCCESS: "#3665f9",
          ERROR: "#ff7e00"
        },
        element: j,
        show: function(o, n) {
          this.message(o, n);
          j.style.display = "block";
          j.style.visibility = "visible";
          j.style.top = "0";
          return this
        },
        hide: function() {
          j.style.top = "-54px";
          setTimeout(function() {
            j.style.display = "none";
            j.style.visibility = "hidden"
          }, 400);
          return this
        },
        message: function(o, n) {
          n = n || this.status.INFO;
          j.style.borderColor = n;
          j.innerHTML = o;
          return this
        },
        append: function() {
          d.appendChild(j);
          return this
        },
        remove: function() {
          var n = j.parentNode;
          if (n) {
            n.removeChild(j)
          }
          return this
        }
      }.append()
    }());
    a = {
      notification: b,
      keypath: function(j, m, n) {
        var l = (m || "").split("."),
          k;
        while (j && l.length) {
          k = l.shift();
          if (j.hasOwnProperty(k)) {
            j = j[k];
            if (l.length === 0 && j !== c) {
              return j
            }
          } else {
            break
          }
        }
        return (n == null) ? null : n
      },
      config: function(k, l) {
        var j = this.keypath(i, k, l);
        if (j === null) {
          b.show("Sorry there was an error reading the bookmarklet setting for key: " + k, b.status.ERROR);
          setTimeout(b.hide, 3000)
        }
        return j
      },
      loadjQuery: function() {
        var j = f.createElement("script"),
          k = "js/jquery.js";
        j.src = this.config("externals.jQuery", k);
        j.onload = function() {
          h = e.jQuery;
          d.removeChild(j);
          a.load(function() {
            h.noConflict(true);
            a.setup()
          })
        };
        d.appendChild(j)
      },
      load: function(j) {
        g.appendChild(h("<link />", {
          rel: "stylesheet",
          href: this.config("externals.styles")
        })[0]);
        h.getScript(this.config("externals.source"), j)
      },
      storeOptions: function() {
        var j = location.href.split(/#|\?/).shift();
        return {
          prefix: this.config("store.prefix"),
          annotationData: {
            uri: j
          },
          loadFromSearch: {
            uri: j,
            all_fields: 1
          }
        }
      },
      permissionsOptions: function() {
        return h.extend({}, {
          userId: function(j) {
            return j && j.id ? j.id : ""
          },
          userString: function(j) {
            return j && j.name ? j.name : ""
          }
        }, this.config("permissions"))
      },
      setup: function() {
        var j = new Annotator(i.target || d);
//        j.addPlugin("Unsupported").addPlugin("Store", this.storeOptions()).addPlugin("Permissions", this.permissionsOptions()).element.data("annotator:headers", this.config("auth.headers"));
        j.addPlugin("Store", this.storeOptions()).element.data("annotator:headers", this.config("auth.headers"));
        if (this.config("tags") === true) {
          j.addPlugin("Tags")
        }
        h.extend(e._annotator, {
          jQuery: h,
          element: d,
          instance: j,
          Annotator: Annotator.noConflict()
        });
        b.message("Annotator is ready!", b.status.SUCCESS);
        setTimeout(function() {
          b.hide();
          setTimeout(b.remove, 800)
        }, 3000)
      },
      init: function() {
        if (e._annotator.instance) {
          e._annotator.Annotator.showNotification("Annotator is already loaded. Try highlighting some text to get started")
        } else {
          b.show("Loading Annotator into page");
          if (e.jQuery === c || !e.jQuery.sub) {
            this.loadjQuery()
          } else {
            h = e.jQuery.sub();
            this.load(h.proxy(this.setup, this))
          }
        }
      }
    };
    if (!e._annotator) {
      e._annotator = {
        bookmarklet: a
      }
    }
    if (!i.test) {
      a.init()
    }
  }({
    externals: {
      source: "../AI/annotator.js",
      styles: "../AI/annotator.css"
    },
    auth: {
      headers: {
        "X-Annotator-Account-Id": "39fc339cf058bd22176771b3e3029975",
        "X-Annotator-User-Id": "annotator-demo",
        "X-Annotator-Auth-Token": "14afb08d0d3189efb5243896f3712efe58bacf312c5a2250149d3c8d3034cd06"
      }
    },
    tags: true,
    store: {
      prefix: "../Al"
    },
    permissions: {
      showViewPermissionsCheckbox: true,
      showEditPermissionsCheckbox: true,
      user: {
        id: "annotator-demo",
        name: "Annotator Demo"
      },
      permissions: {
        read: ["annotator-demo"],
        update: ["annotator-demo"],
        "delete": ["annotator-demo"],
        admin: ["annotator-demo"]
      }
    }
  }, this, this.document, this.jQuery));
}