$(document).ready( function() {
    let brandspin = $( "#brandspin" ).spinner();
    let productspin = $( "#productspin" ).spinner();
    let categoryspin = $( "#categoryspin" ).spinner();
    let cspin1 = $( "#customer1" ).spinner();
    let cspin2 = $( "#customer2" ).spinner();
    let cspin3 = $( "#customer3" ).spinner();
    $.ajax({
      type:"get",
      url:"/api/customerupdate/",
      dataType:"json",
      success:function(result){
        if(result.standard.userbrand){
          $("#c1").attr("checked",true);
        }
        if(result.standard.userproduct){
          $("#c2").attr("checked",true);
        }
        if(result.standard.usercategory){
          $("#c3").attr("checked",true);
        }
        cspin1.spinner("value", result.threshold.userbrand);
        cspin2.spinner("value", result.threshold.userproduct);
        cspin3.spinner("value", result.threshold.usercategory);
      }
    });
    $.ajax({
      type:"get",
      url:"/api/othersupdate/",
      dataType:"json",
      success:function(result){
        if(result.standard.brand){
          $("#wantbrand").attr("checked",true);
        }
        if(result.standard.product){
          $("#wantproduct").attr("checked",true);
        }
        if(result.standard.category){
          $("#wantcategory").attr("checked",true);
        }
        brandspin.spinner("value", result.threshold.brand);
        productspin.spinner("value", result.threshold.product);
        categoryspin.spinner("value", result.threshold.category);
      }
    });
    function customertable(){
      let rowTemplate = '<tr>' +
          '<td><%this.TimeStamp%> </td>' +
          '<td><%this.UserID%> </td>' +
          '<td><%this.AnomalyReason%> </td>' +
          '<td><%this.threshold%> </td>' +
          '<td><%this.value%></td>' +
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
          '<td><%this.value%></td>'+
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
