<%@ include file="include.jsp"%>

<html>
<head>
    <title>Register Passenger Page</title>
</head>
<body>

<div class="container">
    <h1>Register Passenger form</h1>

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