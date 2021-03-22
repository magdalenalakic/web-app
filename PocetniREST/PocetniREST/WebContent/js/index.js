function clickOdjava(){
	return function() {
		sessionStorage.setItem('ulogovan',null); // niko nije ulogovan null
		  $.get({
				url: 'rest/users/odjava',
				success: function() {
					alert('izlogovali ste se!')
					window.location='index.html';
					
					}
				
			});
	};
}

function vratiNaPocetak(){
	
	return function() {
		
		getKategorije();
		getTop10();
	
		window.location='index.html';

	console.log('Vrati na pcoetak');

	}
}

function kreirajPoruku(){
	return function(){

		window.location='dodajPoruku.html';
	}
}

function ispisiKategorije(kateg){
	console.log(kateg);
	let tr = $('<tr  style="height:auto; width: auto; list-style-type:none;"></tr>');
	let naziv = $('<td>' + kateg.naziv + '</td>');
	let opis = $('<td>' + kateg.opis + '</td>');
	let izbrisi = $('<td><img src="img/izbrisi.png" height="16" width="16"></td>');
	let izmjeni = $('<td><img src="img/izmjeni.png" height="16" width="16"></td>');
	
	tr.append(naziv);
	
	
	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	if(user!=null && user!='null' && user!='undefined') {
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
		
		if(uloga=='administrator')
		{
			tr.append(izbrisi).append(izmjeni);
			izbrisi.click(brisanjeKategorije(kateg));
			izmjeni.click(katIzmjena(kateg));
		}
	}
	
	$('#tabelaZaKategorije tbody').append(tr);

	
	naziv.click(biranjeKategorije(kateg));
}

function biranjeKategorije(kateg){
	return function() {
		$('#prikazOglasa').show();
		$('#podaciProdavca').hide();
		$('#prikazRecProdavca').hide();
		$('#ucitajUsere').hide();
			let noviUrl = 'rest/oglas/preuzmiOglaseKateg/';
			noviUrl += kateg.naziv;
			$.get({
				url: noviUrl,
				contentType: 'application/json',
				success: function(oglas){
					
					// $('#tabelaKorpa').hide();
					// $('#tabelaOmilj').hide();

					$('#prikazOglasa tbody').html('');
					console.log(oglas);
					
					for(let o of oglas)
					{
						ispisiOglase(o);
						console.log("--------------------------------UDJE LI OVDJE2");
					}
				}
			
			});
	}
	
}

function katIzmjena(kateg){
	return function() {
			
			
			
			
//			
// let noviUrl = 'rest/kategorija/katIzmjena/';
// noviUrl += kateg.naziv+','+kateg.opis;
// $.get({
// url: noviUrl,
// contentType: 'application/json',
// success: function(kategorija){
// alert('uspjesno ucitana kategorija');
					window.location.href = 'izmjeniKategoriju.html#'+kateg.naziv;
//					
// console.log(kategorija);
//					
//					
// }
//			
// });
	}
	
}

function brisanjeKategorije(kateg){
	return function() {
		
			let noviUrl = 'rest/kategorija/brisanjeKateg/';
			noviUrl += kateg.naziv;
			$.ajax({
				url: noviUrl,
				type: 'DELETE',
				contentType: 'application/json',
				success: function(kategorija){
					alert('uspjesno obrisana kategorija');
					
					$('#prikazKategorija tbody').html('');
					$('#tabelaZaKategorije tbody').html('');
					
					console.log(kategorija);
					
					for(let k of kategorija)
					{
						ispisiKategorije(k);
					}
				}
			
			});
	}
	
}

function getKategorije()
{
	$('#prikazOglasa').show();
	$.get({
		
		url:'rest/kategorija/getKateg',
		contentType: 'application/json',
		success: function(kateg)
		{	
			$('#prikazKat tbody').html('');
			
			for(let k of kateg)
			{
				ispisiKategorije(k);
			}
		}
		
		
	});
}

function ispisiOglase(og){
	console.log(og);
	let sl = og.slika.replace(/%20/, ' ');
	let tr = $('<tr></tr>');
	let slika = $('<td class="w-25 align="center" "><img src="' +  sl + '" class="img-fluid img-thumbnail" ></td>');
	let naziv = $('<td align="center">'+ og.naziv +'</td>');
	let detalji =  $('<td align="center" id="clickMeDetalji"><img src="img/details1.png"></td>');
	let cijena = $('<td align="right">'+ og.cijena +'$ </td>');
	let korpa = $('<td align="center" ><img src="img/bag1.png" ></td>');
	let omiljeni = $('<td align="center" ><img src="img/omiljeniPr.png"></td>');
	let izmjeni = $('<td align="center"><img src="img/edit.png" ></td>');
	let izbrisi = $('<td align="center"><img src="img/brisanje.png" ></td>');
	
	tr.append(slika).append(naziv).append(detalji).append(cijena);
	
	var user = sessionStorage.getItem('ulogovan');

	if(user!=null && user!='null' && user!='undefined') {
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		var uloga = descriptors1.uloga.value;
		
		if(uloga=='prodavac' && !og.dodatUkorpu){
			tr.append(izmjeni).append(izbrisi);
			izbrisi.click(brisanjeOglasa(og));
			izmjeni.click(izmjenaOglsa(og));
		}else if(uloga=='kupac'){
			$('#dodajUkor').show();
			$('#dodajUomilj').show();
			tr.append(korpa).append(omiljeni);
			korpa.click(dodajUkorpu(og));
			omiljeni.click(dodajUomiljene(og));
		}else if(uloga=='administrator'){
			tr.append(izmjeni);
			izmjeni.click(izmjenaOglsa(og));
			if(!og.dodatUkorpu){
				tr.append(izbrisi);
				izbrisi.click(brisanjeOglasa(og));
			}
		}
	
	
	}
	
	detalji.click(clickDetalji(og));
	$('.table-image tbody').append(tr);

		
}

function dodajUkorpu(oglas){
	return function(){
		
		var user = sessionStorage.getItem('ulogovan');
		console.log(user);
		
			var objekat = JSON.parse(user);
			const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
			console.log(descriptors1.username.value);
			var uloga = descriptors1.uloga.value;
			console.log(descriptors1.uloga.value);
			let userUsername = descriptors1.username.value;
		
		
		let url = 'rest/oglas/dodajUkorpu/';
		url += oglas.naziv+','+userUsername;
		console.log(url);
		
		$.get({
			
			url: url,
			contentType: 'application/json',
			success: function(oglas)
			{	
				
				$('#prikazOglasa tbody').html('');
				console.log(oglas);
				
				for(let o of oglas)
				{
					ispisiOglase(o);
				}
				
			},
			error: function(){
				alert('Oglas nije dodat u korpu');
			}
			
			
		});
	}
}

