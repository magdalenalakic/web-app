function readURL(input) {
		if(input.files && input.files[0]){
			var reader = new FileReader();
			var file = input.files[0];
			
			reader.onload = function(e){	
				$('#output').attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
}

function ucitajKategorijeSelect(){
	
	let tdKategorija;
	
	$.get({
		url: 'rest/kategorija/getKateg',
		contentType: 'application/json',
		success: function(kategorije){
			for(let k of kategorije){
				console.log(k.naziv);
				tdKategorija = $('<option value="' + k.naziv+ '">' + k.naziv +'</option>');
				$('#kategSelect').append(tdKategorija);
			}
		}
	});
	
}

$(document).ready(()=>{
	
	$('#datumIs').datepicker({
		minDate: Date.now()
	});
	
	let usernameUser;
	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
		usernameUser = descriptors1.username.value;
		

	$('#fileSlika').change(function(){
		readURL(this);
	});
	
	
	ucitajKategorijeSelect();
	
	$('#dodavanjeOglasa').submit((event)=>{
		event.preventDefault();
			
			let naziv = $('#naziv').val();
			let cijena = $('#cijena').val();
			let slika = $('#output').attr('src');
			if(slika == 'undefined' || slika == 'null' && slika == null || slika == ''){
				alert('Ubacite sliku');
				return;
			}
			let datumIs = $('#datumIs').val();
			let grad = $('#grad').val();
			let opis = $('#opis').val();
			let kategKojojPripada = $('#kategSelect').val();
			
			
			
		/*
		 * if(!naziv || !opis ) { $('#error').text('Oba polja moraju biti
		 * popunjena!'); $('#error').show().delay(3000).fadeOut(); return; }
		 */
			
			let url = 'rest/oglas/dodajOglas/';
			url += usernameUser;
			
			$.post({
				url: url,
				data: JSON.stringify({naziv, cijena, slika, datumIsticanja:datumIs, grad, opis, kategKojojPripada}),
				contentType: 'application/json',
				success: function(data) {		
						$('#success').text('Prodavac uspjesno dodao oglas.');
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