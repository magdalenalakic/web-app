$(document).ready(()=>{
	$('#registracija').submit((event)=>{
		event.preventDefault();
			let username = $('#username').val();
			let password = $('#password').val();
			let ime = $('#ime').val();
			let prezime = $('#prezime').val();
			let tel = $('#tel').val();
			let email = $('#email').val();
			let grad = $('#grad').val();
			
			console.log(grad);
			
			if(!username || !password || !ime || !prezime || !tel || !email || !grad)
			{
				$('#error').text('Sva polja moraju biti popunjena!');
				$('#error').show().delay(3000).fadeOut();
				return;
			}
			$.post({
				url: 'rest/users/registracija',
				data: JSON.stringify({username, password, ime, prezime, tel, grad,  email}),
				contentType: 'application/json',
				success: function(data) {		
						console.log("POST METOD OK!!!");
						sessionStorage.setItem('ulogovan',JSON.stringify(data));
						$('#success').text('Korisnik uspešno registrovan.');
						$('#success').show().delay(3000).fadeOut();
						window.location='index.html';
						
				},
				error: function() {
					console.log("POST METOD GRESKA!!!!");
					$('#error').text('Korisnik sa tim korisnickim imenom vec postoji!');
					$('#error').show().delay(3000).fadeOut();
				}
			});
		
		
	});
});