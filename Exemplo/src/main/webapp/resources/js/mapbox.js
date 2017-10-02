  	  var latitude;
      var longitude;
      var map;
      var marker;
      var cidade;
      function init() {
      	navigator.geolocation.watchPosition(render);
      }
      
      function carregarMapa() {
    	  	latitude = document.getElementById('latitude').value;
    	  	longitude = document.getElementById('longitude').value;
    	  	initMap();
      }

      function render(pos) {
	    	if(!latitude) {
	    		latitude = pos.coords.latitude;
	    	}
	    	if(!longitude) {
	    		longitude = pos.coords.longitude;
	    	}
      	if(!map) {
      		$("#latitude").val(latitude);
          	$("#longitude").val(longitude);
      		initMap();
      	}
      	else {
      		if(latitude != document.getElementById('latitude').value) {
	      		latitude = document.getElementById('latitude').value;
	      		initMap();
      		}
      		if(longitude != document.getElementById('longitude').value) {
	      		longitude = document.getElementById('longitude').value;
	      		initMap();
      		}
      	}
      	 map.addListener('click', function(e) {
      	    placeMarkerAndPanTo(e.latLng, map);
      	  });
      	 
      	function placeMarkerAndPanTo(latLng, map) {
      	  marker.setMap(null);
      	  marker = new google.maps.Marker({
      	    position: latLng,
      	    map: map
      	  });
      	  map.panTo(latLng);
      	  $("#latitude").val(latLng.lat);
          $("#longitude").val(latLng.lng);
      	}

      }

      function initMap() {
	        cidade = {
		lat : latitude,
		lng : longitude};
        map = new google.maps.Map(document.getElementById('map'), {
          zoom: 17,
          center: {
              lat: parseFloat(latitude),
              lng: parseFloat(longitude),
            }
        });
        
        	marker = new google.maps.Marker({
            position:  {
                lat: parseFloat(latitude),
                lng: parseFloat(longitude),
              },
            map: map
         });
      
      }
      
     