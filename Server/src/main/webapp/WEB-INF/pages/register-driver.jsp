<%@ include file="include.jsp"%>

<html>
<head>
    <title>Register Driver Page</title>
</head>
<body>

<div class="container">
    <h1>Register Driver form</h1>

    <!-- for CHANGE registration -->
    <c:if test="${user != null}">
        <form method="post" action="register-driver">
            <ul>
                <li>Input phone:
                    <input name="phone" type="text" value="${user.phone}">
                </li>
                <li>Input password:
                    <input name="pass" type="password" value="${user.pass}">
                </li>
                <li>Input name:
                    <input name="name" type="text" value="${user.name}">
                </li>
                <li>Input car type:
                    <input name="carType" type="text" value="${user.car.type}">
                </li>
                <li>Input car model:
                    <input name="carModel" type="text" value="${user.car.model}">
                </li>
                <li>Input car number:
                    <input name="carNumber" type="text" value="${user.car.number}">
                </li>
            </ul>
            <ul>
                <p>
                    <a href="user-info">
                        <input type="submit" value="REGISTER" name="user-info"/></a>
                </p>
                <p>
                    <a href="login">
                        <input type="button" value="RETURN TO LOGIN" name="login"/></a>
                </p>
            </ul>
        </form>
    </c:if>

    <!-- for NEW registration -->
    <c:if test="${user == null}">
        <form method="post" action="register-driver">
            <ul>
                <li>Input phone:
                    <input name="phone" type="text">
                </li>
                <li>Input password:
                    <input name="pass" type="password">
                </li>
                <li>Input name:
                    <input name="name" type="text">
                </li>
                <li>Input car type:
                    <input name="carType" type="text">
                </li>
                <li>Input car model:
                    <input name="carModel" type="text">
                </li>
                <li>Input car number:
                    <input name="carNumber" type="text">
                </li>
            </ul>
            <ul>
                <p>
                    <a href="user-info">
                        <input type="submit" value="REGISTER" name="user-info"/></a>
                </p>
                <p>
                    <a href="login">
                        <input type="button" value="RETURN TO LOGIN" name="login"/></a>
                </p>
            </ul>
        </form>
    </c:if>


</div>

</body>
</html>