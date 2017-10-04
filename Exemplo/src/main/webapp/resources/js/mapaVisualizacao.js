var latitude;
var longitude;
var map;
var cidade;
function iniciarMapaVisualizacao() {
	latitude = document.getElementById('latitude').value;
	longitude = document.getElementById('longitude').value;
	if (!map) {
		initMap();
	}
}

function initMap() {
	cidade = {
		lat : latitude,
		lng : longitude
	};
	map = new google.maps.Map(document.getElementById('map'), {
		zoom : 17,
		center : {
			lat : parseFloat(latitude),
			lng : parseFloat(longitude),
		}
	});

	marker = new google.maps.Marker({
		position : {
			lat : parseFloat(latitude),
			lng : parseFloat(longitude),
		},
		map : map
	});

}