function dodajUomiljene(oglas){
return function(){
		
		var user = sessionStorage.getItem('ulogovan');
		console.log(user);
		
			var objekat = JSON.parse(user);
			const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
			console.log(descriptors1.username.value);
			var uloga = descriptors1.uloga.value;
			console.log(descriptors1.uloga.value);
			let userUsername = descriptors1.username.value;
		
		
		let url = 'rest/oglas/dodajUomilj/';
		url += oglas.naziv+','+userUsername;
		console.log(url);
		
		$.get({
			
			url: url,
			contentType: 'application/json',
			success: function(oglas)
			{	
				
				$('#prikazOglasa tbody').html('');
				console.log(oglas);
				
				for(let o of oglas)
				{
					ispisiOglase(o);
				}
				alert('Oglas dodaj u listu omiljenih! ');
				
			},
			error: function(){
				alert('Oglas nije dodat u omiljene');
			}
			
			
		});
	}
}

function clickDetalji(oglas){
	return function() {
		
			let noviUrl = 'rest/oglas/detaljiOglas/';
			noviUrl += oglas.id;
			$.get({
				url: noviUrl,
				contentType: 'application/json',
				success: function(og){
					
					// alert('detaljan prikaz oglasa');
					$('#prikazOglasa').hide();
					// $('#prikazOglasa tbody').html('');
					$('#detaljanOg').show();
					$('#detaljanOg tbody').html('');
				
					ispisiDetaljiOglas(og);
					
				}
			
			});
	}
	
}

function ispisiDetaljiOglas(og){
	let sl = og.slika.replace(/%20/, ' ');
	let tr1 = $('<tr></tr>');
	let tr2 = $('<tr></tr>');
	let tr3 = $('<tr></tr>');
	let tr4 = $('<tr></tr>');
	let tr5 = $('<tr></tr>');
	let tr6 = $('<tr></tr>');
	
	let naziv = $('<td align="center" colspan="18">'+ og.naziv +'</td>');
	
	let slika = $('<td class="w-25" align="center"><img src="' +  sl + '" class="img-fluid img-thumbnail" ></td>');
	let datumPostavljanja = $('<td  align="center" rowspan="1" > ' + og.datumPostavljanja + '</td>');
	let datumIsticanja = $('<td align="center" rowspan="1">' + og.datumIsticanja + '</td>');
	
	let grad = $('<td align="center" >' + og.grad + '</td>');
	let lajkovi = $('<td  align="center">'+ og.lajkovi +'</td>');
	let dislajkovi  = $('<td  align="center">'+ og.dislajkovi +'</td>');
		
	let opis =  $('<td align="center" rowspan="20">'+  og.opis +'</td>');
	let cijena = $('<td align="right" >'+ og.cijena +' $ </td>');
	let korpa = $('<td  align="center"><img src="img/bag1.png"></td>');
	
	// let dodajRecenziju = $('<td colspan="17" align="center" id="dodajRec" >'+
	// "Dodaj recenziju" +'</td>');
	let prikaziRecenzije = $('<td colspan="17" align="center" id="prikaziRec">'+ "Prikazi recenzije" +'</td>')
	
	tr1.append(naziv);
	tr2.append(slika).append(datumPostavljanja).append(datumIsticanja);
	tr3.append(grad).append(lajkovi).append(dislajkovi);
	tr4.append(opis).append(cijena).append(korpa);
// tr5.append(dodajRecenziju);
	tr6.append(prikaziRecenzije);
	
	$('#tabelaDetaljan').append(tr1).append(tr2).append(tr3).append(tr4).append(tr6);

// dodajRecenziju.click(clickDodajRecenziju(og));
	prikaziRecenzije.click(clickPreuzmiRecenzije(og));
}

function clickDodajRecenziju(og){
	return function(){
// let noviUrl = 'rest/recenzija/ucitajRecenziju/';
// noviUrl += og.naziv;
// $.get({
// url: noviUrl,
// contentType: 'application/json',
// success: function(og){
		console.log(og.dostavljen);
		if(og.dostavljen == false){
			alert('Recenziju je dozvoljeno dodati samo ukololiko je proizvod dostavljen !');
			
			return;
		}
		
		var user = sessionStorage.getItem('ulogovan');
		console.log(user);
		if(user!=null && user!='null' && user!='undefined') {
			var objekat = JSON.parse(user);
			const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
			console.log(descriptors1.username.value);
			var uloga = descriptors1.uloga.value;
			console.log(descriptors1.uloga.value);
			console.log(descriptors1.username.value);
			if(uloga=='kupac'){
				window.location = 'dodajRecenziju.html#'+og.id+','+descriptors1.username.value;	
			}else{
				alert('Niste ulogovani kao kupac!!!!');
			}
		}else{
			alert('Niste ulogovani kao kupac!!!!');
			return;
		}
				

		
	}
}

function clickPreuzmiRecenzije(og){
	return function(){
		// $('#prikaziRecenzije').hide();
		console.log("--------------------------------UDJE LI OVDJE1");
		// $('#prikazOglasa').show();
		
		
		
		let url = 'rest/recenzija/getRecenzije/';
		url += og.naziv;
		
		console.log(url);
		
		$.get({
			
			url: url,
			contentType: 'application/json',
			success: function(recenzija)
			{	
				console.log("Preuzete recenzije za dati oglas");
				$('#prikaziRecenzije tbody').html('');
			
				
				for(let r of recenzija)
				{
					ispisiRecenzije(r);
					// console.log("--------------------------------UDJE LI
					// OVDJE2");
				}
				
				$('#prikaziRecenzije').show();
			},
			error: function(){
				alert('Nije dobro preuzeo recenzije - get');
			}
			
			
		});
	}
	
	
}

function ispisiRecenzije(r){
	
	var user = sessionStorage.getItem('ulogovan');
	let userUsername = null;
	if(user!=null && user!='null' && user!='undefined') {
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		userUsername = descriptors1.username.value;
	}
	
	let tr = $('<tr></tr>');
	let oglas = $('<td align="center">'+ r.oglas +'</td>');
	let recenzent = $('<td align="center">'+ r.recenzent +'</td>');
	let naslovRecenzije = $('<td align="center">'+ r.naslovRecenzije +'</td>');
	let sadrzajRecenzijel = $('<td align="justify">'+ r.sadrzajRecenzijel +'</td>');
	let izmjeni = $('<td align="center"><img src="img/edit.png" ></td>');
	let izbrisi = $('<td align="center"><img src="img/brisanje.png" ></td>');
	
	let tacan;
	if(r.oglasTacan==true){
		tacan = "Da";
	}else{
		tacan = "Ne";
	}
	let oglasTacan = $('<td align="center">'+ tacan +'</td>');
	
	let ispostovan;
	if(r.ispostovanDogovor==true){
		ispostovan = "Da";
	}else{
		ispostovan = "Ne";
	}
	let ispostovanDogovor = $('<td align="center">'+ ispostovan +'</td>');
	


	tr.append(oglas).append(recenzent).append(naslovRecenzije).append(sadrzajRecenzijel).append(oglasTacan).append(ispostovanDogovor);
	if(userUsername!=null){
		if(userUsername==r.recenzent){
			tr.append(izbrisi).append(izmjeni);
			izmjeni.click(izmjeniRecenziju(r));
			izbrisi.click(izbrisiRecenziju(r));
		}
	}
	$('#tabelaRecenzije tbody').append(tr);
	
	$('#prikaziRecenzije').hide();
	
}

