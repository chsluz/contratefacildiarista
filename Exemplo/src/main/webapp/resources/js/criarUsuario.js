

	function criarNovoUsuario(email,password) {
		$("#idUsuario").val('');
		$("#idErroLogin").val('');
		firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
			  var errorMessage = error.message;
			  console.log(error.code);
			  $("#idErroLogin").val(error.code);
			});
		setTimeout(function() {
			var auth = firebase.auth();
			var erro = document.getElementById('idErroLogin').value;
			console.log(erro);
			if(auth.currentUser != null) {
				$("#idUsuario").val(auth.currentUser.uid);
				console.log(auth.currentUser.uid);
				criarUsuario();
			}
			else {
				console.log(erro);
				erroLogin();
			}
			firebase.auth().signOut().then(function() {
			}).catch(function(error) {
				console.log(error);
			});
		}, 1000);
	}
	
	function erroValidacaoPrimefaces() {
		console.log('Erro de validação do primefaces');
	}