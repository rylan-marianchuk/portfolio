
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);
console.log("Called drawChart")

/** Creating an interactive informative chart for visualization */
function drawChart() {
    /** Manually place the data */
    var data = google.visualization.arrayToDataTable([
        ['mM per L', '6GIX', 'BSA'],
        ['0.25',  63,      11],
        ['0.5',  77,      29],
        ['5.5',  81,       50]
    ]);

    var options = {
        hAxis: {
          title: 'microMoles per L',
          logScale: false
        },
        vAxis: {
          title: '% Chlorophyll Removed',
          logScale: false
        },
        colors: ['#a52714', '#097138'],
        title: 'Chlorophll Removal Efficiency over Concentrations of Aqueous Protein Buffer',
        legend: { position: 'bottom' }
    };

    var chart = new google.visualization.LineChart(document.getElementById('chart-container'));

    chart.draw(data, options);
}

