
var prefix = "/orderQuery/order"
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
					            merchNo:$('#merchNo').val(),
					            orderNo:$('#orderNo').val()
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
									field : 'merchNo', 
									title : '启晗商户号' 
								},
																{
									field : 'orderNo', 
									title : '订单号' 
								},
																{
									field : 'payCompany', 
									title : '支付公司' 
								},
																{
									field : 'payMerch', 
									title : '支付商户号' 
								},
																{
									field : 'outChannel', 
									title : '渠道编码' 
								},
																{
									field : 'title', 
									title : '标题' 
								},
																{
									field : 'product', 
									title : '产品名称' 
								},
																{
									field : 'amount', 
									title : '订单金额(元)' 
								},
																{
									field : 'currency', 
									title : '币种' 
								},
																{
									field : 'orderState', 
									title : '订单状态' 
								},
								{
									field : 'reqTime', 
									title : '请求时间' 
								},
																{
									field : 'userId', 
									title : '商户用户标志' 
								},
																{
									field : 'bankNo', 
									title : '银行卡号' 
								},
																{
									field : 'reqIp', 
									title : '请求ip' 
								},
																{
									field : 'memo', 
									title : '备注留言信息' 
								},
																{
									field : 'crtDate', 
									title : '创建时间' 
								},
																{
									field : 'realAmount', 
									title : '实际支付金额' 
								},
																{
									field : 'costAmount', 
									title : '成本金额' 
								},
																{
									field : 'qhAmount', 
									title : '启晗代理金额' 
								},
																{
									field : 'agentAmount', 
									title : '商户代理金额' 
								},
																{
									field : 'msg', 
									title : '消息提示' 
								},
								{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index){
										return '<a class="btn btn-primary btn-sm" href="#" title="查看详情"  id="pk' + row.merchNo  + '" mce_href="#" onclick="lookDetail(\''
										+ row.merchNo +'\',\'' + row.orderNo +'\',\'' + row
										+ '\')"><i class="fa fa-key"></i></a>';
									}								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

function lookDetail(merchNo,orderNo,row){
	layer.open({
	  title:merchNo + "的" + orderNo + " 详情",
	  content: row.msg //数组第二项即吸附元素选择器或者DOM
	});
}