<%@ include file="../../WEB-INF/pages/include.jsp"%>

<html>
<head>
    <title>Order Find page</title>
</head>
<body>

    <c:set var="orders" value="${orders}"/>
    <c:set var="distances" value="${distancesKm}"/>

    <div class="container">
        <h1>Order Find page</h1>

        <table>
            <c:forEach items="${orders}" var="order">
                <tr>
                    <td>
                        <c:out value="
                            id ${order.id},
                            status ${order.orderStatus},
                            to ${order.to.country} ${order.to.city} ${order.to.street} ${order.to.houseNum},
                            price ${order.price}uah,
                            distance to you: ${distances[orders.indexOf(order)]}km
                            " />

                        <a href="get?id=${order.id}">
                            <input type="button" value="SHOW ORDER INFO" name="order-info"/>
                        </a>

                    </td>
                </tr>
            </c:forEach>
        </table>

        <p>
            <a href="user-info">
                <input type="button" value="RETURN TO MENU" name="user-info"/>
            </a>
        </p>
    </div>

</body>
</html>
