$().ready(function() {
	validateRule();
	var feeRate = "";
	$(".feeRate").each(function(){
		$(this).val(feeRates[$(this).attr("id")]);
	});
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
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
		url : "/pay/merchant/update",
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
			}
		},
		messages : {
			name : {
				required : icon + "请输入名字"
			}
		}
	})
}