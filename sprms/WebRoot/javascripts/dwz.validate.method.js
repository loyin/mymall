/**
 * @requires jquery.validate.js
 * @author ZhangHuihua@msn.com
 */
(function($){
	$.validator.addMethod("alphanumeric", function(value, element) {
		return this.optional(element) || /^\w+$/i.test(value);
	}, "只能是字母数字及下划线");
	
	$.validator.addMethod("lettersonly", function(value, element) {
		return this.optional(element) || /^[a-z]+$/i.test(value);
	}, "只能是字母"); 
	$.validator.addMethod("chinese", function(v, element) {
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^[\u0391-\uFFE5]+$/);
	},'只能输入中文');
	$.validator.addMethod("phone", function(v, element) {
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^[0-9 \(\)]{7,30}$/);
	}, "请输入一个有效电话号码");
	
	$.validator.addMethod("postcode", function(v, element) {
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^[0-9 A-Za-z]{5,20}$/);
	}, "Please specify a valid postcode");
	
	$.validator.addMethod("date", function(v, element) {
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/);
	});
	
	$.validator.addClassRules({
		alphanumeric: { alphanumeric: true },
		lettersonly: { lettersonly: true },
		phone: { phone: true },
		postcode: {postcode: true}
	});

})(jQuery);