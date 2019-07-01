$(document).ready( function() {
  function askforimg1(){
    $.ajax({
      type:"GET",
      url:'/api/heatmap/',
    })
      .done(function(rawImage){
        $('#currrainfall').attr("src", "data:image/png;base64,"+rawImage);
      })
      .fail(function(){
        alert('Picture loading failed!');
      });
  }
  setInterval(askforimg1,3000);
  function askforimg2(){
    $.ajax({
      type:"GET",
      url:'/api/anomaly/',
    })
      .done(function(rawImage){
        $('#anomalyalert').attr("src", "data:image/png;base64,"+rawImage);
      })
      .fail(function(){
        alert('Picture loading failed!');
      });
  }
  setInterval(askforimg2,10000);
  askforimg1();
  askforimg2();
    // $('#streamsubmit').click(function(event){
    //   event.preventDefault();
    //   var counter = 0;
      
    // });
});
