<%@ include file="include.jsp"%>

<html>
<head>
    <title>User History page</title>
</head>
<body>

    <c:set var="list" value="${textOrders}"/>

    <div class="container">
        <h1>User History page</h1>

        <table>
            <c:forEach items="${list}" var="orderInLine">

                <tr>
                    <td>
                        <c:out value="${orderInLine}" />

                        <c:set var="id" value="${orderInLine}"/>

                        <a href="order/get?id=${id}">
                            <input type="button" value="SHOW ORDER INFO" name="order-info"/>
                        </a>


                    </td>
                </tr>
            </c:forEach>
        </table>

        <p>
            <a href="user-info">
                <input type="button" value="RETURN TO MENU" name="user-info"/></a>
        </p>
    </div>

</body>
</html>