function izmjeniRecenziju(r){
	return function(){
		console.log('IZMJENA RECENZIJE');
		var user = sessionStorage.getItem('ulogovan');
		console.log(user);
		if(user!=null && user!='null' && user!='undefined') {
			var objekat = JSON.parse(user);
			const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
			console.log(descriptors1.username.value);
			var uloga = descriptors1.uloga.value;
			let usernameUl = descriptors1.username.value;
			console.log(descriptors1.uloga.value);
			console.log(descriptors1.username.value);
			
			if(uloga=='kupac')
			{
				if(usernameUl==r.recenzent){
					console.log(usernameUl);
					console.log(r.recenzent);
					
					window.location.href = 'izmjeniRecenziju.html#'+r.id;
					
				}else{
					console.log(usernameUl);
					console.log(r.recenzent);
					alert('Niste vi postavili ovu recenziju, nemate mogucnost da je mijenjate ili brisete!!!!');
				}
			}
			
		}
	}
}

function izbrisiRecenziju(r){
	return function(){
		console.log('BRISANJE RECENZIJE');
		var user = sessionStorage.getItem('ulogovan');
		console.log(user);
		if(user!=null && user!='null' && user!='undefined') {
			var objekat = JSON.parse(user);
			const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
			console.log(descriptors1.username.value);
			var uloga = descriptors1.uloga.value;
			let usernameUl = descriptors1.username.value;
			console.log(descriptors1.uloga.value);
			console.log(descriptors1.username.value);
			
			if(uloga=='kupac')
			{
				if(usernameUl==r.recenzent){
					console.log(usernameUl);
					console.log(r.recenzent);
					
					let noviUrl = 'rest/recenzija/brisanjeRec/';
					noviUrl += r.naslovRecenzije;
					$.get({
						url: noviUrl,
						contentType: 'application/json',
						success: function(rec){
							alert('uspjesno obrisana recenzija');
							
							$('#prikaziRecenzije tbody').html('');
						
							for(let r of rec)
							{
								ispisiRecenzije(r);
								
							}
							
							$('#prikaziRecenzije').show();
						}
					
					});
					
				}else{
					console.log(usernameUl);
					console.log(r.recenzent);
					alert('Niste vi postavili ovu recenziju, nemate mogucnost da je mijenjate ili brisete!!!!');
				}
			}
			
		}
	}
}

function brisanjeOglasa(og){
	return function() {

			let noviUrl = 'rest/oglas/brisanjeOg/';
			noviUrl += og.id;
			$.ajax({
				url: noviUrl,
				type: 'DELETE',
				contentType: 'application/json',
				success: function(oglas){
					alert('uspjesno obrisan oglas');
					
					$('#prikazOglasa tbody').html('');
					
					console.log(oglas);
					
					for(let k of oglas)
					{
						ispisiOglase(k);
					}
				}
			
			});
	}
	
}

function izmjenaOglsa(og){
	return function(){
		window.location.href = 'izmjeniOglas.html#'+og.id;
	}
}

function getOglase()
{
	
	
	$('#prikazOglasa').show();
	$.get({
		
		url:'rest/oglas/getOglase',
		contentType: 'application/json',
		success: function(oglas)
		{	
			
			$('#prikazOglasa tbody').html('');
			
			for(let o of oglas)
			{
				ispisiOglase(o);
			}
			
		},
		error: function(){
			alert('Nije moguce dobaviti oglase');
		}
		
		
	});
	
	
}

function getTop10()
{
	
	
	$('#prikazOglasa').show();
	$.get({
		
		url:'rest/oglas/getTop',
		contentType: 'application/json',
		success: function(oglas)
		{	
			
			$('#prikazOglasa tbody').html('');
			
			for(let o of oglas)
			{
				ispisiOglase(o);
			}
			
		},
		error: function(){
			alert('Nije moguce dobaviti oglase');
		}
		
		
	});
	
	
}

function ucitajKorisnike(){
	return function(){
		
		$.get({ 
			
			url:'rest/users/ucitajKorisnike',
			contentType: 'application/json',
			success: function(users)
			{	

				$('#prikazOglasa').hide();
				// $('#prikazOglasa tbody').html('');
				$('#ucitajUsere').show();
				$('#ucitajUsere tbody').html('');
				
				
				for(let o of users)
				{
					ispisiUsere(o);
					console.log("--------------------------------UDJE LI OVDJE2");
					
				}
				
				
			},
			error: function(){
				alert('Nije dobro ispisao usere - get');
			}
			
			
		});
		
	}
	
}

function ispisiUsere(o){
	
	$('#izmjeniA').hide();
	
	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	var uloga;
		var objekat = JSON.parse(user);
		if(objekat==null){
			objekat='nista';
			uloga = 'nista'
			$('.izmjenaKAdmin').hide();
			$('#izmjeniA').hide();
			$('#izmjeniA').hide();
			$('#izmjeniA').hide();
			$('#izmjeniA').hide();
			$('#izmjeniA').hide();
			$('#izmjeniA').hide();
			console.log("Sakrij izmjeni");
				
		}else{
			console.log("izmjeni Prikazi");
			$('.izmjenaKAdmin').show();
			const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
			console.log(descriptors1.username.value);
			uloga = descriptors1.uloga.value;
			console.log(descriptors1.uloga.value);
			console.log(descriptors1.username.value);
		}
		
	
	let tr = $('<tr></tr>')
	let username = $('<td id="usernameUser" align="center">' + o.username + '</td>');
	
// let ul;
// if(o.uloga=='administrator'){
// ul = 'administator';
// }else if(o.uloga=='kupac'){
// ul = 'kupac';
// }else{
// ul = 'prodavac';
// }
	// let select = $('');
	
	let idSelect = 'selectUloga'+o.username;
	
	let ulogaU = $('<td  align="center"><select id="' + idSelect + '"><option> ' + o.uloga +'</option><option value="prodavac">prodavac</option><option value="administrator">administrator</option><option value="kupac">kupac</option></selected></td>');
	let ime = $('<td align="center">' + o.ime + '</td>');
	let prezime = $('<td align="center">' + o.prezime + '</td>');
	let email = $('<td  align="center">' + o.email + '</td>');
	let dugme = $('<td id="dugmeUloga" align="center"> <input type="submit" value="Izmjeni ulogu"/> </td>');
	
	console.log(o.username);
	console.log(o.uloga);
	console.log(idSelect);
	
	tr.append(username).append(ulogaU).append(ime).append(prezime).append(email);
	if(uloga=='administrator'){
		tr.append(dugme);
	}else{
		console.log("Zabrani selekt");
		console.log(idSelect);
		$('#selectUloga'+o.username).prop('disabled', true);		
		$('#selectUloga'+o.username).prop('disabled', true);		
		$('#selectUloga'+o.username).prop('disabled', true);		
		$('#' + idSelect).prop('disabled', true);		
		console.log("!!!!!!!!!!!!");

	}
	
	$('#tabelaUser').append(tr);
	
	// dugme.click(clickDugmeIzmjenaKorisnika(o));
	dugme.click(clickDugmeIzmjenaKorisnika(o));
	
}

