<%@ include file="include.jsp"%>

<html>
<head>
    <title>Order Info page</title>
</head>
<body>

<c:set var="transfered" value="${order}"/>

<div class="container">
    <h1>Order Info page</h1>

    <ul>
        <li>
            <div class="column">
                id : ${transfered.id}
            </div>
        </li>

        <li>
            <div class="column">
                status : ${transfered.orderStatus}
            </div>
        </li>

        <li>
            <div class="column">
                address from : ${transfered.from.country},
                                ${transfered.from.city},
                                ${transfered.from.street},
                                ${transfered.from.houseNum}
            </div>
        </li>

        <li>
            <div class="column">
                address to : ${transfered.to.country},
                                ${transfered.to.city},
                                ${transfered.to.street},
                                ${transfered.to.houseNum}
            </div>
        </li>

        <li>
            <div class="column">
                passenger : ${transfered.passenger.name},
                            ${transfered.passenger.phone}
            </div>
        </li>


        <c:if test="${transfered.driver}">
            <div class="column">
                driver : ${transfered.driver.name},
                    ${transfered.driver.phone}
            </div>

            <div class="column">
                car : ${transfered.driver.car.model},
                    ${transfered.driver.car.number}
            </div>
        </c:if>


        <li>
            <div class="column">
                distance, km : ${transfered.distance}
            </div>
        </li>

        <li>
            <div class="column">
                price, uah : ${transfered.price}
            </div>
        </li>

        <c:if test="${transfered.message != 0}">
            <li>
                <div class="column">
                    message : ${transfered.message}
                </div>
            </li>
        </c:if>

        <p>
            <a href="make-call">
                <input type="button" value="MAKE CALL" name="make-call"/></a>
        </p>
        <p>
            <a href="show-map">
                <input type="button" value="SHOW MAP" name="show-map"/></a>
        </p>

        <c:if test="${transfered.passenger != null}">
            <p>
                <a href="order-cancel">
                    <input type="button" value="CANCEL ORDER" name="order-cancel"/></a>
            </p>
        </c:if>
        <c:if test="${transfered.driver != null}">
            <p>
                <a href="order-take">
                    <input type="button" value="TAKE ORDER" name="order-take"/></a>
            </p>
        </c:if>

        <p>
            <a href="order-done">
                <input type="button" value="ORDER DONE" name="order-done"/></a>
        </p>

        <p>
            <a href="add-message">
                <input type="button" value="ADD MESSAGE" name="add-message"/></a>
        </p>
        <p>
            <a href="user-info">
                <input type="button" value="RETURN TO MENU" name="user-info"/></a>
        </p>

    </ul>
</div>

</body>
</html>

