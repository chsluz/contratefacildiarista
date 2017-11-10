	function criarNovoUsuario() {
		$("#idUsuario").val('');
		$("#idErroLogin").val('');
		var validacao = validarCamposObrigatorios();
		if(!validacao) {
			console.log('campos obrigatórios não preenchidos');
			return null;
		}
		var email = document.getElementById('emailCadastro').value;
		console.log(email);
		var password = document.getElementById('senhaCadastro').value;
		console.log(password);
		firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
			  var errorMessage = error.message;
			  $("#idErroLogin").val(error.code);
			  console.log(error.code);
			});
		setTimeout(function() {
			var auth = firebase.auth();
			var erro = document.getElementById('idErroLogin').value;
			if(auth.currentUser != null) {
				 console.log(auth.currentUser.uid);
				$("#idUsuario").val(auth.currentUser.uid);
				criarUsuario();
				firebase.auth().signOut().then(function() {
				}).catch(function(error) {
					console.log(error);
				});
			}
			else if(erro) {
				console.log(erro);
				erroLogin();
			}
		}, 2000);
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
		if(!nome || !cpf || !telefone || !dtNasc || !rua || !cep || !estado || !cidade || !bairro) {
			return false;
		}
		if(estado == 'Selecione' || cidade == 'Selecione' || bairro == 'Selecione') {
			return false;
		}
		return true;
	}