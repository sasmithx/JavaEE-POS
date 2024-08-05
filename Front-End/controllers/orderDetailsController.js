$("#od_lblContainer,#od_tblContainer").css("display", "none");

$("#od_btnGetDetails").click(function (){
    let value = $("#od_inputOrderId").val();
    console.log(value);
    if(value==""){
        $("#od_lblContainer,#od_tblContainer").css("display", "none");
        alert("Enter an order ID")
    }else{
        od_findOrder(value, function (order_id){
            console.log(order_id);
            if(order_id == null){
                $("#od_lblContainer,#od_tblContainer").css("display", "none");
                alert("Order ID not found");
            }else{
                $.ajax({
                    url: "http://localhost:8080/app/orderDetail?function=getById&id="+value,
                    method: "get",
                    dataType: "json",
                    success: function (resp, textStatus, jqxhr) {
                        console.log(resp);

                        $("#od_lblContainer,#od_tblContainer").css("display", "block");
                        $("#od_lblDate").text(resp.date);
                        $("#od_lblCustId").text(resp.cust_id);
                        $("#od_lblDiscount").text(resp.discount);
                        $("#od_lbltotal").text(resp.total);

                        $("#od_tBody").empty();
                        for (let i = 0; i < resp.order_list.length; i++){
                            let itemCode = resp.order_list[i].item_code;
                            let unitPrice = resp.order_list[i].unit_price;
                            let qty = resp.order_list[i].qty;

                            let row = `<tr>
                                       <td>${itemCode}</td>
                                       <td>${unitPrice}</td>
                                       <td>${qty}</td>
                                  </tr>`;

                            $("#od_tBody").append(row);
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log(jqXHR);
                    }
                });
            }
        });
    }
});

function od_findOrder(id, callback){
    $.ajax({
        url: "http://localhost:8080/app/order?function=getById&id="+id,
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqXHR){
            callback(resp.order_id);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
            callback(null);
        }
    });
}