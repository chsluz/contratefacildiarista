  	  var latitude;
      var longitude;
      var map;
      var marker;
      var cidade;
      function init() {
      	navigator.geolocation.watchPosition(render);
      }

      function render(pos) {
      	latitude = pos.coords.latitude;
      	longitude = pos.coords.longitude;
      	if(!map) {
      		$("#latitude").val(latitude);
          	$("#longitude").val(longitude);
      		initMap();
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
        cidade = {lat: latitude, lng: longitude};
        map = new google.maps.Map(document.getElementById('map'), {
          zoom: 17,
          center: cidade
        });
        
        	marker = new google.maps.Marker({
            position: cidade,
            map: map
         });
      
      }
      
     