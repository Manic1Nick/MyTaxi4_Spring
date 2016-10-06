<%@ include file="include.jsp"%>

<html>
<head>
    <title>Order Info page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function beforeCancelOrder(id) {
            var r = confirm("Are you sure to cancel order ID=" + id + " ?");
            if (r == true) {
                cancelOrder(id);
            }
        }
    </script>

    <script>
        function cancelOrder(id) {
            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/order/cancel",
                data: {
                    orderID : id
                },
                success: function(resp){
                    if (resp == "CANCELLED") {
                        $("#statusOrder").html(resp);
                        alert("Status order was changed to " + resp);
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
        function beforeCloseOrder(id) {
            var r = confirm("Are you sure to close order ID=" + id + " ?");
            if (r == true) {
                closeOrder(id);
            }
        }
    </script>

    <script>
        function closeOrder(id) {
            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/order/close",
                data: {
                    orderID : id
                },
                success: function(resp){
                    if (resp == "CLOSED") {
                        $("#statusOrder").html(resp);
                        alert("Status order was changed to " + resp);
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
        function takeOrder(id) {
            $.ajax({
                type: "GET",
                url: "/${APP_NAME}/order/take",
                data: {
                    orderID : id
                },
                success: function(resp) {
                    if (resp == "TAKEN") {
                        alert("Order " + id + " was taken by you");
                        window.location = "/${APP_NAME}/order/get";
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
        function copyOrderToMakeNew(id) {
            $.ajax({
                type: "POST",
                url: "/${APP_NAME}/order/get",
                data: {
                    orderID : id
                },
                success: function(resp){
                    if (resp == "SUCCESS") {
                        window.location = "/${APP_NAME}/order/use-to-make";
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
        function redirectUserInfo() {
            window.location = "/${APP_NAME}/user-info";
        }
    </script>

    <script>
        function createNewMessage(id) {
            var message = prompt("Please enter your message", "your message");
            if (message != null) {
                addNewMessageToOrder(id, message);
            }
        }
    </script>

    <script>
        function addNewMessageToOrder(id, message) {
            $.ajax({
                type: "POST",
                url: "/${APP_NAME}/order/add-message",
                data: {
                    orderID : id,
                    message : message
                },
                success: function(resp) {
                    if (resp == "SUCCESS") {
                        window.location = "/${APP_NAME}/order/add-message";
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

    <c:set var="user" value="${user}"/>
    <c:set var="order" value="${order}"/>
    <c:set var="passenger" value="${passenger}"/>
    <c:set var="driver" value="${driver}"/>

<div class="container">
    <h1>ORDER INFO</h1>

    <ul>
        <p>
            <button onclick="redirectUserInfo()" style="background-color:lightgrey">
                RETURN TO MENU</button>
        </p>
    </ul>

    <table>
        <tr>
            <td id="idOrder" data-id=${order.id}>id</td>
            <td>${order.id}</td>
        </tr>
        <tr>
            <td>status</td>
            <td id="statusOrder">${order.orderStatus}</td>
        </tr>
        <tr>
            <td>time create</td>
            <td id="timeCreate">${order.timeCreate}</td>
        </tr>

        <c:if test="${order.timeCancelled != null}">
            <tr>
                <td>time cancelled</td>
                <td id="timeCancelled">${order.timeCancelled}</td>
            </tr>
        </c:if>
        <c:if test="${order.timeTaken != null}">
            <tr>
                <td>time taken</td>
                <td id="timeTaken">${order.timeTaken}</td>
            </tr>
        </c:if>
        <c:if test="${order.timeClosed != null}">
            <tr>
                <td>time closed</td>
                <td id="timeClosed">${order.timeClosed}</td>
            </tr>
        </c:if>

        <tr>
            <td>address from</td>
            <td>${order.from.country},
                ${order.from.city},
                ${order.from.street},
                ${order.from.houseNum}</td>
        </tr>
        <tr>
            <td>address to</td>
            <td>${order.to.country},
                ${order.to.city},
                ${order.to.street},
                ${order.to.houseNum}</td>
        </tr>
        <tr>
            <td>passenger</td>
            <td>${passenger.name}, ${passenger.phone}</td>
        </tr>

        <c:if test="${driver != null}">
            <tr>
                <td>driver</td>
                <td>${driver.name}, ${driver.phone}</td>
            </tr>
            <tr>
                <td>car</td>
                <td>${driver.car.model}, ${driver.car.number}</td>
            </tr>
        </c:if>

        <tr>
            <td>distance, km</td>
            <td>${order.distance}</td>
        </tr>
        <tr>
            <td>price, uah</td>
            <td>${order.price}</td>
        </tr>

        <tr>
            <td>message</td>
            <td id="old_message">${order.message}</td>
        </tr>
    </table>

    <ul>
        <c:set var="user" value="${user}"/>
        <div id="user_name" class="comment" data-id=${user.name}></div>

        <p>
            <c:if test="${user.identifier == 'P'}">

                <button onclick="beforeCancelOrder(${order.id})" style="background-color:lightsalmon">
                    CANCEL ORDER</button>

                <button onclick="beforeCloseOrder(${order.id})" style="background-color:yellow">
                    CLOSE ORDER</button>

                <button onclick="copyOrderToMakeNew(${order.id})" style="background-color:lightgreen">
                    COPY AS NEW</button>
            </c:if>
            <c:if test="${user.identifier == 'D'}">
                <c:if test="${order.orderStatus == 'NEW'}">

                    <button onclick="takeOrder(${order.id})" style="background-color:lightgreen">
                        TAKE ORDER</button>
                </c:if>

                <button onclick="beforeCloseOrder(${order.id})" style="background-color:yellow">
                    CLOSE ORDER</button>
            </c:if>
        </p>
        <p>
            <button onclick="makeCall()">
                MAKE CALL</button>

            <button onclick="showMap()">
                SHOW MAP</button>
        </p>
        <p>
            <button onclick="createNewMessage(${order.id})">
                ADD MESSAGE</button>
        </p>
    </ul>
</div>

</body>
</html>
