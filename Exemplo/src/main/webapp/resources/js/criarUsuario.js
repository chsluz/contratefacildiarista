

	function criarNovoUsuario() {
		$("#idUsuario").val('');
		$("#idErroLogin").val('');
		
		
		var validacao = validarCamposObrigatorios();
		if(!validacao) {
			console.log('campos obrigatórios não preenchidos');
			return null;
		}
		
		var email = document.getElementById('emailCadastro').value;
		var password = document.getElementById('senhaCadastro').value;
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
				firebase.auth().signOut().then(function() {
				}).catch(function(error) {
					console.log(error);
				});
			}
			else if(!erro) {
				console.log(erro);
				erroLogin();
			}
		}, 1000);
	}
	
	function validarCamposObrigatorios() {
		var email = document.getElementById('emailCadastro').value;
		var senha = document.getElementById('senhaCadastro').value;
		var nome = document.getElementById('nome').value;
		var cpf = document.getElementById('cpf').value;
		var telefone = document.getElementById('telefone').value;
		var dtNasc = document.querySelector('.dtNasc').querySelector('.hasDatepicker').value;
		var rua = document.getElementById('rua').value;
		var cep = document.getElementById('cep').value;
		var estado = document.querySelector('.selectEstado').querySelector('select').value;
		var cidade = document.querySelector('.selectCidade').querySelector('select').value;
		var bairro = document.querySelector('.selectBairro').querySelector('select').value;
		if(!email || !senha || !nome || !cpf || !telefone || !dtNasc || !rua || !cep || !estado || !cidade || !bairro) {
			return false;
		}
		
		return true;
		
	}