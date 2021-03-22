function getOglasNaziv(ogNaziv){


		let noviUrl = 'rest/oglas/ucitajOglasOdgovor/';
		noviUrl += ogNaziv;
		console.log(noviUrl);
		$.get({
			url: noviUrl,
			contentType: 'application/json',
			success: function(oglas){
				
			
				alert('uspjesno ucitan oglas');
				// console.log(kategorija);
				
				
			}
		
		});
	
}

$(document).ready(()=>{
	
	let str = window.location.hash;
	console.log(str);
	let nazivOglasaPorIprim = str.substring(1);
	console.log(nazivOglasaPorIprim);  
	let part = nazivOglasaPorIprim.split('\;');
	let ogNaziv = part[1];
	let prikNaziv = part[0];
	let primalacPor = part[2];
	console.log(ogNaziv);
	console.log(primalacPor);
	
// getOglasNaziv(ogNaziv);
	
	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
	
		let nazivOglasa = $('#nazivOglasaPor').val(prikNaziv);
	
		
	$('#odgovoriNaPor').submit((event)=>{
		event.preventDefault();
			let nazivOglasa = ogNaziv;
			let naslov = $('#naslov').val();
			let sadrzaj = $('#sadrzaj').val();
			let posiljalac = descriptors1.username.value;
			let primalac = primalacPor;
			
			console.log(nazivOglasa);
			console.log(naslov);
			console.log(sadrzaj);
			console.log(posiljalac);
			console.log(primalac);
			
			console.log("Odgovaram na  porukuuuu ");
			
			if(!nazivOglasa || !naslov ||  !sadrzaj)
			{
				$('#error').text('Polja moraju biti popunjena!');
				$('#error').show().delay(3000).fadeOut();
				return;
			}

			if(uloga == 'prodavac'){
				let userPP = posiljalac+','+primalac;
				let url = 'rest/poruka/posaljiPorukuKP/';
				url += userPP;
				console.log(url);
				console.log(posiljalac);
				console.log(primalac);
				console.log(userPP);
				console.log("*********")
				
				$.post({
				url: url,
				data: JSON.stringify({nazivOglasa, naslov, sadrzaj}),
				contentType: 'application/json',
				success: function(data) {		
						console.log("POST METOD OK!!!");
						console.log("***************************");
						sessionStorage.setItem('poslataPoukaKP',JSON.stringify(data));
						$('#success').text('Kupac uspjesno posalo poruku prodavcu.');
						$('#success').show().delay(3000).fadeOut();
						window.location='index.html';
						
				},
				error: function() {
					console.log("POST METOD GRESKA!!!!");
					$('#error').text('Greska!');
					$('#error').show().delay(3000).fadeOut();
				}
			});
			}
			

			
	});
});