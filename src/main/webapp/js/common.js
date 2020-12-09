(function($) {
	/**
	 * form serializeObject
	 */
	$.fn.serializeObject = function() {
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [ o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	};

	$.fn.ajaxSelectPickerExt = function(options) {
		$this = this;
		if ($this.length > 0) {
			$.each($this, function(index, $select) {
				var ajaxUrl = $($select).attr("data-ajax-url")
				var valueField = $($select).attr("data-value-field");
				var textField = $($select).attr("data-text-field");
				var dataValue = $($select).attr("data-value");
				// var changeCallback = $($select).attr("data-change-callback");
				// alert(changeCallback);
				// alert(ajaxUrl);
				var defaults = {
					ajax : {
						url : ajaxUrl,
						type : 'POST',
						dataType : 'json',
						// Use "{{{q}}}" as a placeholder and Ajax Bootstrap
						// Select will
						// automatically replace it with the value of the search
						// query.
						data : {
							q : '{{{q}}}'
						},
						complete : function() {
							if (dataValue && dataValue != '') {
								$($select).selectpicker('val', dataValue);
							}
						}
					},
					search : false,
					dataValue : dataValue,
					locale : {
						emptyTitle : '请选择'
					},
					log : 3,
					requestDelay : 10,
					preprocessData : function(datas) {
						return $.merge([ {
							text : '请选择',
							value : ''
						} ], $.map(datas, function(data) {
							return {
								text : data[textField],
								value : data[valueField],
								data : {
									subtext : data[textField]
								}
							};
						}));
					},
					preserveSelected : false
				};
				// alert($($($select)).html())
				$($select).selectpicker().ajaxSelectPicker($.extend({}, defaults, options || {})).change(function(e) {
					if ($($select).val() != '' && $.fn.formValidation) {
						$("#form").formValidation('revalidateField', $($select));
					}
					/*
					 * if (changeCallback) { changeCallback($($select).val()); }
					 */
				});
			})
		}
	};
	if(BootstrapDialog){
	    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DEFAULT] = '提示信息';
	    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_INFO] = '提示信息';
	    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_PRIMARY] = '提示信息';
	    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_SUCCESS] = '成功信息';
	    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_WARNING] = '警告信息';
	    BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DANGER] = '错误信息';		
	}
	if (BootstrapDialog && BootstrapDialog.alert) {
		$.alert = BootstrapDialog.alert;
	} else {
		$.alert = window.alert;
	}

	if (BootstrapDialog && BootstrapDialog.confirm) {
		$.confirm = BootstrapDialog.confirm;
	} else {
		$.confirm = window.confirm;
	}
	/**
	 * submit
	 */
	$.submit = function(url, data, callback) {
		// $.loading();
		if ((typeof data == 'string') && data.constructor == String) {
			var formId = data;
			data = $(formId).serialize();
			/*
			 * if (!$(formId).form('validate')) { //$.loading_close(); return; }
			 */
		}

		var request = $.ajax({
			url : url,
			type : "POST",
			async : false,
			data : data,
			dataType : "json"
		});

		var result = {};
		request.done(function(data) {
			result = data;
			if (data.success) {
				// 执行回调方法
				if ((typeof callback == 'function') && callback.constructor == Function) {
					callback(data);
				}

				// 刷新datagrid组件
				// window.parent.$("#dg").datagrid('reload');
				// 如果是弹出层的情况，则成功后自动关闭该弹出层
				// window.parent.$("#dialog").dialog("close");
			} else {
				$.alert(data.message);
			}
		});

		request.always(function() {
			// $.loading_close();
		});
		return result;
	};
	$.getArea = function(address, value) {
		var full = true;
		var _value = [];
		if (/0{5}$/.test(value)) { // 外国
			_value = [ '10200000', value, value ];

		} else {
			_value = address['c' + value.replace(/\d{4}$/, '0000')].d ? [ value.replace(/\d{4}$/, '0000'), value, value ] : [ value.replace(/\d{4}$/, '0000'),
					value.replace(/\d{2}$/, '00'), value ];
		}
		return _value;
	}
	/**
	 * * 对Date的扩展，将 Date 转化为指定格式的String *
	 * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符 * 年(y)可以用 1-4
	 * 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) * eg: * (new Date()).pattern("yyyy-MM-dd
	 * hh:mm:ss.S")==> 2006-07-02 08:09:04.423 (new Date()).pattern("yyyy-MM-dd
	 * E HH:mm:ss") ==> 2009-03-10 二 20:09:04 (new Date()).pattern("yyyy-MM-dd
	 * EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04 (new Date()).pattern("yyyy-MM-dd
	 * EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04 (new Date()).pattern("yyyy-M-d
	 * h:m:s.S") ==> 2006-7-2 8:9:4.18
	 */
	Date.prototype.pattern = function(fmt) {
		var o = {
			"M+" : this.getMonth() + 1, // 月份
			"d+" : this.getDate(), // 日
			"h+" : this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, // 小时
			"H+" : this.getHours(), // 小时
			"m+" : this.getMinutes(), // 分
			"s+" : this.getSeconds(), // 秒
			"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
			"S" : this.getMilliseconds()
		// 毫秒
		};
		var week = {
			"0" : "/u65e5",
			"1" : "/u4e00",
			"2" : "/u4e8c",
			"3" : "/u4e09",
			"4" : "/u56db",
			"5" : "/u4e94",
			"6" : "/u516d"
		};
		if (/(y+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		}
		if (/(E+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
		}
		for ( var k in o) {
			if (new RegExp("(" + k + ")").test(fmt)) {
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
			}
		}
		return fmt;
	}
	$(document).ready(function() {
		pageInit();
		//menuInit();
	});
})(jQuery);
/**
 * init page
 */
