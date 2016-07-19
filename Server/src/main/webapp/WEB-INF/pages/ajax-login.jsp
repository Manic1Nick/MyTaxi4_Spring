<%@ include file="include.jsp"%>

<html>
<head>
    <title>Login</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function sendLogin() {
            var phone = $("#phoneInput").val();
            var password = $("#passInput").val();
            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/login",
                data: {
                    phone : phone,
                    password : password
                },
                success: function(resp){
                    if (resp == "SUCCESS") {
                        redirectUserInfo();
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            };
            $.ajax(confObj);
        }
    </script>

    <script>
        function redirectUserInfo() {
            window.location = "/${APP_NAME}/user-info";
        }
    </script>
    <script>
        function redirectRegPassenger() {
            window.location = "/${APP_NAME}/register-passenger";
        }
    </script>
    <script>
        function redirectRegDriver() {
            window.location = "/${APP_NAME}/register-driver";
        }
    </script>

</head>

<body>
<div align="center" id="login">
    <p>
        <label>Input phone</label>
        <input id="phoneInput" type="text">
    </p>
    <p>
        <label>Input password</label>
        <input id="passInput" type="password"><br>
    </p>
    <p>
        <button onclick="sendLogin()" style="background-color:lightgreen">
            LOGIN</button>
    </p>
    <p>
        <button onclick="redirectRegPassenger()" style="background-color:lightgrey">
            REGISTER PASSENGER</button>
    </p>
    <p>
        <button onclick="redirectRegDriver()" style="background-color:lightgrey">
            REGISTER DRIVER</button>
    </p>
</div>
</body>
</html>