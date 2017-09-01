var lat;
		var lon;
		var map;
		var pontos = [];
		var directions;
		var distance;

		function init() {
			navigator.geolocation.watchPosition(render);
		}

		function render(pos) {
			lat = pos.coords.latitude;
			lon = pos.coords.longitude;
			if(!map) {
				carregarMapa();
			}
		}

		function carregarMapa() {
			var cidade = new L.LatLng(lat,lon);
			distance = 0;

			L.mapbox.accessToken = 'pk.eyJ1IjoiaGVucmlxdWUyODEiLCJhIjoiY2ozYWhubWo0MDB5NDJxbWU1eTlmbTV0bCJ9.BtfSnLM-ru-l02f8VRrsEA';
			map = L.mapbox.map('map', 'mapbox.streets', {
			    zoomControl: true
			}).setView(cidade, 15);

			
			
			map.on('click', onMapClick);

			function onMapClick(e) {
				pontos.push(e.latlng);
				if(pontos.length > 1) {
					directions = L.mapbox.directions();
					directions.setOrigin(pontos[pontos.length -2]);
					directions.waypoints = pontos;
					directions.setDestination(pontos[pontos.length -1]);

					directionsLayer = L.mapbox.directions.layer(directions).addTo(map);
					directionsRoutesControl = L.mapbox.directions.routesControl('routes', directions).addTo(map);
					var route = directions.query();

					sleep(3000).then(() => {
							distance = distance + route.directions.routes[0].distance;
							//var input = document.getElementById('txtDistancia');
							//input.value = distance;
					}); 
					

					
				}
			}

			function sleep(ms) {
			  return new Promise(resolve => setTimeout(resolve, ms));
			}
		}