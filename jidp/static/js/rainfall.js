$(document).ready( function() {
    $('#streamsubmit').click(function(event){
      event.preventDefault();
      var counter = 20;
      function askforimg(){
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
        counter++;
      }
      setInterval(askforimg,1000);
      askforimg();
    });
});
