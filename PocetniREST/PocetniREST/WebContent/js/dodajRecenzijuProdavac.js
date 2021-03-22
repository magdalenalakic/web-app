function getPoljaRecenzijaProdavca(usernameP,recenzent){

//		let noviUrl = 'rest/recenzija/ucitajProdavacRecenzija';
//	//	noviUrl += usernameP;
//		console.log(noviUrl);
//		$.get({
//			url: noviUrl,
//			contentType: 'application/json',
//			success: function(){
				
		
				let prodavac = $('#prodavac').val(usernameP);
				let recenzentKupac = $('#recenzentKupac').val(recenzent);
				alert('uspjesno ucitan prodavac i recenzent');
				//console.log(kategorija);
				
				
//			}
//		
//		});
//	
}
$(document).ready(()=>{
	
	let str = window.location.hash;
	console.log(str);
	let p = str.substring(1);
	console.log(p);
//	if(p.contains('%20')) {
		p.replace('%20', ' ');
//	}

		console.log(p);

	let part = p.split(',');
	let usernameP = part[0];
	let recenzent = part[1];
	console.log(usernameP);
	console.log(recenzent);
	
	getPoljaRecenzijaProdavca(usernameP,recenzent);
	
	$('#dodavanjeRecenzijeProdavac').submit((event)=>{
		event.preventDefault();
		
		let usernameP = $('#prodavac').val();
		let recenzent = $('#recenzentKupac').val();
		let naslovRecenzije = $('#naslov').val();
		let sadrzajRecenzijel = $('#sadrzaj').val();		
		let oglTac =  $("input[name='oglasTacan']:checked").val();
		let ispDog =  $("input[name='ispostovanDogovor']:checked").val();
		
		let oglasTacan = oglTac == 'da' ? true : false;
		let ispostovanDogovor = ispDog == 'da' ? true : false;
	
		let url = 'rest/recenzija/dodajRecenzijuProdavac/';
		url += usernameP;
		$.post({
			url: url,
			data: JSON.stringify({recenzent, naslovRecenzije, sadrzajRecenzijel, oglasTacan, ispostovanDogovor}),
			contentType: 'application/json',
			success: function() {		
					alert('Kupac uspjesno dodao recenziju za prodavca');					
					window.location='index.html';
					
			},
			error: function() {
				console.log("Recenziju ste vec dodali za ovog prodavca!!!");
				alert('Recenziju ste vec dodali za ovog prodavca!!!');
				window.location='index.html';
				$('#error').text('Greska!');
				$('#error').show().delay(3000).fadeOut();
			}
		});
	});
	
});