<%@ include file="include.jsp"%>

<html>
<head>
    <title>User Info page</title>
</head>
<body>

    <c:set var="transfered" value="${user}"/>

    <div class="container">
        <h1>User Info page</h1>

        <ul>
            <li>
                <div class="column">
                    id : ${transfered.id}
                </div>
            </li>

            <li>
                <div class="column">
                    identifier : ${transfered.identifier}
                </div>
            </li>

            <li>
                <div class="column">
                    phone : ${transfered.phone}
                </div>
            </li>

            <li>
                <div class="column">
                    name : ${transfered.name}
                </div>
            </li>


            <c:if test="${transfered.identifier == 'P'}">
                <li>
                    <div class="column">
                        address country : ${transfered.homeAddress.country}
                    </div>
                    <div class="column">
                        address city : ${transfered.homeAddress.city}
                    </div>
                    <div class="column">
                        address street : ${transfered.homeAddress.street}
                    </div>
                    <div class="column">
                        address home number : ${transfered.homeAddress.houseNum}
                    </div>
                </li>

                <p>
                    <a href="order-make">
                        <input type="button" value="FIND TAXI" name="order-make"/></a>
                </p>
                <p>
                    <a href="register-passenger">
                        <input type="button" value="CHANGE REGISTER DATA" name="register-passenger"/></a>
                </p>
            </c:if>

            <c:if test="${transfered.identifier == 'D'}">
                <li>
                    <div class="column">
                        car type : ${transfered.car.type}
                    </div>
                    <div class="column">
                        car model : ${transfered.car.model}
                    </div>
                    <div class="column">
                        car number : ${transfered.car.number}
                    </div>
                </li>

                <p>
                    <a href="order/all">
                        <input type="button" value="FIND PASSENGERS" name="order/all"/></a>
                </p>
                <p>
                    <a href="register-driver">
                        <input type="button" value="CHANGE REGISTER DATA" name="register-driver"/></a>
                </p>
            </c:if>


            <p>
                <a href="order-last">
                    <input type="button" value="SHOW LAST ORDER" name="order-last"/></a>
            </p>
            <p>
                <a href="user-history">
                    <input type="button" value="SHOW MY HISTORY" name="user-history"/></a>
            </p>
            <p>
                <a href="login">
                    <input type="button" value="RETURN TO LOGIN" name="login"/></a>
            </p>
            <p>
                <a href="delete">
                    <input type="button" value="DELETE USER" name="delete"/></a>
            </p>

        </ul>
    </div>

</body>
</html>
