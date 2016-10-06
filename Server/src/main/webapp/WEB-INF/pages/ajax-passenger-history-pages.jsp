<%@ include file="include.jsp"%>

<html>
<head>
    <title>User History page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

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
        function redirectPage(goPage) {
            $.ajax({
                type: "POST",
                url: "/${APP_NAME}/user-history",
                data: {
                    page : goPage
                },
                success: function(resp){
                    if (resp == "SUCCESS") {
                        window.location = "/${APP_NAME}/user-history";
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
<body>
    <c:set var="list" value="${orders}"/>
    <c:set var="page" value="${page}"/>
    <c:set var="pageMax" value="${pageMax}"/>
    <c:set var="orsersOnPage" value="${quantityOrdersOnPage}"/>

    <div class="container">
        <h1>USER HISTORY: page ${page} from ${pageMax}</h1>

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
                <th>time create</th>
                <th>address from</th>
                <th>address to</th>
                <th>distance, km</th>
                <th>price, uah</th>
                <th>show</th>
                <th>use to make new</th>
            </tr>

            <c:forEach items="${list}" var="order">
                <tr>
                    <td><c:out value="${order.id}" /></td>
                    <td><c:out value="${order.orderStatus}"/></td>
                    <td><c:out value="${order.timeCreate}"/></td>
                    <td><c:out value="${order.from.country},
                                        ${order.from.city},
                                        ${order.from.street},
                                        ${order.from.houseNum}"/></td>
                    <td><c:out value="${order.to.country},
                                        ${order.to.city},
                                        ${order.to.street},
                                        ${order.to.houseNum}"/></td>
                    <td><c:out value="${order.distance}"/></td>
                    <td><c:out value="${order.price}"/></td>

                    <td><button onclick="showOrderInfo(${order.id})"
                                style="background-color:yellow">
                                        SHOW</button></td>

                    <td><button onclick="copyOrderToMakeNew(${order.id})"
                                style="background-color:lightgreen">
                                        USE NOW</button></td>
                </tr>
            </c:forEach>
        </table>

        <ul>
            <p>
                <c:if test="${page != 1}">
                    <button onclick="redirectPage(${page - 1})">
                        <= PREVIOUS ${ordersOnPage}
                    </button>
                </c:if>
                <c:if test="${page != pageMax}">
                    <button onclick="redirectPage(${page + 1})">
                        NEXT ${ordersOnPage} =>
                    </button>
                </c:if>
            </p>
        </ul>
    </div>

</body>
</html>
