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
});
