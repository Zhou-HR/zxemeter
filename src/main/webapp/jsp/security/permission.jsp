<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>资源列表</title>
    <link rel="stylesheet" href="assets/plugins/bootstrap-select/css/bootstrap-select.css"/>
    <script type="text/javascript" src="assets/plugins/bootstrap-select/js/bootstrap-select.js"></script>

    <link href="assets/plugins/zTree_v3/css/metroStyle/metroStyle.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.min.js"></script>
    <!-- icon -->
    <link rel="stylesheet" href="assets/plugins/bootstrap-iconpicker/css/bootstrap-iconpicker.min.css"/>

    <link href="assets/plugins/formvalidation/css/formValidation.min.css" rel="stylesheet" type="text/css"/>
    <script src="assets/plugins/formvalidation/formValidation.min.js" type="text/javascript"></script>
    <script src="assets/plugins/formvalidation/framework/bootstrap.js" type="text/javascript"></script>
    <script src="assets/plugins/formvalidation/language/zh_CN.js" type="text/javascript"></script>

    <script type="text/javascript" src="assets/plugins/jquery.form.js"></script>
    <script type="text/javascript" src="assets/others/script/ajax-dict-select.js"></script>
    <script type="text/javascript" src="assets/others/script/validation-ext.js"></script>

    <style>
        .ztree .switch {
            position: static;
        }
    </style>
