<%@ include file="include.jsp"%>

<html>
<head>
    <title>User History page</title>
</head>
<body>

    <c:set var="transfered" value="${textOrders}"/>

    <div class="container">
        <h1>User History page</h1>

        <table>
            <c:forEach items="${transfered}" var="order">
                <tr>
                    <td>
                        <c:out value="${order}" />
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