function clickDugmeIzmjenaKorisnika(o){
// return function(){
//		
//
// let uloga=$('#selectUloga').val();
// let username=$('#usernameUser').val();
// console.log('uloga', uloga);
// console.log('username', username);
//		
// let noviUrl = 'rest/users/izmjeniUlogu/';
// noviUrl += uloga+','+user.username;
//		
// console.log("(((((((((((((((((((((((((((((()))))))))))))))))))))))))))");
// console.log(user.uloga);
// console.log(user.username);
//		
// $.post({
// url: noviUrl,
// contentType: 'application/json',
// success: function(user){
// alert('uspjesno izmjenjena uloga');
//				
// $('#ucitajUsere tbody').html('');
// //$('#table tbody').html('');
//				
// console.log(user);
//				
// for(let k of user)
// {
// ispisiOglase(k);
// console.log("--------------------------------UDJE LI OVDJE - USER IZMJENA");
// }
// }
//		
// });
// }
	return function(){
		 		
				let uloga=$('#selectUloga' + o.username).val();
				let username=o.username;
				console.log(uloga);
				console.log(username);
				
				let noviUrl = 'rest/users/izmjeniUlogu/';
				noviUrl += uloga+','+username;
				
				console.log("(((((((((((((((((((((((((((((()))))))))))))))))))))))))))");
				
				
				$.post({
					url: noviUrl,
					contentType: 'application/json',
					success: function(user){
						alert('uspjesno izmjenjena uloga');
						
						$('#ucitajUsere tbody').html('');
						// $('#table tbody').html('');
						
						console.log(user);
						
						for(let k of user)
						{
							ispisiOglase(k);
							console.log("--------------------------------UDJE LI OVDJE - USER IZMJENA");
						}
					}
				
				});

	
}
}

function prikaziKorpu(){
	return function () {
		
		console.log("-------------------> <---------------------------")
		
		var user = sessionStorage.getItem('ulogovan');
		
		
			var objekat = JSON.parse(user);
			const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
			var uloga = descriptors1.uloga.value;
			let userUsername = descriptors1.username.value;
			console.log("KORPA!!!!");
		
		$('#prikazOglasa').hide();
		$('#prikazOmilj').hide();
		$('#prikaziRecenzije').hide();
		$('#detaljanOg').hide();
		$('#prikazPoruka').hide();
		$('#podaciProdavca').hide();
		$('#prikazRecProdavca').hide();
		$('#ucitajUsere').hide();
		
		let noviUrl = 'rest/oglas/prikaziKorpu/';
		noviUrl += userUsername;
		
		$.get({
			
			url:noviUrl,
			contentType: 'application/json',
			success: function(oglas)
			{	
				
				$('#prikazKorpe tbody').html('');
				
				
				for(let o of oglas)
				{
					ispisiKorpu(o);
					
				}
				$('#prikazKorpe').show();

				noviUrl = 'rest/oglas/kupacDostavljeni/';
				noviUrl += userUsername;	
					$.get({
					
					url:noviUrl,
					contentType: 'application/json',
					success: function(oglas)
					{							
						
						for(let o of oglas)
						{
							ispisiKorpuDostavljenih(o);
							
						}
						
					},
					error: function(){
						alert('Nije prikazana korpa');
					}
					
					
				});
				
			},
			error: function(){
				alert('Nije prikazana korpa');
			}
			
			
		});
		
	}
}

function prikaziOmiljene(){
	return function () {
		
		console.log("-------------------> <---------------------------")
		// console.log("OMILJENIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
		var user = sessionStorage.getItem('ulogovan');
		
		
			var objekat = JSON.parse(user);
			const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
			var uloga = descriptors1.uloga.value;
			let userUsername = descriptors1.username.value;
			console.log("OMILJENI!!!!");
		
		$('#prikazOglasa').hide();
		$('#prikazKorpe').hide();
		$('#prikaziRecenzije').hide();
		$('#detaljanOg').hide();
		$('#prikazPoruka').hide();
		$('#podaciProdavca').hide();
		$('#prikazRecProdavca').hide();
		$('#ucitajUsere').hide();
		
		let noviUrl = 'rest/oglas/prikaziOmiljene/';
		noviUrl += userUsername;
		console.log(noviUrl);
		
		$.get({
			
			url:noviUrl,
			contentType: 'application/json',
			success: function(oglas)
			{	
				
				$('#prikazOmilj tbody').html('');
				
				
				for(let o of oglas)
				{
					ispisiOmilj(o);
					
				}
// $('#prikazOmilj').show();
// console.log("PRIKAZI OMILJENEE!!!!!");
			$('#prikazOmilj').show();
			},
			error: function(){
				alert('Nisu prikazani omiljeni proizvodi');
			}
			
			
		});
	}
}

function prikaziPoruke(){
	return function(){
		var user = sessionStorage.getItem('ulogovan');
		
		
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		var uloga = descriptors1.uloga.value;
		let userUsername = descriptors1.username.value;
		console.log("PORUKE!!!!");
	
	$('#prikazOglasa').hide();
	$('#prikazOmilj').hide();
	$('#prikazKorpe').hide();
	$('#prikaziRecenzije').hide();
	$('#detaljanOg').hide();
	$('#podaciProdavca').hide();
	$('#prikazRecProdavca').hide();
	
	
	let noviUrl = 'rest/poruka/prikaziPoruke/';
	noviUrl += userUsername;
	console.log(noviUrl);
	
		$.get({
			
			url:noviUrl,
			contentType: 'application/json',
			success: function(poruke)
			{	
				
				$('#prikazPoruka tbody').html('');
				
				for(let p of poruke)
				{
					ispisiPoruke(p);
					
				}
				$('#prikazPoruka').show();
				
			},
			error: function(){
				alert('Nisu prikazane poruke');
			}
			
			
		});
	}
}

