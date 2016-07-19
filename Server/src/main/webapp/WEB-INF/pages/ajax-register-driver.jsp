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
                    if (resp == "success") {
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
        function returnToLogin() {
            window.location = "/${APP_NAME}/login";
        }
    </script>
</head>
<body>

<div class="container">
    <h1>Register Driver form</h1>

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
                <input id="user_phone" type="text" value="${user.phone}">
            </li>
            <li>Input password:
                <input id="user_pass" type="password" value="${user.pass}">
            </li>
            <li>Input name:
                <input id="user_user_name" type="text" value="${user.name}">
            </li>
            <li>Input car type:
                <input id="user_carType" type="text" value="${user.car.type}">
            </li>
            <li>Input car model:
                <input id="user_carModel" type="text" value="${user.car.model}">
            </li>
            <li>Input car number:
                <input id="user_carNumber" type="text" value="${user.car.number}">
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
                <input id="new_phone" type="text">
            </li>
            <li>Input password:
                <input id="new_pass" type="password">
            </li>
            <li>Input name:
                <input id="new_name" type="text">
            </li>
            <li>Input car type:
                <input id="new_carType" type="text">
            </li>
            <li>Input car model:
                <input id="new_carModel" type="text">
            </li>
            <li>Input car number:
                <input id="new_carNumber" type="text">
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