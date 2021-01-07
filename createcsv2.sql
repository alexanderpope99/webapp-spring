-- NU UITA SA ADAUGI HEADER: Tools -> Options -> Query Results -> SQL Server -> Results to grid

-- ADRESE
--select id, adresa, localitate, judet from adresa_psql;

-- SOCIETATI
--select * from societate_psql;

-- ACTE IDENTITATE
--select id, cnp, tip, serie, numar, datanasterii, eliberatde, dataeliberarii, loculnasterii from actidentitate_psql;

-- PERSOANE
--select id, gen, nume, prenume, idactidentitate, idadresa, starecivila, telefon, cnp from persoana_psql

-- IBAN-URI
--select id, iban from contbancar_psql;

-- CONTRACTE
--SELECT [id],[tip],[nr],[marca],[dindata],[dataincepere],[idpunctlucru],[idcentrucost],[idechipa],[iddepartament],[functiedebaza],[calculdeduceri],[studiisuperioare],[normalucru],[salariutarifar],[monedasalariu],[conditiimunca],[pensieprivata],[cotizatiepensieprivata],[avans],[monedaavans],[zilecoan],[ultimazilucru],[casasanatate],[gradinvaliditate],[functie],[nivelstudii],[cor],[sindicat],[cotizatiesindicat],[spor],[pensionar],[echipa],[modplata],[idcontbancar] FROM [Salarizare123].[dbo].[contract_psql]

-- ANGAJATI
--select * from angajat_psql;

-- CONCEDII ODIHNA
--select * from co_psql;

-- CONCEDII MEDICALE - sterge NULL din csv
--select * from cm_psql;

-- PERSOANE INTRETINERE
--select * from persoanaintretinere_psql;