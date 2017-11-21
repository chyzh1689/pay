var prefix = "/pay/payAudit"
$(function() {
	//时间框
	$(".form_datetime").datetimepicker({
		  language : 'zh-CN',
		  format: 'yyyy-mm-dd',
	      weekStart: 1,
	      todayBtn:  1,
	      autoclose: 1,
		  todayHighlight: 1,
		  startView: 2,
		  minView: 2,
		  forceParse: 0
	});
	$(".form_datetime").val(formatDate(new Date()));
	$('#auditType').val(0);
	$('#auditResult').val(0);
	load();
});

function formatDate(date){
	return date.getFullYear()+"-"+
	(date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1)+"-"+
	(date.getDate() < 10 ? "0" + date.getDate() : date.getDate());
}

function intTime2Str(time){
	if(time != null){
		var date = new Date(); 
		date.setTime(1000 * time); 
		var year = date.getFullYear();  
		var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;  
		var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();  
		var hour = date.getHours()< 10 ? "0" + date.getHours() : date.getHours();  
		var minute = date.getMinutes()< 10 ? "0" + date.getMinutes() : date.getMinutes();  
		var second = date.getSeconds()< 10 ? "0" + date.getSeconds() : date.getSeconds();  
		return year + "-" + month + "-" + day+" "+hour+":"+minute+":"+second;  
	}else{
		return "";
	}
}

function load() {
	$('#exampleTable').bootstrapTable({
		method : 'get', // 服务器数据的请求方式 get or post
		url : prefix + "/list", // 服务器数据的加载地址
	//	showRefresh : true,
	//	showToggle : true,
	//	showColumns : true,
		iconSize : 'outline',
		toolbar : '#exampleToolbar',
		striped : true, // 设置为true会有隔行变色效果
		dataType : "json", // 服务器返回的数据类型
		pagination : true, // 设置为true会在底部显示分页条
		// queryParamsType : "limit",
		// //设置为limit则会发送符合RESTFull格式的参数
		singleSelect : false, // 设置为true将禁止多选
		// contentType : "application/x-www-form-urlencoded",
		// //发送到服务器的数据编码类型
		pageSize : 10, // 如果设置了分页，每页数据条数
		pageNumber : 1, // 如果设置了分布，首页页码
		//search : true, // 是否显示搜索框
		showColumns : false, // 是否显示内容下拉框（选择显示的列）
		sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
		queryParams : function(params) {
			return {
				//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
				limit: params.limit,
				offset:params.offset,
	            orderNo:$('#orderNo').val(),
	            merchNo:$('#merchNo').val(),
	            auditResult:$('#auditResult').val(),
	            auditType:$('#auditType').val(),
	            beginDate:$('#beginDate').val(),
	            endDate:$('#endDate').val()
			};
		},
		// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
		// queryParamsType = 'limit' ,返回参数必须包含
		// limit, offset, search, sort, order 否则, 需要包含:
		// pageSize, pageNumber, searchText, sortName,
		// sortOrder.
		// 返回false将会终止请求
		columns : [
				{
					checkbox : true
				},
				{
					field : 'merchNo', 
					title : '商户号' 
				},
												{
					field : 'orderNo', 
					title : '订单号' 
				},
												{
					field : 'auditType', 
					title : '审核类型' ,
					formatter : function(value, row, index) {
						return auditTypes[value];
					}
					
				},
				{
					field : 'auditResult', 
					title : '审核结果', 
					formatter : function(value, row, index) {
						return auditResults[value];
					}
				},
												{
					field : 'auditor', 
					title : '审核人' 
				},
												{
					field : 'auditTime', 
					title : '审核时间' 
				},
												{
					field : 'crtTime', 
					title : '创建时间',
					formatter : function(value, row, index) {
						return intTime2Str(value);
					}
				},
				{
					field : 'memo', 
					title : '备注' 
				},
												{
					title : '操作',
					field : 'auditResult',
					align : 'center',
					formatter : function(value, row, index) {
						if(value == 0){
							return '<a class="btn btn-primary btn-sm ' + s_audit_h + '" href="#" title="通过"  mce_href="#" onclick="audit(\''
							+ row.orderNo + '\',\'' + row.merchNo + '\',\'' + row.auditType + '\',' + 1
							+ ')"><i class="fa fa-calendar-check-o"></i>通过</a> ' +
							'<a class="btn btn-danger btn-sm ' + s_audit_h + '" href="#" title="不通过"  mce_href="#" onclick="audit(\''
							+ row.orderNo + '\',\'' + row.merchNo + '\',\'' + row.auditType + '\',' + 2
							+ ')"><i class="fa fa-calendar-check-o"></i>不通过</a> ';
						}else{
							return "已审核";
						}
					}
				} ]
	});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

function audit(orderNo,merchNo,auditType,auditResult) {
	var msg = (auditResult == 1?'通过':'不通过');
	layer.confirm('确定要审核选中的记录'+msg+'？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/audit",
			type : "post",
			data : {
				orderNo : orderNo,
				merchNo : merchNo,
				auditType : auditType,
				auditResult : auditResult
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function batchAudit(auditResult) {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要审核的数据");
		return;
	}
	var msg = (auditResult == 1?'通过':'不通过');
	layer.confirm("确认要批量审核" + msg + "选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var orderNos = new Array();
		var merchNos = new Array();
		var auditTypes = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		var auditFlag = false;
		$.each(rows, function(i, row) {
			if(row['auditResult'] != 0){
				auditFlag = true;
			}
			orderNos[i] = row['orderNo'];
			merchNos[i] = row['merchNo'];
			auditTypes[i] = row['auditType'];
		});
		if(auditFlag){
			layer.msg("包含已审核过的数据，请重新选择！");
			return;
		}
		$.ajax({
			type : 'POST',
			data : {
				orderNos : orderNos,
				merchNos : merchNos,
				auditTypes : auditTypes,
				auditResult : auditResult
			},
			url : prefix + '/batchAudit',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function(result) {
		layer.msg("批量审核" + msg + "已取消！");
	});
}