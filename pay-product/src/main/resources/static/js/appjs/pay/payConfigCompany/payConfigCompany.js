
var prefix = "/pay/payConfigCompany"
$(function() {
	load();
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
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
					            company:$('#company').val()
					           // username:$('#searchName').val()
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
									field : 'company', 
									title : '支付公司' ,
									formatter:function(value, row, index) {
										if(value != null){
											return  payCompanys[value];
										}
									}
								},
																{
									field : 'payMerch', 
									title : '支付商户' 
								},
																{
									field : 'outChannel', 
									title : '渠道' ,
									formatter:function(value, row, index) {
										if(value != null){
											return  outChannels[value];
										}
									}
								},
																{
									field : 'costRate', 
									title : '支付成本费率' 
								},
																{
									field : 'qhRate', 
									title : '启晗代理费率' 
								},
																{
									field : 'maxPayAmt', 
									title : '单笔最大支付额' 
								},
																{
									field : 'minPayAmt', 
									title : '单笔最新支付额' 
								},
																{
									field : 'crtTime', 
									title : '创建时间' 
								},
																{
									field : 'payPeriod', 
									title : '支付时间段' ,
									formatter:function(value, row, index) {
										if(value != null){
											var result = "";
											var datas = value.split(",");
											var nums = null;
											var start = null;
											var hour = null;
											var minute = null;;
											for(var i = 0, length = datas.length; i < length; i++){
												if(result.length>0){
													result += ",";
												}
												if(datas[i]){
													nums = datas[i].split("-");
													if(nums.length = 2){
														start = parseInt(nums[0]);
														minute = start%60;
														hour = (start - minute)/60;
														result += (hour<10?"0"+hour:hour) + ":" + (minute<10?"0"+minute:minute);
														result += "-";
														start = parseInt(nums[1]);
														minute = start%60;
														hour = (start - minute)/60;
														result += (hour<10?"0"+hour:hour) + ":" + (minute<10?"0"+minute:minute);
													}
												}
											}
											return result;
										}
										return "";
									}
								},
																{
									field : 'ifClose', 
									title : '是否关闭' ,
									formatter:function(value, row, index) {
										return value ==0?"开启":"关闭";
									}
								},
								{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.company + '\',\'' + row.payMerch + '\',\'' + row.outChannel
												+ '\')"><i class="fa fa-edit"></i></a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ row.company + '\',\'' + row.payMerch + '\',\'' + row.outChannel
												+ '\')"><i class="fa fa-remove"></i></a> ';
										return e + d ;
									}
								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
function add() {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '550px' ],
		content : prefix + '/add' // iframe的url
	});
}
function edit(company,payMerch,outChannel) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '550px' ],
		content : prefix + '/edit/' + company +"/" + payMerch + "/" + outChannel // iframe的url
	});
}
function remove(company,payMerch,outChannel) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				company : company,
				payMerch : payMerch,
				outChannel : outChannel
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

function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var companys = new Array();
		var payMerchs = new Array();
		var outChannels = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			companys[i] = row['company'];
			payMerchs[i] = row['payMerch'];
			outChannels[i] = row['outChannel'];
		});
		$.ajax({
			type : 'POST',
			data : {
				ids : ids,
				payMerchs:payMerchs,
				outChannels :outChannels
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});
}