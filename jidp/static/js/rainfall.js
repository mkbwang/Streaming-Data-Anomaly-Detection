$(document).ready( function() {
    
    
    $('#streamsubmit').click(function(event){
      event.preventDefault();
      const counter = 1;

      while (counter < 10){
        $.delay(800).ajax({
          type:"POST",
          url:'/api/image/',
          contentType: 'application/json', 
          data:JSON.stringify({picid: counter})
        })
         .done(function(rawImage){
            $('#currrainfall').attr("src", "data:image/png;base64,"+rawImage);
         })
         .fail(function(){
            alert('Picture loading failed!');
         });
         counter++;
      }
    });
});
