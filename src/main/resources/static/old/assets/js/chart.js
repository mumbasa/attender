/* ====== Index ======

1. DUAL LINE CHART
2. DUAL LINE CHART2
3. LINE CHART
4. LINE CHART1
5. LINE CHART2
6. AREA CHART
7. AREA CHART1
8. AREA CHART2
9. AREA CHART3
10. GRADIENT LINE CHART
11. DOUGHNUT CHART
12. POLAR CHART
13. RADAR CHART
14. CURRENT USER BAR CHART
15. ANALYTICS - USER ACQUISITION
16. ANALYTICS - ACTIVITY CHART
17. HORIZONTAL BAR CHART1
18. HORIZONTAL BAR CHART2
19. DEVICE - DOUGHNUT CHART
20. BAR CHART
21. BAR CHART1
22. BAR CHART2
23. BAR CHART3
24. GRADIENT LINE CHART1
25. GRADIENT LINE CHART2
26. GRADIENT LINE CHART3
27. ACQUISITION3
28. STATISTICS

====== End ======*/

$(document).ready(
		function() {
			"use strict";
			var acquisition3 = document.getElementById("barsum");
			var labe= ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"]

			var late=[]
			var absent=[]
			var deficit=[]
			var months=[]
			var year=new Date().getFullYear()
			
			if (acquisition3 !== null) {

			    //pure javascript
			    var pathname = window.location.pathname;
			    var id=String(window.location).split("=")[1]
			    // to show it in an alert window
			  
				  $.ajax({
					    url: "/attendance/get/"+id+"/agg/"+year+"/",
					    method: "GET",
					    success: function(datas) {
					      console.log(datas);
					      var player = [];
					      var score = [];

					      for(var i in datas) {
					    	 console.log(datas[i].latenessount)
					        absent.push( datas[i].absents);
					    	 deficit.push(datas[i].deficit)
					        late.push(datas[i].latenessount);
					        var t =(datas[i].month)-1
					        months.push(labe[t])
					      }
					      
					      

					    }      
				  
				 
				  });
				  
				var acChart3 = new Chart(acquisition3, {
					// The type of chart we want to create
					type : "bar",

					// The data for our dataset
					data : {
						labels : months,
						datasets : [ {
							label : "lateness",
							backgroundColor : "rgb(76, 132, 255)",
							borderColor : "rgba(76, 132, 255,0)",
							data : late,
							pointBackgroundColor : "rgba(76, 132, 255,0)",
							pointHoverBackgroundColor : "rgba(76, 132, 255,1)",
							pointHoverRadius : 3,
							pointHitRadius : 30
						}, {
							label : "Absent",
							backgroundColor : "rgb(254, 196, 0)",
							borderColor : "rgba(254, 196, 0,0)",
							data : absent,
							pointBackgroundColor : "rgba(254, 196, 0,0)",
							pointHoverBackgroundColor : "rgba(254, 196, 0,1)",
							pointHoverRadius : 3,
							pointHitRadius : 30
						} ]
					},

					// Configuration options go here
					options : {
						responsive : true,
						maintainAspectRatio : false,
						legend : {
							display : false
						},
						scales : {
							xAxes : [ {
								gridLines : {
									display : false
								}
							} ],
							yAxes : [ {
								gridLines : {
									display : true
								},
								ticks : {
									beginAtZero : true,
									stepSize : 5,
									fontColor : "#8a909d",
									fontFamily : "Roboto, sans-serif",
									max : 20
								}
							} ]
						},
						tooltips : {}
					}
				});
				document.getElementById("customLegend").innerHTML = acChart3
						.generateLegend();
			}

		});
