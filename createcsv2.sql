-- NU UITA SA ADAUGI HEADER: Tools -> Options -> Query Results -> SQL Server -> Results to grid

-- ADRESE
select id, adresa, localitate, judet from adresa_psql;

-- SOCIETATI
select id, capsoc, cif, email, fax, idcaen, nume, regcom, telefon, idadresa from societate_psql;

-- ACTE IDENTITATE - sterge NULL din .csv
select id, cnp, dataeliberarii, datanasterii, eliberatde, loculnasterii, numar, serie, tip from actidentitate_psql;

-- PERSOANE
select id, cnp, email, gen, nume, prenume, starecivila, telefon, idactidentitate, idadresa from persoana_psql

-- CONT BANCAR
select id, iban from contbancar_psql;

-- CONTRACTE - sterge NULL din .csv
SELECT 
	id, avans, calculdeduceri, casasanatate, conditiimunca, cor, cotizatiepensieprivata,
	cotizatiesindicat, dindata, dataincepere, functie, functiedebaza, gradinvaliditate, marca,
	monedaavans, monedasalariu, nivelstudii, normalucru, nr, pensieprivata, pensionar, salariutarifar,
	sindicat, spor, studiisuperioare, tip, ultimazilucru, zilecoan, idcentrucost, idcontbancar, iddepartament, idechipa, idpunctlucru
FROM contract_psql;

-- ANGAJATI
select idpersoana, idcontract, idsocietate from angajat_psql;

-- CONCEDII ODIHNA
select id, dela, panala, sporuripermanente, tip, idcontract from co_psql;

-- CONCEDII MEDICALE - sterge NULL din csv
select 
	id, bazacalcul, bazacalculplafonata, cnpcopil, codboala, codboalainfcont,
	codindemnizatie, codurgenta, conditii, continuare, dataeliberare, datainceput, 
	dela, indemnizatiefaambp, indemnizatiefirma, indemnizatiefnuass, locprescriere,
	mediezilnica, nr, nravizmedic, panala, procent, serie, urgenta, zilebazacalcul,
	zilefaambp, zilefirma, zilefnuass, idcontract
from cm_psql;

-- select 
-- 	id, dela, panala, bazacalcul, bazacalculplafonata, cnpcopil, codboala, codboalainfcont,
-- 	codindemnizatie, codurgenta, conditii, continuare, dataeliberare, datainceput, 
-- 	indemnizatiefaambp, indemnizatiefirma, indemnizatiefnuass, locprescriere, 
-- 	mediezilnica, nr, nravizmedic, procent, serie, urgenta, zilebazacalcul,
-- 	zilefaambp, zilefirma, zilefnuass, idcontract
-- from cm_psql;

-- PERSOANE INTRETINERE
select id, cnp, coasigurat, datanasterii, grad, valid, intretinut, nume, prenume, idangajat from persoanaintretinere_psql;