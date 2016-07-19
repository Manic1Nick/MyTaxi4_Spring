<%@ include file="include.jsp"%>

<html>
<head>
    <title>Order Info page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function cancelOrder() {
            var id = $("#idOrder").data('id');
            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/order/cancel?id=" + id,
                data: {
                    id : id
                },
                success: function(resp){
                    if (resp == "CANCELLED") {
                        $("#statusOrder").html("status : " + resp);
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
        function closeOrder() {
            var id = $("#idOrder").data('id');
            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/order/close?id=" + id,
                data: {
                    id : id
                },
                success: function(resp){
                    if (resp == "CLOSED") {
                        $("#statusOrder").html("status : " + resp);
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
        function redirectUserInfo() {
            window.location = "/${APP_NAME}/user-info";
        }
    </script>

</head>
<body>

    <c:set var="transfered" value="${order}"/>
    <%--<div id="idOrder" class="comment" data-id=${transfered.id}></div>--%>

<div class="container">
    <h1>Order Info page</h1>

    <ul>
        <p>
            <button onclick="redirectUserInfo()"  style="background-color:lightgrey">
                RETURN TO MENU</button>
        </p>
    </ul>

    <ul>
        <li>
            <div id="idOrder" class="column" data-id=${transfered.id}>
                id : ${transfered.id}
            </div>
        </li>

        <li>
            <div id="statusOrder" class="column">
                status : ${transfered.orderStatus}
            </div>
        </li>

        <li>
            <div class="column">
                address from : ${transfered.from.country},
                                ${transfered.from.city},
                                ${transfered.from.street},
                                ${transfered.from.houseNum}
            </div>
        </li>

        <li>
            <div class="column">
                address to : ${transfered.to.country},
                                ${transfered.to.city},
                                ${transfered.to.street},
                                ${transfered.to.houseNum}
            </div>
        </li>

        <li>
            <div class="column">
                passenger : ${transfered.passenger.name},
                            ${transfered.passenger.phone}
            </div>
        </li>


        <c:if test="${transfered.driver != null}">
            <div class="column">
                driver : ${transfered.driver.name},
                    ${transfered.driver.phone}
            </div>

            <div class="column">
                car : ${transfered.driver.car.model},
                    ${transfered.driver.car.number}
            </div>
        </c:if>


        <li>
            <div class="column">
                distance, km : ${transfered.distance}
            </div>
        </li>

        <li>
            <div class="column">
                price, uah : ${transfered.price}
            </div>
        </li>

        <c:if test="${transfered.message != null}">
            <li>
                <div class="column">
                    message : ${transfered.message}
                </div>
            </li>
        </c:if>

        <c:set var="user" value="${user}"/>


        <c:if test="${user.identifier == 'P'}">
            <p>
                <button onclick="cancelOrder()">
                    CANCEL ORDER</button>
            </p>
        </c:if>
        <c:if test="${user.identifier == 'D'}">
            <p>
                <button onclick="takeOrder()">
                    TAKE ORDER</button>
            </p>
        </c:if>

        <p>
            <button onclick="closeOrder()">
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
            <button onclick="addMessage()">
                ADD MESSAGE</button>
        </p>
    </ul>
</div>

</body>
</html>
