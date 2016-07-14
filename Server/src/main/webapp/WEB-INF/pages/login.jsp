<%@ include file="include.jsp"%>
<html>
<head>
    <title>Login</title>
</head>
<body>

<div class="container">
    <h1>Input login info</h1>

    <form method="post" action="login">
        <ul>
            <li>Input phone:
                <input name="phone" type="text">
            </li>
            <li>Input password:
                <input name="pass" type="password">
            </li>

            <p>
                <a href="user-info">
                    <input type="submit" value="LOGIN" name="user-info"/>
                </a>
            </p>
        </ul>

        <ul>
            <p>
            <a href="register-passenger">
                <input type="button" value="REGISTER PASSENGER" name="register-passenger"/>
            </a>
            </p>

            <p>
                <a href="register-driver">
                    <input type="button" value="REGISTER DRIVER" name="register-driver"/>
                </a>
            </p>
        </ul>
    </form>


</div>

</body>
</html>
