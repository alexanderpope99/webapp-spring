drop table adresa_psql, societate_psql, actidentitate_psql, 
		persoana_psql,contbancar_psql, contract_psql, 
		angajat_psql, co_psql, cm_psql,
		persoanaintretinere_psql;

create table adresa_psql (
	id	int not null identity(1,1) primary key,
	adresa nvarchar(255),
	localitate nvarchar(255),
	judet nvarchar(255),
	denumiresocietate nvarchar(255),
	cnp nvarchar(255)
);

create table societate_psql (
	id int not null identity(1, 1) primary key,
	nume nvarchar(255),
	idcaen nvarchar(40),
	cif nvarchar(40),
	capsoc int,
	regcom nvarchar(50),
	idadresa int,
	email nvarchar(100),
	telefon nvarchar(100),
	fax nvarchar(100),
);

create table actidentitate_psql (
	id int not null identity(1, 1) primary key,
	cnp varchar(20),
	tip nvarchar(80) default 'Carte de identitate',
	serie varchar(5), -- will be null
	numar varchar(10),
	datanasterii date,
	eliberatde varchar(2), -- will be null
	dataeliberarii date, -- null
	loculnasterii varchar(2), -- will be null
	cnppersonal nvarchar(100)
);

create table persoana_psql (
	id int not null identity(1, 1) primary key,
	gen nvarchar(10),
	nume nvarchar(50),
	prenume nvarchar(50),
	idactidentitate int,
	idadresa int,
	starecivila nvarchar(50),
	email nvarchar(50),
	telefon nvarchar(50),
	cnp nvarchar(50),
	cnppersonal nvarchar(100),
	denumiresocietate nvarchar(100)
);

create table contbancar_psql (
	id int not null identity(1, 1) primary key,
	iban nvarchar(100),
	numebanca nvarchar(50),
	numecontract nvarchar(50),
	cnp nvarchar(100)
);

create table contract_psql (
	id int not null identity(1, 1) primary key,
	tip nvarchar(50),
	nr nvarchar(20),
	marca nvarchar(20),
	dindata date,
	dataincepere date,
	idpunctlucru int,
	idcentrucost int,
	idechipa int,
	iddepartament int,
	functiedebaza bit,
	calculdeduceri bit,
	studiisuperioare bit,
	normalucru int,
	salariutarifar float,
	monedasalariu varchar(5) DEFAULT 'RON',
	conditiimunca nvarchar(20) DEFAULT 'Normale',
	pensieprivata bit DEFAULT 0,
	cotizatiepensieprivata float DEFAULT 0,
	avans float DEFAULT 0,
	monedaavans varchar(5),
	zilecoan int,
	ultimazilucru date,
	casasanatate nvarchar(50),
	gradinvaliditate nvarchar(20) DEFAULT 'valid',
	functie nvarchar(50),
	nivelstudii nvarchar(20),
	cor varchar(20),
	sindicat bit DEFAULT 0,
	cotizatiesindicat float DEFAULT 0,
	spor varchar(20) DEFAULT '0',
	pensionar bit,
	echipa int,
	modplata nvarchar(20),
	idcontbancar int,
	cnppersonal nvarchar(50)
);

create table angajat_psql(
	idpersoana int not null primary key,
	idcontract int,
	idsocietate int
);

create table co_psql(
	id int not null identity(1, 1) primary key,
	tip nvarchar(100),
	dela date,
	panala date,
	sporuripermanente bit default 0,
	idcontract int
);

create table cm_psql(
	dela date,
	panala date,
	continuare bit,
	datainceput date,
	dataeliberare date,
	codurgenta nvarchar(100),
	procent float,
	codboalainfcont nvarchar(100),
	bazacalcul float,
	bazacalculplafonata float,
	zilebazacalcul int,
	mediezilnica float,
	zilefirma int,
	indemnizatiefirma float,
	zilefnuass int,
	indemnizatiefnuass float,
	locprescriere nvarchar(100),
	nravizmedic nvarchar(50),
	codboala nvarchar(100),
	urgenta bit DEFAULT 0,
	conditii nvarchar(100),
	idcontract int,
	id int not null identity(1, 1) primary key,
	cnpcopil nvarchar(50),
	codindemnizatie nvarchar(2),
	nr nvarchar(100),
	serie nvarchar(100),
	indemnizatiefaambp float,
	zilefaambp int
);

create table persoanaintretinere_psql(
	id int not null identity(1, 1) primary key,
	nume nvarchar(50),
	prenume nvarchar(50),
	cnp varchar(13),
	datanasterii date,
	grad nvarchar(50),
	valid nvarchar(20) default 'valid',
	intretinut bit,
	coasigurat bit,
	idangajat int
);

