var config = {
			apiKey : "AIzaSyBVfcPnHvNli_y2b9qeYKcIv6keSRGjEc8",
			authDomain : "contratediarista-64f5f.firebaseapp.com",
			databaseURL : "https://contratediarista-64f5f.firebaseio.com",
			projectId : "contratediarista-64f5f",
			storageBucket : "contratediarista-64f5f.appspot.com",
			messagingSenderId : "166088401573"
		};
		firebase.initializeApp(config);


			function logar() {
				if (firebase.auth().currentUser != null) {
					firebase.auth().signOut().then(function() {
						  // Sign-out successful.
						}, function(error) {
							var errorCode = error.code;
					        var errorMessage = error.message;
					        console.log(errorCode);
					        console.log(errorMessage);
						});
			      } 
		        var email = document.getElementById('email').value;
       			var password = document.getElementById('senha').value;
		        firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
		          // Handle Errors here.
		          var errorCode = error.code;
		          var errorMessage = error.message;
		          // [START_EXCLUDE]
		          if (errorCode === 'auth/wrong-password') {
		        	 	 $("#idErroLogin").val('auth/wrong-password');
		        	 	erroLogin();
				    console.log(error);
		          }
		          else if(errorCode == 'auth/user-not-found') {
		        	  	$("#idErroLogin").val('auth/user-not-found');
		        	  	erroLogin();
				    console.log(error);
			      }
		           else {
		            $("#idErroLogin").val(error.message);
		            erroLogin();
			        console.log(error);
		          }
		          
		          // [END_EXCLUDE]
		        });

		        sleep(500).then(() => {
		        		var user = firebase.auth().currentUser;
			        if(user != null) {
				        console.log(user.uid)
				        $("#idUsuario").val(user.uid);
				        logarUsuario();
			        }
			        else {
				        console.log('usuario vazio');
				    }
				}); 
		        
			}

			function sleep(ms) {
				  return new Promise(resolve => setTimeout(resolve, ms));
			}

			function createLogin() {
				var email = document.getElementById('emailCadastro').value;
				var password = document.getElementById('senhaCadastro').value;
				if(firebase.auth().currentUser == null) {
					firebase.auth().createUserWithEmailAndPassword(email, password)
				    .catch(function(error) {
					  // Handle Errors here.
					  var errorCode = error.code;
					  var errorMessage = error.message;
					  if (errorCode == 'auth/weak-password') {
					    alert('The password is too weak.');
					  } else {
					    alert(errorMessage);
					  }
					  console.log(error);
					});
					 var user = firebase.auth().currentUser;
					 if(user != null) {
			        		$("#idUsuario").val(user.uid);
			        		criarUsuario();
					 }
				}
			}

			function excluirLogin() {
			}