function pageInit() {
	$(".selectpicker").filter(".with-ajax").ajaxSelectPickerExt();

	$('input[data-toggle="datepicker"]').each(function(index, $datepicker) {
		$($datepicker).datepicker({
			language : 'zh-CN',
			format : 'yyyy-mm-dd',
			autoclose : true
		}).on('changeDate', function(event) {
			if ($.fn.formValidation) {
				$("#form").formValidation('revalidateField', $($datepicker));
			}
		});
	});
}
/**
 * init menu
 */
function menuInit(){
    $.ajax({
		url: 'permission/findMenus.s',
		type: 'POST',
		dataType : "json",
		success :function(data){
			var topLevel = $.grep(data, function(n) {
				return n.parentId == '0';
			});
			topLevel.sort(function(a,b){
				return (a.sort - b.sort);  
			});
			$.each(topLevel,function(index,top){
				var children = $.grep(data, function(n) {
					return n.parentId == top.id;
				});
				top['children']=children.sort(function(a,b){
					return (a.sort - b.sort);
				});
				$('#menu-tmp').tmpl(top).appendTo("#t-menu");
			});		
		}
	});
}
/**
 * file input
 */
function initFileinput(input, initData, postData) {
	var $input = $(input);
	var op = {
		uploadUrl : "category/saveImages.s",
		footerTemplate : '<div class="file-thumbnail-footer">\n' + '   <div style="margin:5px 0">\n'
		// + ' <input class="kv-input kv-init form-control input-sm
		// {TAG_CSS_INIT}" value="{TAG_VALUE}" placeholder="Enter
		// caption...">\n'
		+ '   </div>\n' + '   {actions}\n' + '</div>',
		otherActionButtons : '',
		previewThumbTags : {
			'{TAG_VALUE}' : '', // no value
			'{TAG_CSS_NEW}' : '', // new thumbnail input
			'{TAG_CSS_INIT}' : 'hide' // hide the initial input
		},
		initialPreviewThumbTags : [],
		initialPreview : [],
		initialPreviewConfig : [],
	};
	$.extend(op, initData);
	$input.fileinput({
		uploadUrl : op.uploadUrl, // server upload action
		uploadAsync : false,
		overwriteInitial : false,
		showRemove: false,
		dropZoneEnabled : true,
		minFileCount : 1,
		maxFileCount : 5,
		layoutTemplates : {
			footer : op.footerTemplate,
			actionUpload : ''
		},
		allowedFileExtensions : [ 'jpg', 'gif', 'png' ],
		otherActionButtons : op.otherActionButtons,
		previewThumbTags : op.previewThumbTags,
		initialPreviewThumbTags : op.initialPreviewThumbTags,
		initialPreview : op.initialPreview,
		initialPreviewConfig : op.initialPreviewConfig,
		uploadExtraData : function() { // callback example
			var out = {}, key, i = 0;
			$('.kv-input:visible').each(function() {
				$el = $(this);
				key = $el.hasClass('kv-new') ? 'new_' + i : 'init_' + i;
				out[key] = $el.val();
				i++;
			});
			$.extend(out, postData);
			return out;
		}
	});
}