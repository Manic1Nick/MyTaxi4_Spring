<%@ include file="../../WEB-INF/pages/include.jsp"%>

<html>
<head>
    <title>Order Find page</title>
</head>
<body>

    <!--get "links" from servlet "order/get"-->
    <c:set var="receivedLinks" value="${links}"/>

    <div class="container">
        <h1>Order Find page</h1>
        <table>

            <!--take each link "order/get?id=..." to screen-->
            <c:forEach items="${receivedLinks}" var="link">

                <!--get textOrder from each link-->
                <c:set var="textOrder" value="${link}"/>
                <tr>
                    <td>

                        <!--NOT WORK!!!!!!! no text on screen! only link-->
                        <c:out value="${textOrder}" />

                        <!--need "textOrder" on screen!!!-->
                        <a href="${link}">
                            <!--button is good, link works-->
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
