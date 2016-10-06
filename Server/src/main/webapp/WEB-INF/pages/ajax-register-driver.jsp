<%@ include file="include.jsp"%>

<html>
<head>
    <title>Register Driver Page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function sendNewRegData() {
            var phone = $("#new_phone").val();
            var password = $("#new_pass").val();
            var name = $("#new_name").val();
            var carType = $("#new_carType").val();
            var carModel = $("#new_carModel").val();
            var carNumber = $("#new_carNumber").val();

            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/register-driver",
                data: {
                    phone : phone,
                    password : password,
                    name : name,
                    carType : carType,
                    carModel : carModel,
                    carNumber : carNumber
                },
                success: function(resp){
                    if (resp == "SUCCESS") {
                        alert("Congratulations!\nYou are registered successful!\nWelcome to MyTaxi! :)");
                        window.location = "/${APP_NAME}/user-info";
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
        function sendChangedRegData() {
            var phone = $("#user_phone").val();
            var password = $("#user_pass").val();
            var name = $("#user_name").val();
            var carType = $("#user_carType").val();
            var carModel = $("#user_carModel").val();
            var carNumber = $("#user_carNumber").val();

            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/register-driver",
                data: {
                    phone : phone,
                    password : password,
                    name : name,
                    carType : carType,
                    carModel : carModel,
                    carNumber : carNumber
                },
                success: function(resp){
                    if (resp == "SUCCESS") {
                        alert("Your register data was changed");
                        window.location = "/${APP_NAME}/user-info";
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
        function returnToLogin() {
            window.location = "/${APP_NAME}/login";
        }
    </script>

    <script>
        function colorBackground(x) {
            x.style.background = "lemonchiffon";
        }
    </script>

    <style>
        h1 {
            font-family: arial, sans-serif;
            color: darkslateblue;
        }

        body {
            font-family: arial, sans-serif;
        }
    </style>

</head>
<body>

<c:set var="user" value="${user}"/>

<div class="container">
    <h1>REGISTER DRIVER</h1>

    <!-- for CHANGE registration -->
    <c:if test="${user != null}">
        <ul>
            <p>
                <button onclick="redirectUserInfo()"  style="background-color:lightgrey">
                    RETURN TO MENU</button>
            </p>
        </ul>
        <ul>
            <li>Input phone:
                <input id="user_phone" type="text" value="${user.phone}" onfocus="colorBackground(this)">
            </li>
            <li>Input password:
                <input id="user_pass" type="password" value="${user.pass}" onfocus="colorBackground(this)">
            </li>
            <li>Input name:
                <input id="user_name" type="text" value="${user.name}" onfocus="colorBackground(this)">
            </li>
            <li>Input car type:
                <input id="user_carType" type="text" value="${user.car.type}" onfocus="colorBackground(this)">
            </li>
            <li>Input car model:
                <input id="user_carModel" type="text" value="${user.car.model}" onfocus="colorBackground(this)">
            </li>
            <li>Input car number:
                <input id="user_carNumber" type="text" value="${user.car.number}" onfocus="colorBackground(this)">
            </li>
        </ul>

        <ul>
            <p>
                <button onclick="sendChangedRegData()"  style="background-color:lightgreen">
                    CHANGE REGISTRATION DATA</button>
            </p>
        </ul>
    </c:if>

    <!-- for NEW registration -->
    <c:if test="${user == null}">
        <ul>
            <li>Input phone:
                <input id="new_phone" type="text" onfocus="colorBackground(this)">
            </li>
            <li>Input password:
                <input id="new_pass" type="password" onfocus="colorBackground(this)">
            </li>
            <li>Input name:
                <input id="new_name" type="text" onfocus="colorBackground(this)">
            </li>
            <li>Input car type:
                <input id="new_carType" type="text" onfocus="colorBackground(this)">
            </li>
            <li>Input car model:
                <input id="new_carModel" type="text" onfocus="colorBackground(this)">
            </li>
            <li>Input car number:
                <input id="new_carNumber" type="text" onfocus="colorBackground(this)">
            </li>
        </ul>
        <ul>
            <p>
                <button onclick="sendNewRegData()"  style="background-color:lightgreen">
                    REGISTER</button>
            </p>
        </ul>
    </c:if>

    <ul>
        <p>
            <button onclick="returnToLogin()"  style="background-color:lightgrey">
                EXIT TO LOGIN</button>
        </p>
    </ul>

</div>

</body>
</html>