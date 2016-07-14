<%@ include file="/WEB-INF/pages/include.jsp"%>
<html>
<head>
    <title>Main</title>

    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
          integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
          crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
          integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r"
          crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
            integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
            crossorigin="anonymous"></script>


</head>
<body>

<div class="header">

    <ul>
        <li><a href="login">Login</a></li>
    </ul>

    <c:if test="${!inSystem}">
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

    </c:if>
    <c:if test="${inSystem}">
        <ul>
            <li>Hello ${currentUserName}</li>
        </ul>
    </c:if>

</div>

</body>
</html>