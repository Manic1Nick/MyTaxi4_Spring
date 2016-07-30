<%@ include file="include.jsp"%>

<html>
<head>
    <title>User Info page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

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

    <script>
        function redirectMakeOrder() {
            window.location = "/${APP_NAME}/order/make";
        }
    </script>

    <script>
        function redirectFindNewOrders() {
            $.ajax({
                type: "POST",
                url: "/${APP_NAME}/order/get/all-new",
                success: function(resp) {
                    if (resp.includes("SUCCESS")) {
                        window.location = "/${APP_NAME}/order/get/all-new";
                    } else if (resp.includes("NOTHING")) {
                        alert("There are no open orders with status NEW");
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            });
        }
    </script>

    <script>
        function redirectGetLastOrder() {
            var confObj = {
                type:"GET",
                url: "/${APP_NAME}/order/get/last",
                success: function(resp){
                    if (resp.includes("id:")) {
                        var arrayResp = resp.split(":", 2);
                        window.location = "/${APP_NAME}/order/get?id=" + arrayResp[1];
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
        function getAllUserOrders() {
            $.ajax({
                type: "POST",
                url: "/${APP_NAME}/user/get/orders",
                success: function(resp) {
                    if (resp.includes("SUCCESS")) {
                        window.location = "/${APP_NAME}/user-history";
                    } else if (resp.includes("NOTHING")) {
                        alert("You don't have any orders");
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            });
        }
    </script>

    <script>
        function redirectLogin() {
            var r = confirm("Are you sure to exit?");
            if (r == true) {
                window.location = "/${APP_NAME}/login";
            }
        }
    </script>

    <script>
        function beforeDeleteUser() {
            $.ajax({
                type:"GET",
                url: "/${APP_NAME}/user/delete",
                success: function(resp){
                    if (resp.includes("id:")) {
                        var arrayResp = resp.split(":", 2);
                        var r = confirm("Are you sure to delete user ID=" + arrayResp[1] + " ?");
                        if (r == true) {
                            deleteUser();
                        }
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            });
        }
    </script>

    <script>
        function deleteUser() {
            $.ajax({
                type:"POST",
                url: "/${APP_NAME}/user/delete",
                success: function(resp){
                    if (resp.includes("id:")) {
                        var arrayResp = resp.split(":", 2);
                        alert("User ID=" + arrayResp[1] + " will be deleted");
                        redirectLogin();
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            });
        }
    </script>

    <style>
        h1 {
            font-family: arial, sans-serif;
            color: darkslateblue;
        }

        div {
            font-family: arial, sans-serif;
        }

        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 50%;
        }

        td, th {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }
    </style>

</head>
<body>

    <div id="user-info"></div>

    <c:set var="transfered" value="${user}"/>

    <div class="container">
        <h1>USER INFO</h1>

        <table>
            <tr>
                <td>id</td>
                <td>${transfered.id}</td>
            </tr>
            <tr>
                <td>identifier</td>
                <td>${transfered.identifier}</td>
            </tr>
            <tr>
                <td>phone</td>
                <td>${transfered.phone}</td>
            </tr>
            <tr>
                <td>name</td>
                <td>${transfered.name}</td>
            </tr>

            <c:if test="${transfered.identifier == 'P'}">
                <tr>
                    <td>address country</td>
                    <td>${transfered.homeAddress.country}</td>
                </tr>
                <tr>
                    <td>address city</td>
                    <td>${transfered.homeAddress.city}</td>
                </tr>
                <tr>
                    <td>address street</td>
                    <td>${transfered.homeAddress.street}</td>
                </tr>
                <tr>
                    <td>address home number</td>
                    <td>${transfered.homeAddress.houseNum}</td>
                </tr>
            </c:if>
            <c:if test="${transfered.identifier == 'D'}">
                <tr>
                    <td>car type</td>
                    <td>${transfered.car.type}</td>
                </tr>
                <tr>
                    <td>car model</td>
                    <td>${transfered.car.model}</td>
                </tr>
                <tr>
                    <td>car number</td>
                    <td>${transfered.car.number}</td>
                </tr>
            </c:if>

        </table>

        <ul>
            <c:if test="${transfered.identifier == 'P'}">
                <p>
                    <button onclick="redirectMakeOrder()" style="background-color:lightgreen">
                        FIND TAXI</button>
                </p>
                <p>
                    <button onclick="redirectRegPassenger()">
                        CHANGE REGISTER DATA</button>
                </p>
            </c:if>

            <c:if test="${transfered.identifier == 'D'}">
                <p>
                    <button onclick="redirectFindNewOrders()" style="background-color:lightgreen">
                        FIND PASSENGERS</button>
                </p>
                <p>
                    <button onclick="redirectRegDriver()">
                        CHANGE REGISTER DATA</button>
                </p>
            </c:if>

            <p>
                <button onclick="redirectGetLastOrder()">
                    SHOW LAST ORDER</button>
            </p>
            <p>
                <button onclick="getAllUserOrders()">
                    SHOW MY HISTORY</button>
            </p>
            <p>
                <button onclick="redirectLogin()" style="background-color:lightgrey">
                    EXIT TO LOGIN</button>
            </p>
            <p>
                <button onclick="beforeDeleteUser()" style="background-color:lightgrey">
                    DELETE USER</button>
            </p>
        </ul>
    </div>

</body>
</html>
