
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
		
        logarUser(email, password);

        sleep(1000).then(() => {
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
		}); 
        
	}
	
	function logarUser(email,senha) {
		firebase.auth().signInWithEmailAndPassword(email, senha).catch(function(error) {
	          console.log('erro ao logar');
	          var errorCode = error.code;
	          var errorMessage = error.message;
	          if (errorCode === 'auth/wrong-password') {
	        	 $("#idErroLogin").val('auth/wrong-password');
			    console.log(error);
	          }
	          else if(errorCode == 'auth/user-not-found') {
	        	$("#idErroLogin").val('auth/user-not-found');
			    console.log(error);
		      }
	           else {
	            $("#idErroLogin").val(error.message);
		        console.log(error);
	          }
	        });
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
		var email = document.getElementById('emailCadastro').value;
		var password = document.getElementById('senhaCadastro').value;
		logarUser(email, password);
		var user = firebase.auth().currentUser;
		user.delete().then(function() {
		 console.log('usuario excluido com sucesso no firebase');
		}, function(error) {
			 console.log('erro ao excluir usuario no firebase.');
		});

	}