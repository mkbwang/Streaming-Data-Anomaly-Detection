$(document).ready( function() {
    let brandspin = $( "#brandspin" ).spinner();
    let productspin = $( "#productspin" ).spinner();
    let categoryspin = $( "#categoryspin" ).spinner();
    let cspin1 = $( "#customer1" ).spinner();
    let cspin2 = $( "#customer2" ).spinner();
    let cspin3 = $( "#customer3" ).spinner();
    $('#submit1').click();
    $('#submit2').click();
    function customertable(){
      let rowTemplate = '<tr>' +
          '<td><%this.TimeStamp%> </td>' +
          '<td><%this.UserID%> </td>' +
          '<td><%this.AnomalyReason%> </td>' +
          '<td><%this.threshold%> </td>' +
          '</tr>';
      $.ajax({
        type:"get",
        url:"/api/customer/",
        dataType:"json",
        success:function(result){
          $('#customertable').renderTable({
            template:rowTemplate,
            data:result,
            pagination:{
              rowPageCount:10
            }
          });
        },
        error: function(msg){
          console.log("Customer information loading failed!");
        }
      });
    };
    
    function otherstable(){
      let rowTemplate = '<tr>' +
          '<td><%this.TimeStamp%> </td>' +
          '<td><%this.Case%> </td>' +
          '<td><%this.ID%> </td>' +
          '<td><%this.threshold%> </td>' +
          '</tr>';
      $.ajax({
        type:"get",
        url:"/api/otherstable/",
        dataType:"json",
        success:function(result){
          $('#otherstable').renderTable({
            template:rowTemplate,
            data:result,
            pagination:{
              rowPageCount:10
            }
          });
        },
        error: function(msg){
          console.log("Others information loading failed!");
        }
      });
    };
    setInterval(customertable,1000);
    customertable();
    setInterval(otherstable,1000);
    otherstable();
    
} );
