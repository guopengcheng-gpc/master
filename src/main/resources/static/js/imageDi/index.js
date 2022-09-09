
(function($) {
	// 私有方法
	var privateFun = function() {};
	// 合并对象等
	var colorPicker = (function() {
		function colorPicker(element, options) {
			// 设置为true深拷贝,合并对象
			var initDefault = $.extend(true, {}, $.fn.colorPicker.default)
			this.settings = $.extend(true, initDefault, options || {});
			this.element = element;
			// 初始化插件
			this.btn_ts = null,
				this.btn_opacity = null,
				this.settings.opacity = 1,
				this.btn_color = null,
				this.color_ts = "255,0,0"
			this.show = false
			this.init()
		}
		// 在colorPicker原型上定义方法,让实例可以调用这些方法
		colorPicker.prototype = {
			// 功能方法
			init: function() {
				this.creatHtml()
			},
			creatHtml: function() {
				var html = ''
				html += '<span class="showColor" style="text-align:center;font-size:20px"></span>'
				html += '<div class="color-picker">'
				html += '	<div class="color-main">'
				html += '		<div class="color-hue">'
				html += '			<div class="color-hue-slider__bar"></div>'
				html += '			<div class="color-hue-slider__thumb"></div>'
				html += '		</div>'
				html += '		<div class="color-svpanel">'
				html += '			<div class="color-svpanel__white"></div>'
				html += '			<div class="color-svpanel__black"></div>'
				html += '			<div class="color-svpanel__cursor">'
				html += '				<div class="color-svpanel__cursordiv"></div>'
				html += '			</div>'
				html += '		</div>'
				html += '	</div>'
				html += '	<div class="color-alpha">'
				html += '		<div class="color-alpha-slider__bar"></div>'
				html += '		<div class="color-alpha-slider__thumb"></div>'
				html += '	</div>'
				html += '	<div class="color-btns">'
				html += '		<span class="color-dropdown__value">'
				html += '			<input type="text" autocomplete="off"/>'
				html += '		</span>'
				html += '		<button class="clear">清空</button>'
				html += '		<button class="sure">确定</button>'
				html += '	</div>'
				html += '</div>'
				var x = this.element.offset().left
				var y = this.element.offset().top + this.element.outerHeight()
				this.element.append(html)
				this.element.css("position", "relative")
				this.element.find(".color-picker").css({
					"left": x + "px",
					"top": y + "px"
				})
				this.element.find(".showColor").css({
					"background": this.settings.initColor
				})
				this.events()
				// .fadeIn(300)
				// this.element.find(".color-picker").slideDown(300);
			},
			events: function() {
				var that = this


				function move(obj, type) {
					obj.on("mousedown", function(e) {
						var box_x = that.element.find(".color-picker").offset().left
						var box_y = that.element.find(".color-picker").offset().top

						$(document).mousemove(function(e) {
							if (type == 1) {
								var y = e.clientY - 8 - box_y
								if (y >= 0 && y <= 180) {
									obj.css("top", y + "px")
									that.element.find(".color-svpanel").css(
										"background-color", "rgba(" + getcolor(
											y) + "," + that.settings.result
										.opacity + ")")
									that.element.find(".color-alpha-slider__bar")
										.css("background",
											"linear-gradient(to right, rgba(255, 0, 0, 0) 0%, rgb(" +
											getcolor(y) + ") 100%)")
									that.element.find(
										".color-dropdown__value input").val(
										"rgba(" + getcolor(y) + "," + that
										.settings.result.opacity + ")")
									that.element.find(".showColor").css(
										"background", "rgba(" + that.settings
										.result.color + "," + that.settings
										.result.opacity + ")")

								}
							}
							if (type == 2) {
								var x = e.clientX - 8 - box_x
								if (x >= 0 && x <= 278) {
									obj.css("left", x + "px")
									that.opacity = (x / 278).toFixed(2)
									that.settings.result.opacity = that.opacity
									that.element.find(
										".color-dropdown__value input").val(
										"rgba(" + that.color_ts + "," + that
										.settings.result.opacity +
										")")
									that.element.find(".showColor").css(
										"background", "rgba(" + that.settings
										.result.color + "," + that.settings
										.result.opacity + ")")

								}
							}
							if (type == 3) {

								var x = e.clientX - 6 - box_x
								var y = e.clientY - 6 - box_y
								if (x < 0) { x = 0 }
								if (x > 280) { x = 280 }
								if (y < 0) { y = 0 }
								if (y > 180) { y = 180 }
								obj.css({
									"left": x + "px",
									"top": y + "px"
								})
								setColor(x, y)
							}
						});

						$(document).mouseup(function() {
							$(document).off('mousemove');
						});
					})

				}
				that.element.find(".color-svpanel").click(function(e) {
					var box_x = that.element.find(".color-picker").offset().left
					var box_y = that.element.find(".color-picker").offset().top
					var x = e.clientX - 6 - box_x
					var y = e.clientY - 6 - box_y
					that.element.find(".color-svpanel__cursor").css({
						"left": x + "px",
						"top": y + "px"
					})
					setColor(x, y)
				})
				that.element.find(".color-hue").click(function(e) {
					var box_y = that.element.find(".color-picker").offset().top
					var y = e.clientY - 8 - box_y
					that.element.find(".color-hue-slider__thumb").css("top", y + "px")
					that.element.find(".color-svpanel").css("background-color", "rgba(" +
						getcolor(y) + "," + that.settings.result.opacity + ")")
					that.element.find(".color-alpha-slider__bar").css("background",
						"linear-gradient(to right, rgba(255, 0, 0, 0) 0%, rgb(" +
						getcolor(y) + ") 100%)")
					that.element.find(".color-dropdown__value input").val("rgba(" +
						getcolor(y) + "," + that.settings.result.opacity + ")")
					that.element.find(".showColor").css("background", "rgba(" + that
						.settings.result.color + "," + that.settings.result.opacity +
						")")

				})
				that.element.find(".color-alpha-slider__bar").click(function(e) {
					var box_x = that.element.find(".color-picker").offset().left
					var x = e.clientX - 8 - box_x
					that.element.find(".color-alpha-slider__thumb").css("left", x + "px")
					that.opacity = (x / 278).toFixed(2)
					that.settings.result.opacity = that.opacity
					that.element.find(".color-dropdown__value input").val("rgba(" + that
						.color_ts + "," + that.settings.result.opacity +
						")")
					that.element.find(".showColor").css("background", "rgba(" + that
						.settings.result.color + "," + that.settings.result.opacity +
						")")

				})
				$(document).on("click",function(){
					$(".color-picker").slideUp(300)
					that.show = false
				})
				$(".color-picker").on("click",function(e){
					var ev=e||window.event
					ev.stopPropagation()
				})
				move(that.element.find(".color-hue-slider__thumb"), 1)
				move(that.element.find(".color-alpha-slider__thumb"), 2)
				move(that.element.find(".color-svpanel__cursor"), 3)



				that.a = 255,
					that.b = 0,
					that.c = 0

				function getcolor(y) {
					var result
					if (y <= 30) {
						that.b = (y / 30 * 255).toFixed(0)
					} else if (y <= 60) {
						that.a = ((60 - y) / 30 * 255).toFixed(0)
					} else if (y <= 90) {
						that.c = ((y - 60) / 30 * 255).toFixed(0)
					} else if (y <= 120) {
						that.b = ((120 - y) / 30 * 255).toFixed(0)
					} else if (y <= 150) {
						that.a = ((y - 120) / 30 * 255).toFixed(0)
					} else if (y <= 180) {
						that.c = ((180 - y) / 30 * 255).toFixed(0)
					}
					that.color_ts = that.a + "," + that.b + "," + that.c
					that.settings.result.color = that.a + "," + that.b + "," + that.c
					return that.a + "," + that.b + "," + that.c
				}

				function setColor(x, y) {
					var o = that.color_ts.split(",")
					var a = o[0],
						b = o[1],
						c = o[2]
					var c_a = a == 255 ? 255 : (255 - (255 - a) * (x / 280)).toFixed(0)
					var c_b = b == 255 ? 255 : (255 - (255 - b) * (x / 280)).toFixed(0)
					var c_c = c == 255 ? 255 : (255 - (255 - c) * (x / 280)).toFixed(0)
					c_a = ((1 - (y / 180)) * c_a).toFixed(0)
					c_b = ((1 - (y / 180)) * c_b).toFixed(0)
					c_c = ((1 - (y / 180)) * c_c).toFixed(0)
					var result = c_a + "," + c_b + "," + c_c
					that.settings.result.color = c_a + "," + c_b + "," + c_c
					that.element.find(".color-dropdown__value input").val("rgba(" + result + "," +
						that.settings.result.opacity + ")")
					that.element.find(".showColor").css("background", "rgba(" + that.settings.result
						.color + "," + that.settings.result.opacity + ")")
					that.element.find(".showColor").text(parseInt(c_a,10).toString(10)+"," + parseInt(c_b,10).toString(10)+"," + parseInt(c_c,10).toString(10))

				}






				this.element.find(".showColor").click(function(e) {
					var ev=e||window.event
					ev.stopPropagation()
					$(".color-picker").slideUp(300)
					if (!that.show) {
						that.element.find(".color-picker").slideDown(300)
					} else {
						that.element.find(".color-picker").slideUp(300)
					}
					that.show = !that.show
				})
				//确定
				this.element.find(".sure").click(function() {
					that.element.find(".showColor").css("background", "rgba(" + that
						.settings.result.color + "," + that.settings.result.opacity +
						")")
					that.element.find(".color-picker").slideUp(300)
					that.show = false
					if(that.settings.callback && typeof that.settings.callback=="function"){
						that.settings.callback("rgba(" + that.settings.result.color + "," + that
							.settings.result.opacity + ")")
					}
				})
				//取消
				this.element.find(".clear").click(function() {
					that.element.find(".showColor").css("background", "rgba(255,255,255,1)")
					that.element.find(".color-picker").slideUp(300)
					that.element.find(".color-dropdown__value input").val("rgba(255,0,0,1)")
					that.element.find(".color-svpanel__cursor").css({
						"left":"100%",
						"top":0
					})
					that.element.find(".color-hue-slider__thumb").css({
						"left":0,
						"top":0
					})
					that.element.find(".color-alpha-slider__thumb").css({
						"left":"100%",
						"top":0
					})
					that.show = false
				})
			}
		};
		return colorPicker;
	})();
	// 创建对象实例
	$.fn.colorPicker = function(options) {
		// 实现链式调用
		return this.each(function() {
			var me = $(this),
				instance = me.data("colorPicker");
			// 判断实例是否存在，不存在则创建对象，并将该对象及配置项传入
			if (!instance) {
				instance = new colorPicker(me, options);
				// 存放实例
				me.data("colorPicker", instance);
			}
			// 在外部可以通过$(selection).colorPicker("init")
			if ($.type(options) === "String" || $.type(options) === "string") {
				return instance[options]();
			}
		});
	};
	// 配置参数
	$.fn.colorPicker.default = {
		//默认颜色(目前没开发默认颜色)
		initColor: 'rgba(255,255,255,1)',
		// 选择颜色后执行的回调函数
		// callback: "",
		result: {
			color: "255,255,255",
			opacity: 1
		}
	};
	// 在插件内部初始化，但是这种写法需要在目标元素上添加color-picker
	$(function() {
		$("[color-picker]").colorPicker();
	})
})(jQuery)
