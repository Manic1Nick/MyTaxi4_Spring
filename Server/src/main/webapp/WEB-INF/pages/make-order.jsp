<%@ include file="include.jsp"%>

<html>
<head>
    <title>Make Order Page</title>
</head>
<body>

<div class="container">
    <h1>Make Order form</h1>

    <form method="post" action="make-order">
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

            <li>Submit:
                <input type="submit">
            </li>
        </ul>
    </form>


</div>

</body>
</html>