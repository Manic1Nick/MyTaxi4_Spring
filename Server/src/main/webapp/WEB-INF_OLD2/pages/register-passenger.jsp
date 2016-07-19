<%@ include file="include.jsp"%>

<html>
<head>
    <title>Register Passenger Page</title>
</head>
<body>

<c:set var="user" value="${user}"/>

<div class="container">
    <h1>Register Passenger form</h1>

    <!-- for CHANGE registration -->
    <c:if test="${user != null}">
        <form method="post" action="register-passenger">
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
                <li>Input country:
                    <input name="country" type="text" value="${user.homeAddress.country}">
                </li>
                <li>Input city:
                    <input name="city" type="text" value="${user.homeAddress.city}">
                </li>
                <li>Input street:
                    <input name="street" type="text" value="${user.homeAddress.street}">
                </li>
                <li>Input house number:
                    <input name="houseNum" type="text" value="${user.homeAddress.houseNum}">
                </li>
            </ul>
            <ul>
                <p>
                    <a href="/${APP_NAME}/user-info">
                        <input type="submit" value="REGISTER" name="user-info"/></a>
                </p>
                <p>
                    <a href="/${APP_NAME}/login">
                        <input type="button" value="RETURN TO LOGIN" name="login"/></a>
                </p>
            </ul>
        </form>
    </c:if>

    <!-- for NEW registration -->
    <c:if test="${user == null}">
        <form method="post" action="register-passenger">
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
                <li>Input country:
                    <input name="country" type="text">
                </li>
                <li>Input city:
                    <input name="city" type="text">
                </li>
                <li>Input street:
                    <input name="street" type="text">
                </li>
                <li>Input house number:
                    <input name="houseNum" type="text">
                </li>
            </ul>
            <ul>
                <p>
                    <a href="/${APP_NAME}/user-info">
                        <input type="submit" value="REGISTER" name="user-info"/></a>
                </p>
                <p>
                    <a href="/${APP_NAME}/login">
                        <input type="button" value="RETURN TO LOGIN" name="login"/></a>
                </p>
            </ul>
        </form>
    </c:if>

</div>

</body>
</html>