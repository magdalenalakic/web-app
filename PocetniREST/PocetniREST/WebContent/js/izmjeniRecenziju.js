function getRecIzmjena(id){

		
		let noviUrl = 'rest/recenzija/ucitajRecIzmjena/';
		noviUrl += id;
		console.log(noviUrl);
		$.get({
			url: noviUrl,
			contentType: 'application/json',
			success: function(rec){
				
				console.log(rec.naslovRecenzije);
				
				let oglas = $('#nazivOglasa').val(rec.oglas);
				let recenzent = $('#recenzentKupac').val(rec.recenzent);
				let naslovRecenzije = $('#naslov').val(rec.naslovRecenzije);
				let sadrzajRecenzijel = $('#sadrzaj').val(rec.sadrzajRecenzijel);
				//slika
				console.log(rec.oglasTacan);
				console.log(rec.ispostovanDogovor);
				
				if(rec.oglasTacan==false){
					console.log('Nije ispostovan dog');
					document.getElementById('tNe').checked = true;
				}else{
					console.log('Dogovor ispostovan');
					document.getElementById('tDa').checked = true;
				}
				
				if(rec.ispostovanDogovor==false){
					console.log('Nije ispostovan dog');
					document.getElementById('iNe').checked = true;
				}else{
					console.log('Dogovor ispostovan');
					document.getElementById('iDa').checked = true;
				}
				
				//let oglasTacan =  $("input[name='oglasTacan']:checked").val(rec.oglasTacan);
				//let ispostovanDogovor =  $("input[name='ispostovanDogovor']:checked").val(rec.ispostovanDogovor);

				alert('uspjesno ucitan oglas');
				//console.log(kategorija);
				
				
			}
		
		});
	
}

$(document).ready(()=>{
	let str = window.location.hash;
	console.log(str);
	var id = str.substring(1);
	
	getRecIzmjena(id);
	
	$('#izmjenaRecenzije').submit((event)=>{
		event.preventDefault();

		let oglas = $('#nazivOglasa').val();
		let recenzent = $('#recenzentKupac').val();
		let naslovRecenzije = $('#naslov').val();
		let sadrzajRecenzijel = $('#sadrzaj').val();
		let oglTac =  $("input[name='oglasTacan']:checked").val();
		let ispDog =  $("input[name='ispostovanDogovor']:checked").val();
		let idOglas = null;
		
		let oglasTacan = oglTac == 'da' ? true : false;
		let ispostovanDogovor = ispDog == 'da' ? true : false;

		let url = 'rest/recenzija/izmjeniRec/';
		url += id;
		console.log(url);	
		$.post({
			url: url,
			data: JSON.stringify({idOglas, oglas, recenzent, naslovRecenzije, sadrzajRecenzijel, oglasTacan, ispostovanDogovor}),
			contentType: 'application/json',
			success: function(rec) {		
					alert('Uspjesna izmjena');
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