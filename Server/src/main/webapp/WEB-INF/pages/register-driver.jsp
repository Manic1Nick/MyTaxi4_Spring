<%@ include file="include.jsp"%>

<html>
<head>
    <title>Register Driver Page</title>
</head>
<body>

<div class="container">
    <h1>Register Driver form</h1>

    <form method="post" action="register-driver">
w
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


</div>

</body>
</html>