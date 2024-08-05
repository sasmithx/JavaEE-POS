getAllCustomers();

//save btn action ------------------------------------------------------------------------------------------------------
$("#c_btnSave").click(function() {
    let id = $("#c_inputCustId").val();
    let name = $("#c_inputCustName").val();
    let address = $("#c_inputCustAddress").val();
    let contact = $("#c_inputCustContact").val();

    let custObj ={
        id: id,
        name: name,
        address: address,
        contact: contact
    }

    let jsonObj = JSON.stringify(custObj);
    $.ajax({
        url: "http://localhost:8080/app/customer",
        method: "post",
        contentType: "application/json",
        data: jsonObj,
        success: function (resp, textStatus, jqxhr) {
            if(jqxhr.status==201){
                alert("customer saved successfully");
                clearTxtFields();
                getAllCustomers();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if(jqXHR.status==409){
                alert("Duplicate values. Please check again");
                return;
            }else{
                alert("Something happened. Customer not added");
            }
        }
    });
});

//update btn action-----------------------------------------------------------------------------------------------------
$("#c_btnUpdate").click(function () {
    let id = $("#c_inputCustId").val().trim();
    let name = $("#c_inputCustName").val();
    let address = $("#c_inputCustAddress").val();
    let contact = $("#c_inputCustContact").val();

    let custObj ={
        id: id,
        name: name,
        address: address,
        contact: contact
    }

    let jsonObj = JSON.stringify(custObj);
    $.ajax({
        url: "http://localhost:8080/app/customer",
        method: "put",
        contentType: "application/json",
        data: jsonObj,
        success: function (resp, textStatus, jqxhr) {
            if(jqxhr.status==204){
                alert("customer updated successfully");
                clearTxtFields();
                getAllCustomers();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if(jqXHR.status==409){
                alert("Duplicate values. Please check again");
                return;
            }else{
                alert("Something happened. Customer details not updated");
            }
        }
    });
})

//delete btn action-----------------------------------------------------------------------------------------------------
$("#c_btnDelete").click(function () {
    let id = $("#c_inputCustId").val().trim();
    deleteCustomer(id.trim());
})

//clear btn action------------------------------------------------------------------------------------------------------
$("#c_btnClear").click(function () {
    clearTxtFields();
})

//search btn action-----------------------------------------------------------------------------------------------------
$("#c_btnSearch").click(function () {
    let custId = $("#c_inpSearch").val();

    $.ajax({
        url: "http://localhost:8080/app/customer?function=getById&id="+custId,
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqXHR){
            console.log(resp);

            if(resp.id == undefined){
                alert("No customer with the id: " + custId);
                $("#c_inpSearch").val("");
                return;
            }
            setDataToTxtFields(resp.id, resp.name, resp.address, resp.contact);
            $("#c_inpSearch").val("");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
        }
    });
})

function deleteCustomer(id) {
    let customer = findCustomer(id, function (customerId) {
        console.log(customerId);
        if (customerId == undefined) {
           alert("No customer with the id: " + id + " found");
        } else {
            $.ajax({
                url: "http://localhost:8080/app/customer?id="+id,
                method: "delete",
                success: function (resp, textStatus, jqXHR){
                    console.log(resp);

                    if(jqXHR.status==204){
                        alert("customer deleted successfully");
                        clearTxtFields();
                        getAllCustomers();
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("Something happened. Customer not removed");
                }
            });
        }
    });
}

function findCustomer(id, callback) {
    $.ajax({
        url: "http://localhost:8080/app/customer?function=getById&id="+id,
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqXHR){
            callback(resp.id);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
            callback(null);
        }
    });
}

function getAllCustomers() {
    $("#c_tBody").empty();

    $.ajax({
        url: "http://localhost:8080/app/customer?function=getAll",
        method: "get",
        dataType: "json",
        success: function (resp, textStatus, jqxhr) {
            console.log(resp);

            $.each(resp, function(index, customer) {
                let row = `
                    <tr>
                        <td>${customer.id}</td>
                        <td>${customer.name}</td>
                        <td>${customer.address}</td>
                        <td>${customer.contact}</td>
                    </tr>
                `
                $("#c_tBody").append(row);
            });
            onTblRowClick();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function onTblRowClick() {
    let singleClickTimer;

    $("#c_tBody>tr").on("mousedown", function (event) {
        if (event.which === 1) { // Left mouse button (1) clicked
            let row = $(this);
            if (singleClickTimer) {
                clearTimeout(singleClickTimer);
                singleClickTimer = null;
                // Handle double click
                deleteCustomer(row.children().eq(0).text());
                getAllCustomers();
            } else {
                singleClickTimer = setTimeout(function () {
                    singleClickTimer = null;
                    // Handle single click
                    let id = row.children().eq(0).text();
                    let name = row.children().eq(1).text();
                    let address = row.children().eq(2).text();
                    let contact = row.children().eq(3).text();
                    setDataToTxtFields(id, name, address, contact);
                    $("#c_btnDelete").prop("disabled", false);
                    $("#c_collapseOne").collapse("show");
                    $("#c_collapseOne")[0].scrollIntoView({ behavior: "smooth", block: "center" });
                }, 300); // Adjust the delay (300 milliseconds) as needed
            }
        }
    });
}

function setDataToTxtFields(id,name,address,contact){
    $("#c_collapseOne").collapse("show");
    $("#c_inputCustId").val(id);
    $("#c_inputCustName").val(name);
    $("#c_inputCustAddress").val(address);
    $("#c_inputCustContact").val(contact);

    $("#c_inputCustId,#c_inputCustName,#c_inputCustAddress,#c_inputCustContact").addClass("border-secondary-subtle");
    setBtn();
    $("#c_btnDelete").prop("disabled", false);
}

function clearTxtFields(){
    $("#c_inputCustId,#c_inputCustName,#c_inputCustAddress,#c_inputCustContact").val("");
    $("#c_inputCustId,#c_inputCustName,#c_inputCustAddress,#c_inputCustContact").addClass("border-secondary-subtle");
    setBtn();
    $(".err-label").css("display", "none");
}