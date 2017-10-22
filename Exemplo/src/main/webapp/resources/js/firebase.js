
	function logar() {
		console.log('logar()');
		if(firebase == null) {
			console.log('erro firebase');
		}
		if (firebase.auth().currentUser != null) {
			deslogarUsuario();
	    } 
        var email = document.getElementById('email').value;
		var password = document.getElementById('senha').value;
		firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
	          console.log('erro ao logar');
	          var errorCode = error.code;
	          var errorMessage = error.message;
	          $("#idErroLogin").val('auth/user-not-found');
	          erroLogin();
	        });
		setTimeout(function() {
        	console.log('validar usuario e chamar bean');
        	var user = firebase.auth().currentUser;
	        if(user != null) {
		        console.log(user.uid)
		        $("#idUsuario").val(user.uid);
		        logarUsuario();
		        console.log('chamou bean');
	        }
	        else {
		        console.log('usuario vazio');
		    }
		}, 2000);
	}
	
	function logarUser(email,senha) {
		var erroCode;
		firebase.auth().signInWithEmailAndPassword(email, senha).catch(function(error) {
	          console.log('erro ao logar');
	          errorCode = error.code;
	          var errorMessage = error.message;
	        });
		setTimeout(function() {
			if(erroCode) {
				$("#idErroLogin").val('auth/user-not-found');
				return false;
			}
			else {
				return true;
			}
		}, 1000);
	}

	function sleep(ms) {
		setTimeout(function() {
			
		}, ms);
	}

	function createLogin() {
		console.log('createLogin()');
		if(firebase == null) {
			console.log('firebase vazio');
			return;
		}
		var email = document.getElementById('emailCadastro').value;
		var password = document.getElementById('senhaCadastro').value;
		
		if(email == null || password == null) {
			console.log('email ou senha vazio');
			return;
		}
		if (firebase.auth().currentUser != null) {
			deslogarUsuario();
	    } 
		console.log('criando novo usuario');
		var errorCode;
		firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
			  errorCode = error.code;
			  var errorMessage = error.message;
			  $("#idErroLogin").val(errorCode);
			  console.log(error);
			});
		
		while (errorCode == null && firebase.auth().currentUser == null) {
			console.log('sem retorno')
		}
		console.log('validar usuario e chamar bean');
		var user = firebase.auth().currentUser;
    	if(user != null) {
	        console.log(user.uid);
	        $("#idUsuario").val(user.uid);
	        deslogarUsuario();
    	}
    	else {
    		$("#idUsuario").val('');
    	}
	}

	function deslogarUsuario() {
		firebase.auth().signOut().then(function() {
		}, function(error) {
			console.log('erro ao deslogar');
			var errorCode = error.code;
	        var errorMessage = error.message;
	        console.log(errorCode);
	        console.log(errorMessage);
		});
		console.log('deslogado')
	}
	
	function excluirLogin() {
		console.log('excluirLogin()');
		var email = document.getElementById('emailCadastro').value;
		var password = document.getElementById('senhaCadastro').value;
		logarUser(email, password);
		setTimeout(function() {
			var user = firebase.auth().currentUser;
			user.delete().then(function() {
				console.log('usuario excluido com sucesso no firebase');
			}, function(error) {
				 console.log('erro ao excluir usuario no firebase.');
			});
		}, 1000);
	}
	
	function excluirUsuarioFirebaseLogin() {
		console.log('excluirUsuarioFirebaseLogin()');
		var email = document.getElementById('email').value;
		var password = document.getElementById('senha').value;
		logarUser(email, password);
		setTimeout(function() {
			var user = firebase.auth().currentUser;
			user.delete().then(function() {
				console.log('usuario excluido com sucesso no firebase');
			}, function(error) {
				 console.log('erro ao excluir usuario no firebase.');
			});
		}, 1000);
	}