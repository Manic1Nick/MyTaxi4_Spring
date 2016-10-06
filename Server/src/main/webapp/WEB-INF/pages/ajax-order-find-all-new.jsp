<%@ include file="../../WEB-INF/pages/include.jsp"%>

<html>
<head>
    <title>Order Find page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function redirectUserInfo() {
            window.location = "/${APP_NAME}/user-info";
        }
    </script>

    <script>
        function showOrderInfo(id) {
            $.ajax({
                type: "POST",
                url: "/${APP_NAME}/order/get",
                data: {
                    orderID : id
                },
                success: function(resp){
                    if (resp == "SUCCESS") {
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
        function takeOrderNow(id) {
            $.ajax({
                type: "GET",
                url: "/${APP_NAME}/order/take",
                data: {
                    orderID : id
                },
                success: function(resp) {
                    if (resp == "TAKEN") {
                        alert("Order " + id + " was taken by you");
                        showOrderInfo(id);
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
        function helloCurrentOrders(quantity) {
           alert("Found " + quantity + " orders with status NEW");
        }
    </script>

    <style>
        h1 {
            font-family: arial, sans-serif;
            color: darkslateblue;
        }

        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }

        td, th {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        tr:nth-child(even) {
            background-color: #dddddd;
        }
    </style>
</head>
<body onload="helloCurrentOrders(${quantity})">

    <c:set var="list" value="${orders}"/>

    <div class="container">
        <h1>CURRENT NEW ORDERS</h1>

        <ul>
            <p>
                <button onclick="redirectUserInfo()" style="background-color:lightgrey">
                    RETURN TO MENU</button>
            </p>
        </ul>

        <table>
            <tr>
                <th>id</th>
                <th>status</th>
                <th>address to</th>
                <th>price, uah</th>
                <th>distance to you, km</th>
                <th>show</th>
                <th>take</th>
            </tr>

            <c:forEach items="${list}" var="order">
                <tr>
                    <td><c:out value="${order.id}"/></td>
                    <td><c:out value="${order.orderStatus}"/></td>
                    <td><c:out value="${order.to.country},
                                        ${order.to.city},
                                        ${order.to.street},
                                        ${order.to.houseNum}"/></td>
                    <td><c:out value="${order.price}"/></td>
                    <td><c:out value="${order.distanceToDriver}"/></td>

                    <td><button onclick="showOrderInfo(${order.id})"
                                style="background-color:yellow">
                                    SHOW</button></td>

                    <td><button onclick="takeOrderNow(${order.id})"
                                style="background-color:lightgreen">
                                    TAKE NOW</button></td>
                </tr>
            </c:forEach>
        </table>
    </div>

</body>
</html>
