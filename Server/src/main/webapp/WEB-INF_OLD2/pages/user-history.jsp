<%@ include file="include.jsp"%>

<html>
<head>
    <title>User History page</title>
</head>
<body>

    <c:set var="list" value="${orders}"/>

    <div class="container">
        <h1>User History page</h1>

        <table>
            <c:forEach items="${list}" var="order">
                <tr>
                    <td>
                        <c:out value="
                            id ${order.id},
                            status ${order.orderStatus},
                            from ${order.from.country} ${order.from.city} ${order.from.street} ${order.from.houseNum},
                            to ${order.to.country} ${order.to.city} ${order.to.street} ${order.to.houseNum},
                            distance ${order.distance}km,
                            price ${order.price}uah
                            " />

                        <a href="/${APP_NAME}/order/get?id=${order.id}">
                            <input type="button" value="SHOW ORDER INFO" name="order-info"/>
                        </a>

                    </td>
                </tr>
            </c:forEach>
        </table>

        <p>
            <a href="/${APP_NAME}/user-info">
                <input type="button" value="RETURN TO MENU" name="user-info"/></a>
        </p>
    </div>

</body>
</html>
