$(document).ready(function(){
    $('#submit0').click();
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
                console.log(option['series'][0]['data']);
                option['series'][1]['data'] = result.a2;
                console.log(option['series'][0]['data']);
                anomalychart.setOption(option);
            },
            error: function(errormsg){
                console.log("fetch anomaly rainfall failed!")
            }
        });
    }
    askforalert();
    // var dom = document.getElementById("anomalyalert");
    // var myChart = echarts.init(dom);
    // option = {
    //     xAxis: {
    //         scale: false
    //     },
    //     yAxis: {
    //         scale: false,
    //         inverse: true,
    //     },
    //     series: [{
    //         type: 'effectScatter',
    //         symbolSize: 10,
    //         color:"#ca8622",
    //         data: [
    //             [400, 105.2],
    //             [153.4, 142],
    //             [120, 400],
    //             [220, 50]
    //         ]
    //     }, 
    //     {
    //         type: 'effectScatter',
    //         symbolSize: 10,
    //         data: [
    //             [300, 105.2],
    //             [153.4, 182]
    //         ]
    //     },{
    //         type: 'scatter',
    //         data: [[500, 0], [0, 500], [500, 500], [0, 0]
    //         ],
    //     }]
    // };
});