insert into adresa_psql(adresa, localitate, judet, denumiresocietate) select strada, localitate, judet, denumire from societati;
insert into adresa_psql(adresa, localitate, judet, cnp) select adresa, localitate, judet, cnp from personal;

insert into societate_psql(nume, idcaen, cif, capsoc, regcom, idadresa, email, telefon, fax) 
	select denumire, caen, cif, capitalsocial, regcom, (select id from adresa_psql ap where ap.denumiresocietate=societati.denumire), email, telefon, fax
		from societati

insert into actidentitate_psql(cnp, numar, datanasterii, cnppersonal) 
	select codnumericpersonal, nractidentitate,  
		concat('19', substring(codnumericpersonal, 2, 2),'-', substring(codnumericpersonal, 4, 2),'-',substring(codnumericpersonal, 6, 2)) as 'datanasterii',
		cnp
		from personal;

insert into persoana_psql(gen, nume, prenume, idactidentitate, idadresa, starecivila, telefon, cnp, cnppersonal, denumiresocietate)
	select
		(case when substring(cnp, 1, 1) = '1' then 'Dl.' else 'Dna.' end), 
		nume, 
		prenume,
		(select id from actidentitate_psql act where act.cnppersonal=personal.cnp),
		(select id from adresa_psql adr where adr.cnp=personal.cnp),
		(case when casatorit = 1 then 'Căsătorit' else 'Necăsătorit' end),
		telefon,
		codnumericpersonal,
		cnp,
		denumiresocietate
	from personal;

insert into contbancar_psql(iban,cnp) 
	select 
		codiban, 
		cnp
	from personal;

insert into contract_psql(
		tip,
		nr,
		dindata,
		dataincepere,
		functiedebaza,
		calculdeduceri,
		studiisuperioare,
		normalucru,
		salariutarifar,
		zilecoan,
		ultimazilucru,
		casasanatate,
		functie,
		cor,
		pensionar,
		idcontbancar,
		cnppersonal)
	select 
		tipraport,
		nr,
		dindata,
		datainceput,
		functiedebaza,
		impozit,
		studiisuperioare,
		duratazilucru,
		salariu,
		nrzileco,
		datasfarsit,
		p.casasanatate,
		functia,
		cor,
		p.pensionar,
		(select id from contbancar_psql where p.cnp=cnp) as idcontbancar,
		p.cnp
	from contractemunca c left join personal p on c.nume=p.numecontract;

INSERT INTO angajat_psql(idpersoana,idcontract,idsocietate)
	select p.id as 'idpersoana', c.id as 'idcontract', s.id as 'idsocietate'
	from 
		societate_psql s inner join persoana_psql p on s.nume = p.denumiresocietate 
		inner join contract_psql c on p.cnppersonal=c.cnppersonal

insert into co_psql(tip, dela, panala, idcontract)
	select 
		motiv,
		dela, 
		panala,
		(select id from contract_psql c where c.cnppersonal = concediu.cnppersonal)
	from concediu;

insert into cm_psql(
	dela, panala, continuare, datainceput, 
	serie, nr, codindemnizatie, 
	procent, 
	bazacalcul, zilebazacalcul, mediezilnica,
	locprescriere, codurgenta, codboala, dataeliberare, 
	zilefirma, zilefnuass, zilefaambp, 
	indemnizatiefirma, indemnizatiefnuass, indemnizatiefaambp, 
	cnpcopil, 
	idcontract
	)
	select 
		dela, panala, continuare, cminitial,
		serie, numar, codindemnizatie, 
		convert(float, substring(procent, 1, (len(procent)-1))),
		bazacalcul, zilebazacalcul, mediezilnica,
		locprescriere, codurgenta, codboala, dataprescriere,
		zilesupfirma, zilesupfnuass, zilesupfaambp,
		indemnizatiifirma, indemnizatiifnuass, indemnizatiifaambp,
		cnpcopil,
		(select id from contract_psql c where c.cnppersonal = certificatmedical.cnppersonal)
	from certificatmedical;

insert into persoanaintretinere_psql(nume, prenume, cnp, datanasterii, grad, intretinut, coasigurat, idangajat)
	select 
		c.nume, 
		c.prenume, 
		c.cnp, 
		concat('19', substring(c.cnp, 2, 2),'-', substring(c.cnp, 4, 2),'-',substring(c.cnp, 6, 2)) as 'datanasterii',
		c.gradrudenie, 
		c.intretinut, 
		c.coasigurat, 
		p.id as idangajat 
	from coasigurati c join persoana_psql p on c.cnppersonal = p.cnppersonal;