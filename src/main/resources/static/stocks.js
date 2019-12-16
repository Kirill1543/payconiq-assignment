$.ajax({
    url: '/api/stocks',
    type: 'GET',
    success: function(data) {
        var table = document.getElementById("stocks_table");
        var lastId = 0;
        $.each(data, function (i, stock) {
            var input_price = document.createElement("input");
            input_price.type = "text";
            var update_button = document.createElement("button");
            update_button.innerHTML = "Update";
            update_button.onclick=function(){
                updatePrice(stock.id, input_price.value);
            };
            var history_button = document.createElement("button");
            history_button.innerHTML = "History";
            history_button.onclick=function(){
                var this_url = window.location.href;
                password=1234;
                window.open(this_url + 'history.html?id='+stock.id,"");
            };
            lastId = i + 1;
            var row = table.insertRow(lastId);
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);
            var cell4 = row.insertCell(3);
            var cell5 = row.insertCell(4);
            var cell6 = row.insertCell(5);

            cell1.innerHTML = stock.id;
            cell2.innerHTML = stock.name;
            cell3.innerHTML = stock.currentPrice;
            cell4.innerHTML = stock.lastUpdate;
            cell5.appendChild(input_price);
            cell5.appendChild(update_button);
            cell6.appendChild(history_button);
        });
        var input_new_name = document.createElement("input");
        input_new_name.type = "text";
        var input_new_price = document.createElement("input");
        input_new_price.type = "text";

        var add_new_button = document.createElement("button");
        add_new_button.innerHTML = "+";
        add_new_button.onclick=function(){
            addNewStock(input_new_name.value, input_new_price.value);
        };

        var row = table.insertRow(lastId + 1);
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
        var cell3 = row.insertCell(2);
        var cell4 = row.insertCell(3);
        var cell5 = row.insertCell(4);
        cell1.appendChild(add_new_button)
        cell2.appendChild(input_new_name);
        cell3.appendChild(input_new_price);
    }
});
function addNewStock(name, price){
    $.ajax({
        url: '/api/stocks',
        type : "POST",
        dataType : 'json',
        contentType: 'application/json;charset=UTF-8',
        data :JSON.stringify( { "name": name, "price": price } ),
        success : function(result) {
            console.log(result);
        },
        error: function(xhr, resp, text) {
            showError(xhr, resp, text);
        }
    })
}
function updatePrice(id, price){
    $.ajax({
        url: '/api/stocks/'+ id,
        type : "PUT",
        dataType : 'json',
        contentType: 'application/json;charset=UTF-8',
        data :JSON.stringify( { "price": price } ),
        success : function(result) {
            console.log(result);
        },
        error: function(xhr, resp, text) {
            showError(xhr, resp, text);
        }
    })
}
function showError(xhr, resp, text) {
    var errorObj = JSON.parse(xhr.responseText);
    alert(errorObj.error + ":" + errorObj.message);
    console.log(xhr, resp, text);
}