$(document).ready(function(){
    $( "#rainthreshold" ).slider({
        range: "max",
        min: 10,
        max: 40,
        slide: function( event, ui ) {
          $( "#currbar" ).val( ui.value );
        }
      });
    $( "#currbar" ).val( $( "#rainthreshold" ).slider( "value" ) );
    $.ajax({
        type:"get",
        url:"/api/rainupdate/",
        dataType : "json",
        success: function(result){
            $("#rainthreshold").slider('value',result.threshold);
            $( "#currbar" ).val( result.threshold );
        }
    });
    function askforalert(){
        let anomalychart = echarts.init(document.getElementById('anomalyalert'));
        let option = {
            xAxis: {
                scale: false,
                "axisTick": {
                    "show": false
                },
                "axisLine": {      
                    "show": false
                }
            },
            yAxis: {
                scale: false,
                inverse: true,
                "axisTick": { 
                    "show": false
                },
                "axisLine": { 
                    "show": false
                }
            },
            series: [{
                type: 'effectScatter',
                symbolSize: 6,
                color:"#ca8622",
                data: [
                ]
            }, 
            {
                type: 'effectScatter',
                symbolSize: 6,
                data: [
                ]
            },{
                type: 'scatter',
                data: [[100, 0], [0, 100], [100, 100], [0, 0]
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
    setInterval(askforalert, 3000);
    askforalert();
});
