$("#o_inputOrdQty").val(0);
$("#o_inputDiscount").val(0);

$(document).ready(function () {
    let nextOrderId = generateNextOrderID();
    $("#o_inputOrderId").val(nextOrderId);

    let currentDate = new Date();
    var formattedDate = currentDate.toISOString().split('T')[0];
    $("#o_inputOrderDate").val(formattedDate);

    loadItemCodes();
    loadCustIds();
});

function generateNextOrderID() {
    $.ajax({
        url: "http://localhost:8080/app/order?function=getLastId",
        method: "get",
        success: function (resp, textStatus, jqxhr) {
            console.log(resp);

            if(resp == "no_ids"){
                $("#o_inputOrderId").val("ORD-001");
            }else{
                let lastId = resp;
                splitOrderId(lastId);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("error - generateNextOrderId");
            console.log(jqXHR);
        }
    });
}

function splitOrderId(lastId) {
    let strings = lastId.split("ORD-");
    let id = parseInt(strings[1]);
    ++id;
    let digit = String(id).padStart(3, '0');
    $("#o_inputOrderId").val("ORD-" + digit);;
}

$("#o_inputCustId").on("change",function (){
    let selectedOption = $(this).val();

    $.ajax({
        url: "http://localhost:8080/app/customer?function=getById&id="+selectedOption,
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqXHR){
            console.log(resp);

            $("#o_lblCustName").text(resp.name);
            $("#o_lblCustContact").text(resp.contact);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
        }
    });
});

$("#o_inputItmCode").on("change",function (){
    let selectedOption = $(this).val();

    o_findItem(selectedOption, function (resp){
        $("#o_lblItmName").text(resp.name);

        approximatedNumber = resp.price.toFixed(2);
        $("#o_lblItmUnitPrice").text(approximatedNumber);
        $("#o_lblItmQtyLeft").text(resp.qty);
    })
});

let finalTotal;
let subTotal;
$("#btnAddToCart").click(function (){
   let itmCode = $("#o_inputItmCode").val();
   let itmName = $("#o_lblItmName").text();
   let unitPrice =  $("#o_lblItmUnitPrice").text();
   let qty = $("#o_inputOrdQty").val();
   let total = parseFloat(unitPrice)*parseFloat(qty);

   if(itmCode != null){
       let itemDBQty = parseFloat($("#o_lblItmQtyLeft").text());
       let tableCheck = "notFound";
       $("#o_tBody tr").each(function() {
           let cellData = $(this).find("td:eq(0)").text();
           if(itmCode == cellData){     //if the itemCode is already in the table
               tableCheck= "found";
               let ordQtyValidResult = ordQtyValidation(qty);
               if(ordQtyValidResult){
                   let crntQty = parseFloat($(this).find("td:eq(3)").text());
                   let newQty = crntQty+parseFloat(qty);

                   if(newQty>itemDBQty){
                       alert("insufficient item amount. Please order less than the amount left.");
                   }else{
                       $(this).find("td:eq(3)").text(newQty);
                       let newTotal = parseFloat(unitPrice)*newQty;
                       $(this).find("td:eq(4)").text(newTotal);
                   }
               }else{
                   alert("Order quantity required");
               }
           }
       });

       //if the itemCode is not already in the table
       if(tableCheck == "notFound"){
           let ordQtyValidResult = ordQtyValidation(qty);
           if(ordQtyValidResult){
               if(parseFloat(qty)> itemDBQty ){
                   alert(`insufficient item amount. Please enter an amount less than ${itemDBQty}.`);
               }else{
                   let row = `<tr>
                   <td>${itmCode}</td>
                   <td>${itmName}</td>
                   <td>${unitPrice}</td>
                   <td>${qty}</td>
                    <td>${total}</td>
               </tr>`;

               $("#o_tBody").append(row);
               orderTblRowClicked();
               }
           }else{
               alert("Order quantity required");
           }
       }
   }else{
       alert("Please select an item first")
   }

    finalTotal = 0;
    $("#o_tBody tr").each(function() {
        let eachItemTotal = parseFloat($(this).find("td:eq(4)").text());
        finalTotal = finalTotal + eachItemTotal;
        $("#o_lblTotal").html("&nbsp;" + finalTotal + "/=");
    });

    let discount = $("#o_inputDiscount").val();
    if(discount===""){
        subTotal = finalTotal;
    }else{
        let reduced_amount = (finalTotal/100)*parseFloat(discount);
        subTotal = finalTotal-reduced_amount;
    }
    $("#o_lblSubTotal").html("&nbsp;" + subTotal + "/=");
});

$("#o_inputDiscount").on("keyup", function (e){
    if(finalTotal==undefined){

    }else{
        let discount = $("#o_inputDiscount").val();
        if(discount===""){
            subTotal = finalTotal;
        }else{
            let reduced_amount = (finalTotal/100)*parseFloat(discount);
            subTotal = finalTotal-reduced_amount;
        }
        $("#o_lblSubTotal").html("&nbsp;" + subTotal + "/=");
    }
});

