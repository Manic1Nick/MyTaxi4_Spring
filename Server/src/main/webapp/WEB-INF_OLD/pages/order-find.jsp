<%@ include file="../../WEB-INF/pages/include.jsp"%>

<html>
<head>
    <title>Order Find page</title>
</head>
<body>

    <!--get "ids" from servlet "order/all"-->
    <c:set var="orderIds" value="${ids}"/>

    <div class="container">
        <h1>Order Find page</h1>
        <table>

            <!--get each id-->
            <c:forEach items="${orderIds}" var="id">

                <!--get textOrder from each link ERROR !!!-->
                <c:set var="textOrder" value="${id}"/>
                <tr>
                    <td>
                        <c:out value="${textOrder}" />
                        <a href="get?id=${id}">
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
