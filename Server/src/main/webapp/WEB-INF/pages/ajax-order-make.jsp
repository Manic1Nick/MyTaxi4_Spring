<%@ include file="include.jsp"%>

<html>
<head>
    <title>Make Order Page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function redirectUserInfo() {
            window.location = "/${APP_NAME}/user-info";
        }
    </script>


    <%-- make order --%>
    <script>
        function makeOrderFromNewData() {
            var addressFrom =
                    $("#countryFrom").val() + "," +
                    $("#cityFrom").val() + "," +
                    $("#streetFrom").val() + "," +
                    $("#houseNumFrom").val();
            var addressTo =
                    $("#countryTo").val() + "," +
                    $("#cityTo").val() + "," +
                    $("#streetTo").val() + "," +
                    $("#houseNumTo").val();
            var message = $("#message").val();
            makeOrder(addressFrom, addressTo, message);
        }
    </script>

    <script>
        function makeOrderFromInsertData() {
            var addressFrom =
                    $("#insert_countryFrom").val() + "," +
                    $("#insert_cityFrom").val() + "," +
                    $("#insert_streetFrom").val() + "," +
                    $("#insert_houseNumFrom").val();
            var addressTo =
                    $("#insert_countryTo").val() + "," +
                    $("#insert_cityTo").val() + "," +
                    $("#insert_streetTo").val() + "," +
                    $("#insert_houseNumTo").val();
            var message = $("#insert_message").val();

            makeOrder(addressFrom, addressTo, message);
        }
    </script>

    <script>
        function makeOrder(addressFrom, addressTo, message) {
            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/order/make",
                data: {
                    addressFrom : addressFrom,
                    addressTo : addressTo,
                    message : message
                },
                success: function(resp){
                    if (resp.includes(":")) {
                        var arrayResp = resp.split(":", 2);
                        var id = arrayResp[1];
                        alert("Order ID=" + id + " was created");
                        window.location = "/${APP_NAME}/order/get?id=" + id;
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            };
            $.ajax(confObj);
        }
    </script>


    <%-- calculate order--%>
    <script>
        function calculateOrderFromNewData() {
            var addressFrom =
                    $("#countryFrom").val() + "," +
                    $("#cityFrom").val() + "," +
                    $("#streetFrom").val() + "," +
                    $("#houseNumFrom").val();
            var addressTo =
                    $("#countryTo").val() + "," +
                    $("#cityTo").val() + "," +
                    $("#streetTo").val() + "," +
                    $("#houseNumTo").val();
            calculateOrder(addressFrom, addressTo);
        }
    </script>

    <script>
        function calculateOrderFromInsertData() {
            var addressFrom =
                    $("#insert_countryFrom").val() + "," +
                    $("#insert_cityFrom").val() + "," +
                    $("#insert_streetFrom").val() + "," +
                    $("#insert_houseNumFrom").val();
            var addressTo = $("#insert_countryTo").val() + "," +
                    $("#insert_cityTo").val() + "," +
                    $("#insert_streetTo").val() + "," +
                    $("#insert_houseNumTo").val();
            calculateOrder(addressFrom, addressTo);
        }
    </script>

    <script>
        function calculateOrder(addressFrom, addressTo) {
            var confObj = {
                type:"POST",
                url: "/${APP_NAME}/order/calculate",
                data: {
                    addressFrom : addressFrom,
                    addressTo : addressTo
                },
                success: function(resp){
                    if (resp.includes(",")) {
                        var arrayResp = resp.split(",", 2);
                        var distance = arrayResp[0];
                        var price = arrayResp[1];
                        alert("distance " + distance + "km, " +
                                "price " + price + "uah");
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            };
            $.ajax(confObj);
        }
    </script>


    <%-- insert data --%>
    <script>
        function insertHomeAddressAsNew() {
            var confObj = {
                type:"GET",
                url: "/${APP_NAME}/user/get-address",
                success: function(resp){
                    if (resp.includes(",")) {
                        var arrayResp = resp.split(",", 4);
                        $("#countryTo").val(arrayResp[0]);
                        $("#cityTo").val(arrayResp[1]);
                        $("#streetTo").val(arrayResp[2]);
                        $("#houseNumTo").val(arrayResp[3]);
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            };
            $.ajax(confObj);
        }
    </script>
    <script>
        function changeHomeAddress() {
            var confObj = {
                type:"GET",
                url: "/${APP_NAME}/user/get-address",
                success: function(resp){
                    if (resp.includes(",")) {
                        var arrayResp = resp.split(",", 4);
                        $("#insert_countryTo").val(arrayResp[0]);
                        $("#insert_cityTo").val(arrayResp[1]);
                        $("#insert_streetTo").val(arrayResp[2]);
                        $("#insert_houseNumTo").val(arrayResp[3]);
                    } else {
                        alert(resp);
                    }
                },
                error: function(jsonResponse){
                    alert(jsonResponse);
                }
            };
            $.ajax(confObj);
        }
    </script>

    <script>
        function insertUserLocationAsNew() {
            var confObj = {
                type:"GET",
                url: "/${APP_NAME}/user/get-location",
                success: function(resp){
                    if (resp.includes(",")) {
                        var arrayResp = resp.split(",", 4);
                        $("#countryFrom").val(arrayResp[0]);
                        $("#cityFrom").val(arrayResp[1]);
                        $("#streetFrom").val(arrayResp[2]);
                        $("#houseNumFrom").val(arrayResp[3]);
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            };
            $.ajax(confObj);
        }
    </script>
    <script>
        function changeUserLocation() {
            var confObj = {
                type:"GET",
                url: "/${APP_NAME}/user/get-location",
                success: function(resp){
                    if (resp.includes(",")) {
                        var arrayResp = resp.split(",", 4);
                        $("#insert_countryFrom").val(arrayResp[0]);
                        $("#insert_cityFrom").val(arrayResp[1]);
                        $("#insert_streetFrom").val(arrayResp[2]);
                        $("#insert_houseNumFrom").val(arrayResp[3]);
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            };
            $.ajax(confObj);
        }
    </script>

    <script>
        function insertDataOfLastOrderAsNew() {
            var confObj = {
                type:"GET",
                url: "/${APP_NAME}/order/last-addresses",
                success: function(resp){
                    if (resp.includes(";")) {
                        var arrayResp = resp.split(";", 2);
                        var addressFrom = arrayResp[0].split(",", 4);
                        $("#countryFrom").val(addressFrom[0]);
                        $("#cityFrom").val(addressFrom[1]);
                        $("#streetFrom").val(addressFrom[2]);
                        $("#houseNumFrom").val(addressFrom[3]);
                        var addressTo = arrayResp[1].split(",", 4);
                        $("#countryTo").val(addressTo[0]);
                        $("#cityTo").val(addressTo[1]);
                        $("#streetTo").val(addressTo[2]);
                        $("#houseNumTo").val(addressTo[3]);
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            };
            $.ajax(confObj);
        }
    </script>

    <script>
        function insertDataOfLastOrderAsInstead() {
            var confObj = {
                type:"GET",
                url: "/${APP_NAME}/order/last-addresses",
                success: function(resp){
                    if (resp.includes(";")) {
                        var arrayResp = resp.split(";", 2);
                        var addressFrom = arrayResp[0].split(",", 4);
                        $("#insert_countryFrom").val(addressFrom[0]);
                        $("#insert_cityFrom").val(addressFrom[1]);
                        $("#insert_streetFrom").val(addressFrom[2]);
                        $("#insert_houseNumFrom").val(addressFrom[3]);
                        var addressTo = arrayResp[1].split(",", 4);
                        $("#insert_countryTo").val(addressTo[0]);
                        $("#insert_cityTo").val(addressTo[1]);
                        $("#insert_streetTo").val(addressTo[2]);
                        $("#insert_houseNumTo").val(addressTo[3]);
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }
            };
            $.ajax(confObj);
        }
    </script>

    <style>
        h1 {
            font-family: arial, sans-serif;
            color: darkslateblue;
        }
        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 50%;
        }
        td, th {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }
    </style>

</head>
<body>

<c:set var="transfered" value="${order}"/>

<div class="container">
    <h1>MAKE ORDER</h1>

    <ul>
        <p>
            <button onclick="redirectUserInfo()" style="background-color:lightgrey">
                RETURN TO MENU</button>
        </p>
    </ul>


    <%--for NEW data--%>
    <c:if test="${transfered == null}">
        <table>
            <tr>
                <td>Input country from:</td>
                <td><input id="countryFrom" type="text"></td>
            </tr>
            <tr>
                <td>Input city from:</td>
                <td><input id="cityFrom" type="text"></td>
            </tr>
            <tr>
                <td>Input street from:</td>
                <td><input id="streetFrom" type="text"></td>
            </tr>
            <tr>
                <td>Input house number from:</td>
                <td><input id="houseNumFrom" type="text"></td>
            </tr>
        </table></br>

        <table>
            <tr>
                <td>Input country to:</td>
                <td><input id="countryTo" type="text"></td>
            </tr>
            <tr>
                <td>Input city to:</td>
                <td><input id="cityTo" type="text"></td>
            </tr>
            <tr>
                <td>Input street from:</td>
                <td><input id="streetTo" type="text"></td>
            </tr>
            <tr>
                <td>Input house number from:</td>
                <td><input id="houseNumTo" type="text"></td>
            </tr>
            <tr>
                <td>Input your message to driver:</td>
                <td><input id="message" type="text"></td>
            </tr>
        </table>

        <p>
            <button onclick="makeOrderFromNewData()" style="background-color:lightgreen">
                MAKE ORDER</button>

            <button onclick="calculateOrderFromNewData()" style="background-color:yellow">
                CALCULATE ORDER</button>
        </p>
        <ul>
            <p>
                <button onclick="insertUserLocationAsNew()">USE YOUR LOCATION</button>
            </p>
            <p>
                <button onclick="insertHomeAddressAsNew()">USE YOUR HOME ADDRESS</button>
            </p>
            <p>
                <button onclick="insertDataOfLastOrderAsNew()">USE YOUR LAST ORDER</button>
            </p>
        </ul>
    </c:if>


    <%--for INSERT data--%>
    <c:if test="${transfered != null}">
        <table>
            <tr>
                <td>Input country from:</td>
                <td><input id="insert_countryFrom" type="text" value="${transfered.from.country}"></td>
            </tr>
            <tr>
                <td>Input city from:</td>
                <td><input id="insert_cityFrom" type="text" value="${transfered.from.city}"></td>
            </tr>
            <tr>
                <td>Input street from:</td>
                <td><input id="insert_streetFrom" type="text" value="${transfered.from.street}"></td>
            </tr>
            <tr>
                <td>Input house number from:</td>
                <td><input id="insert_houseNumFrom" type="text" value="${transfered.from.houseNum}"></td>
            </tr>
        </table></br>

        <table>
            <tr>
                <td>Input country to:</td>
                <td><input id="insert_countryTo" type="text" value="${transfered.to.country}"></td>
            </tr>
            <tr>
                <td>Input city to:</td>
                <td><input id="insert_cityTo" type="text" value="${transfered.to.city}"></td>
            </tr>
            <tr>
                <td>Input street from:</td>
                <td><input id="insert_streetTo" type="text" value="${transfered.to.street}"></td>
            </tr>
            <tr>
                <td>Input house number from:</td>
                <td><input id="insert_houseNumTo" type="text" value="${transfered.to.houseNum}"></td>
            </tr>
            <tr>
                <td>Input your message to driver:</td>
                <td><input id="insert_message" type="text" value="${transfered.message}"></td>
            </tr>
        </table>

        <p>
            <button onclick="makeOrderFromInsertData()" style="background-color:lightgreen">
                MAKE ORDER</button>

            <button onclick="calculateOrderFromInsertData()" style="background-color:yellow">
                CALCULATE ORDER</button>
        </p>
        <ul>
            <p>
                <button onclick="changeUserLocation()">USE YOUR LOCATION</button>
            </p>
            <p>
                <button onclick="changeHomeAddress()">USE YOUR HOME ADDRESS</button>
            </p>
            <p>
                <button onclick="insertDataOfLastOrderAsInstead()">USE YOUR LAST ORDER</button>
            </p>
        </ul>
    </c:if>
</div>
</body>
</html>