function ispisiKorpu(og){
	let sl = og.slika.replace(/%20/, ' ');
	let tr = $('<tr></tr>');
	let slika = $('<td class="w-25 align="center" "><img src="' +  sl + '" class="img-fluid img-thumbnail" ></td>');
	// let slika = $('<td>SLIKA</td>');
	let naziv = $('<td align="center">'+ og.naziv +'</td>');
// let detalji = $('<td align="center" id="clickMeDetalji"><img
// src="img/details1.png"></td>');
	let cijena = $('<td align="right">'+ og.cijena +'$ </td>');
	let dostavljen = $('<td align="center"><input type="checkbox"   id="myCheck' + brojac + '"></td>');	
	// let dodajRecenziju = $('<td align="center" id="dodajRec" ><b>' + '+' +
	// '</b></td>');
	// let prikaziRecenzije = $('<td align="center" id="prikaziRec"><img
	// src="img/rec.png"></td>')
	// let like = $('<td id="like" align="center"><img
	// src="img/like.png"></td>');
	// let dislike = $('<td id="dislike" align="center"><img
	// src="img/dislike.png"></td>');
	// let likeP = $('<td id="likeP" align="center"><img
	// src="img/userL.png"></td>');
	// let dislikeP = $('<td id="dislikeP" align="center"><img
	// src="img/userD.png"></td>');
	// let prodavacOpcije = $('<td align="center"><img
	// src="img/user.png"></td>');
	// tr.append(slika).append(naziv).append(cijena).append(dostavljen).append(dodajRecenziju).append(prikaziRecenzije).append(like).append(dislike);
	tr.append(slika).append(naziv).append(cijena).append(dostavljen);
	
	
	$('#tabelaKorpa tbody').append(tr);
	
	
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
	
	// dodajRecenziju.click(clickDodajRecenziju(og));
	// prikaziRecenzije.click(clickPreuzmiRecenzije(og));
	// like.click(clickLike(og, usernameUser));
	// dislike.click(clickDislike(og, usernameUser));
	// prodavacOpcije.click(clickprodavacOpcije(usernameUser));
	// console.log(og);
	
	// if(og.dostavljen == true){
		// $('.prodavacOp').show();
		// tr.append(prodavacOpcije);
	// }
	// prodavacOpcije.click(clickProdavacOpcije(og.prodavac.username));
	if(og.dostavljen!=true){
		dostavljen.click(clickDostavljen(og, brojac));
	}
	
	brojac++;
}

function ispisiKorpuDostavljenih(og){
	
	let sl = og.slika.replace(/%20/, ' ');
	let tr = $('<tr></tr>');
	let slika = $('<td class="w-25 align="center" "><img src="' +  sl + '" class="img-fluid img-thumbnail" ></td>');
	let naziv = $('<td align="center">'+ og.naziv +'</td>');
	let cijena = $('<td align="right">'+ og.cijena +'$ </td>');
	let dodajRecenziju = $('<td  align="center" id="dodajRec" ><b>' + '+' + '</b></td>');
	let prikaziRecenzije = $('<td  align="center" id="prikaziRec"><img src="img/rec.png"></td>')
	let like = $('<td id="like" align="center"><img src="img/like.png"></td>');
	let dislike = $('<td id="dislike" align="center"><img src="img/dislike.png"></td>');

	let prodavacOpcije = $('<td align="center"><img src="img/user.png"></td>');
	tr.append(slika).append(naziv).append(cijena).append(dodajRecenziju).append(prikaziRecenzije).append(like).append(dislike).append(prodavacOpcije);
	
	
	$('#tabelaDostavljeni tbody').append(tr);
	
	
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
	
	dodajRecenziju.click(clickDodajRecenziju(og));
	prikaziRecenzije.click(clickPreuzmiRecenzije(og));
	like.click(clickLike(og, usernameUser));
	dislike.click(clickDislike(og, usernameUser));
	
	if(og.dostavljen == true){
		$('.prodavacOp').show();
		tr.append(prodavacOpcije);
		console.log(og.prodavac);
		prodavacOpcije.click(clickProdavacOpcije(og.prodavac));
	}
	

}

function clickProdavacOpcije(usernameProdavca){
	return function(){
		alert('Prikaz opcija za prodavca');
		
		$('#prikazProdavca').show();
		
		let url = 'rest/users/ucitajProdavca/';
		url += usernameProdavca;
		
		$.get({ 
			
			url:url,
			contentType: 'application/json',
			success: function(user)
			{	

				$('#prikazOglasa').hide();
				// $('#prikazOglasa tbody').html('');
				$('#prikazProdavca').show();
				$('#prikazProdavca tbody').html('');
				
				
				console.log(user);
				ispisiProdavca(user);
				console.log("--------------------------------UDJE LI OVDJE2");				
				
				
				$('#podaciProdavca').show();
				$('#podaciProdavca').show();
				$('#podaciProdavca').show();
				$('.prijaviProdavca').show();
			// $('#tabelaPrikazP').show();
				$('#nazivP').val(user.ime);
				$('#prezime').val(user.prezime);
				$('#gradProd').val(user.grad);
				$('#brLajkP').val(user.prodavacLajk);
				$('#brDislajkP').val(user.prodavacDislajk);
				
				console.log('PODACI O PRODAVCUUU !!!!');
				
				
			},
			error: function(){
				alert('Nije dobro ispisao usere - get');
			}
			
			
		});
		
		
	}
}

function ispisiProdavca(userP){
	let usernameUser;
	var user = sessionStorage.getItem('ulogovan');
	console.log(user.username);
	console.log(userP.username);
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
		usernameUser = descriptors1.username.value;
	
	let tr = $('<tr></tr>');
	let naziv = $('<td align="center">'+ userP.username +'</td>');
	let like = $('<td id="like" align="center"><img src="img/like.png"></td>');
	let dislike = $('<td id="dislike" align="center"><img src="img/dislike.png"></td>');
	let dodajRecenziju = $('<td  align="center" id="dodajRec" ><b>' + '+' + '</b></td>');
	let prikaziRecenzije = $('<td  align="center" id="prikaziRec"><img src="img/rec.png"></td>')
	
	tr.append(naziv).append(like).append(dislike).append(dodajRecenziju).append(prikaziRecenzije);
	like.click(clickLikeProdavac(userP.username, usernameUser));
	dislike.click(clickDislikeProdavac(userP.username,usernameUser));
	dodajRecenziju.click(clickDodajRecenzijuProdavac(userP.username,usernameUser));
	prikaziRecenzije.click(clickprikaziRecenzijeProdavca(userP.username));
	
	$('#tabelaProdavac').append(tr);

}

function clickLike(og,usernameUser){
	return function(){
		
		og.naziv.replace(/%20/g, " ");
		console.log(og.naziv);
		let url = 'rest/oglas/lajkuj/';
		url += og.id + '/' + usernameUser;
		console.log(url);
		
		$.post({
			url: url,
			contentType: 'application/json',
			success: function(){
				alert('Lajkovan proizvod');

				console.log("---------sasadadadda");
				document.getElementById('dislike').disabled = true;
				document.getElementById('like').disabled = true;
				console.log("Zabrani dislajk")
			},error: function(){
				alert('Proizvod ste vec lajkovali ili prethodno dislajkovali');
			}
		});
	}
}

