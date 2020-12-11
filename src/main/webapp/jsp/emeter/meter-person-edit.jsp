<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>电表委派</title>

    <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
    <script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
</head>
<body>
<section class="content">
    <div class="box box-primary">
        <form id="edit-form" class="form-horizontal" action="${empty entity? 'meter/save' : 'meter/save'}" method="post"
              data-fv-framework="bootstrap">
            <c:if test="${not empty entity}">
                <input type="hidden" name="id" value="${entity.id}"/>
            </c:if>
            <div class="box-body">

                <div class="form-group">
                    <label class="col-sm-2 control-label">表号</label>
                    <div class="col-sm-6">
                        <input type="text" readonly class="form-control" name="meterNo" value="${entity.meterNo}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">委派人员</label>

                    <div class="col-sm-6">
                        <select data-dict-url="meter/person?id=${entity.id}" data-dict-value="${entity.userCode}"
                                id="userCode" name="userCode" class="form-control selectpicker" title="请选择"
                                data-fv-notempty="false" data-fv-icon="false"></select>
                    </div>
                </div>


            </div>

        </form>
    </div>
</section>
</body>
</html>
