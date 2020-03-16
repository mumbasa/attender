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

$(document)
		.ready(
				function() {
					"use strict";
					var labe = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun",
							"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ]

					var late = []
					var absent = []
					var deficit = []
					var months = []
					var year = new Date().getFullYear()

					$.ajax({
						url : "/attendance/get/attendance/year/"+ 2016,
						method : "GET",
						success : function(datas) {
							console.log(datas);
					
							for ( var i in datas) {
								console.log(datas[i].latenessount)
								absent.push(datas[i].absents);
								deficit.push(datas[i].deficit)
								late.push(datas[i].latenessount);
								var t = (datas[i].month) - 1
								months.push(labe[t])
							}
							
							/* ======== 14. CURRENT USER BAR CHART ======== */
							var cUser = document.getElementById("currentUser");
							if (cUser !== null) {
								var myUChart = new Chart(cUser, {
									type : "bar",
									data : {
										labels : months,
										datasets : [ {
											label : "lateness",
											data : late,
											// data: [2, 3.2, 1.8, 2.1, 1.5,
											// 3.5, 4,
											// 2.3, 2.9, 4.5, 1.8, 3.4, 2.8],
											backgroundColor : "#4c84ff"
										} ]
									},
									options : {
										responsive : true,
										maintainAspectRatio : false,
										legend : {
											display : false
										},
										scales : {
											xAxes : [ {
												gridLines : {
													drawBorder : true,
													display : false,
												},
												ticks : {
													fontColor : "#8a909d",
													fontFamily : "Roboto, sans-serif",
													display : false, // hide
																		// main
																		// x-axis
																		// line
													beginAtZero : true,
													callback : function(tick, index,
															array) {
														return index % 2 ? "" : tick;
													}
												},
												barPercentage : 1.8,
												categoryPercentage : 0.2
											} ],
											yAxes : [ {
												gridLines : {
													drawBorder : true,
													display : true,
													color : "#eee",
													zeroLineColor : "#eee"
												},
												ticks : {
													fontColor : "#8a909d",
													fontFamily : "Roboto, sans-serif",
													display : true,
													beginAtZero : true
												}
											} ]
										},

										tooltips : {
											mode : "index",
											titleFontColor : "#888",
											bodyFontColor : "#555",
											titleFontSize : 12,
											bodyFontSize : 15,
											backgroundColor : "rgba(256,256,256,0.95)",
											displayColors : true,
											xPadding : 10,
											yPadding : 7,
											borderColor : "rgba(220, 220, 220, 0.9)",
											borderWidth : 2,
											caretSize : 6,
											caretPadding : 5
										}
									}
								});
							}
							
							var activity = document.getElementById("activity");
							if (activity !== null) {
								var activityData = [ {
								late,
									absent
								}
									];

								var config = {
									// The type of chart we want to create
									type : "line",
									// The data for our dataset
									data : {
										labels : months,
										datasets : [
												{
													label : "Lateness",
													backgroundColor : "transparent",
													borderColor : "rgb(82, 136, 255)",
													data : late,
													lineTension : 0,
													pointRadius : 5,
													pointBackgroundColor : "rgba(255,255,255,1)",
													pointHoverBackgroundColor : "rgba(255,255,255,1)",
													pointBorderWidth : 2,
													pointHoverRadius : 7,
													pointHoverBorderWidth : 1
												},
												{
													label : "Absents",
													backgroundColor : "transparent",
													borderColor : "rgb(255, 199, 15)",
													data : absent,
													lineTension : 0,
													borderDash : [ 10, 5 ],
													borderWidth : 1,
													pointRadius : 5,
													pointBackgroundColor : "rgba(255,255,255,1)",
													pointHoverBackgroundColor : "rgba(255,255,255,1)",
													pointBorderWidth : 2,
													pointHoverRadius : 7,
													pointHoverBorderWidth : 1
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
													display : false,
												},
												ticks : {
													fontColor : "#8a909d", // this
																			// here
												},
											} ],
											yAxes : [ {
												gridLines : {
													fontColor : "#8a909d",
													fontFamily : "Roboto, sans-serif",
													display : true,
													color : "#eee",
													zeroLineColor : "#eee"
												},
												ticks : {
													// callback: function(tick,
													// index,
													// array) {
													// return (index % 2) ? "" :
													// tick;
													// }
													stepSize : 200,
													fontColor : "#8a909d",
													fontFamily : "Roboto, sans-serif"
												}
											} ]
										},
										tooltips : {
											mode : "index",
											intersect : false,
											titleFontColor : "#888",
											bodyFontColor : "#555",
											titleFontSize : 12,
											bodyFontSize : 15,
											backgroundColor : "rgba(256,256,256,0.95)",
											displayColors : true,
											xPadding : 10,
											yPadding : 7,
											borderColor : "rgba(220, 220, 220, 0.9)",
											borderWidth : 2,
											caretSize : 6,
											caretPadding : 5
										}
									}
								};

								var ctx = document.getElementById("activity")
										.getContext("2d");
								var myLine = new Chart(ctx, config);

								var items = document
										.querySelectorAll("#user-activity .nav-tabs .nav-item");
								items
										.forEach(function(item, index) {
											item
													.addEventListener(
															"click",
															function() {
																config.data.datasets[0].data = activityData[index].first;
																config.data.datasets[1].data = activityData[index].second;
																myLine.update();
															});
										});
							}
							console.log(datas);

						}
					
					

					});

		
					
					
					
				
			});

