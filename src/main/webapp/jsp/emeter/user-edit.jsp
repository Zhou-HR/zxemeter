<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>编辑用户</title>

    <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
    <script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
</head>
<body>
<section class="content">
    <div class="box box-primary">
        <form id="edit-form" class="form-horizontal" action="${empty entity? 'user/save' : 'user/edit'}" method="post"
              data-fv-framework="bootstrap">

        </form>
    </div>
</section>
</body>
</html>
