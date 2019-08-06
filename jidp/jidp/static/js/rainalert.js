$(document).ready(function(){
    var dom = document.getElementById("anomalyalert");
    var myChart = echarts.init(dom);
    option = {
        xAxis: {
            scale: false
        },
        yAxis: {
            scale: false
        },
        series: [{
            type: 'effectScatter',
            symbolSize: 20,
            data: [
                [400, 105.2],
                [153.4, 142]
            ]
        }, {
            type: 'scatter',
            data: [[500, 0], [0, 500], [500, 500], [0, 0]
            ],
        }]
    };
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    };
});