$("#o_btnPurchase").click(function (){
   if($("#o_inputCustId").val()!= null){
        if($("#o_tBody tr").length==0){
            alert("Add something to the cart before trying to purchase")
        }else{
            let orderId = $("#o_inputOrderId").val();
            let orderDate = $("#o_inputOrderDate").val();
            let custId = $("#o_inputCustId").val();
            let discount = parseFloat($("#o_inputDiscount").val());
            let finalPrice = subTotal;
            let orderDetails = [];

            if(discount>=0 && discount<=100){
                if($("#o_inputCash").val()==""){
                    alert("input cash amount before purchasing")
                }else{

                    $("#o_tBody tr").each(function() {
                        let orderDetail = {
                            order_id: orderId,
                            item_code: $(this).children().eq(0).text(),
                            unit_price: $(this).children().eq(2).text(),
                            qty: $(this).children().eq(3).text()
                        }
                        orderDetails.push(orderDetail);
                    });

                    let orderObj = {
                        order_id: orderId,
                        date: orderDate,
                        cust_id: custId,
                        discount: discount,
                        total: finalPrice,
                        order_list: orderDetails
                    }

                    let jsonObj = JSON.stringify(orderObj);
                    $.ajax({
                        url: "http://localhost:8080/app/order",
                        method: "post",
                        contentType: "application/json",
                        data: jsonObj,
                        success: function (resp, textStatus, jqxhr) {
                            alert("Order placed successfully");

                            let nextOrderId = generateNextOrderID();
                            $("#o_inputOrderId").val(nextOrderId);

                            let currentDate = new Date();
                            var formattedDate = currentDate.toISOString().split('T')[0];
                            $("#o_inputOrderDate").val(formattedDate);

                            document.getElementById("o_inputCustId").selectedIndex= -1;
                            document.getElementById("o_inputItmCode").selectedIndex= -1;
                            $("#o_inputOrdQty").val(0);
                            $("#o_inputDiscount").val(0);
                            $("#o_tBody").empty();
                            $("#o_lblCustName,#o_lblCustContact,#o_lblItmName,#o_lblItmUnitPrice,#o_lblItmQtyLeft,#o_lblTotal,#o_lblSubTotal").text("");
                            $("#o_inputCash,#o_inputBalance").val("");
                            finalTotal=0;
                            subTotal=0;
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log(jqXHR);
                            alert("Something went wrong. Order not placed")
                        }
                    });
                }
            }else {
                alert("discount must be between 0 and 100");
            }
        }
   }else{
       alert("Please select a customer ID")
   }
});

$("#o_inputCash").on("keyup", function(){
   let cash =  parseFloat($("#o_inputCash").val());
   let balance = cash - subTotal;
   if(isNaN(balance)){
   }else{
       if(balance>=0){
           $("#o_inputBalance").val(balance);
           $("#o_btnPurchase").prop("disabled", false);
           $("#o_inputCash").css({
               "background-color" : "white",
               "color" : "black"
           });
       }else{
           $("#o_btnPurchase").prop("disabled", true);
           $("#o_inputCash").css({
               "background-color" : "#eb4a4c",
               "color" : "white"
           });
           $("#o_inputBalance").val("Insufficient Cash");
       }
   }
});

function orderTblRowClicked(){
    $("#o_tBody tr:last-of-type").dblclick(function (){
        let result = confirm("are you sure to remove this item form the cart?")
        if(result){
            $(this).remove();

            finalTotal = 0;
            if($("#o_tBody tr").length == 0){
                $("#o_lblTotal").html(0);
            }else{
                $("#o_tBody tr").each(function() {
                    let eachItemTotal = parseFloat($(this).find("td:eq(4)").text());
                    finalTotal = finalTotal + eachItemTotal;
                    $("#o_lblTotal").html("&nbsp;" + finalTotal + "/=");
                });
            }

            let discount = $("#o_inputDiscount").val();
            if(discount===""){
                subTotal = finalTotal;
            }else{
                let reduced_amount = (finalTotal/100)*parseFloat(discount);
                subTotal = finalTotal-reduced_amount;
            }
            $("#o_lblSubTotal").html("&nbsp;" + subTotal + "/=");
        }
    });
}


function loadCustIds(){
    $.ajax({
        url: "http://localhost:8080/app/customer?function=getAll",
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqxhr) {
            console.log(resp);

            $.each(resp, function(index, customer) {
                let option = `<option>${customer.id}</option>`
                $("#o_inputCustId").append(option);
            });
            document.getElementById("o_inputCustId").selectedIndex= -1;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("error - loadCustIds");
            console.log(jqXHR);
        }
    });
}

function loadItemCodes(){
    $.ajax({
        url: "http://localhost:8080/app/item?function=getAll",
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqxhr) {

            $.each(resp, function(index, item) {
                let option = `<option>${item.code}</option>`
                $("#o_inputItmCode").append(option);
            });
            document.getElementById("o_inputItmCode").selectedIndex= -1;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function o_findItem(id, callback){
    $.ajax({
        url: "http://localhost:8080/app/item?function=getById&id="+id,
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqXHR){
            console.log(resp);

            callback(resp);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
        }
    });
}