</head>
<body>
<div class="row" style="margin-top:10px;margin-left:10px;">
    <div class="col-xs-4">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    资源列表
                    <button class="btn btn-success btn-xs pull-right" title="新增" onclick="setFormToAdd()">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <div class="clearfix"></div>
                </h3>
            </div>
            <div class="panel-body">
                <div id="tree" class="ztree"></div>
            </div>
        </div>
    </div>
    <div class="col-xs-8">
        <form id="editForm" class="form-horizontal" role="form" action="permission/save" method="post"
              data-fv-message="This value is not valid"
              data-fv-icon-valid="glyphicon glyphicon-ok" data-fv-icon-invalid="glyphicon glyphicon-remove"
              data-fv-icon-validating="glyphicon glyphicon-refresh">
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">资源名称</label>
                <div class="col-sm-6">
                    <input class="form-control" id="id" name="id" type="hidden" value="0"/>
                    <input class="form-control" id="parentId" name="parentId" type="hidden" value="0"/>
                    <input class="form-control" id="name" name="name" type="text" data-fv-notempty="true"/>
                </div>
            </div>
            <div class="form-group">
                <label for="path" class="col-sm-2 control-label">图标</label>
                <div class="col-sm-6">
                    <button name="icon" class="btn btn-default" data-iconset="glyphicon" data-icon="fa fa-suitcase"
                            role="iconpicker" data-search="false"></button>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">依附于</label>
                <div class="col-sm-6">
                    <input class="form-control" name="parentName" id="parentName" type="text" placeholder="请单击选择"
                           readonly onclick="showMenu()">
                    <div id="menuContent"
                         style="display: none;border: 1px solid rgba(0,0,0,.2);box-shadow: 0 5px 10px rgba(0,0,0,.2);">
                        <ul id="comboTree" class="ztree" style="overflow-y: scroll;height: 190px"></ul>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="path" class="col-sm-2 control-label">路径(以“/”开头)</label>
                <div class="col-sm-6">
                    <input class="form-control" id="path" name="path" type="text"/>
                </div>
            </div>
            <div class="form-group">
                <label for="type" class="col-sm-2 control-label">资源类型</label>
                <div class="col-sm-6">
                    <select data-dict-url="dict/list/PERMISSION/TYPE" data-dict-value="${entity.type.value}"
                            data-fv-icon="false" data-fv-notempty="true" class="form-control selectpicker" id="type"
                            name="type" title="请选择"></select>
                </div>
            </div>
            <div class="form-group">
                <label for="path" class="col-sm-2 control-label">排序</label>
                <div class="col-sm-6">
                    <input class="form-control" id="ordered" name="ordered" type="text" value="1"
                           data-fv-notempty="true" data-fv-integer="true"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-push-3 col-sm-9">
                    <button type="button" id="save" onclick="toSubmit()" class="btn btn-primary">新增</button>
                    <button type="button" id="reset" class="btn btn-default" onclick="toReset()">取消</button>
                    <button type="button" id="delete" onclick="doDel()" data-toggle="modal" class="btn btn-danger"
                            style="display: none">删除
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    // 设置右侧表单为新增模式
    function setFormToAdd() {
        $("form").data('formValidation').resetForm();
        $('#editForm').clearForm(true);
        $("#id").val('0');
        $("#parentId").val('0');
        $("#type").selectpicker('val', '');
        $('#delete').hide();
        $('#save').html('新增');
        $('#editForm').attr("action", "permission/save");
    }

    // 设置右侧表单为编辑模式
    function setFormToEdit() {
        $("form").data('formValidation').resetForm();
        $('#editForm').clearForm(true);
        $("#type").selectpicker('val', '');
        $('#delete').show();
        $('#save').html('修改');
        $('#editForm').attr("action", "permission/edit");
    }

    function toReset() {
        if ($('#editForm').attr("action") == "permission/edit") {
            setFormToAdd();
        } else {
            var treeObj = $.fn.zTree.getZTreeObj("tree");
            var node = treeObj.getNodeByParam("id", $("#id").val(), null);
            initForm(null, null, node);
        }
    }

    function toSubmit() {
        $("form").data('formValidation').validate();
        if ($("form").data('formValidation').isValid()) {
            $("#editForm").ajaxSubmit(function (data) {
                if (data.success) {
                    $.alert("操作成功！");
                    // 刷新树中的数据
                    refreshTrees();

                    // 设置左侧树选中刚才新增或修改的节点
                    var treeObj = $.fn.zTree.getZTreeObj("tree");
                    var node = treeObj.getNodeByParam("id", data.message, null);
                    treeObj.selectNode(node);

                    // 设置右侧表单为编辑模式
                    initForm(null, null, node);
                } else {
                    $.error(data.message);
                }
            });
        }
    }

    function doDel() {
        var id = $("#id").val();
        if (id == null || id == "") {
            return;
        }

        BootstrapDialog.confirm('该操作会删除节点“' + $("#name").val() + "”，以及其子节点，是否确认删除?", function (result) {
            if (!result) {
                return;
            }

            // 获取该节点下所有的子节点
            var treeObj = $.fn.zTree.getZTreeObj("tree");
            var node = treeObj.getNodeByParam("id", id, null);
            var ids = [];
            ids = getChildren(ids, node);

            // 把需要删除的节点拼接成字符串
            var permissionIds = "";
            $.each(ids, function (index, value) {
                permissionIds += ("," + value);
            });

            $.ajax({
                type: "POST",
                url: 'permission/remove',
                data: {"ids": permissionIds},
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        $.alert(data.message);
                        setFormToAdd();
                        refreshTrees();
                    } else {
                        $.error(data.message);
                    }
                }
            });
        });
    }

    // 获取所有的子节点ID
    function getChildren(ids, treeNode) {
        ids.push(treeNode.id);
        if (treeNode.isParent) {
            for (var obj in treeNode.children) {
                getChildren(ids, treeNode.children[obj]);
            }
        }
        return ids;
    }

    var setting = {
        view: {
            dblClickExpand: false
        },
        data: {
            simpleData: {
                enable: true,
                pIdKey: "parentId"
            }
        }
    };

    // 显示右侧的comboTree
    function showMenu() {
        var obj = $("#parentName");
        var offset = $("#parentName").offset();
        $("#menuContent").css({left: offset.left + "px", top: offset.top + obj.outerHeight() + "px"}).slideDown("fast");

        $("body").bind("mousedown", onBodyDown);
    }

    // 隐藏右侧的comboTree
    function hideMenu() {
        $("#menuContent").fadeOut("fast");
        $("body").unbind("mousedown", onBodyDown);
    }

    // 绑定隐藏事件
    function onBodyDown(event) {
        if (!(event.target.id == "parentName" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
            hideMenu();
        }
    }

    // 生成树
    function refreshTrees() {
        $.ajax({
            url: 'permission/all',
            dataType: "json",
            async: false
        }).done(function (datas) {
            expandNodes($.fn.zTree.init($("#tree"), $.extend({}, setting, {
                callback: {
                    onClick: initForm
                }
            }), datas), 0);

            // 为右侧的comboTree额外增加根节点展示
            datas.push({id: '0', name: '根节点', parentId: '-1'});

            expandNodes($.fn.zTree.init($("#comboTree"), $.extend({}, setting, {
                callback: {
                    beforeClick: check,
                    onClick: setParentId
                }
            }), datas), 2);
        });
    }

    // 展开节点
    function expandNodes(treeObj, level) {
        var nodes = treeObj.transformToArray(treeObj.getNodes());
        $.each(nodes, function (index, node) {
            if (node.level < level) {
                treeObj.expandNode(node, true, false, false);
            }
        });
    }

    $(function () {
        refreshTrees();
    });

    // 为右侧表单赋值
    function initForm(event, treeId, treeNode) {
        setFormToEdit();

        $("#id").val(treeNode.id);
        $("#name").val(treeNode.name);
        $("#path").val(treeNode.path);
        $("#type").selectpicker('val', treeNode.type.value);
        $("#ordered").val(treeNode.ordered);
        $("button[name='icon']").iconpicker('setIcon', treeNode.icon == null ? "" : treeNode.icon);

        if (treeNode.parentId == null) {
            treeNode.parentId = "0";
        }

        var treeObj = $.fn.zTree.getZTreeObj("comboTree");
        treeObj.cancelSelectedNode();
        var parentNode = treeObj.getNodeByParam("id", treeNode.parentId, null);
        treeObj.selectNode(parentNode);
        setParentId(null, null, parentNode);
    }

    // 检查是否可以选择
    function check(treeId, treeNode, clickFlag) {
        if (treeNode.id == $("#id").val()) {
            $.alert("不能选择依附于自己");
            return false;
        }
        return true;
    }

    // 设置父节点
    function setParentId(event, treeId, parentNode) {
        if (parentNode == null) {
            $("#parentId").val('0');
            $("#parentName").val('');
            return;
        }

        $("#parentId").val(parentNode.id);
        var parentName = parentNode.name;
        parentNode = parentNode.getParentNode();

        while (parentNode != null) {
            parentName = parentNode.name + "/" + parentName;
            parentNode = parentNode.getParentNode();
        }

        $("#parentName").val(parentName);
        hideMenu();
    }
</script>

<script type="text/javascript" src="assets/plugins/bootstrap-iconpicker/js/iconset/iconset-glyphicon.min.js"></script>
<script type="text/javascript" src="assets/plugins/bootstrap-iconpicker/js/bootstrap-iconpicker.js"></script>
</body>
</html>