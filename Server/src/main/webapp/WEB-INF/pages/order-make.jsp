<%@ include file="include.jsp"%>

<html>
<head>
    <title>Make Order Page</title>
</head>
<body>

<div class="container">
    <h1>Make Order form</h1>

    <form method="post" action="order-make">

        <c:set var="addressFrom" value="${addressFrom}"/>
        <c:set var="addressTo" value="${addressTo}"/>
        <c:set var="distance" value="${distance}"/>
        <c:set var="price" value="${price}"/>

        <c:if test="${addressFrom != null}">
            <ul>
                <li>Input country from:
                    <input name="countryFrom" type="text" val="${addressFrom.country}">
                </li>
                <li>Input city from:
                    <input name="cityFrom" type="text" val="${addressFrom.city}">
                </li>
                <li>Input street from:
                    <input name="streetFrom" type="text" val="${addressFrom.street}">
                </li>
                <li>Input house number from:
                    <input name="houseNumFrom" type="text" val="${addressFrom.houseNum}">
                </li>
            </ul>
        </c:if>
        <c:if test="${addressTo != null}">
            <ul>
                <li>Input country from:
                    <input name="countryTo" type="text" val="${addressTo.country}">
                </li>
                <li>Input city from:
                    <input name="cityTo" type="text" val="${addressTo.city}">
                </li>
                <li>Input street from:
                    <input name="streetTo" type="text" val="${addressTo.street}">
                </li>
                <li>Input house number from:
                    <input name="houseNumTo" type="text" val="${addressTo.houseNum}">
                </li>
            </ul>
        </c:if>

        <c:if test="${addressFrom == null}">
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
        </c:if>
        <c:if test="${addressTo == null}">
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
        </c:if>

        <c:if test="${distance != null}">
                <ul>
                    <li>Distance:
                        <input name="distance" type="text" val="${distance}">
                    </li>
                </ul>
        </c:if>
        <c:if test="${price != null}">
            <ul>
                <li>Price:
                    <input name="price" type="text" val="${price}">
                </li>
            </ul>
        </c:if>


        <ul>
            <p>
                <a href="order-calculate">
                    <input type="button" value="CALCULATE ORDER" name="order-calculate"/></a>
            </p>
            <p>
                <a href="submit">
                    <input type="button" value="MAKE ORDER" name="submit"/></a>
            </p>
            <p>
                <a href="order-make">
                    <input type="button" value="USE YOUR LOCATION" name="order-make"/></a>
            </p>
            <p>
                <a href="order-make">
                    <input type="button" value="USE YOUR HOME ADDRESS" name="order-make"/></a>
            </p>
            <p>
                <a href="user-info">
                    <input type="button" value="RETURN TO MENU" name="user-info"/></a>
            </p>
        </ul>
    </form>


</div>

</body>
</html>