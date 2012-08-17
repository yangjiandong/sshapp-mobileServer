// JavaScript Document
Ext.namespace("Ext.Drawing");
Ext.Drawing.version = 'version 0.0.1';
Ext.Drawing.Shape = function(el, forceNew) {
  this.dom = el;
  Ext.Drawing.Shape.superclass.constructor.call(this);
};
Ext.extend(Ext.Drawing.Shape, Ext.Element);

Ext.Drawing.svgRenderer = function() {
  this.createShape = function(config) {
    var d, s;
    if (!config || !config.type)
      return null;
    else {
      switch (config.type) {
        case "rect" :
          d = document.createElementNS("http://www.w3.org/2000/svg", "rect");
          s = new Ext.Drawing.Shape(d, true);

          s.setWidth = function(w) {
            s.set({
                  width : w
                });
          };
          s.setHeight = function(h) {
            s.set({
                  height : h
                });
          };
          s.setX = function(x) {
            s.set({
                  "x" : x
                });
          };
          s.setY = function(y) {
            s.set({
                  "y" : y
                });
          };
          s.setXY = function(x, y) {
            s.set({
                  "x" : x,
                  "y" : y
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  "x" : x,
                  "y" : y,
                  "width" : w,
                  "height" : h
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.dom.width.baseVal.value;
          };
          s.getHeight = function() {
            return s.dom.height.baseVal.value;
          };
          s.getX = function() {
            return s.dom.x.baseVal.value;
          };
          s.getY = function() {
            return s.dom.y.baseVal.value;
          };
          s.getXY = function() {
            return {
              x : s.dom.x.baseVal.value,
              y : s.dom.y.baseVal.value
            };

          };
          s.getBound = function() {
            return {
              x : s.dom.x.baseVal.value,
              y : s.dom.y.baseVal.value,
              width : s.dom.width.baseVal.value,
              height : s.dom.height.baseVal.value
            };
          };
          s.getLeft = function() {
            s.getX();
          };
          s.getTop = function() {
            s.getY();
          };
          s.getBottom = function() {
            return s.dom.y.baseVal.value + s.dom.height.baseVal.value;
          };
          s.getRight = function(r) {
            return s.dom.x.baseVal.value + s.dom.width.baseVal.value;
          };
          break;
        case "roundrect" :
          d = document.createElementNS("http://www.w3.org/2000/svg", "rect");
          s = new Ext.Drawing.Shape(d, true);

          s.setWidth = function(w) {
            s.set({
                  width : w
                });
          };
          s.setHeight = function(h) {
            s.set({
                  height : h
                });
          };
          s.setX = function(x) {
            s.set({
                  "x" : x
                });
          };
          s.setY = function(y) {
            s.set({
                  "y" : y
                });
          };
          s.setXY = function(x, y) {
            s.set({
                  "x" : x,
                  "y" : y
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  "x" : x,
                  "y" : y,
                  "width" : w,
                  "height" : h
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.dom.width.baseVal.value;
          };
          s.getHeight = function() {
            return s.dom.height.baseVal.value;
          };
          s.getX = function() {
            return s.dom.x.baseVal.value;
          };
          s.getY = function() {
            return s.dom.y.baseVal.value;
          };
          s.getXY = function() {
            return {
              x : s.dom.x.baseVal.value,
              y : s.dom.y.baseVal.value
            };

          };
          s.getBound = function() {
            return {
              x : s.dom.x.baseVal.value,
              y : s.dom.y.baseVal.value,
              width : s.dom.width.baseVal.value,
              height : s.dom.height.baseVal.value
            };
          };
          s.getLeft = function() {
            s.getX();
          };
          s.getTop = function() {
            s.getY();
          };
          s.getBottom = function() {
            return s.dom.y.baseVal.value + s.dom.height.baseVal.value;
          };
          s.getRight = function(r) {
            return s.dom.x.baseVal.value + s.dom.width.baseVal.value;
          };
          if (!Ext.isEmpty(config.rx))
            s.set({
                  rx : config.rx
                });
          if (!Ext.isEmpty(config.ry))
            s.set({
                  ry : config.ry
                });
          break;
        case "circle" :
          d = document.createElementNS("http://www.w3.org/2000/svg", "circle");
          s = new Ext.Drawing.Shape(d, true);
          s.setWidth = function(w) {
            s.set({
                  r : w / 2
                });
          };
          s.setHeight = function(h) {
            var x = s.getX(true);
            s.set({
                  r : h / 2
                });
            s.setX(x);
          };
          s.setX = function(x) {
            s.set({
                  "cx" : x + s.getWidth() / 2
                });
          };
          s.setY = function(y) {
            s.set({
                  "cy" : y + s.getHeight() / 2
                });
          };
          s.setXY = function(x, y) {
            s.set({
                  "cx" : x + getWidth() / 2,
                  "cy" : y + getHeight() / 2
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  "cx" : x + getWidth() / 2,
                  "cy" : y + getHeight() / 2,
                  "r" : w
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.dom.r.baseVal.value * 2;
          };
          s.getHeight = function() {
            return s.dom.r.baseVal.value * 2;
          };
          s.getX = function() {
            return s.dom.cx - s.dom.r.baseVal.value;
          };
          s.getY = function() {
            return s.dom.cy - s.dom.r.baseVal.value;
          };
          s.getXY = function() {
            return {
              x : s.dom.cx - s.dom.r.baseVal.value,
              y : s.dom.cy - s.dom.r.baseVal.value
            };

          };
          s.getBound = function() {
            return {
              x : s.dom.cx - s.dom.r.baseVal.value,
              y : s.dom.cy - s.dom.r.baseVal.value,
              width : s.dom.r.baseVal.value,
              height : s.dom.r.baseVal.value
            };
          };
          s.getLeft = function() {
            return s.getX();
          };
          s.getTop = function() {
            return s.getY();
          };
          s.getBottom = function() {
            return s.dom.r.baseVal.value + s.dom.cy.baseVal.value;
          };
          s.getRight = function(r) {
            return s.dom.r.baseVal.value + s.dom.cx.baseVal.value;
          };
          break;
        case "ellipse" :
          d = document.createElementNS("http://www.w3.org/2000/svg", "ellipse");
          s = new Ext.Drawing.Shape(d, true);
          s.setWidth = function(w) {
            s.set({
                  rx : w / 2
                });
          };
          s.setHeight = function(h) {
            s.set({
                  ry : h / 2
                });
          };
          s.setX = function(x) {
            s.set({
                  "cx" : x + s.getWidth() / 2
                });
          };
          s.setY = function(y) {
            s.set({
                  "cy" : y + s.getHeight() / 2
                });
          };
          s.setXY = function(x, y) {
            s.set({
                  "cx" : x + getWidth() / 2,
                  "cy" : y + getHeight() / 2
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  "cx" : x + getWidth() / 2,
                  "cy" : y + getHeight() / 2,
                  "rx" : w,
                  "ry" : h
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.dom.rx.baseVal.value * 2;
          };
          s.getHeight = function() {
            return s.dom.ry.baseVal.value * 2;
          };
          s.getX = function() {
            return s.dom.cx - s.dom.rx.baseVal.value;
          };
          s.getY = function() {
            return s.dom.cy - s.dom.ry.baseVal.value;
          };
          s.getXY = function() {
            return {
              x : s.dom.cx - s.dom.rx.baseVal.value,
              y : s.dom.cy - s.dom.ry.baseVal.value
            };

          };
          s.getBound = function() {
            return {
              x : s.dom.cx - s.dom.rx.baseVal.value,
              y : s.dom.cy - s.dom.ry.baseVal.value,
              width : s.dom.rx.baseVal.value,
              height : s.dom.ry.baseVal.value
            };
          };
          s.getLeft = function() {
            return s.getX();
          };
          s.getTop = function() {
            return s.getY();
          };
          s.getBottom = function() {
            return s.dom.ry.baseVal.value + s.dom.cy.baseVal.value;
          };
          s.getRight = function(r) {
            return s.dom.rx.baseVal.value + s.dom.cx.baseVal.value;
          };
          break;
        case "line" :
          d = document.createElementNS("http://www.w3.org/2000/svg", "line");
          s = new Ext.Drawing.Shape(d, true);

          s.getMinx = function() {
            if (s.dom.x1.baseVal.value < s.dom.x2.baseVal.value)
              return "x1";
            else
              return "x2";
          };
          s.getMiny = function() {
            if (s.dom.y1.baseVal.value < s.dom.y2.baseVal.value)
              return "y1";
            else
              return "y2";
          };
          s.getMaxx = function() {
            if (s.dom.x1.baseVal.value > s.dom.x2.baseVal.value)
              return "x1";
            else
              return "x2";
          };
          s.getMaxy = function() {
            if (s.dom.y1.baseVal.value > s.dom.y2.baseVal.value)
              return "y1";
            else
              return "y2";
          };
          s.setX1 = function(x) {
            s.set({
                  x1 : x
                });
          }
          s.setX2 = function(x) {
            s.set({
                  x2 : x
                });
          }
          s.setY1 = function(y) {
            s.set({
                  y1 : y
                });
          }
          s.setY2 = function(y) {
            s.set({
                  y2 : y
                });
          }
          s.setWidth = function(w) {
            var xd = w - getWidth();
            x3 = s.dom[s.getMaxx()].baseVal.value + xd;
            var value = {};
            value[s.getMaxx()] = x3;
            s.set(value);
          };
          s.setHeight = function(h) {
            var yd = h - getWidth();
            y3 = s.dom[s.getMaxy()].baseVal.value + yd;
            var value = {};
            value[s.getMaxy()] = y3;
            s.set(value);
          };
          s.setX = function(x) {
            var xd = x - s.dom[s.getMinx()].baseVal.value;
            var x3 = s.dom[s.getMaxx()].baseVal.value + xd;
            var value = {};
            value[s.getMaxx()] = x3;
            value[s.getMinx()] = x;
            s.set(value);
          };
          s.setY = function(y) {
            var yd = y - s.dom[s.getMiny()].baseVal.value;
            var y3 = s.dom[s.getMaxy()].baseVal.value + yd;
            var value = {};
            value[s.getMaxy()] = y3;
            value[s.getMiny()] = y;
            s.set(value);
          };
          s.setXY = function(x, y) {
            var xd = x - s.dom[s.getMinx()].baseVal.value;
            var x3 = s.dom[s.getMaxx()].baseVal.value + xd;
            var yd = y - s.dom[s.getMiny()].baseVal.value;
            var y3 = s.dom[s.getMaxy()].baseVal.value + yd;
            var value = {};
            value[s.getMaxx()] = x3;
            value[s.getMinx()] = x;
            value[s.getMaxy()] = y3;
            value[s.getMiny()] = y;
            s.set(value);

          };
          s.setBound = function(x, y, w, h) {
            var value = {};
            value[s.getMinx()] = x;
            value[s.getMiny()] = y;
            s.set(value);
            var xd = w - getWidth();
            x3 = s.dom[s.getMaxx()].baseVal.value + xd;
            value[s.getMaxx()] = x3;
            var yd = h - getWidth();
            y3 = s.dom[s.getMaxy()].baseVal.value + yd;
            value = {};
            value[s.getMaxx()] = x3;
            value[s.getMaxy()] = y3;
            s.set(value);
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.dom[s.getMaxx()].baseVal.value - s.dom[s.getMinx()].baseVal.value;
          };
          s.getHeight = function() {
            return s.dom[s.getMaxy()].baseVal.value - s.dom[s.getMiny()].baseVal.value;
          };
          s.getX = function() {
            return s.dom[s.getMinx()].baseVal.value;
          };
          s.getY = function() {
            return s.dom[s.getMiny()].baseVal.value;
          };
          s.getXY = function() {
            return {
              x : s.dom[s.getMinx()].baseVal.value,
              y : s.dom[s.getMiny()].baseVal.value
            };

          };
          s.getBound = function() {
            return {
              x : s.dom[s.getMinx()].baseVal.value,
              y : s.dom[s.getMiny()].baseVal.value,
              width : s.dom[s.getMaxx()].baseVal.value - s.dom[s.getMinx()].baseVal.value,
              height : [s.getMaxy()].baseVal.value - s.dom[s.getMiny()].baseVal.value
            };
          };
          s.getLeft = function() {
            return s.getX();
          };
          s.getTop = function() {
            return s.getY();
          };
          s.getBottom = function() {
            return s.dom[s.getMaxy()].baseVal.value;
          };
          s.getRight = function(r) {
            return s.dom[s.getMaxx()].baseVal.value;
          };
          if (!Ext.isEmpty(config.points)) {
            s.setX1(Ext.isEmpty(config.points.x1) ? 0 : config.points.x1);
            s.setX2(Ext.isEmpty(config.points.x2) ? 0 : config.points.x2);
            s.setY1(Ext.isEmpty(config.points.y1) ? 1 : config.points.y1);
            s.setY2(Ext.isEmpty(config.points.y2) ? 1 : config.points.y2);
          } else {
            s.setX1(0);
            s.setX2(0);
            s.setY1(1);
            s.setY2(1);
          }
          break;
        case "polyline" :
          d = document.createElementNS("http://www.w3.org/2000/svg", "polyline");
          s = new Ext.Drawing.Shape(d, true);
          s.getPoints = function() {
            var p = s.dom.getAttribute("points");
            var ps = new Array();
            if (!Ext.isEmpty(p)) {
              var pts = p.split(" ");
              for (var i in pts) {
                ps.push(i.split(","));
              }
            }

            return ps;
          }
          s.addPoint = function(point) {
            var pts = s.dom.getAttribute("points") ? s.dom.getAttribute("points") : "";
            pts += " " + point[0] + "," + point[1];
            s.dom.setAttribute("points", pts);
          }
          s.addPoints = function(points) {
            var l = points.length;
            for (var i = 0; i < l; i++)
              s.addPoint(points[i]);
          }

          if (!Ext.isEmpty(config.points))
            s.addPoints(config.points);
          break;
        case "polygon" :
          d = document.createElementNS("http://www.w3.org/2000/svg", "polygon");
          s = new Ext.Drawing.Shape(d, true);
          s.getPoints = function() {
            var p = s.dom.getAttribute("points");
            var ps = new Array();
            if (!Ext.isEmpty(p)) {
              var pts = p.split(" ");
              for (var i in pts) {
                ps.push(i.split(","));
              }
            }
            return ps;
          }
          s.addPoint = function(point) {
            var pts = s.dom.getAttribute("points") ? s.dom.getAttribute("points") : "";
            pts += " " + point[0] + "," + point[1];
            s.dom.setAttribute("points", pts);
          }
          s.addPoints = function(points) {
            var l = points.length;
            for (var i = 0; i < l; i++)
              s.addPoint(points[i]);
          }

          if (!Ext.isEmpty(config.points))
            s.addPoints(config.points);
          break;
        case "text" :
          d = document.createElementNS("http://www.w3.org/2000/svg", "text");
          d.textContent = config.text;
          s = new Ext.Drawing.Shape(d, true);

          s.setWidth = function(w) {
            s.set({
                  width : w
                });
          };
          s.setHeight = function(h) {
            s.set({
                  height : h
                });
          };
          s.setX = function(x) {
            s.set({
                  "x" : x
                });
          };
          s.setY = function(y) {
            s.set({
                  "y" : y
                });
          };
          s.setXY = function(x, y) {
            s.set({
                  "x" : x,
                  "y" : y
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  "x" : x,
                  "y" : y,
                  "width" : w,
                  "height" : h
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.dom.width.baseVal.value;
          };
          s.getHeight = function() {
            return s.dom.height.baseVal.value;
          };
          s.getX = function() {
            return s.dom.x.baseVal.value;
          };
          s.getY = function() {
            return s.dom.y.baseVal.value;
          };
          s.getXY = function() {
            return {
              x : s.dom.x.baseVal.value,
              y : s.dom.y.baseVal.value
            };

          };
          s.getBound = function() {
            return {
              x : s.dom.x.baseVal.value,
              y : s.dom.y.baseVal.value,
              width : s.dom.width.baseVal.value,
              height : s.dom.height.baseVal.value
            };
          };
          s.getLeft = function() {
            s.getX();
          };
          s.getTop = function() {
            s.getY();
          };
          s.getBottom = function() {
            return s.dom.y.baseVal.value + s.dom.height.baseVal.value;
          };
          s.getRight = function(r) {
            return s.dom.x.baseVal.value + s.dom.width.baseVal.value;
          };
          break;
      }
      if (!Ext.isEmpty(config.fill)) {
        s.set({
              fill : this.createFill(config.fill)
            });
      } else
        s.set({
              fill : "none"
            });
      if (config.stroke) {
        s.set(this.createStroke(config.stroke));
      } else
        s.set({
              stroke : "none"
            });
      return s;
    }
  };
  this.init = function(e, s) {
    var el = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    el.setAttribute("width", 600 + "px");
    el.setAttribute("height", 600 + "px");
    e.insertFirst(el);
    var def = document.createElementNS("http://www.w3.org/2000/svg", "defs");
    el.appendChild(def);
    this.defs = def;
    this.elt = el;
    s.root = el;
    return el;
  };
  this.getRoot = function() {
    return this.elt;
  }
  this.createStroke = function(config) {
    var pen = {};
    if (!Ext.isEmpty(config.color)) {
      pen.stroke = config.color;
    }
    if (!Ext.isEmpty(config.width)) {
      pen["stroke-width"] = config.width;
    }
    if (!Ext.isEmpty(config.linecap)) {
      pen["stroke-linecap"] = config.linecap;
    }
    if (!Ext.isEmpty(config.linejoin)) {
      pen["stroke-linejoin"] = config.linejoin;
    }
    if (!Ext.isEmpty(config.opacity)) {
      pen["stroke-opacity"] = config.opacity;
    }
    return pen;
  }
  this.createFill = function(config) {
    if (typeof config == "string")
      return config;
    else {
      if (!Ext.isEmpty(config.url))
        return "url(#" + config.url + ")";
      else {
        if (config.type == "color" || !Ext.isEmpty(config.color))
          return config.color;
        else if (config.type == "linear") {
          var grad = document.createElementNS("http://www.w3.org/2000/svg", "linearGradient");

          if (Ext.isEmpty())
            grad.id = Ext.id();
          else
            grad.id = config.id;

          if (!Ext.isEmpty(config.x1))
            grad.setAttribute("x1", config.x1);
          if (!Ext.isEmpty(config.x1))
            grad.setAttribute("y1", config.y1);
          if (!Ext.isEmpty(config.x1))
            grad.setAttribute("x2", config.x2);
          if (!Ext.isEmpty(config.x1))
            grad.setAttribute("y2", config.y2);
          Ext.each(config.stops, function(it, ind, ar) {
                var st = document.createElementNS("http://www.w3.org/2000/svg", "stop");
                st.setAttribute("offset", it.offset);
                var style = "stop-color:" + it.color + "; ";
                if (!Ext.isEmpty(it.opacity))
                  style += "stop-opacity:" + it.opacity;
                st.setAttribute("style", style);

                grad.appendChild(st);
              });
          this.defs.appendChild(grad);
          return "url(#" + grad.id + ")";
        } else if (config.type == "radial") {
          var grad = document.createElementNS("http://www.w3.org/2000/svg", "radialGradient");

          if (Ext.isEmpty())
            grad.id = Ext.id();
          else
            grad.id = config.id;

          grad.setAttribute("cx", config.cx);
          grad.setAttribute("cy", config.cy);
          grad.setAttribute("fx", config.fx);
          grad.setAttribute("fy", config.fy);
          grad.setAttribute("r", config.r);
          Ext.each(config.stops, function(it, ind, ar) {
                var st = document.createElementNS("http://www.w3.org/2000/svg", "stop");
                st.setAttribute("offset", it.offset);
                var style = "stop-color:" + it.color + "; ";
                if (!Ext.isEmpty(it.opacity))
                  style += "stop-opacity:" + it.opacity;
                st.setAttribute("style", style);
                grad.appendChild(st);
              });
          this.defs.appendChild(grad);
          return "url(#" + grad.id + ")";
        }
      }
    }

  }
};
Ext.Drawing.vmlRenderer = function() {

  this.createShape = function(config) {
    var d, s;
    if (Ext.isEmpty(config) || Ext.isEmpty(config.type))
      return null;
    else {
      switch (config.type) {
        case "rect" :
          d = this.doc.createElement("v:rect");
          s = new Ext.Drawing.Shape(d, true);
          s.setWidth = function(w) {
            s.setStyle({
                  width : w
                });
          };
          s.setHeight = function(h) {
            s.setStyle({
                  height : h
                });
          };
          s.setX = function(x) {
            s.setStyle({
                  left : x
                });
          };
          s.setY = function(y) {
            s.setStyle({
                  top : y
                });
          };
          s.setXY = function(x, y) {
            s.setStyle({
                  left : x,
                  top : y
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  left : x,
                  top : y,
                  width : w,
                  height : h
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.getStyle("width");
          };
          s.getHeight = function() {
            return s.getStyle("height");
          };
          s.getX = function() {
            return s.getStyle("left");
          };
          s.getY = function() {
            return s.getStyle("top");
          };
          s.getXY = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top")
            };

          };
          s.getBound = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top"),
              width : s.getStyle("width"),
              height : s.getStyle("height")
            };
          };
          s.getLeft = function() {
            s.getX();
          };
          s.getTop = function() {
            s.getY();
          };
          s.getBottom = function() {
            return s.getY() + s.dom.getHeight();
          };
          s.getRight = function(r) {
            return s.getX() + s.dom.getWidth();
          };
          break;
        case "ellipse" :
          d = this.doc.createElement("v:oval");
          s = new Ext.Drawing.Shape(d, true);

          s.setWidth = function(w) {
            s.setStyle({
                  width : w
                });
          };
          s.setHeight = function(h) {
            s.setStyle({
                  height : h
                });
          };
          s.setX = function(x) {
            s.setStyle({
                  left : x
                });
          };
          s.setY = function(y) {
            s.setStyle({
                  top : y
                });
          };
          s.setXY = function(x, y) {
            s.setStyle({
                  left : x,
                  top : y
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  left : x,
                  top : y,
                  width : w,
                  height : h
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.getStyle("width");
          };
          s.getHeight = function() {
            return s.getStyle("height");
          };
          s.getX = function() {
            return s.getStyle("left");
          };
          s.getY = function() {
            return s.getStyle("top");
          };
          s.getXY = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top")
            };

          };
          s.getBound = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top"),
              width : s.getStyle("width"),
              height : s.getStyle("height")
            };
          };
          s.getLeft = function() {
            s.getX();
          };
          s.getTop = function() {
            s.getY();
          };
          s.getBottom = function() {
            return s.getY() + s.dom.getHeight();
          };
          s.getRight = function(r) {
            return s.getX() + s.dom.getWidth();
          };
          break;
        case "circle" :
          d = this.doc.createElement("v:oval");
          s = new Ext.Drawing.Shape(d, true);
          s.setWidth = function(w) {
            s.setStyle({
                  width : w,
                  height : w
                });
          };
          s.setHeight = function(h) {
            s.setStyle({
                  height : h,
                  width : h
                });
          };
          s.setX = function(x) {
            s.setStyle({
                  left : x
                });
          };
          s.setY = function(y) {
            s.setStyle({
                  top : y
                });
          };
          s.setXY = function(x, y) {
            s.setStyle({
                  left : x,
                  top : y
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  left : x,
                  top : y,
                  width : Math.max(w, h),
                  height : Math.max(w, h)
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.getStyle("width");
          };
          s.getHeight = function() {
            return s.getStyle("height");
          };
          s.getX = function() {
            return s.getStyle("left");
          };
          s.getY = function() {
            return s.getStyle("top");
          };
          s.getXY = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top")
            };

          };
          s.getBound = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top"),
              width : s.getStyle("width"),
              height : s.getStyle("height")
            };
          };
          s.getLeft = function() {
            s.getX();
          };
          s.getTop = function() {
            s.getY();
          };
          s.getBottom = function() {
            return s.getY() + s.dom.getHeight();
          };
          s.getRight = function(r) {
            return s.getX() + s.dom.getWidth();
          };
          break;
        case "roundrect" :
          d = this.doc.createElement("v:roundrect");
          s = new Ext.Drawing.Shape(d, true);
          s.setWidth = function(w) {
            s.dom.setStyle({
                  width : w,
                  height : w
                });
          };
          s.setHeight = function(h) {
            s.setStyle({
                  height : h,
                  width : h
                });
          };
          s.setX = function(x) {
            s.setStyle({
                  left : x
                });
          };
          s.setY = function(y) {
            s.setStyle({
                  top : y
                });
          };
          s.setXY = function(x, y) {
            s.setStyle({
                  left : x,
                  top : y
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  left : x,
                  top : y,
                  width : Math.max(w, h),
                  height : Math.max(w, h)
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.getStyle("width");
          };
          s.getHeight = function() {
            return s.getStyle("height");
          };
          s.getX = function() {
            return s.getStyle("left");
          };
          s.getY = function() {
            return s.getStyle("top");
          };
          s.getXY = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top")
            };

          };
          s.getBound = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top"),
              width : s.getStyle("width"),
              height : s.getStyle("height")
            };
          };
          s.getLeft = function() {
            s.getX();
          };
          s.getTop = function() {
            s.getY();
          };
          s.getBottom = function() {
            return s.getY() + s.dom.getHeight();
          };
          s.getRight = function(r) {
            return s.getX() + s.dom.getWidth();
          };
          if (!Ext.isEmpty(config.rx))
            s.setStyle("arcsize", config.rx);
          if (!Ext.isEmpty(config.ry))
            s.setStyle("arcsize", config.ry);
          break;
        case "line" :
          d = this.doc.createElement("v:line");
          d.from = '0,0';
          d.to = '0,0';
          s = new Ext.Drawing.Shape(d, true);
          s.getMinx = function() {
            var x1, x2;
            if (Ext.isEmpty(s.dom.from))
              x1 = 0;
            else if (s.dom.from.indexOf(',') != -1)
              x1 = parseFloat(s.dom.from.split(',')[0]);
            else
              x1 = parseFloat(s.dom.from);

            if (Ext.isEmpty(s.dom.to))
              x2 = 0;
            else if (s.dom.to.indexOf(',') != -1)
              x2 = parseFloat(s.dom.to.split(',')[0]);
            else
              x2 = parseFloat(s.dom.to);

            if (x1 < x2)
              return "x1";
            else
              return "x2";
          };
          s.getMiny = function() {
            var y1, y2;

            if (Ext.isEmpty(s.dom.from) || s.dom.from.indexOf(',') == -1)
              y1 = 0;
            else
              y1 = parseFloat(s.dom.from.split(',')[1]);

            if (Ext.isEmpty(s.dom.to) || s.dom.to.indexOf(',') == -1)
              y2 = 0;
            else
              y2 = parseFloat(s.dom.to.split(',')[1]);

            if (x1 < x2)
              return "x1";
            else
              return "x2";
          };
          s.getMaxx = function() {
            var x1, x2;
            if (Ext.isEmpty(s.dom.from))
              x1 = 0;
            else if (s.dom.from.indexOf(',') != -1)
              x1 = parseFloat(s.dom.from.split(',')[0]);
            else
              x1 = parseFloat(s.dom.from);

            if (Ext.isEmpty(s.dom.to))
              x2 = 0;
            else if (s.dom.to.indexOf(',') != -1)
              x2 = parseFloat(s.dom.to.split(',')[0]);
            else
              x2 = parseFloat(s.dom.to);

            if (x1 > x2)
              return "x1";
            else
              return "x2";
          };
          s.getMaxy = function() {
            var y1, y2;

            if (Ext.isEmpty(s.dom.from) || s.dom.from.indexOf(',') == -1)
              y1 = 0;
            else
              y1 = parseFloat(s.dom.from.split(',')[1]);

            if (Ext.isEmpty(s.dom.to) || s.dom.to.indexOf(',') == -1)
              y2 = 0;
            else
              y2 = parseFloat(s.dom.to.split(',')[1]);

            if (y1 > y2)
              return "y1";
            else
              return "y2";
          };
          s.setX1 = function(x) {
            var y1;

            if (Ext.isEmpty(s.dom.from) || s.dom.from.indexOf(',') == -1)
              y1 = 0;
            else
              y1 = parseFloat(s.dom.from.split(',')[1]);

            var f = x + "," + y1;
            s.set({
                  from : f
                });
          }
          s.setX2 = function(x) {
            var y2;

            if (Ext.isEmpty(s.dom.to) || s.dom.to.indexOf(',') == -1)
              y2 = 0;
            else
              y2 = parseFloat(s.dom.to.split(',')[1]);

            var f = x + "," + y2;
            s.set({
                  to : f
                });
          }
          s.setY1 = function(y) {
            var x1;
            if (Ext.isEmpty(s.dom.from))
              x1 = 0;
            else if (s.dom.from.indexOf(',') != -1)
              x1 = parseFloat(s.dom.from.split(',')[0]);
            else
              x1 = parseFloat(s.dom.from);

            var f = x1 + "," + y;
            s.set({
                  from : f
                });
          }
          s.setY2 = function(y) {
            var x2;
            if (Ext.isEmpty(s.dom.to))
              x2 = 0;
            else if (s.dom.to.indexOf(',') != -1)
              x2 = parseFloat(s.dom.to.split(',')[0]);
            else
              x2 = parseFloat(s.dom.to);

            var f = x2 + "," + y;
            s.set({
                  to : f
                });
          }
          s.setWidth = function(w) {
            var wd = s.getWidth();
            wd = w - wd;
            if (s.getMaxx() == "x1")
              s.setX1(s.getX1() + wd);
            else
              s.setX2(s.getX2() + wd);
          };
          s.setHeight = function(h) {
            var wd = s.getHeight();
            hg = h - hg;
            if (s.getMaxy() == "x1")
              s.setX1(s.getX1() + hg);
            else
              s.setX2(s.getX2() + hg);
          };
          s.setX = function(x) {
            var xd = x - ((s.getMinx() == "x1") ? s.getX1() : s.getX2());
            var x3 = ((s.getMaxx() == "x1") ? s.getX1() : s.getX2()) + xd;
            s.setX1((s.getMaxx() == "x1") ? x3 : x);
            s.setX2((s.getMaxx() == "x2") ? x3 : x);
          };
          s.setY = function(y) {
            var yd = x - ((s.getMiny() == "y1") ? s.getY1() : s.getY2());
            var y3 = ((s.getMaxy() == "y1") ? s.getY1() : s.getY2()) + yd;
            s.setY1((s.getMaxy() == "y1") ? y3 : y);
            s.setY2((s.getMaxy() == "y2") ? y3 : y);
          };
          s.setXY = function(x, y) {
            var xd = x - ((s.getMinx() == "x1") ? s.getX1() : s.getX2());
            var x3 = ((s.getMaxx() == "x1") ? s.getX1() : s.getX2()) + xd;
            s.setX1((s.getMaxx() == "x1") ? x3 : x);
            s.setX2((s.getMaxx() == "x2") ? x3 : x);

            var yd = x - ((s.getMiny() == "y1") ? s.getY1() : s.getY2());
            var y3 = ((s.getMaxy() == "y1") ? s.getY1() : s.getY2()) + yd;
            s.setY1((s.getMaxy() == "y1") ? y3 : y);
            s.setY2((s.getMaxy() == "y2") ? y3 : y);
          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  left : x,
                  top : y,
                  width : w,
                  height : h
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.getStyle("width");
          };
          s.getHeight = function() {
            return s.getStyle("height");
          };
          s.getX = function() {
            return s.getStyle("left");
          };
          s.getY = function() {
            return s.getStyle("top");
          };
          s.getXY = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top")
            };

          };
          s.getBound = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top"),
              width : s.getStyle("width"),
              height : s.getStyle("height")
            };
          };
          s.getLeft = function() {
            s.getX();
          };
          s.getTop = function() {
            s.getY();
          };
          s.getBottom = function() {
            return s.getY() + s.dom.getHeight();
          };
          s.getRight = function(r) {
            return s.getX() + s.dom.getWidth();
          };

          if (!Ext.isEmpty(config.points)) {
            s.setX1(Ext.isEmpty(config.points.x1) ? 0 : config.points.x1);
            s.setX2(Ext.isEmpty(config.points.x2) ? 0 : config.points.x2);
            s.setY1(Ext.isEmpty(config.points.y1) ? 1 : config.points.y1);
            s.setY2(Ext.isEmpty(config.points.y2) ? 1 : config.points.y2);
          } else {
            s.setX1(0);
            s.setX2(0);
            s.setY1(1);
            s.setY2(1);
          }
          break;
        case "polyline" :
          d = this.doc.createElement("v:polyline");
          s = new Ext.Drawing.Shape(d, true);
          s.getPoints = function() {
            var p = s.dom.getAttribute("points");
            var ps = new Array();
            if (!Ext.isEmpty(p)) {
              var pts = p.split(" ");
              for (var i in pts) {
                ps.push(i.split(","));
              }
            }
            return ps;
          }
          s.addPoint = function(point) {
            var pts = s.dom.getAttribute("points") ? s.dom.getAttribute("points") : "";
            pts += " " + point[0] + "," + point[1];
            var p = document.createAttribute("points");
            p.value = pts;
            s.dom.attributes.setNamedItem(p);
          }
          s.addPoints = function(points) {
            var l = points.length;
            for (var i = 0; i < l; i++)
              s.addPoint(points[i]);
          }
          if (!Ext.isEmpty(config.points))
            s.addPoints(config.points);
          break;
        case "text" :
          d = this.doc.createElement("v:sharp");
          t = this.doc.createElement("v:textbox");
          t.innerHTML = config.text;
          d.appendChild(t);
          s = new Ext.Drawing.Shape(d, true);
          s.setWidth = function(w) {
            s.setStyle({
                  width : w
                });
          };
          s.setHeight = function(h) {
            s.setStyle({
                  height : h
                });
          };
          s.setX = function(x) {
            s.setStyle({
                  left : x
                });
          };
          s.setY = function(y) {
            s.setStyle({
                  top : y
                });
          };
          s.setXY = function(x, y) {
            s.setStyle({
                  left : x,
                  top : y
                });

          };
          s.setBound = function(x, y, w, h) {
            s.set({
                  left : x,
                  top : y,
                  width : w,
                  height : h
                });
          };
          s.setLeft = function(l) {
            s.setX(l);
          };
          s.setTop = function(t) {
            s.setY(t);
          };
          s.setBottom = function(b) {
            s.setHeight(b - s.getY());
          };
          s.setRight = function(r) {
            s.setWidth(r - s.getX());
          };

          s.getWidth = function() {
            return s.getStyle("width");
          };
          s.getHeight = function() {
            return s.getStyle("height");
          };
          s.getX = function() {
            return s.getStyle("left");
          };
          s.getY = function() {
            return s.getStyle("top");
          };
          s.getXY = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top")
            };

          };
          s.getBound = function() {
            return {
              x : s.getStyle("left"),
              y : s.getStyle("top"),
              width : s.getStyle("width"),
              height : s.getStyle("height")
            };
          };
          s.getLeft = function() {
            s.getX();
          };
          s.getTop = function() {
            s.getY();
          };
          s.getBottom = function() {
            return s.getY() + s.dom.getHeight();
          };
          s.getRight = function(r) {
            return s.getX() + s.dom.getWidth();
          };
          break;
      }
      s.setStyle("position", "absolute");
      if (!Ext.isEmpty(config.fill)) {
        this.createFill(config.fill, s);
      } else
        s.set({
              fill : "none"
            });
      if (config.stroke) {
        this.createStroke(config.stroke, s);
      } else
        this.createStroke(null, s);
      return s;
    }
  }
  this.init = function(e, s) {
    var iframe = document.createElement("iframe");
    iframe.frameBorder = '0';
    iframe.src = Ext.SSL_SECURE_URL;
    iframe.style.width = "100%";
    iframe.style.height = "100%";
    e.appendChild(iframe);
    this.doc = iframe.contentWindow.document;
    this.doc.open();
    this.doc.write('<html xmlns="http://www.w3.org/1999/xhtml"><head></head><body></body></html>');
    this.doc.close();
    this.doc.namespaces.add("v", "urn:schemas-microsoft-com:vml");
    this.doc.createStyleSheet().addRule("v\\:*", "behavior:url(#default#VML)");
    var task = {
      run : function() {
        if (this.doc.body || this.doc.readyState == 'complete') {
          Ext.TaskMgr.stop(task);
          s.root = this.doc.body;
        }
      },
      interval : 10,
      duration : 10000,
      scope : this
    };
    Ext.TaskMgr.start(task);
  };
  this.getRoot = function() {
    return this.doc.body;
  }
  this.createFill = function(config, o) {
    var f = this.doc.createElement("v:fill");
    o.dom.appendChild(f);
    o.fill = f;
    f.on = true;

    if (typeof config == "string") {
      f.color = config;
      f.type = "solid";
    } else {
      if (!Ext.isEmpty(config.url))
        return "url(#" + config.url + ")";
      else {
        if (config.type == "color"
            || (!Ext.isEmpty(config.color) && !((config.type == "linear") || (config.type == "radial")))) {
          f.color = config.color;
          f.type = "solid";
        } else if (config.type == "linear") {
          var stops = "";
          Ext.each(config.stops, function(it, ind, ar) {
                if (ind != 0 && ind != ar.length - 1)
                  stops += it.offset * 100 + "% " + it.color + ",";
              });
          f.colors = stops;
          if (Ext.isArray(config.stops)) {
            f.color = config.stops[0].color;
            if (Ext.isEmpty(config.stops[0].opacity))
              f.opacity = config.stops[0].opacity;
            f.color2 = config.stops[config.stops.length - 1].color;
            if (Ext.isEmpty(config.stops[config.stops.length - 1].opacity))
              f.opacity2 = config.stops[config.stops.length - 1].opacity;
          }
          f.type = "gradient";
        } else if (config.type == "radial") {
          var stops = "";
          Ext.each(config.stops, function(it, ind, ar) {
                stops += it.offset * 100 + "% " + it.color + ";"
              });
          f.colors = stops;
          f.type = "gradientradial";
          f.method = "linear";
        }
      }
    }
  };
  this.createStroke = function(config, o) {
    var f = this.doc.createElement("v:stroke");
    o.dom.appendChild(f);
    o.stroke = f;
    if (Ext.isEmpty(config)) {
      f.on = false;
      return f;
    }
    f.on = true;
    if (!Ext.isEmpty(config.color)) {
      f.color = config.color;
    }
    if (!Ext.isEmpty(config.width)) {
      f.weight = config.width;
    }
    if (!Ext.isEmpty(config.linecap)) {
      f.endcap = config.linecap;
    }
    if (!Ext.isEmpty(config.linejoin)) {
      f.joinStyle = config.linejoin;
    }
    if (!Ext.isEmpty(config.opacity)) {
      f.opacity = config.opacity;
    }
  };

};
Ext.Drawing.Surface = function(config) {
  if (!Ext.isEmpty(config)) {
    switch (config.render) {
      case "svg" :
        this.render = new Ext.Drawing.svgRenderer();
        break;
      case "vml" :
        this.render = new Ext.Drawing.vmlRenderer();
        break;
      default :
        if (Ext.isIE)
          this.render = new Ext.Drawing.vmlRenderer();
        else
          this.render = new Ext.Drawing.svgRenderer();
    }
  } else {
    if (Ext.isIE)
      this.render = new Ext.Drawing.vmlRenderer();
    else
      this.render = new Ext.Drawing.svgRenderer();
  }
  this.render.init(config.El, this);
}