function clickDislike(og, usernameUser){
	return function(){
		console.log(og.dostavljen);

		og.naziv.replace(/%20/g, " ");
		console.log(og.naziv);
		let url = 'rest/oglas/dislajkuj/';
		url += og.id + '/' + usernameUser;
		console.log(url);
		
		$.post({
			url: url,
			contentType: 'application/json',
			success: function(){
				alert('Disajkovan proizvod');

				console.log("---------sasadadadda");
				document.getElementById('dislike').disabled = true;
				document.getElementById('like').disabled = true;
				console.log("Zabrani lajk")
			},error: function(){
				alert('Proizvod ste vec dislajkovali ili je prethodno lajkovan');
			}
		});
	}
}

function clickLikeProdavac(usernameP, usernameUser){
	return function(){
		
		
		let url = 'rest/users/lajkujProdavca/';
		url +=  usernameP + '/' + usernameUser;
		console.log(url);
		
		$.get({
			url: url,
			contentType: 'application/json',
			success: function(){
				alert('Lajkovan prodavac');

				console.log("---------sasadadadda");
				document.getElementById('dislike').disabled = true;
				document.getElementById('like').disabled = true;
				console.log("Zabrani dislajk")
			},error: function(){
				alert('Prodavca ste vec lajkovali ili prethodno dislajkovali');
			}
		});
	}
}

function clickDislikeProdavac(usernameP, usernameUser){
	return function(){
	let url = 'rest/users/dislajkujProdavca/';
	url +=  usernameP + '/' + usernameUser;
	console.log(url);
	
	$.get({
		url: url,
		contentType: 'application/json',
		success: function(){
			alert('Dislajkovan prodavac');

			console.log("---------sasadadadda");
			document.getElementById('dislike').disabled = true;
			document.getElementById('like').disabled = true;
			// console.log("Zabrani dislajk")
		},error: function(){
			alert('Prodavca ste vec dislajkovali ili prethodno lajkovali');
		}
	});
	}
}

function clickDodajRecenzijuProdavac(usernameP, usernameUser){
	return function(){
		console.log("KLIKKKKKK NA DODAJ REC PRODAVAC");
		
		let url = 'rest/recenzija/provjeraRecenzijaProdavac/';
		url +=  usernameP + '/' + usernameUser;
		console.log(url);
		
		$.get({
			url: url,
			contentType: 'application/json',
			success: function(){
				alert('Moguce je dodati recenziju');

				window.location = 'dodajRecenzijuProdavac.html#'+usernameP+','+usernameUser;	
				// console.log("Zabrani dislajk")
			},error: function(){
				alert('Vec ste dodali recenziju za ovog prodavca');
			}
		});
	}
}

function clickprikaziRecenzijeProdavca(usernameP){
	return function(){
		
		$('#prikazOglasa').hide();
		
		let url = 'rest/recenzija/prikaziRecenzijeP/';
		url += usernameP;
		console.log(usernameP);
		console.log("''''''''''''''''''")
		$.get({
			url: url,
			contentType: 'application/json',
			success: function(recenzije){
				console.log("Prikaz recenzija prodavca");
				
				$('#prikazRecProdavca tbody').html('');
				for(let r of recenzije){
					ispisiRecProdavca(r);
				}
				
				$('#prikazRecProdavca').show();
				console.log('Prikazane rec prodavca!!!!');
				
				
				
			
			
			}
			
		});
		
		
	}
}

function ispisiRecProdavca(r){
	$('#prikazKorpe').hide();
	$('#prikazProdavca').hide();
	
	let tr = $('<tr></tr>');
	let prodavac = $('<td align="center">'+ r.oglas +'</td>');
	let recenzent = $('<td align="center">'+ r.recenzent +'</td>');
	let naslovRecenzije = $('<td align="center">'+ r.naslovRecenzije +'</td>');
	let sadrzajRecenzijel = $('<td align="center">'+ r.sadrzajRecenzijel +'</td>');
	
	let tacan;
	if(r.oglasTacan==true){
		tacan = "Da";
	}else{
		tacan = "Ne";
	}
	let oglasTacan = $('<td align="center">'+ tacan +'</td>');
	
	let ispostovan;
	if(r.ispostovanDogovor==true){
		ispostovan = "Da";
	}else{
		ispostovan = "Ne";
	}
	let ispostovanDogovor = $('<td align="center">'+ ispostovan +'</td>');
	
	
	tr.append(prodavac).append(recenzent).append(naslovRecenzije).append(sadrzajRecenzijel).append(oglasTacan).append(ispostovanDogovor);
	$('#tabelaRecProdavca tbody').append(tr);
	
	
}

function clickDostavljen(og,brojac){
	return function(){
		
		console.log(og);
		og.dostavljen = true;
		document.getElementById('myCheck' + brojac).checked = true;
		document.getElementById('myCheck' + brojac).disabled = true;
		
	
			og.naziv.replace(/%20/g, " ");
		
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
			
			
		let noviUrl = 'rest/oglas/dostavljeni/';
		noviUrl += og.id+'/'+usernameUser;
		console.log(noviUrl);
		$.get({
			
			url: noviUrl,
			contentType: 'application/json',
			success: function(){
				console.log("Proizvod je dostavljen!!");
				
				noviUrl = 'rest/oglas/prikaziKorpu/';
				noviUrl += usernameUser;
				$.get({
					url:noviUrl,
					contentType: 'application/json',
					success: function(oglas)
					{	
						
						$('#prikazKorpe tbody').html('');
						
						for(let o of oglas)
						{
							ispisiKorpu(o);
							
						}
						$('#prikazKorpe').show();

						noviUrl = 'rest/oglas/kupacDostavljeni/';
						noviUrl += usernameUser;	
							$.get({
							
							url:noviUrl,
							contentType: 'application/json',
							success: function(oglas)
							{							
								
								for(let o of oglas)
								{
									ispisiKorpuDostavljenih(o);
									
								}
							},
							error: function(){
								alert('Nije prikazana korpa');
							}
						});
						
					},
					error: function(){
						alert('Nije prikazana korpa');
					}
					
					
				});
				
			} 
			
		});
		
		
		
	}
}

function ispisiOmilj(og){
	let sl = og.slika.replace(/%20/, ' ');
	let tr = $('<tr></tr>');
	let slika = $('<td class="w-25 align="center" "><img src="' +  sl + '" class="img-fluid img-thumbnail" ></td>');
	// let slika = $('<td>SLIKA</td>');
	let naziv = $('<td align="center">'+ og.naziv +'</td>');
	// let detalji = $('<td align="center" id="clickMeDetalji"><img
	// src="img/details1.png"></td>');
	let cijena = $('<td align="right">'+ og.cijena +'$ </td>');
	
	tr.append(slika).append(naziv).append(cijena);
	$('#tabelaOmilj tbody').append(tr);

}

