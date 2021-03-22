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
	}
}

function getPorIzmjena(idPor){

	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
	
	ucitajOglaseSelect();
	ucitajPrimaoceSelect();
	let noviUrl = 'rest/poruka/ucitajPorIzmjena/';
	noviUrl += idPor;
	console.log(noviUrl);
	$.get({
		url: noviUrl,
		contentType: 'application/json',
		success: function(por){
			
			console.log(por);
			
			let nazivOglasa = $('#nazivOglasa').val(por.idOglasa);
			let naslov = $('#naslov').val(por.naslov);
			let sadrzaj = $('#sadrzaj').val(por.sadrzaj);
			
			if(uloga=='administrator'){
				let primalac = $('#selectPrimalac').val(por.primalac);
			}
				
			alert('uspjesno ucitana poruka');
			// console.log(kategorija);
			
			
		}
	
	});

}

$(document).ready(()=>{
	
	let str = window.location.hash;
	console.log(str);
	let idPor = str.substring(1);
	console.log(idPor);  
	
	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
	
	
	getPorIzmjena(idPor);	
	
	if(uloga == 'administrator'){
		$('#primalac').show();
		$('#selectPrimalac').prop('disabled', true);
		console.log('Prikazi korisnikeeeeeeeee!!! ');
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('#primalac').show();
		$('.primalacA').show();
		
	}else{
		// $('#nazivOglasa').attr('readonly', 'readonly');
		$('#nazivOglasa').prop('disabled', true);
		console.log('Zabrani promjenu oglasa !!! ')
	}
	 
	console.log("NANANNANANA");
	
	$('#izmjenaPoruke').submit((event)=>{
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
			
			
			if(uloga == 'kupac' ){
				if(!nazivOglasa || !naslov ||  !sadrzaj)
				{
					$('#error').text('Polja moraju biti popunjena!');
					$('#error').show().delay(3000).fadeOut();
					return;
				}
			}else if(uloga == 'administrator' || uloga == 'prodavac'){
				nazivOglasa = null;
				if( !naslov ||  !sadrzaj)
				{
					$('#error').text('Polja moraju biti popunjena!');
					$('#error').show().delay(3000).fadeOut();
					return;
				}
			}
			
			if(uloga == 'kupac'){
				
				let url = 'rest/poruka/izmjeniPorukuKP/';
				url += idPor;
				console.log(url);
				
				$.post({
				url: url,
				data: JSON.stringify({nazivOglasa, naslov, sadrzaj}),
				contentType: 'application/json',
				success: function(data) {		
						console.log("Ispjesna izmjena!!!");
						console.log(data);
						console.log("***************************");
						
						window.location='index.html';
						
				},
				error: function() {
					console.log("Izmjena nije uspjela!!!");
					$('#error').text('Greska!');
					$('#error').show().delay(3000).fadeOut();
				}
			});
			} else if(uloga=='prodavac'){
				console.log("Prodavac salje poruku ADMINISTRATORU !!!");
				let url = 'rest/poruka/izmjeniPorukuA/';
				url += idPor;
				console.log(url);
				
				$.post({
				url: url,
				data: JSON.stringify({nazivOglasa, naslov, sadrzaj}),
				contentType: 'application/json',
				success: function(data) {		
						console.log("Izmjena uspjela!!!");
						console.log("***************************");
						sessionStorage.setItem('poslataPoukaKP',JSON.stringify(data));
						$('#success').text('Kupac uspjesno posalo poruku prodavcu.');
						$('#success').show().delay(3000).fadeOut();
						window.location='index.html';
						
				},
				error: function() {
					console.log("Izmjena nije uspjela!!");
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
				
				let url = 'rest/poruka/izmjeniPorukuKorisniku/';
				url += idPor;
				
				console.log(url);
				
				$.post({
				url: url,
				data: JSON.stringify({nazivOglasa, naslov, sadrzaj}),
				contentType: 'application/json',
				success: function(data) {		
						console.log("Izmjena poruke uspjela!!!");
						console.log(data);
						console.log("***************************");
						
						window.location='index.html';
						
				},
				error: function() {
					console.log("Izmjena nije uspjela!!!!");
					$('#error').text('Greska!');
					$('#error').show().delay(3000).fadeOut();
				}
			});
			}
			

			
	});
});