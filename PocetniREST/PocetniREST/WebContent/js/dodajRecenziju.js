function getOglasRecenzija(ogId, recenzent){

		let noviUrl = 'rest/recenzija/ucitajOgRecenzija/';
		noviUrl += ogId;
		console.log(noviUrl);
		$.get({
			url: noviUrl,
			contentType: 'application/json',
			success: function(oglas){
				
		
				let oglasNaziv = $('#nazivOglasa').val(oglas.naziv);
				let recenzentKupac = $('#recenzentKupac').val(recenzent);
				alert('uspjesno ucitan oglas');
				// console.log(kategorija);
				
				
			}
		
		});
	
}

$(document).ready(()=>{
	
	let str = window.location.hash;
	console.log(str);
	let p = str.substring(1);
	console.log(p);
// if(p.contains('%20')) {
		p.replace('%20', ' ');
// }

		console.log(p);

	let part = p.split(',');
	var idOglas = part[0];
	let recenzent = part[1];
	
	getOglasRecenzija(idOglas,recenzent);
	
	$('#dodavanjeRecenzije').submit((event)=>{
		event.preventDefault();
		
		let oglas = $('#nazivOglasa').val();
		let recenzent = $('#recenzentKupac').val();
		let naslovRecenzije = $('#naslov').val();
		let sadrzajRecenzijel = $('#sadrzaj').val();		
		let oglTac =  $("input[name='oglasTacan']:checked").val();
		let ispDog =  $("input[name='ispostovanDogovor']:checked").val();
		
		let oglasTacan = oglTac == 'da' ? true : false;
		let ispostovanDogovor = ispDog == 'da' ? true : false;
		
		let url = 'rest/recenzija/dodajRecenziju/';
		url += idOglas;

		$.post({
			url: url,
			data: JSON.stringify({idOglas, oglas, recenzent, naslovRecenzije, sadrzajRecenzijel, oglasTacan, ispostovanDogovor}),
			contentType: 'application/json',
			success: function() {		
					alert('Kupac uspjesno dodao recenziju za dostavljeni oglas');					
					window.location='index.html';
			},
			error: function() {
				alert('Recenziju ste vec dodali za ovaj oglas!!!');
				window.location='index.html';
				$('#error').text('Greska!');
				$('#error').show().delay(3000).fadeOut();
			}
		});
	});
	
});