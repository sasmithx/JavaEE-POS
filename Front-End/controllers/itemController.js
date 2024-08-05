getAllItems();

//save btn action-------------------------------------------------------------------------------------------------------
$("#i_btnSave").click(function() {
    let code = $("#inputItemCode").val();
    let name = $("#inputItemName").val();
    let price = $("#inputItemUnitPrice").val();
    let qty = $("#inputItemQtyOnHand").val();

    let itemObj ={
        code: code,
        name: name,
        price: price,
        qty: qty
    }

    let jsonObj = JSON.stringify(itemObj);
    $.ajax({
        url: "http://localhost:8080/app/item",
        method: "post",
        contentType: "application/json",
        data: jsonObj,
        success: function (resp, textStatus, jqxhr) {
            if(jqxhr.status==201){
                alert("item added successfully");
                clearItemTxtFields();
                getAllItems();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if(jqXHR.status==409){
                alert("Duplicate values. Please check again");
                return;
            }else{
                alert("Something happened. Item not added");
            }
        }
    });
});

//update btn action-----------------------------------------------------------------------------------------------------
$("#i_btnUpdate").click(function () {
    let code = $("#inputItemCode").val();
    let name = $("#inputItemName").val();
    let price = $("#inputItemUnitPrice").val();
    let qty = $("#inputItemQtyOnHand").val();

    let itemObj ={
        code: code,
        name: name,
        price: price,
        qty: qty
    }

    let jsonObj = JSON.stringify(itemObj);
    $.ajax({
        url: "http://localhost:8080/app/item",
        method: "put",
        contentType: "application/json",
        data: jsonObj,
        success: function (resp, textStatus, jqxhr) {
            if(jqxhr.status==204){
                alert("item updated successfully");
                clearItemTxtFields();
                getAllItems();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if(jqXHR.status==409){
                alert("Duplicate values. Please check again");
                return;
            }else{
                alert("Something happened. Item details not updated");
            }
        }
    });
})


//delete btn action-----------------------------------------------------------------------------------------------------
$("#i_btnDelete").click(function () {
    let id = $("#inputItemCode").val();
    deleteItem(id.trim());
})

//clear btn action------------------------------------------------------------------------------------------------------
$("#i_btnClear").click(function () {
    clearItemTxtFields();
})

//search btn action-----------------------------------------------------------------------------------------------------
$("#i_btnSearch").click(function () {
    let itemId = $("#i_inpSearch").val().trim();

    $.ajax({
        url: "http://localhost:8080/app/item?function=getById&id="+itemId,
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqXHR){
            console.log(resp);

            if(resp.code == undefined){
                alert("No item with the id: " + itemId);
                $("#i_inpSearch").val("");
                return;
            }
            approximatedNumber = resp.price.toFixed(2);
            setDataToItemTxtFields(resp.code, resp.name, approximatedNumber, resp.qty);
            $("#i_inpSearch").val("");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
        }
    });
})

function deleteItem(id) {
    let item = findItem(id, function (itemId) {
        console.log(itemId);
        if (itemId == undefined) {
            alert("No item with the id: " + id + " found");
        } else {
            $.ajax({
                url: "http://localhost:8080/app/item?id="+id,
                method: "delete",
                success: function (resp, textStatus, jqXHR){
                    console.log(resp);

                    if(jqXHR.status==204){
                        alert("item deleted successfully");
                        clearItemTxtFields();
                        getAllItems();
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("Something happened. item not removed");
                }
            });
        }
    });
}

function findItem(id, callback) {
    $.ajax({
        url: "http://localhost:8080/app/item?function=getById&id="+id,
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqXHR){
            callback(resp.code);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
            callback(null);
        }
    });
}

function getAllItems() {
    $("#i_tBody").empty();

    $.ajax({
        url: "http://localhost:8080/app/item?function=getAll",
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqxhr) {
            console.log(resp);

            $.each(resp, function(index, item) {
                approximatedNumber = item.price.toFixed(2);
                let row = `
                    <tr>
                        <td>${item.code}</td>
                        <td>${item.name}</td>
                        <td>${approximatedNumber}</td>
                        <td>${item.qty}</td>
                    </tr>
                `
                $("#i_tBody").append(row);
            });
            onTblItemRowClick();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function onTblItemRowClick() {
    let singleClickTimer;

    $("#i_tBody>tr").on("mousedown", function (event) {
        if (event.which === 1) { // Left mouse button (1) clicked
            let row = $(this);
            if (singleClickTimer) {
                clearTimeout(singleClickTimer);
                singleClickTimer = null;
                // Handle double click
                deleteItem(row.children().eq(0).text());
                getAllItems();
            } else {
                singleClickTimer = setTimeout(function () {
                    singleClickTimer = null;
                    // Handle single click
                    let code = row.children().eq(0).text();
                    let name = row.children().eq(1).text();
                    let unitPrice = row.children().eq(2).text();
                    let qty = row.children().eq(3).text();
                    setDataToItemTxtFields(code, name, unitPrice, qty);
                    $("#i_collapseOne").collapse("show");
                    $("#i_collapseOne")[0].scrollIntoView({ behavior: "smooth", block: "center" });
                }, 300); // Adjust the delay (300 milliseconds) as needed
            }
        }
    });
}

function setDataToItemTxtFields(code,name,unitPrice,qty){
    $("#i_collapseOne").collapse("show");
    $("#inputItemCode").val(code);
    $("#inputItemName").val(name);
    $("#inputItemUnitPrice").val(unitPrice);
    $("#inputItemQtyOnHand").val(qty);

    $("#inputItemCode,#inputItemName,#inputItemUnitPrice,#inputItemQtyOnHand").addClass("border-secondary-subtle");
    setItemBtn();
}

function clearItemTxtFields(){
    $("#inputItemCode,#inputItemName,#inputItemUnitPrice,#inputItemQtyOnHand").val("");
    $("#inputItemCode,#inputItemName,#inputItemUnitPrice,#inputItemQtyOnHand").addClass("border-secondary-subtle");
    setItemBtn();
    $(".i_err-label").css("display", "none");
}