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
		url: 'rest/oglas/getKateg',
		contentType: 'application/json',
		success: function(kategorije){
			for(let k of kategorije){
				console.log(k.naziv);
				tdKategorija = $('<option value="' +k.naziv+ '">' + k.naziv +'</option>');
				$('#kategSelect').append(tdKategorija);
			}
		}
	});
	
}

function getOglasIzmjena(idOglas){

		ucitajKategorijeSelect(idOglas);
		let noviUrl = 'rest/oglas/ucitajOgIzmjena/';
		noviUrl += idOglas;
		console.log(noviUrl);
		$.get({
			url: noviUrl,
			contentType: 'application/json',
			success: function(oglas){
				
				console.log(oglas);
				let naziv = $('#naziv').val(oglas.naziv);
				let opis = $('#opis').val(oglas.opis);
				let cijena = $('#cijena').val(oglas.cijena);
				let date = oglas.datumIsticanja.split('-');
				let datumIsticanja = $('#datumIs').val(date[2]+'-'+date[1]+'-'+date[0]);
				let grad = $('#grad').val(oglas.grad);		
				let slika = $('#output').attr('src', oglas.slika);
				let kategKojojPripada = $('#kategSelect').val(oglas.kategKojojPripada);
				
				alert('uspjesno ucitan oglas');				
				
			}
		
		});
	
}

$(document).ready(()=>{
	let str = window.location.hash;
	let idOglas = str.substring(1);
	getOglasIzmjena(idOglas);
	
	$('#izmjeniOglas').submit((event)=>{
		event.preventDefault();
		
		let naziv = $('#naziv').val();
		let cijena = $('#cijena').val();
		let datumIsticanja = $('#datumIs').val();
		let grad = $('#grad').val();		
		let slika =$('#output').attr('src');
		let kategKojojPripada = $('#kategSelect').val();
		let opis = $('#opis').val();
		
		
		
		if(!naziv || !opis )
		{
			$('#error').text('Oba polja moraju biti popunjena!');
			$('#error').show().delay(3000).fadeOut();
			return;
		}
		let url = 'rest/oglas/izmjeniOglas/';
		url += idOglas;

		$.ajax({
			url: url,
			type:'PUT',
			data: JSON.stringify({naziv, cijena, datumIsticanja, grad, opis, slika, kategKojojPripada}),
			contentType: 'application/json',
			success: function(oglas) {		
					alert('Uspjesna izmjena');
					window.location='index.html';
			},
			error: function() {
				alert('Izmjena nije uspjela');
				$('#error').text('Greska!');
				$('#error').show().delay(3000).fadeOut();
			}
		});
	});
	
});