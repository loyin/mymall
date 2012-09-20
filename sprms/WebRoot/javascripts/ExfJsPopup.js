(function($) {
    function ExfJsPopup(config, id) {
        if ($.inArray(id, $.exfpopup.popupArray) == -1) {
            $.exfpopup.popupArray.push(id)
        }
        if ($("#ExfPopup_" + id).size() > 0) {
            var obj = eval(id);
            obj.isopen = true;
            if ($.isPlainObject(config)) {
                obj.config = {};
                $.extend(obj.config, config)
            }
            return obj
        } else {
            this.isopen = true
        }
        this.IsIE6 = ($.browser.msie && $.browser.version == "6.0") ? true: false;
        this._getScroll = function() {
            var w3c = (document.getElementById) ? true: false;
            var agt = navigator.userAgent.toLowerCase();
            var ie = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1) && (agt.indexOf("omniweb") == -1));
            var IeTrueBody = (document.compatMode && document.compatMode != "BackCompat") ? document.documentElement: document.body;
            var o = new Object();
            o.left = ie ? IeTrueBody.scrollLeft: window.pageXOffset;
            o.top = ie ? IeTrueBody.scrollTop: window.pageYOffset;
            o.width = ie ? IeTrueBody.scrollWidth: window.pageXWidth;
            o.height = ie ? IeTrueBody.scrollHeight: window.pageXHeight;
            return o
        };
        this.config = {};
        if ($.isPlainObject(config)) {
            $.extend(this.config, config)
        }
        this.id = id;
        this._onCreate();
        this._isbutton = false
    }
    ExfJsPopup.prototype._onCreate = function() {
        var $this = this;
        this.targetbody = $("#span_ExfJsPopup");
        this.body = $('<div class="popup_body" id="ExfPopup_' + this.id + '"><div class="popup_mask"></div><div class="popup_main"><table class="popup_table"></table></div></div>').appendTo(this.targetbody);
        this.mask = this.body.children(".popup_mask");
        this.popup = this.body.children(".popup_main");
        this.table = this.popup.children(".popup_table");
        this.popup_header = $('<tr class="popup_header"><td class="exf_left"><div style="width:13px;"></div></td><td class="exf_center" valign="top"><td class="exf_right"><div style="width:13px;"></div></td></tr>').appendTo(this.table);
        this.popup_header_title = $('<div class="title"><span></span></div>').appendTo(this.popup_header.children(".exf_center"));
        this.popup_header_tools = $('<div class="tools"></div>').appendTo(this.popup_header.children(".exf_center"));
        this.popup_header_tools_max = $('<span class="max" title="\u6700\u5927\u5316/\u8fd8\u539f"></span>').appendTo(this.popup_header_tools);
        this.popup_header_tools_close = $('<span class="close" title="\u5173\u95ed"></span>').appendTo(this.popup_header_tools);
        this.popup_content = $('<tr class="popup_content"><td class="exf_left"></td><td class="exf_center" valign="top"></td><td class="exf_right"></td></tr>').appendTo(this.table);
        this.popup_content_note = $('<div class="exf_note"><img width="32px" height="32px" /><div class="notetext"><span class="title"></span><span class="msg"></span></div></div>').appendTo(this.popup_content.children(".exf_center"));
        this.popup_content_body = $('<div class="exf_content"></div>').appendTo(this.popup_content.children(".exf_center"));
        this.popup_content_buttons = $('<div class="exf_buttons">').appendTo(this.popup_content.children(".exf_center"));
        this.popup_bottom = $('<tr class="popup_bottom"><td class="exf_left"></td><td class="exf_center"></td><td class="exf_right"></td></tr>').appendTo(this.table);
        this.popup_header_tools_max.hover(function() {
            if ($(this).data("enabled") == true) {
                $this._istoolsdown = true;
                $(this).parent().addClass("focusB")
            }
        },
        function() {
            if ($(this).data("enabled") == true) {
                $this._istoolsdown = false;
                $(this).parent().removeClass("focusB")
            }
        });
        this.popup_header_tools_close.hover(function() {
            if ($(this).data("enabled") != false) {
                $this._istoolsdown = true;
                $(this).parent().addClass("focusR").parents("tr").children(":last").attr("class", "exf_rightfocus")
            }
        },
        function() {
            if ($(this).data("enabled") != false) {
                $this._istoolsdown = false;
                $(this).parent().removeClass("focusR").parents("tr").children(":last").attr("class", "exf_right")
            }
        });
        this.popup_header_tools_close.bind("click", this,
        function(e) {
            if ($(this).data("enabled") != false) {
                e.data._onHide(e)
            }
        });
        this.popup_header_tools_max.bind("click", this, this._onMax);
        this.popup_header.children(".exf_center").bind("dblclick", this, this._onMax);
        this.mask.bind("selectstart", this._onNotSelect);
        this.popup_header.bind("selectstart", this._onNotSelect);
        this.popup_content_note.bind("selectstart", this._onNotSelect);
        this.popup_content_buttons.bind("selectstart", this._onNotSelect);
        this.popup_content.children(".exf_right").bind("selectstart", this._onNotSelect);
        this.popup_bottom.bind("selectstart", this._onNotSelect);
        this.popup_bottom.children(".exf_right").bind("selectstart", this._onNotSelect);
        this.popup_bottom.children(".exf_center").bind("selectstart", this._onNotSelect);
        this.popup_content.children(".exf_right").bind("selectstart", this._onNotSelect);
        this.popup_header.children(".exf_center").bind("mousedown", this, this._onMoveStart)
    };
    ExfJsPopup.prototype._onNotSelect = function() {
        return false
    };
    ExfJsPopup.prototype._onMoveStart = function(e) {
        $this = e.data;
        if ($this.config.ismove != true) {
            return
        }
        if ($this._ismax == true) {
            return
        }
        if ($this._istoolsdown == true) {
            return
        }
        $this._isdown = true;
        $this.popup_moveline = $("<div/>", {
            "class": "popup_moveline",
            css: ({
                display: "none",
                width: $this.table.width() - 10,
                height: $this.table.height() - 10,
                left: $this.popup.offset().left + 10,
                top: $this.popup.offset().top + 10
            })
        }).appendTo($this.body);
        $(document).bind("selectstart", $this._onNotSelect);
        var scroll = $this._getScroll();
        var offset = $this.popup.offset();
        var obj = {
            obj: $this,
            left: e.clientX - offset.left + scroll.left,
            top: e.clientY - offset.top + scroll.top
        };
        if ($this.IsIE6) {
            obj.left -= scroll.left;
            obj.top -= scroll.top
        }
        $(document).bind("mousemove", obj, $this._onMoveIng);
        $(document).bind("mouseup", $this, $this._onMoveEnd)
    };
    ExfJsPopup.prototype._onMoveIng = function(e) {
        $this = e.data.obj;
        if ($this._isdown != true) {
            return
        }
        if ($this.popup_moveline.css("display") == "none") {
            $this.popup_moveline.show()
        }
        $this._ismove = true;
        var x = e.clientX - e.data.left + 5;
        var y = e.clientY - e.data.top + 5;
        $this.popup_moveline.css({
            top: y,
            left: x
        })
    };
    ExfJsPopup.prototype._onMoveEnd = function(e) {
        $this = e.data;
        $(document).unbind("mousemove", $this._onMoveIng);
        $(document).unbind("mouseup", $this._onMoveEnd);
        $(document).unbind("selectstart", $this._onNotSelect);
        $this._isdown = false;
        $this.popup.stop();
        if ($this._ismove != true) {
            return
        }
        $this._ismove = false;
        var offset = $this.popup_moveline.offset();
        $this.popup_moveline.remove();
        var scroll = $this._getScroll();
        var left = offset.left - scroll.left - 5,
        top = offset.top - scroll.top - 5;
        var _w = $(window).width();
        var _h = $(window).height();
        if ($this.IsIE6) {
            left += scroll.left;
            top += scroll.top
        }
        if ($this.IsIE6) {
            _w += scroll.left;
            _h += scroll.top
        }
        if (_w < left + $this.popup.width()) {
            left = _w - $this.popup.width()
        } else {
            if (left < 0) {
                left = 0
            }
        }
        if (_h < top + $this.popup.height()) {
            top = _h - $this.popup.height()
        } else {
            if (top < 0) {
                top = 0
            }
        }
        if ($this.config.isani != false) {
            $this.popup.animate({
                left: left,
                top: top
            },
            300)
        } else {
            $this.popup.css({
                left: left,
                top: top
            })
        }
    };
    ExfJsPopup.prototype._onResizeStart = function(e) {
        $this = e.data.obj;
        if ($this._ismax == true) {
            return
        }
        $this._isresize = true;
        var offset = $this.popup.offset();
        var _l = offset.left;
        var _t = offset.top;
        if (!$this.IsIE6) {
            var scroll = $this._getScroll();
            _l -= scroll.left;
            _t -= scroll.top
        }
        $this.popup_moveline = $("<div/>", {
            "class": "popup_moveline",
            css: ({
                width: $this.table.width() - 10,
                height: $this.table.height() - 10,
                left: _l + 5,
                top: _t + 5
            })
        }).appendTo($this.body);
        var offset = $this.popup.offset();
        var obj = {
            obj: $this,
            type: e.data.type,
            offset: $this.popup.offset()
        };
        obj.w = $this.table.width() - 10 - e.clientX;
        obj.h = $this.table.height() - 10 - e.clientY;
        $(document).bind("mousemove", obj, $this._onResizeIng);
        $(document).bind("mouseup", $this, $this._onResizeEnd);
        $(document).bind("selectstart", $this._onNotSelect)
    };
    ExfJsPopup.prototype._onResizeIng = function(e) {
        $this = e.data.obj;
        if ($this._isresize != true) {
            return
        }
        if ($this.popup_moveline.css("display") == "none") {
            $this.popup_moveline.show()
        }
        $this._ismove = true;
        var width = e.data.w + e.clientX;
        var height = e.data.h + e.clientY;
        var offset = e.data.offset;
        if ($this.config.resize.minWidth > 0) {
            if (width < $this.config.resize.minWidth) {
                width = $this.config.resize.minWidth
            }
        }
        if ($this.config.resize.minHeight > 0) {
            if (height < $this.config.resize.minHeight) {
                height = $this.config.resize.minHeight
            }
        }
        var scroll = $this._getScroll();
        if ($this.config.resize.maxWidth > 0) {
            if (width > $this.config.resize.maxWidth) {
                width = $this.config.resize.maxWidth
            }
        } else {
            if (width > $(window).width() - 10) {
                width = $(window).width() - 10
            } else {
                if (width + offset.left > $(window).width() - 10 + scroll.left) {
                    width = $(window).width() - 10 - offset.left + scroll.left
                }
            }
        }
        if ($this.config.resize.maxHeight > 0) {
            if (height > $this.config.resize.maxHeight) {
                height = $this.config.resize.maxHeight
            }
        } else {
            if (height > $(window).height() - 10) {
                height = $(window).height() - 10
            } else {
                if (height + offset.top > $(window).height() - 10 + scroll.top) {
                    height = $(window).height() - 10 - offset.top + scroll.top
                }
            }
        }
        if (width < 250) {
            width = 250
        }
        var _minheight = $this.popup_header.height() + $this.popup_bottom.height() + 4 + 7;
        if (!$this._isEmpty($this.config.note.title)) {
            _minheight += $this.popup_content_note.outerHeight()
        }
        if ($this._isbutton || $this.config.buttons.length > 0) {
            _minheight += $this.popup_content_buttons.outerHeight()
        }
        if (height < _minheight) {
            height = _minheight
        }
        switch (e.data.type) {
        case 0:
            $this.popup_moveline.css({
                width:
                width,
                height: height
            });
            break;
        case 1:
            $this.popup_moveline.css({
                height:
                height
            });
            break;
        case 2:
            $this.popup_moveline.css({
                width:
                width
            });
            break
        }
    };
    ExfJsPopup.prototype._onResizeEnd = function(e) {
        $this.popup_content_body.stop();
        $this.popup.stop();
        $this = e.data;
        $(document).unbind("mousemove", $this._onResizeIng);
        $(document).unbind("mouseup", $this._onResizeEnd);
        $(document).unbind("selectstart", $this._onNotSelect);
        if ($this._ismove != true) {
            return
        }
        var width = $this.popup_moveline.width() + 10;
        var height = $this.popup_moveline.height() + 10;
        $this.popup.css({
            width: $this.table.width(),
            height: $this.table.height()
        });
        $this.popup_moveline.remove();
        if ($this.config.isani != false) {
            $this.popup.animate({
                width: width,
                height: height
            },
            300);
            $this.popup_content_body.animate({
                height: $this._getContentHeight(height)
            },
            300)
        } else {
            $this.popup.css({
                width: width,
                height: height
            });
            $this.popup_content_body.css({
                height: $this._getContentHeight(height)
            })
        }
        $this._ismove = false;
        $this._isresize = false
    };
    ExfJsPopup.prototype._onMax = function(e, nosave) {
        var $this = e.data;
        if ($this.config.ismax != true) {
            return
        }
        $this.popup_content_body.stop();
        $this.popup.stop();
        if ($this._ismax == true) {
            $this._ismax = false;
            var _oldMax = $this._oldMax;
            $this._oldMax = null;
            if ($this.config.isani != false) {
                $this.popup.animate({
                    left: _oldMax.l,
                    top: _oldMax.t,
                    width: _oldMax.w,
                    height: _oldMax.h
                },
                300);
                $this.popup_content_body.animate({
                    height: _oldMax.h2
                },
                300,
                function() {
                    setTimeout(function() {
                        $this._rePosition(e)
                    },
                    1)
                })
            } else {
                $this.popup.css({
                    left: _oldMax.l,
                    top: _oldMax.t,
                    width: _oldMax.w,
                    height: _oldMax.h
                });
                $this.popup_content_body.css({
                    height: _oldMax.h2
                });
                $this._rePosition(e)
            }
            $this.popup_header_tools.removeClass("max")
        } else {
            var scroll = $this._getScroll();
            var _offset = $this.popup.offset();
            if (nosave != true) {
                $this._oldMax = {
                    l: _offset.left,
                    t: _offset.top,
                    w: $this.popup.width(),
                    h: $this.popup.height(),
                    h2: $this.popup_content_body.height()
                };
                if (!$this.IsIE6) {
                    $this._oldMax.l -= scroll.left;
                    $this._oldMax.t -= scroll.top
                }
            }
            var maxW = $(window).width();
            var maxH = $(window).height();
            if ($this.config.isani != false) {
                if ($this.IsIE6) {
                    $this.popup.animate({
                        left: scroll.left,
                        top: scroll.top,
                        width: maxW,
                        height: maxH
                    },
                    300);
                    $this.popup_content_body.animate({
                        height: $this._getContentHeight(maxH - 7)
                    },
                    300);
                    $this._ismax = true
                } else {
                    $this.popup.animate({
                        left: -8,
                        top: -8,
                        width: maxW + 16,
                        height: maxH + 16
                    },
                    300);
                    $this.popup_content_body.animate({
                        height: $this._getContentHeight(maxH + 16)
                    },
                    300);
                    $this._ismax = true;
                    $this.popup_header_tools.addClass("max")
                }
            } else {
                if ($this.IsIE6) {
                    $this.popup.css({
                        left: scroll.left,
                        top: scroll.top,
                        width: maxW,
                        height: maxH
                    });
                    $this.popup_content_body.css({
                        height: $this._getContentHeight(maxH - 7)
                    });
                    $this._ismax = true
                } else {
                    $this.popup.css({
                        left: -8,
                        top: -8,
                        width: maxW + 16,
                        height: maxH + 16
                    });
                    $this.popup_content_body.css({
                        height: $this._getContentHeight(maxH + 16)
                    });
                    $this._ismax = true;
                    $this.popup_header_tools.addClass("max")
                }
            }
        }
    };
    ExfJsPopup.prototype._getContentHeight = function(height) {
        var h1 = height - this.popup_header.height() - this.popup_bottom.height() - 4;
        if (!this._isEmpty(this.config.note.title)) {
            h1 = h1 - this.popup_content_note.outerHeight()
        }
        if (this._isbutton) {
            h1 = h1 - this.popup_content_buttons.outerHeight()
        }
        return h1
    };
    ExfJsPopup.prototype._setConfig = function() {
        this._addbuttons();
        this.title(this.config.title);
        if (this.config.ismask == true) {
            this.mask.show()
        } else {
            this.mask.hide()
        }
        this.popup_header_tools_max.data("enabled", this.config.ismax);
        if (this.config.isclose) {
            this.popup_header_tools_close.data("enabled", true);
            this.popup_header_tools.show();
            if (this.config.ismax) {
                this.popup_header_tools.removeClass("single")
            } else {
                this.popup_header_tools.addClass("single")
            }
        } else {
            this.popup_header_tools_close.data("enabled", false);
            if (this.config.ismax) {
                this.popup_header_tools.show()
            } else {
                this.popup_header_tools.hide()
            }
        }
        if (!this._isEmpty(this.config.note.title)) {
            this.popup_content_note.show();
            if (this.config.note.icon != null && this.config.note.icon.length > 0) {
                this.popup_content_note.children("img").attr("src", this.config.note.icon).show();
                this.popup_content_note.children(".notetext").css("margin-left", "0px")
            } else {
                this.popup_content_note.children("img").hide();
                this.popup_content_note.children(".notetext").css("margin-left", "10px")
            }
            this.popup_content_note.children(".notetext").children(".title").text(this.config.note.title);
            this.popup_content_note.children(".notetext").children(".msg").text(this.config.note.msg)
        } else {
            this.popup_content_note.hide()
        }
        this.popup_bottom.children(".exf_right").unbind();
        this.popup_bottom.children(".exf_center").unbind();
        this.popup_content.children(".exf_right").unbind();
        if (this.config.isresize) {
            this.popup_bottom.children(".exf_right").css({
                cursor: "nw-resize",
                "-moz-user-select": "none"
            });
            this.popup_bottom.children(".exf_center").css({
                cursor: "n-resize",
                "-moz-user-select": "none"
            });
            this.popup_content.children(".exf_right").css({
                cursor: "w-resize",
                "-moz-user-select": "none"
            });
            this.popup_bottom.children(".exf_right").bind("mousedown", {
                obj: this,
                type: 0
            },
            function(e) {
                if (e.data.obj.config.isresize) {
                    e.data.obj._onResizeStart(e)
                }
            });
            this.popup_bottom.children(".exf_center").bind("mousedown", {
                obj: this,
                type: 1
            },
            function(e) {
                if (e.data.obj.config.isresize) {
                    e.data.obj._onResizeStart(e)
                }
            });
            this.popup_content.children(".exf_right").bind("mousedown", {
                obj: this,
                type: 2
            },
            function(e) {
                if (e.data.obj.config.isresize) {
                    e.data.obj._onResizeStart(e)
                }
            })
        } else {
            this.popup_bottom.children(".exf_right").removeAttr("style");
            this.popup_bottom.children(".exf_center").removeAttr("style");
            this.popup_content.children(".exf_right").removeAttr("style")
        }
    };
    ExfJsPopup.prototype._rePosition = function(v) {
        var $this = v == null ? this: v.data;
        $this.popup.stop();
        $this.popup_content_body.stop();
        var _w = $(window).width();
        var _h = $(window).height();
        var _left = _w / 2 - $this.table.width() / 2;
        var _top = _h / 2 - $this.table.height() / 2;
        var scroll = $this._getScroll();
        if ($this.IsIE6) {
            $this.mask.width($(window).width() + scroll.left);
            $this.mask.height(scroll.height);
            if (v == null) {
                _left += scroll.left;
                _top += scroll.top
            }
        }
        if (v != null) {
            var offset = $this.popup.offset();
            _left = offset.left;
            _top = offset.top;
            if ($this.IsIE6) {
                _w += scroll.left;
                _h += scroll.top
            }
            if ($this.IsIE6) {
                if (_w < _left + $this.popup.width()) {
                    _left = _w - $this.popup.width()
                }
                if (_h < _top + $this.popup.height()) {
                    _top = _h - $this.popup.height()
                }
            }
            if (!$this.IsIE6) {
                _left -= scroll.left;
                _top -= scroll.top
            }
        }
        if (_left < 0) {
            _left = 0
        }
        if (_top < 0) {
            _top = 0
        }
        if (v == null) {
            $this.popup.css({
                left: _left,
                top: _top
            })
        } else {
            $this.popup.animate({
                left: _left,
                top: _top
            },
            400)
        }
        if ($this._ismax) {
            $this._ismax = false;
            $this._onMax({
                data: $this
            },
            true)
        }
    };
    ExfJsPopup.prototype._reSize = function(blFamer) {
        this.popup.width(this.config.width);
        if (this.config.height != null) {
            var h1 = this._getContentHeight(this.config.height);
            this.popup.height(this.config.height);
            this.popup_content_body.height(h1)
        } else {
            this.popup.height("auto");
            if (blFamer != true) {
                this.popup_content_body.height("auto")
            }
        }
        if (this.popup.width() > $(window).width()) {
            this.popup.width($(window).width())
        }
        if (this.popup.height() > $(window).height()) {
            var h1 = this._getContentHeight($(window).height());
            this.popup.height($(window).height());
            this.popup_content_body.height(h1)
        }
        this._rePosition();
        $(window).bind("resize", this, this._rePosition);
        if (this.IsIE6) {
            $(window).bind("scroll", this, this._onScroll)
        }
    };
    ExfJsPopup.prototype._onScroll = function(e) {
        if (e.data._ismax) {
            $this.popup.stop();
            var scroll = $this._getScroll();
            $this.popup.animate({
                left: scroll.left,
                top: scroll.top
            },
            300)
        } else {
            e.data._rePosition(e)
        }
    },
    ExfJsPopup.prototype._onHide = function(e) {
        var $this = e == null ? this: e.data;
        if ($this.isopen == false) {
            return
        }
        if ($.isFunction($this.config.onclose)) {
            var _data = null;
            if ($.isFunction($this.config.data)) {
                _data = $this.config.data()
            } else {
                _data = $this.config.data
            }
            $this.config.onclose($this, _data)
        }
        $this.body.hide();
        $this._ismax = false;
        $this.isopen = false;
        if ($this.IsIE6) {
            $(window).unbind("scroll", $this._onScroll)
        }
        $.exfpopup.popupArray = $.grep($.exfpopup.popupArray,
        function(n, i) {
            return n == $this.id
        },
        true);
        if ($this.iFrame != null) {
            $this.body.remove()
        }
    };
    $.extend(ExfJsPopup.prototype, {
        _isEmpty: function(v) {
            if (v == null) {
                return true
            } else {
                return v.replace(/(^ *)|( *$)/g, "") == ""
            }
        },
        _addBtn: function(item) {
            var span = $("<span>");
            span.addClass("exf_button");
            if (item.hasOwnProperty("text")) {
                span.html(item.text)
            }
            if (item.hasOwnProperty("title")) {
                span.attr("title", item.title)
            }
            if (item.hasOwnProperty("callback")) {
                span.bind("click", {
                    target: this,
                    callback: item.callback
                },
                function(e) {
                    var _this = e.data.target;
                    var isResult = true;
                    var _data = null;
                    if ($.isFunction(_this.config.data)) {
                        _data = _this.config.data()
                    } else {
                        _data = _this.config.data
                    }
                    if ($.isFunction(e.data.callback)) {

                        if (_this.iFrame != null) {
                            isResult = e.data.callback(_this, _this.iFrame.get(0).contentWindow, _data)
                        } else {
                            isResult = e.data.callback(_this, _data)
                        }
                    }
                    if (isResult != false) {
                        _this.close()
                    }
                })
            } else {
                span.bind("click", this,
                function(e) {
                    e.data.close()
                })
            }
            span.hover(function() {
                $(this).addClass("focus")
            },
            function() {
                $(this).removeClass("focus")
            });
            this.popup_content_buttons.append(span);
            this._isbutton = true
        },
        _addbuttons: function() {
            this.clearButtons();
            for (var i = 0; i < this.config.buttons.length; i++) {
                var item = this.config.buttons[i];
                if (item.hasOwnProperty("visible")) {
                    if (item.visible == false) {
                        continue
                    }
                }
                this._addBtn(item)
            }
            if (this._isbutton) {
                this.popup_content_buttons.show()
            } else {
                this.popup_content_buttons.hide()
            }
        },
        buttons: function(items) {
            this.config.buttons = items;
            this._addbuttons()
        },
        clearButtons: function() {
            this.popup_content_buttons.find("span").remove();
            this._isbutton = false
        },
        title: function(title) {
            this.popup_header_title.children("span").html(title)
        },
        resize: function(w, h) {
            var $this = this;
            if (h != null) {
                this.config.height = h
            }
            if (w != null) {
                this.config.width = w
            }
            var maxW = $(window).width();
            var maxH = $(window).height();
            if (maxW < w) {
                this.config.width = maxW
            }
            if (maxH < h) {
                this.config.height = maxH
            }
            if (this.config.isani != false) {
                if (this.IsIE6) {
                    this.popup.animate({
                        left: (maxW - w) / 2,
                        top: (maxH - h) / 2,
                        width: w,
                        height: h
                    },
                    300);
                    this.popup_content_body.animate({
                        height: $this._getContentHeight(h)
                    },
                    300)
                } else {
                    this.popup.animate({
                        left: (maxW - w) / 2,
                        top: (maxH - h) / 2,
                        width: w,
                        height: h
                    },
                    300);
                    this.popup_content_body.animate({
                        height: $this._getContentHeight(h)
                    },
                    300)
                }
            } else {
                this._reSize()
            }
        },
        width: function(w) {
            if (typeof w == "number") {
                this.resize(w, null)
            }
        },
        height: function(h) {
            if (typeof h == "number") {
                this.resize(null, h)
            }
        },
        close: function() {
            this._onHide()
        }
    });
    function temp_ExfJsPopup() {
        $this = this;
        $("script").each(function(i) {
            if ($(this).attr("src") != undefined) {
                if (($(this).attr("src") + "").match(/exfjspopup/i)) {
                    var _jspath = $(this).attr("src").substring(0, $(this).attr("src").lastIndexOf("/") + 1);
                    $this.jspath = _jspath;
                    if ($.browser.msie && $.browser.version == "6.0") {
                        document.write('<link href="' + _jspath + 'ExfJsPopupIE6.css" rel="stylesheet" type="text/css" />')
                    } else {
                        document.write('<link href="' + _jspath + 'ExfJsPopupIE7.css" rel="stylesheet" type="text/css" />')
                    }
                    return true
                }
            }
            return _jspath
        });
        document.write('<span id="span_ExfJsPopup"></span>');
        this.popupArray = new Array();
        this.defaultOption = {
            id: "",
            title: "",
            width: 300,
            height: null,
            ismax: true,
            isclose: true,
            ismask: true,
            isresize: true,
            ismove: true,
            data: null,
            isani: true,
            resize: {
                minWidth: 0,
                minHeight: 0,
                maxWidth: 0,
                maxHeight: 0
            },
            note: {
                icon: "",
                title: null,
                msg: ""
            },
            onload: null,
            onclose: null,
            buttons: []
        }
    }
    $.extend(temp_ExfJsPopup.prototype, {
        open: function(data, config) {
            if (typeof config == "string") {
                config = {
                    title: config
                }
            }
            if (config.id == null) {
                var id = "EJPOpen";
                if (typeof data == "string") {
                    if (data.substr(0, 1) == "#") {
                        id = data.substr(1, data.length)
                    }
                } else {
                    if ($(data).data("exfjspopupid") == null) {
                        id = new Date().getTime().toString(16);
                        $(data).data("exfjspopupid", id)
                    } else {
                        id = $(data).data("exfjspopupid")
                    }
                }
                config.id = "EXF_" + id
            }
            config.id = config.id.replace(/-/, "_");
            var ishas = $("#ExfPopup_" + config.id).size() > 0;
            var defConfig = {};
            $.extend(defConfig, this.defaultOption);
            $.extend(defConfig, config);
            var EJPObject = new ExfJsPopup(defConfig, config.id);
            eval(config.id + "=EJPObject;");
            if (typeof data == "string" && data.substr(0, 1) != "#") {
                EJPObject.popup_content_body.html(data)
            } else {
                if (!ishas) {
                    var ele = $(data);
                    EJPObject.popup_content_body.append(ele);
                    ele.show()
                }
            }
            EJPObject.body.show();
            EJPObject._setConfig();
            EJPObject._reSize();
            if ($.isFunction(EJPObject.config.onload)) {
                EJPObject.config.onload()
            }
            return EJPObject
        },
        msgbox: function(msg,title,msgtype,callback, callcancel, ismask) {
            if ($.isFunction(msgtype)) {
                if ($.isFunction(callback)) {
                    callcancel = callback
                }
                callback = msgtype;
                msgtype = 1
            }
            if (msgtype === undefined) {
                msgtype = 1
            }
            var config = {
                title: msgtype == 2 ? "\u8b66\u544a": (title!=''&&title!=undefined?title:"\u63d0\u793a"),
                width: 300,
                ismax: false,
                isresize: false,
                ismask: ismask == true,
                buttons: [{
                    text: msgtype == 3 ? "\u662f": "\u786e \u5b9a",
                    callback: callback
                },
                {
                    text: msgtype == 3 ? "\u5426": "\u53d6 \u6d88",
                    callback: callcancel,
                    visible: msgtype == 3
                }]
            };
            var defConfig = {};
            $.extend(defConfig, this.defaultOption);
            $.extend(defConfig, config);
            EJPMsg = new ExfJsPopup(defConfig, "EJPMsg");
            EJPMsg._setConfig();
            EJPMsg.popup_content_body.css("margin", "10px");
            var _table = '<table><tr><td><span class="popup_icon_' + msgtype + '"></span></td><td nowarp>' + msg + "</td></tr></table>";
            EJPMsg.popup_content_body.html(_table);
            EJPMsg.body.show();
            EJPMsg._reSize();
            return EJPMsg
        },
        url: function(url, config) {
            if (typeof config == "string") {
                config = {
                    title: config
                }
            } else {
                if (config.title == null) {
                    config.title = url
                }
            }
            if (config.id == null) {
                config.id = "EXF_" + new Date().getTime().toString(16)
            }
            var defConfig = {};
            $.extend(defConfig, this.defaultOption);
            $.extend(defConfig, config);
            EJPUrl = new ExfJsPopup(defConfig, config.id);
            eval(config.id + "=EJPUrl;");
            EJPUrl.title("Loading...");
            EJPUrl.popup_content_note.hide();
            EJPUrl.popup_content_buttons.hide();
            EJPUrl.popup_header_tools.hide();
            EJPUrl.popup.width(300);
            EJPUrl.popup.height(95);
            EJPUrl.popup_content_body.height(40);
            EJPUrl.popup_content_body.html("<div class='loading'>\u6b63\u5728\u52a0\u8f7d\uff0c\u8bf7\u7a0d\u5019...</div>");
            EJPUrl.body.show();
            EJPUrl._rePosition();
            var IFRAME = $("<iframe frameBorder='0' scrolling='auto' style='width:100%;height:0;'></iframe>").appendTo(EJPUrl.popup_content_body);
            setTimeout(function() {
                IFRAME.attr("src", url);
                IFRAME.one("load",
                function() {
                    EJPUrl.popup_content_body.children(".loading").remove();
                    $(this).show();
                    $(this).css({
                        width: "100%",
                        height: "100%"
                    });
                    if (EJPUrl.config.height == null) {
                        try {
                            var frame = $(this).get().reverse()[0];
                            var height = 0;
                            if ($.browser.msie) {
                                height = $(frame.Document).height()
                            } else {
                                height = $(frame.contentDocument).height()
                            }
                            EJPUrl.popup_content_body.height(height)
                        } catch(e) {
                            EJPUrl.popup_content_body.height(EJPUrl._getContentHeight($(window).height()))
                        }
                    }
                    EJPUrl._setConfig();
                    EJPUrl._reSize(EJPUrl.config.height == null);
                    EJPUrl.iFrame = IFRAME;
                    if ($.isFunction(EJPUrl.config.onload)) {
                        EJPUrl.config.onload(IFRAME, IFRAME.get(0).contentWindow)
                    }
                })
            },
            500);
            return EJPUrl
        },
        close: function() {
            if ($.exfpopup.popupArray.length > 0) {
                var id = $.exfpopup.popupArray[$.exfpopup.popupArray.length - 1];
                eval("var _exfjspopup=" + id);
                if (_exfjspopup.config.isclose == true) {
                    _exfjspopup.close()
                }
            }
        }
    });
    $.exfpopup = new temp_ExfJsPopup();
    $.exfpopup.version = "1.4.0.0";
    $.exfpopup.author = "";
    $.fn.extend({
        popup: function(config, callback) {
            $.exfpopup.open(this, config, callback)
        }
    });
    $(document).keyup(function(event) {
        if (event.keyCode == 27) {
            $.exfpopup.close()
        }
    })
})(jQuery);