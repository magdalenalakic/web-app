function getKategorijaIzmjena(kategNaziv){

		
		let noviUrl = 'rest/kategorija/ucitajKatIzmjena/';
		noviUrl += kategNaziv;
		console.log(noviUrl);
		$.get({
			url: noviUrl,
			contentType: 'application/json',
			success: function(kategorija){
				alert('uspjesno ucitana kategorija');
				console.log(kategorija.naziv);
				console.log(kategorija.opis);
				let naziv = $('#naziv').val(kategorija.naziv);
				let opis = $('#opis').val(kategorija.opis);
				
				//console.log(kategorija);
				
				
			}
		
		});
	
}

$(document).ready(()=>{
	let str = window.location.hash;
	console.log(str);
	let kategNaziv = str.substring(1);
	console.log(kategNaziv);  
	
	getKategorijaIzmjena(kategNaziv);
	
	console.log('aaaaaaaa');

	
	
	$('#izmjenaKategorije').submit((event)=>{
		event.preventDefault();
		
		let naziv = $('#naziv').val();
		let opis = $('#opis').val();
		
		console.log(naziv);
		console.log(opis);
		
		if(!naziv || !opis )
		{
			$('#error').text('Oba polja moraju biti popunjena!');
			$('#error').show().delay(3000).fadeOut();
			return;
		}
		let url = 'rest/kategorija/izmjeniKat/';
		url += naziv+','+opis+','+kategNaziv;
		console.log(url);	
		$.ajax({
			url: url,
			type: 'PUT',
			data: JSON.stringify({naziv, opis}),
			contentType: 'application/json',
			success: function(kategorija) {		
					alert('Uspjesna izmjena');
					console.log(kategorija);
					console.log("***************************");
				//	sessionStorage.setItem('izmjenjaKategorija',JSON.stringify(data));
					
					window.location='index.html';
					
			},
			error: function() {
				console.log("Izmjena nije uspjela!!!!");
				alert('Izmjena nije uspjela');
				$('#error').text('Greska!');
				$('#error').show().delay(3000).fadeOut();
			}
		});
	});
	
});