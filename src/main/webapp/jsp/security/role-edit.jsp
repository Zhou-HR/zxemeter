<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>角色编辑</title>

    <link href="assets/plugins/zTree_v3/css/metroStyle/metroStyle.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.min.js"></script>

    <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
    <script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
    <style>
        .ztree .switch {
            position: static;
        }
    </style>
</head>
<body>
<section class="content">
    <div class="box box-primary">
        <form class="form-horizontal" action="${empty entity? 'role/save' : 'role/edit'}" method="post">
            <c:if test="${not empty entity}">
                <input type="hidden" name="id" value="${entity.id}"/>
            </c:if>

            <div class="box-body">
                <div class="form-group">
                    <label class="col-sm-2 control-label">角色名</label>

                    <div class="col-sm-6">
                        <input type="text" class="form-control" name="name" value="${entity.name}"
                               data-fv-notempty="true">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">描述</label>

                    <div class="col-sm-6">
                        <input type="text" class="form-control" name="description" value="${entity.description}"
                               data-fv-notempty="true">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">状态</label>

                    <div class="col-sm-6">
                        <select data-dict-url="dict/list/STATUS/STATUS" data-dict-value="${entity.status.value}"
                                id="e_status" name="status" class="form-control selectpicker" title="请选择"
                                data-fv-notempty="true" data-fv-icon="false"></select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">权限列表</label>

                    <div id="permissions"></div>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" placeholder="请单击选择" readonly id="checkedVal"
                               onclick="showMenu()">
                        <div id="menuContent"
                             style="display: none;border: 1px solid rgba(0,0,0,.2);box-shadow: 0 5px 10px rgba(0,0,0,.2);">
                            <ul id="treeDemo" class="ztree" style="overflow-y: scroll;height: 190px"></ul>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</section>
<script>
    var setting = {
        async: {
            enable: true,
            url: "permission/all"
        },
        check: {
            enable: true
        },
        view: {
            dblClickExpand: false
        },
        data: {
            simpleData: {
                enable: true,
                pIdKey: "parentId"
            }
        },
        callback: {
            onAsyncSuccess: setDefaultPermissions,
            beforeClick: beforeClick,
            onCheck: onCheck
        }
    };

    function setDefaultPermissions(event, treeId, treeNode, msg) {
        <c:if test="${not empty entity}">
        var permissions = new Array();
        <c:forEach items="${permissions}" var="permission">
        permissions.push('${permission.id}');
        </c:forEach>

        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        $.each(permissions, function (index, value) {
            var node = treeObj.getNodeByParam("id", value, null);
            treeObj.checkNode(node, true);
            onCheck(event, treeId, node);
        });
        </c:if>
    }

    function beforeClick(treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(treeId);
        zTree.checkNode(treeNode, !treeNode.checked, null, true);
        return false;
    }

    function onCheck(e, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(treeId),
            nodes = zTree.getCheckedNodes(true),
            v = "";
        for (var i = 0, l = nodes.length; i < l; i++) {
            v += nodes[i].name + ",";
        }
        if (v.length > 0) v = v.substring(0, v.length - 1);
        $("#checkedVal").attr("value", v);

        $("#permissions").html("");
        var permissions = '';
        $.each(nodes, function (index, value) {
            permissions += (value.id + ",");
        });
        $("#permissions").append("<input type='hidden' name='permissions' value='" + permissions + "'/>");
    }

    function showMenu() {
        var obj = $("#checkedVal");
        var offset = $("#checkedVal").offset();
        $("#menuContent").css({left: offset.left + "px", top: offset.top + obj.outerHeight() + "px"}).slideDown("fast");

        $("body").bind("mousedown", onBodyDown);
    }

    function hideMenu() {
        $("#menuContent").fadeOut("fast");
        $("body").unbind("mousedown", onBodyDown);
    }

    function onBodyDown(event) {
        if (!(event.target.id == "checkedVal" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
            hideMenu();
        }
    }

    $(function () {
        $.fn.zTree.init($("#treeDemo"), setting);
    });
</script>
</body>
</html>
