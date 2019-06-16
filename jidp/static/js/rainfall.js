function funcx()
   {
   // your code here
   // break out here if needed
   setTimeout(funcx, 1800);
   }
$(document).ready( function() {
    $('#streamsubmit').click(function(event){
      event.preventDefault();
      let counter = 1;

      for (counter=10;counter<=13;counter++){
        $.ajax({
          type:"POST",
          url:'/api/heatmap/',
          contentType: 'application/json', 
          data:JSON.stringify({picid: counter}),
        })
          .done(function(rawImage){
            $('#currrainfall').attr("src", "data:image/png;base64,"+rawImage);
          })
          .fail(function(){
            alert('Picture loading failed!');
          });
        funcx();
      }
    });
});
