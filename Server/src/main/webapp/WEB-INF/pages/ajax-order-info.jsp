<%@ include file="include.jsp"%>

<html>
<head>
    <title>Order Info page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function cancelOrder(id) {
            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/order/cancel",
                data: {
                    id : id
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
        function closeOrder(id) {
            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/order/close",
                data: {
                    id : id
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
                    id : id
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
                    id : id,
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

    <c:set var="transfered" value="${order}"/>
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
            <td id="idOrder" data-id=${transfered.id}>id</td>
            <td>${transfered.id}</td>
        </tr>
        <tr>
            <td>status</td>
            <td id="statusOrder">${transfered.orderStatus}</td>
        </tr>
        <tr>
            <td>time create</td>
            <td id="timeCreate">${transfered.timeCreate}</td>
        </tr>

        <c:if test="${transfered.timeCancelled != null}">
            <tr>
                <td>time cancelled</td>
                <td id="timeCancelled">${transfered.timeCancelled}</td>
            </tr>
        </c:if>
        <c:if test="${transfered.timeTaken != null}">
            <tr>
                <td>time taken</td>
                <td id="timeTaken">${transfered.timeTaken}</td>
            </tr>
        </c:if>
        <c:if test="${transfered.timeClosed != null}">
            <tr>
                <td>time closed</td>
                <td id="timeClosed">${transfered.timeClosed}</td>
            </tr>
        </c:if>

        <tr>
            <td>address from</td>
            <td>${transfered.from.country},
                ${transfered.from.city},
                ${transfered.from.street},
                ${transfered.from.houseNum}</td>
        </tr>
        <tr>
            <td>address to</td>
            <td>${transfered.to.country},
                ${transfered.to.city},
                ${transfered.to.street},
                ${transfered.to.houseNum}</td>
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
            <td>${transfered.distance}</td>
        </tr>
        <tr>
            <td>price, uah</td>
            <td>${transfered.price}</td>
        </tr>

        <tr>
            <td>message</td>
            <td id="old_message">${transfered.message}</td>
        </tr>
    </table>

    <ul>
        <c:set var="user" value="${user}"/>
        <div id="user_name" class="comment" data-id=${user.name}></div>

        <c:if test="${user.identifier == 'P'}">
            <p>
                <button onclick="cancelOrder(${transfered.id})">
                    CANCEL ORDER</button>
            </p>
        </c:if>
        <c:if test="${user.identifier == 'D'}">
            <c:if test="${transfered.orderStatus == 'NEW'}">
                <p>
                    <button onclick="takeOrder(${transfered.id})" style="background-color:lightgreen">
                        TAKE ORDER</button>
                </p>
            </c:if>
        </c:if>

        <p>
            <button onclick="closeOrder(${transfered.id})">
                CLOSE ORDER</button>
        </p>

        <p>
            <button onclick="makeCall()">
                MAKE CALL</button>
        </p>
        <p>
            <button onclick="showMap()">
                SHOW MAP</button>
        </p>
        <p>
            <button onclick="createNewMessage(${transfered.id})">
                ADD MESSAGE</button>
        </p>
    </ul>
</div>

</body>
</html>
