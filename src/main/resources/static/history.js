var id = getUrlParamValue("id");
$.ajax({
    url: '/api/stocks/' + id,
    type: 'GET',
    success: function(data) {
            var stockId = document.getElementById("stock_id");
            var stockName = document.getElementById("stock_name");
            var currentPrice = document.getElementById("current_price");
            var lastUpdate = document.getElementById("last_update");

            stockId.innerHTML = "Stock ID: " + data.id;
            stockName.innerHTML = "Stock Name: " + data.name;
            currentPrice.innerHTML = "Current Price: " + data.currentPrice;
            lastUpdate.innerHTML = "Last Update: " + data.lastUpdate;
        },
        error: function(xhr, resp, text) {
            showError(xhr, resp, text);
        }
})
$.ajax({
    url: '/api/stocks/' + id + '/history',
    type: 'GET',
    success: function(data) {
        var table = document.getElementById("history_table");
        $.each(data, function (i, history) {
            var row = table.insertRow(i + 1);
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);

            cell1.innerHTML = history.price;
            cell2.innerHTML = history.startDate;
            cell3.innerHTML = history.endDate;
        });
    },
    error: function(xhr, resp, text) {
        showError(xhr, resp, text);
    }
});
function getUrlParamValue(paramName) {
    var pageUrl = window.location.search.substring(1);
    var params = pageUrl.split('&');
    for (var i = 0; i < params.length; i++)
    {
        var param = params[i].split('=');
        if (param[0] == paramName)
        {
            return param[1];
        }
    }
    return null;
}
function showError(xhr, resp, text) {
    var errorObj = JSON.parse(xhr.responseText);
    alert(errorObj.error + ":" + errorObj.message);
    console.log(xhr, resp, text);
}