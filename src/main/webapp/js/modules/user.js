/**
 * Created by zhangry on 2017/3/15.
 */
userListManager = {
    formatter: {
        // 生成序号
        index: function (value, row, index) {
            return userListManager.number * userListManager.size + index + 1;
        }
    }
}

; (function($) {
    var gridTable = $("#gridTable").bootstrapTable({
        columns: [
            {field: "num", title: "序号", width: 10, formatter: "userListManager.formatter.index"},
            {field: "id", title: "id", width: 80, visible: false},
            {field: "name", title: "姓名", width: 80, align: "center"},
            {field: "sex", title: "性别", width: 80, align: "center"},
            {field: "age", title: "年龄", width: 80, align: "center"},
            {field: "userNo", title: "用户编号", width: 80, align: "center"}
        ],
        striped: true,
        pagination: true,
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 25, 50, 100],
        sidePagination: "server",
        // showColumns: true,
        method: "post",
        url: "${ctxp}/../gridTable",
        dataField: "result",
        totalField: "totalCount",
        init: function () {
            //设置grid高度
            var gridHeight = $("#mainGrid").offsetHeight;
            var pageHeight = $(".page-content").offsetHeight;
            var formHeight = $("#searchTitle").offsetHeight;
            gridHeight = pageHeight - formHeight - 44;
        },
        // 查询参数
        queryParams: function(params){
            params.queryMsg = {
            };
            params.offset = params.offset / params.limit + 1;
            //为计算行数使用
            userListManager.number = params.offset - 1;
            userListManager.size = params.limit;
            return params;
        },
        onPageChange: function(number, size) {
        },
        onClickRow: function (row, elem) {
        }
    });
    $("#searchBtn").click(function () {
        $("#gridTable").bootstrapTable("refresh");
    });
})(jQuery);