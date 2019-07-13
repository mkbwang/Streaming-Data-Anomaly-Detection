$(document).ready(function(){
    $( "#rainthreshold" ).slider({
        range: "max",
        min: 6,
        max: 18,
        value: 1,
        slide: function( event, ui ) {
          $( "#currbar" ).val( ui.value );
        }
      });
    $( "#currbar" ).val( $( "#rainthreshold" ).slider( "value" ) );
    function askforalert(){
        let anomalychart = echarts.init(document.getElementById('anomalyalert'));
        let option = {
            xAxis: {
                scale: false
            },
            yAxis: {
                scale: false,
                inverse: true,
            },
            series: [{
                type: 'effectScatter',
                symbolSize: 10,
                color:"#ca8622",
                data: [
                ]
            }, 
            {
                type: 'effectScatter',
                symbolSize: 10,
                data: [
                ]
            },{
                type: 'scatter',
                data: [[500, 0], [0, 500], [500, 500], [0, 0]
                ],
            }]
        };
        $.ajax({
            type: "get",
            url: "/api/anomaly/",
            dataType : "json",
            success: function(result){
                option['series'][0]['data'] = result.a1;
                option['series'][1]['data'] = result.a2;
                anomalychart.setOption(option);
            },
            error: function(errormsg){
                console.log("fetch anomaly rainfall failed!");
            }
        });
    }
    setInterval(askforalert, 5000);
    askforalert();
});
