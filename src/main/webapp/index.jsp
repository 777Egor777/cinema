<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<style>
    /* The container */
    .green {
        display: block;
        position: relative;
        padding-left: 35px;
        margin-bottom: 12px;
        cursor: pointer;
        font-size: 22px;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    /* Hide the browser's default checkbox */
    .green input {
        position: absolute;
        opacity: 0;
        cursor: pointer;
        height: 0;
        width: 0;
    }

    /* Create a custom checkbox */
    .checkmark-green {
        position: absolute;
        top: 0;
        left: 0;
        height: 25px;
        width: 25px;
        background-color: #eee;
    }

    /* On mouse-over, add a grey background color */
    .green:hover input ~ .checkmark-green {

    }

    /* When the checkbox is checked, add a blue background */
    .green input:checked ~ .checkmark-green {
        background-color: #008000;
    }

    /* Create the checkmark/indicator (hidden when not checked) */
    .checkmark-green:after {
        content: "";
        position: absolute;
        display: none;
    }

    /* Show the checkmark when checked */
    .green input:checked ~ .checkmark-green:after {
        display: block;
    }

    /* Style the checkmark/indicator */
    .green .checkmark-green:after {
        left: 0;
        top: 0;
        width: 0;
        height: 0;
        border: 0 solid white;
    }

    /* The container */
    .red {
        display: block;
        position: relative;
        padding-left: 35px;
        margin-bottom: 12px;
        cursor: pointer;
        font-size: 22px;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    /* Hide the browser's default checkbox */
    .red input {
        position: absolute;
        opacity: 0;
        cursor: pointer;
        height: 0;
        width: 0;
    }

    /* Create a custom checkbox */
    .checkmark-red {
        position: absolute;
        top: 0;
        left: 0;
        height: 25px;
        width: 25px;
        background-color: #eee;
    }

    /* On mouse-over, add a grey background color */
    .red:hover input ~ .checkmark-red {

    }

    /* When the checkbox is checked, add a blue background */
    .red input:checked ~ .checkmark-red {
        background-color: #ff0000;
    }

    /* Create the checkmark/indicator (hidden when not checked) */
    .checkmark-red:after {
        content: "";
        position: absolute;
        display: none;
    }

    /* Show the checkmark when checked */
    .red input:checked ~ .checkmark-red:after {
        display: block;
    }

    /* Style the checkmark/indicator */
    .red .checkmark-red:after {
        left: 0;
        top: 0;
        width: 0;
        height: 0;
        border: 0 solid white;
    }
</style>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

    <script>
        $(document).ready(
            function() {
                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:8080/cinema/hall.do',
                    contentType: 'application/json',
                    dataType: 'json'
                }).done(function(response) {
                    let rowsNum = response.rowsNum;
                    let colsNum = response.colsNum;
                    let places = response.places;
                    let content = '<thead><tr><th style="width: 120px;">Ряд / Место</th>';
                    for (let i = 0; i < colsNum; i++) {
                        content+= '<th>' + (i+1) + '</th>'
                    }
                    let i = 0;
                    content+= '</tr></thead><tbody>';
                    for (let iRow = 0; iRow < rowsNum; iRow++) {
                        content+= '<tr><th>' + (iRow + 1) + '</th>';
                        for (let iCol = 0; iCol < colsNum; iCol++) {
                            let tdContent = "";//"<input type=\"checkbox\" name=\"place" + places[i].id
                            //+ "\" value=\"" + places[i].id + "\"" + ">";
                            let placeText = "Ряд " + (iRow+1) + ", Место " + (iCol+1);
                            if (places[i].filled === "true") {
                                tdContent = "<label class=\"red\">" + placeText
                                    + "<input type=\"checkbox\" checked=\"checked\" disabled>"
                                + "<span class=\"checkmark-red\"></span></label>";
                            } else {
                                tdContent = "<label class=\"green\">" + placeText
                                    + "<input type=\"checkbox\" name=\"place" + places[i].id
                                    + "\" value=\"" + places[i].id + "\"" + ">"
                                    + "<span class=\"checkmark-green\"></span></label>";
                            }
                            content+= '<td>' + tdContent + '</td>';
                            i++;
                        }
                        content+='</tr>';
                    }
                    content+='</tbody>';
                    $('#main_table').html(content);
                }).fail(function(err) {
                    alert(err);
                });
            }
        )
    </script>

    <title>Кинотеатр</title>
</head>
<body>
<div class="container">
        <h4>
            Бронирование мест на сеанс
        </h4>
        <div class="row pt-3">
        <form action="<%=request.getContextPath()%>/hall.do" method="post">
        <table class="table table-bordered" id="main_table">

        </table>
                <button type="submit" class="btn btn-primary">Оплатить</button>
        </form>
    </div>
</div>
</body>
</html>