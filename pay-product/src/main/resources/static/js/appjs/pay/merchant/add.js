$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	var feeRateStr = "{";
	var feeRate = "";
	$(".feeRate").each(function(){
		feeRate = $(this).val();
		if(feeRate){
			feeRateStr += $(this).attr("id")+":"+feeRate+","
		}
	});
	if(feeRateStr.length>1){
		feeRateStr = feeRateStr.substring(0, feeRateStr.length - 1);
	}
	feeRateStr +="}";
	$("#feeRate").val(feeRateStr);
	$.ajax({
		cache : true,
		type : "POST",
		url : "/pay/merchant/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			},
			merchNo : {
				remote : {
					url : "/pay/merchant/exist", // 后台处理程序
					type : "post", // 数据发送方式
					dataType : "json", // 接受数据格式
					data : { // 要传递的数据
						merchNo : function() {
							return $("#merchNo").val();
						}
					}
				}
			}
		},
		messages : {
			name : {
				required : icon + "请输入商户名称"
			},
			merchNo : {
				remote : icon + "商户号已经存在"
			}
		}
	});
}