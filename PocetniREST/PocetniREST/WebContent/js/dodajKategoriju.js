$(document).ready(()=>{
	$('#dodavanjeKategorije').submit((event)=>{
		event.preventDefault();
			let naziv = $('#naziv').val();
			let opis = $('#opis').val();
						
			if(!naziv || !opis )
			{
				$('#error').text('Oba polja moraju biti popunjena!');
				$('#error').show().delay(3000).fadeOut();
				return;
			}
			$.post({
				url: 'rest/kategorija/dodajNovuKat',
				data: JSON.stringify({naziv, opis}),
				contentType: 'application/json',
				success: function(data) {		
						
						$('#success').text('Administrator uspjesno dodao kategoriju.');
						$('#success').show().delay(3000).fadeOut();
						window.location='index.html';
						
				},
				error: function() {
					$('#error').text('Greska!');
					$('#error').show().delay(3000).fadeOut();
				}
			});
			
	});
});