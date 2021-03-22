function ucitajOglaseSelect(){
	
	let tdOglas;
	
	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
	
	if(uloga=='kupac'){
		$.get({
			url: 'rest/oglas/getOglase',
			contentType: 'application/json',
			success: function(oglasi){
				for(let o of oglasi){
					// console.log(o.naziv);
					tdOglas = $('<option value="'+ o.id + '">' + o.naziv +'</option>');
					$('#nazivOglasa').append(tdOglas);
				}
			}
		});
	}else if(uloga=='prodavac'){
		console.log('Prodavac = ucitaj samo moje oglase');
		
		let url = 'rest/oglas/getOglaseMoje/';
		url += descriptors1.username.value;
		console.log(url);
		
		$.get({
			url: url,
			contentType: 'application/json',
			success: function(oglasi){
				for(let o of oglasi){
					// console.log(o.naziv);
					tdOglas = $('<option value="'+ o.id + '">' + o.naziv +'</option>');
					$('#nazivOglasa').append(tdOglas);
				}
			}
		});
	}else{
		tdOglas = $('<option value="'+null+'">' + 'XXXXXXXXXXXXX' +'</option>');
		// $('#nazivOglasa').val('XXXXXXXXXXXXXXXXXXXXX');
		$('#nazivOglasa').prop('disabled', 'disabled');
	}
	
	
	
}

function ucitajPrimaoceSelect(){
	let tdPrimalac;
	
	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
	
	if(uloga=='administrator'){
		$.get({
			url: 'rest/poruka/getUserePrimaoce',
			contentType: 'application/json',
			success: function(useri){
				
				for(let o of useri){
					// console.log(o.naziv);
					tdPrimalac = $('<option value="'+o.username+ '">' + o.username + ' - ' + o.uloga +'</option>');
					$('#selectPrimalac').append(tdPrimalac);
				}
			}
		});
	}else if(uloga=='prodavac'){
		$.get({
			url: 'rest/poruka/getAdminPrimaoce',
			contentType: 'application/json',
			success: function(useri){
				
				for(let o of useri){
					tdPrimalac = $('<option value="'+o.username+ '">' + o.username + ' - ' + o.uloga +'</option>');
					$('#selectPrimalac').append(tdPrimalac);
				}
			}
		});
	}
}

$(document).ready(()=>{
	
	
	
	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
	
	ucitajOglaseSelect();
		
	if(uloga == 'administrator'){
		ucitajPrimaoceSelect();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('.primalacA').show();
		$('#nazivOglasa').hide();
		$('#lblNazivOglasa').hide();
	}
	
	if(uloga == 'prodavac'){
		ucitajPrimaoceSelect();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('.primalacA').show();
		$('#nazivOglasa').hide();
		$('#lblNazivOglasa').hide();
	}
	
	$('#dodavanjePoruke').submit((event)=>{
		event.preventDefault();
			let nazivOglasa = $('#nazivOglasa').val();
			let naslov = $('#naslov').val();
			let sadrzaj = $('#sadrzaj').val();
			let posiljalac = descriptors1.username.value;
			
			console.log(nazivOglasa);
			console.log(naslov);
			console.log(sadrzaj);
			console.log(posiljalac);
			console.log(descriptors1.username.value)
			
			console.log("Slanje  porukeeeeeeeeee ");
			
			
			if(uloga == 'kupac' || uloga == 'prodavac'){
				if(!nazivOglasa || !naslov ||  !sadrzaj)
				{
					$('#error').text('Polja moraju biti popunjena!');
					$('#error').show().delay(3000).fadeOut();
					return;
				}
			}else if(uloga == 'administrator'){
				nazivOglasa = null;
				if( !naslov ||  !sadrzaj)
				{
					$('#error').text('Polja moraju biti popunjena!');
					$('#error').show().delay(3000).fadeOut();
					return;
				}
			}
			
			
			
			if(uloga == 'kupac'){
				
				let url = 'rest/poruka/posaljiPorukuKP/';
				url += posiljalac;
				console.log(url);
				
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
			} else if(uloga=='prodavac'){
				$('#primalac').show();

				console.log("Prodavac salje poruku ADMINISTRATORU !!!");
				let url = 'rest/poruka/posaljiPorukuA/';
				url += posiljalac;
				console.log(url);
				
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
			}else{
				$('#primalac').show();
			
				console.log("POLJEEEEEEEEEEEEEEEEEEEEEEEEee");
				let primalacc = $('#selectPrimalac').val();
				
				
				console.log("Admin salje poruku korisniku (K ili P)");
				console.log(primalacc);
				
				let url = 'rest/poruka/posaljiPorukuKorisniku/';
				url += primalacc;
				
				console.log(url);
				
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