function ispisiPoruke(p){
	
		
		var odg = false;
		var user = sessionStorage.getItem('ulogovan');

		console.log(p);
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		var uloga = descriptors1.uloga.value;
		let userUsername = descriptors1.username.value;
		
		
		let tr1 = $('<tr></tr>');
		let tr2 = $('<tr id="razmakNeki"></tr>');
		let tr3 = $('<tr></tr>');
		let posiljalac = $('<td align="center">'+ p.posiljalac +' ---> </td>');
		let nazOgl = '';
		if(p.nazivOglasa != null){
			nazOgl = p.nazivOglasa;
		}
		let nazivOg = $('<td align="center">[ za oglas: '+ nazOgl +' ]</td>');
		let naslov = $('<td align="center"><b> Naslov poruke: '+ p.naslov +' </b></td>');
		let sadrzaj = $('<td id="sadrzajPoruke" align="center" colspan="2" >'+ p.sadrzaj +'</td>');
		let datumIvrijemePor = $('<td align="center">'+ p.datumIvrijemePor +'</td>');
		
		let primalac = $('<td align="center">[ '+ p.primalac +' ]</td>');
		let trPrazan = $('<tr><td id="prazanR" colspan="4"> </td></tr>');
		
		let imena = $('<td align="center">'+p.posiljalac + ' --->[ '+ p.primalac + ' ]</td>');
		
		let odgovor = $('<td  id="odgovor">' + 'Odgovori' + '</button></td>');
		let izbrisi = $('<td id="izbrisiPor" align="center">' + 'Izbrisi' + '</button></td>')
		let izmjeni = $('<td id="izmjeniPor" align="center">' + 'Izmjeni' + '</button></td>')
		
		
		
		tr1.append(naslov).append(nazivOg).append(datumIvrijemePor);
		tr2.append(imena).append(sadrzaj);
		$('#tabelaPoruke tbody').append(tr1).append(tr2);
		
		if(uloga=='prodavac' && p.ulogaPosiljaoca=='kupac'){
			tr3.append(odgovor);
			let idOglasaPor = p.idOglasa;
			let nazivOglasaPor = p.nazivOglasa
			let pos = p.posiljalac;
			let nazivOglasaPorIprim = nazivOglasaPor+';'+idOglasaPor +';' + pos;
			
			console.log("Prodavac moze da odgovori kupcu");
			// $('').css('background-color', 'red');
			$('#tabelaPoruke tbody').append(tr3);
			odgovor.click(odgovoriKupcu(nazivOglasaPorIprim));

		}
		
		izbrisi.click(izbrisiPoruku(p));
		tr3.append(izbrisi);
		console.log(userUsername);
		console.log(p.posiljalac);
		if(userUsername==p.posiljalac){
			izmjeni.click(izmjeniPoruku(p));
			tr3.append(izmjeni);
		}
		$('#tabelaPoruke tbody').append(tr3);
		$('#tabelaPoruke tbody').append(trPrazan);
	
}

function odgovoriKupcu(nazivOglasaPorIprim){
	return function(){
		console.log(nazivOglasaPorIprim);
		window.location = 'odgovoriNaPoruku.html#'+ nazivOglasaPorIprim;
		
	}
}

function izbrisiPoruku(p){
	return function() {
		
		var user = sessionStorage.getItem('ulogovan');
		console.log(user);
		
			var objekat = JSON.parse(user);
			const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
			console.log(descriptors1.username.value);
			var uloga = descriptors1.uloga.value;
			console.log(descriptors1.uloga.value);
			console.log(descriptors1.username.value);
			usernameUser = descriptors1.username.value;
		
		let noviUrl = 'rest/poruka/brisanjePor/';
		noviUrl += p.id+','+usernameUser;
		console.log(noviUrl);
		$.get({
			url: noviUrl,
			contentType: 'application/json',
			success: function(poruke){
				alert('uspjesno obrisana poruka');
				
				$('#prikazPoruka tbody').html('');
				// $('#table tbody').html('');
				
				console.log('Brisanje por');
				
				for(let p of poruke)
				{
					ispisiPoruke(p);
					console.log("***--------------------------------UDJE LI OVDJE - PORUKA");
				}
			}
		
		});
	}
}

function izmjeniPoruku(p){
	return function() {
		console.log('***------------> Izmjena porukee !!!');
		window.location = 'izmjeniPoruku.html#' + p.id;
	}

}	

function getMojeOglase(userUsername){
	
	
	let	url = 'rest/oglas/getOglaseMoje/';
	url += userUsername;
	
	$.get({	
		url: url,
		contentType: 'application/json',
		success: function(oglasi){
			
			$('#prikazOglasa tbody').html('');
			
			for(let o of oglasi){
				ispisiOglase(o);
			}
			$('#prikazFiltriranje').show();
		}
	});
}

function getAdminOglase(){
	
	
	let	url = 'rest/oglas/getSveOglase/admin';
	
	$.get({	
		url: url,
		contentType: 'application/json',
		success: function(oglasi){
			
			$('#prikazOglasa tbody').html('');
			
			for(let o of oglasi){
				ispisiOglase(o);
			}
		}
	});
}

function clickNajpopularniji(){
	return function(){
		
		console.log("Prikaz napopularnijih oglasa");
		
		$.get({
			url: 'rest/oglas/najpopularniji',
			contentType: 'application/json',
			success: function(oglasi){
				
				console.log("PRIKAZ");
				$('#prikazOglasa tbody').html('');
				for(let o of oglasi){
					ispisiOglase(o);
				}
			},
			error: function(){
				console.log("Nisu prikazani najpopularniji");
			}
		});
	}
}

let brojac = 1;


