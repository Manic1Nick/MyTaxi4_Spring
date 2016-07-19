<%@ include file="include.jsp"%>

<html>
<head>
    <title>Make Order Page</title>
</head>
<body>

<div class="container">
    <h1>Make Order form</h1>

    <c:set var="testOrder" value="${testOrder}"/>

    <c:if test="${testOrder == null}">

        <form method="post" action="order-make">

            <ul>
                <li>Input country from:
                    <input name="countryFrom" type="text">
                </li>
                <li>Input city from:
                    <input name="cityFrom" type="text">
                </li>
                <li>Input street from:
                    <input name="streetFrom" type="text">
                </li>
                <li>Input house number from:
                    <input name="houseNumFrom" type="text">
                </li>
            </ul>
            <ul>
                <li>Input country to:
                    <input name="countryTo" type="text">
                </li>
                <li>Input city to:
                    <input name="cityTo" type="text">
                </li>
                <li>Input street to:
                    <input name="streetTo" type="text">
                </li>
                <li>Input house number to:
                    <input name="houseNumTo" type="text">
                </li>
            </ul>
            <ul>
                <li>Input your message to driver:
                    <input name="message" type="text">
                </li>
            </ul>

            </c:if>

            <c:if test="${testOrder != null}">

                <ul>
                    <div class="column">
                        Address from : ${testOrder.from.country},
                            ${testOrder.from.city},
                            ${testOrder.from.street},
                            ${testOrder.from.houseNum}
                    </div>
                    <div class="column">
                        Address to : ${testOrder.to.country},
                            ${testOrder.to.city},
                            ${testOrder.to.street},
                            ${testOrder.to.houseNum}
                    </div>
                </ul>

                <ul>
                    <div class="column">
                        Message : ${testOrder.message}
                    </div>
                </ul>
                <ul>
                    <li>
                        <div class="column">
                            Distance, km : ${testOrder.distance}
                        </div>
                    </li>
                    <li>
                        <div class="column">
                            Price, uah : ${testOrder.price}
                        </div>
                    </li>
                </ul>
            </c:if>

            <ul>
                <p>
                    <a href="/${APP_NAME}/order-make">
                        <input type="submit" value="MAKE ORDER" name="order-make"/>
                    </a>
                </p>

            </ul>

        </form>

    <ul>
        <p>
            <a href="/${APP_NAME}/order-calculate">
                <input type="submit" value="CALCULATE ORDER" name="order-calculate"/>
            </a>
        </p>
        <p>
            <a href="/${APP_NAME}/order-make">
                <input type="button" value="USE YOUR LOCATION" name="order-make"/>
            </a>
        </p>
        <p>
            <a href="/${APP_NAME}/order-make">
                <input type="button" value="USE YOUR HOME ADDRESS" name="order-make"/>
            </a>
        </p>
        <p>
            <a href="/${APP_NAME}/user-info">
                <input type="button" value="RETURN TO MENU" name="user-info"/>
            </a>
        </p>
    </ul>

</div>

</body>
</html>