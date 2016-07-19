<%@ include file="include.jsp"%>

<html>
<head>
    <title>Register Passenger Page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function sendNewRegData() {
            var phone = $("#new_phone").val();
            var password = $("#new_pass").val();
            var name = $("#new_name").val();
            var country = $("#new_country").val();
            var city = $("#new_city").val();
            var street = $("#new_street").val();
            var houseNum = $("#new_houseNum").val();

            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/register-passenger",
                data: {
                    phone : phone,
                    password : password,
                    name : name,
                    country : country,
                    city : city,
                    street : street,
                    houseNum : houseNum
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
            var phone = ${transfered.id}
            var password = $("#user_pass").val();
            var name = $("#user_name").val();
            var country = $("#user_country").val();
            var city = $("#user_city").val();
            var street = $("#user_street").val();
            var houseNum = $("#user_houseNum").val();

            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/register-passenger",
                data: {
                    phone : phone,
                    password : password,
                    name : name,
                    country : country,
                    city : city,
                    street : street,
                    houseNum : houseNum
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

<c:set var="user" value="${user}"/>

<div class="container">
    <h1>Register Passenger form</h1>

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
                <input id="user_name" type="text" value="${user.name}">
            </li>
            <li>Input country:
                <input id="user_country" type="text" value="${user.homeAddress.country}">
            </li>
            <li>Input city:
                <input id="user_city" type="text" value="${user.homeAddress.city}">
            </li>
            <li>Input street:
                <input id="user_street" type="text" value="${user.homeAddress.street}">
            </li>
            <li>Input house number:
                <input id="user_houseNum" type="text" value="${user.homeAddress.houseNum}">
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
            <li>Input country:
                <input id="new_country" type="text">
            </li>
            <li>Input city:
                <input id="new_city" type="text">
            </li>
            <li>Input street:
                <input id="new_street" type="text">
            </li>
            <li>Input house number:
                <input id="new_houseNum" type="text">
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