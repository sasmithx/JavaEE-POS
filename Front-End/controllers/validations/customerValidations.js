// validation for customers
const CUS_ID_REGEX = /^(C00-)[0-9]{3}$/;
const CUS_NAME_REGEX = /^[A-Za-z ]{4,}$/;
const CUS_ADDRESS_REGEX = /^[A-Za-z0-9., -]{8,}$/;
const CUS_CONTACT_REGEX = /^\+94\d{9}$|^(0\d{9})$|^(0\d{2}-\d{7})$/;

//add validations and text fields to the
let c_validationArray = new Array();
c_validationArray.push({field: $("#c_inputCustId"), regEx: CUS_ID_REGEX});
c_validationArray.push({field: $("#c_inputCustName"), regEx: CUS_NAME_REGEX});
c_validationArray.push({field: $("#c_inputCustAddress"), regEx: CUS_ADDRESS_REGEX});
c_validationArray.push({field: $("#c_inputCustContact"), regEx: CUS_CONTACT_REGEX});

setBtn();
$(".err-label").css("display", "none");

$("#c_inputCustId,#c_inputCustName,#c_inputCustAddress,#c_inputCustContact").on("keydown keyup", function (e) {
    let indexNo = c_validationArray.indexOf(c_validationArray.find((c) => c.field.attr("id") == e.target.id));

    if (e.key == "Tab") {
        e.preventDefault();
    }

    checkValidations(c_validationArray[indexNo]);

    setBtn();

    if (e.key == "Enter") {
        if (e.target.id != c_validationArray[c_validationArray.length - 1].field.attr("id")) {
            if (checkValidations(c_validationArray[indexNo])) {
                c_validationArray[indexNo + 1].field.focus();
            }
        } else {
            // if (checkValidations(c_validationArray[indexNo])) {
            //     saveCustomer();
            // }
        }
    }
});

function checkValidations(object) {
    if (object.regEx.test(object.field.val())) {
        setBorder(true, object);
        object.field.parent().children().eq(2).css("display", "none");
        return true;
    }
    setBorder(false, object);
    object.field.parent().children().eq(2).css("display", "block");
    return false;
}

function setBorder(bool, ob) {
    if(ob.field.val().length>0){
        if(!bool){
            ob.field.css("border", "2px solid red" );
            ob.field.removeClass("border-secondary-subtle");

        }else{
            ob.field.css("border", "2px solid green" );
            ob.field.removeClass("border-secondary-subtle");

        }
    }else{
        ob.field.addClass("border-secondary-subtle");
    }
}

function setBtn() {
    if (checkAll()) {
        $("#c_btnSave").prop("disabled", false);
        $("#c_btnUpdate").prop("disabled", false);
    } else {
        $("#c_btnSave").prop("disabled", true);
        $("#c_btnUpdate").prop("disabled", true);
    }

    let id = $("#c_inputCustId").val();
    findCustomer(id, function (customerId) {
        console.log(customerId);
        if (customerId == null) {
            $("#c_btnDelete").prop("disabled", true);
        } else {
            $("#c_btnDelete").prop("disabled", false);
        }
    });
}

function checkAll() {
    for (let i = 0; i < c_validationArray.length; i++) {
        if (!checkValidations(c_validationArray[i])) return false;
    }
    return true;
}