$(document).ready(()=>{
	
	
	
	$('.dropbtn').hide();
	$('.dropbtnOglas').hide();
	$('.dropbtnRec').hide();
	// $('#dodajK').hide();
	$('#ulogaKorisnici').hide();
	$('#ucitajUsere').hide();
	$('#prikaziRecenzije').hide();
	$('#korpaPrikaz').hide();
	$('#omiljeniPrikaz').hide();
	$('#porukePrikaz').hide();
	$('#kreirajPoru').hide();
	$('#podaciProdavca').hide();
	$('#prikazFiltriranje').hide();
	$('#prikazPretragaKorisnici').hide();
	
	getKategorije();
	// getOglase();
	getTop10();
	
	let usernameUser;
	var user = sessionStorage.getItem('ulogovan');
	console.log(user);
	if(user!=null && user!='null' && user!='undefined') {
		var objekat = JSON.parse(user);
		const descriptors1 = Object.getOwnPropertyDescriptors(objekat);
		console.log(descriptors1.username.value);
		var uloga = descriptors1.uloga.value;
		console.log(descriptors1.uloga.value);
		console.log(descriptors1.username.value);
		usernameUser = descriptors1.username.value;
		
		if(uloga=='administrator')
		{
			$('#prikazPretragaKorisnici').show();
			getAdminOglase();
			$('#ucitajUsere').hide();
			$('#registracija').hide();
			$('.dropbtn').show();
			
			$('#dodajK').show();
			$('#ulogaKorisnici').show();
			$('#odj').show();
			$('#prijava').hide();
			$('#ucitajUsere').hide();
			
			// za admina da edituje oglase i ostalo
			$('#izbrisiOg').show();
			$('#izmjeniOg').show();
	
			$('#ulogaKorisnici').click(ucitajKorisnike());
			
			$('#prikaziMojeRec').hide();
			$('#prikazFiltriranje').hide();

		}
		else if(uloga=='prodavac'){
			$('#prikazPretragaKorisnici').show();
			console.log('prodavac');
			$('#registracija').hide();
			$('.dropbtnOglas').show();
			$('#izbrisiOg').show();
			$('#izmjeniOg').show();
			// $('#dodajUkor').show();
			$('#odj').show();
			getMojeOglase(usernameUser);
			$('#prikazFiltriranje').show();
			$('#podaciProdavca').show();
			$('#nazivP').val(descriptors1.ime.value);
			$('#prezime').val(descriptors1.prezime.value);
			$('#gradProd').val(descriptors1.grad.value);
			$('#brLajkP').val(descriptors1.prodavacLajk.value);
			$('#brDislajkP').val(descriptors1.prodavacDislajk.value);
			$('.prijaviProdavca').hide();
			console.log('PODACI O PRODAVCUUU !!!!');
			
			$('#prikaziMojeRec').click(clickprikaziRecenzijeProdavca(usernameUser));
			
		}
		else{
			$('#prikazPretragaKorisnici').show();			
			$('#korpaPrikaz').show();
			$('#omiljeniPrikaz').show();
			
			
			$('#korpaPrikaz').click(prikaziKorpu());
			$('#omiljeniPrikaz').click(prikaziOmiljene());
			
			
			$('.dropbtnRec').show();
			$('.reg').show();
			$('#prikaziMojeRec').hide();

		}
		console.log("------------------ "  + uloga + "-------------");
		$('#kreirajPoru').show();
		$('#porukePrikaz').click(prikaziPoruke());
		$('#porukePrikaz').show();
		$('#prijava').hide();
		// $('.reg').fadeOut(10);
	// $('#dodajDugme').hide();
		$('#odj').fadeIn(1000);
		console.log("PRIJAVLJEN KAO K-A-P")
		$('#ulogovanUloga').val(uloga);
		$('#ulogovanKime').val(descriptors1.username.value);
		
	} else {
		$('#registracija').fadeIn(1000);
		console.log("Korisnik nije ulogovan");
		$('error').text('Korisnik nije ulogovan!');
		$('error').show().delay(10000);
		
		
	}
	
	
	$('#odjava').click(clickOdjava());
	$('#pocetak').click(vratiNaPocetak());
	$('#kreirajPoru').click(kreirajPoruku());
	
	
	$('#prijava').submit((event)=>{
		event.preventDefault();
		
		let username=$('#username').val();
		let password=$('#password').val();
		console.log('username', username);
		console.log('password', password);
		
		if(!username || !password)
		{
			$('#error').text('Oba polja morate popuniti!');
			$("#error").show().delay(4000).fadeOut();
			alert('Obavezna polja nisu popunjena!');
			return;
		}
		
		$.post({
			url: 'rest/users/prijava',
			data: JSON.stringify({username, password}),
			contentType: 'application/json',
			success: function(data) {
				if(data==null){
					
					$('#error').text('Neispravno korisnicko ime i/ili lozinka!');
					$('#error').fadeIn(4000);
					alert('Podaci nisu ispravno uneseni!');
				}else{
					sessionStorage.setItem('ulogovan',JSON.stringify(data));
					
					$('#success').text('Korisnik uspjesno prijavljen.');
					$('#success').fadeIn();
					
					alert('Uspjeh!!');
					window.location='index.html';	
				}

			}
		});
		
	});
	
	
	$('#prikazFiltriranje').submit((event)=>{
		
		$('#podaciProdavca').show();
		// $('#prikazRecProdavca').show();
		
		event.preventDefault();
		console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		
		let status = $('.statusFil').val();
		
		console.log(status);

		console.log("**----> Filtriranje po statusu *********************")
		
		let url = 'rest/oglas/filtriranjeStatus/';
		url += status +'/'+ usernameUser;
		console.log(status);
		$.get({
			url: url,
			contentType: 'application/json',
			success: function(oglasi) {
				
					alert('Filtriranje OK');
					
					$('#prikazOglasa tbody').html('');
					// $('#prikazOglasa tbody').html('');
		
					for(let o of oglasi){
						ispisiOglase(o);
					}
					
				

			},
			error: function(){
				alert('Filtriranje greskaaaaaa');
			}
			
			
		});
		
	});
	
	
	$('#prikazPretragaKorisnici').submit((event)=>{
		
		$('#podaciProdavca').hide();
		$('#prikazRecProdavca').hide();
		
		event.preventDefault();
		let ime= $('#ime').val();
		let grad = $('.gradP').val();
		

		
		$.post({
			url: 'rest/users/pretragaK/'+ime+'/'+grad,
			contentType: 'application/json',
			success: function(data) {
				
					alert('Pretraga OK');
					
					$('#prikazOglasa').hide();
					$('#ucitajUsere').show();
					$('#ucitajUsere tbody').html('');
					
					for(let u of data){
						ispisiUsere(u);
					}
			},
			error: function(){
				alert('Pretragaaaa greskaaaaaa');
			}
			
			
		});
		
	});
	
	$('#prikazPretragaOglasa').submit((event)=>{
		
		$('#podaciProdavca').hide();
		$('#prikazRecProdavca').hide();
		
		event.preventDefault();
		let naziv= $('#naziv').val();
		let minCijena = $('#minCijena').val();
		let maxCijena = $('#maxCijena').val();
		let minOcjena = $('#minOcjena').val();
		let maxOcjena = $('#maxOcjena').val();
		let grad = $('.gradOglas').val();
		
		
		let url = 'rest/users/pretragaO/';
		url += naziv + '/' + minCijena + '/' + maxCijena + '/' + minOcjena + '/' + maxOcjena  + '/' + grad;
		console.log(url);
		$.post({
			url: url,
			contentType: 'application/json',
			success: function(oglasi) {
				
					alert('Pretraga OK');
					$('#prikazOglasa tbody').html('');
					for(let o of oglasi){
						ispisiOglase(o);
					}	
			},
			error: function(){
				alert('Pretragaaaa greskaaaaaa');
			}
			
			
		});
		
	});
	
});

