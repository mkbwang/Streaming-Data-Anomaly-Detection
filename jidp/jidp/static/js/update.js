$(document).ready(function(){
    $('#submit0').click(function(event){
        event.preventDefault();
        threshold = {"threshold": $('#currbar').val()};
        $.ajax({
            type:"post",
            url:"/api/rainupdate/",
            contentType: 'application/json',
            data:JSON.stringify(threshold),
            success:function(result){},
            error:function(msg){
                alert("Fail to send the message!");
            }
        });
    });
    $('#submit1').click(function(event){
        event.preventDefault();
        let customer = {};
        let valid=true;
        if (document.getElementById('c1').checked){
            customer["userbrand"] = $( "#customer1" ).val();
        }
        if (document.getElementById('c2').checked){
            customer["userproduct"] = $( "#customer2" ).val();
        }
        if (document.getElementById('c3').checked){
            customer["usercategory"] = $( "#customer3" ).val();
        }
        for (let value of Object.values(customer)){
            try{
                Number(value);
            }
            catch(e){
                alert("Please input valid numbers and resubmit!");
                valid=false;
                break;
            }
        }
        if (valid){
            $.ajax({
                type:"post",
                url:"/api/customerupdate/",
                contentType: 'application/json',
                data:JSON.stringify(customer),
                success:function(result){},
                error:function(msg){
                    // alert("Fail to send the message!");
                    alert(msg);
                }
            });
        }
    });
    $('#submit2').click(function(event){
        event.preventDefault();
        let others = {};
        let valid=true;
        if (document.getElementById('wantbrand').checked){
            others["brand"] = $( "#brandspin" ).val();
        }
        if (document.getElementById('wantproduct').checked){
            others["product"] = $( "#productspin" ).val();
        }
        if (document.getElementById('wantcategory').checked){
            others["category"] = $( "#categoryspin" ).val();
        }
        for (let value of Object.values(others)){
            if(isNaN(Number(value))){
                alert("Please input valid numbers and resubmit!");
                valid=false;
                break;
            }
        }
        if (valid){
            $.ajax({
                type:"post",
                url:"/api/othersupdate/",
                contentType: 'application/json',
                data:JSON.stringify(others),
                success:function(result){},
                error:function(msg){
                    alert("Fail to send the message!");
                }
            });
        }
    });
});
