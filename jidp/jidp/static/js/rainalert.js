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
            symbolSize: 10,
            color:"#ca8622",
            data: [
                [400, 105.2],
                [153.4, 142],
                [120, 400],
                [220, 50]
            ]
        }, 
        {
            type: 'effectScatter',
            symbolSize: 10,
            data: [
                [300, 105.2],
                [153.4, 182]
            ]
        },{
            type: 'scatter',
            data: [[500, 0], [0, 500], [500, 500], [0, 0]
            ],
        }]
    };
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    };
});
