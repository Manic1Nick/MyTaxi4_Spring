<%@ include file="../../WEB-INF/pages/include.jsp"%>

<html>
<head>
    <title>Order Find page</title>
</head>
<body>

    <!--get "links" from servlet "order/get"-->
    <c:set var="links" value="${links}"/>

    <div class="container">
        <h1>Order Find page</h1>
        <table>

            <!--run for each link "order/get?id=..."-->
            <c:forEach items="${links}" var="link">

                <!--get textOrder from each link-->
                <c:set var="textOrder" value="${link}"/>
                <tr>
                    <td>

                        <!--add each textOrder to table on screen-->
                        <c:out value="${textOrder}" />

                        <!--name of servlet = link(?)-->
                        <a href="link">

                            <!--button to each link-->
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
