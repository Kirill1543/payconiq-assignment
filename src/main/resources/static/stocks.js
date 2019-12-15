$.ajax({
    url: '/api/stocks',
    type: 'GET',
    success: function(data) {
       var stocksTable = '';
       $.each(data, function (i, stock) {
           var removeRow=document.createElement("BUTTON");
           removeRow.innerHTML= "Update Price";
           removeRow.onclick=function(){
               removeRowbyItem(this);
           };
           element.type = "text";
           element.value = stock.currentPrice;
           element.name = "update_" + stock.id;
           stocksTable +=
           '<tr><td>' + stock.id
           + '</td><td>' + stock.name
           + '</td><td>' + stock.currentPrice
           + '</td><td>' + stock.lastUpdate
           + '</td><td>' + element
           + '</td></tr>';
       });
       $('#stocks_table').append(stocksTable);
    }
});
$(document).ready(function(){
        $("#add_stock_submit").on('click', function(){
            $.ajax({
                url: '/api/stocks',
                type : "POST",
                dataType : 'json',
                contentType: 'application/json;charset=UTF-8',
                data :JSON.stringify( { "name": $('#stock_name').val(), "price": $('#stock_price').val() } ),
                success : function(result) {
                    console.log(result);
                },
                error: function(xhr, resp, text) {
                    console.log(xhr, resp, text);
                }
            })
        });
    });
$(document).ready(function(){
        $("#update_stock_submit").on('click', function(){
            $.ajax({
                url: '/api/stocks/'+$('#update_id').val(),
                type : "PUT",
                dataType : 'json',
                contentType: 'application/json;charset=UTF-8',
                data :JSON.stringify( { "price": $('#update_price').val() } ),
                success : function(result) {
                    console.log(result);
                },
                error: function(xhr, resp, text) {
                    console.log(xhr, resp, text);
                }
            })
        });
    });
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
            console.log(xhr, resp, text);
        }
    })
}