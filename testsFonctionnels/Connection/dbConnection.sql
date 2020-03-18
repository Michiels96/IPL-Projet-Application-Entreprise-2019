-- pas de CHECK, uniquement des UNIQUE, SERIAL ET NOT NULL/NULL

drop schema if exists SMMDB cascade;
create schema SMMDB;

create table SMMDB.utilisateurs(
	id_utilisateur serial primary key,
	pseudo varchar(200) not null,
	mot_de_passe varchar(100) not null,
	nom varchar(150) not null,
	prenom varchar(100) not null,
	email varchar(100) not null,
	role varchar(2),
	date_naissance timestamp null,
	adresse varchar(200) null,
	commune varchar(100) null,
	code_postal integer null,
	num_telephone integer null,
	sexe character(1) null,
	nombre_annee_reussie integer null,
	num_compte character(16) null,
	titulaire_compte varchar(150) null,
	nom_banque varchar(150) null, 
	code_bic varchar(11) null,
	nationalite integer not null references SMMDB.pays(id_pays)
);

create table SMMDB.pays(
	id_pays serial primary key,
	nom varchar(100) not null,
	programme varchar(8) not null
);

create table SMMDB.partenaires(
	id_partenaire serial primary key, 
	nom_legal varchar(100) not null,
	nom_affaire varchar(100) not null,
	nom_complet varchar(100) not null,
	departement character(3) not null,
	type_organisation varchar(100) not null,
	nombre_employe integer not null,
	adresse varchar(100) not null,
	pays integer not null references SMMDB.pays(id_pays),
	region varchar(100) null,
	code_postal integer not null,
	ville varchar(100) not null,
	email varchar(100) null,
	site_web varchar(100) null,
	num_tel varchar(20) not null
);

create table SMMDB.documents_depart(
	id_documents_depart serial primary key,
	preuve_test_linguistique boolean default false not null,
	contrat_bourse boolean default false not null,
	convention_stage_etude boolean default false not null,
	charte_etudiant boolean default false not null,
	document_engagement boolean default false not null
);

create table SMMDB.documents_retour(
	id_documents_retour serial primary key not null,
	attestation_sejour boolean default false not null,
	stage_releve_note boolean default false not null,
	rapport_final boolean default false not null,
	preuve_passage_test boolean default false not null
);

create table SMMDB.mobilites(
	id_mobilite serial primary key,
	programme varchar(8) not null,
	statut character(3) not null,
	financement decimal(4,2) not null,
	localisation varchar(100) not null,
	periode character(2) null,
	partenaire integer not null references SMMDB.partenaires(id_partenaire),
	etudiant integer not null references SMMDB.utilisateurs(id_utilisateur),
	documents_depart integer null references SMMDB.documents_depart(id_documents_depart),
	documents_retour integer null references SMMDB.documents_retour(id_documents_retour)
	niveau_preference integer not null,
	etat_mobilite varchar(15) not null,
	mobilite_annule boolean default false not null
);

insert into SMMDB.pays(nom, programme) values('Suisse','fame');
insert into SMMDB.pays(nom, programme) values('USA','fame');
-- belgique tjrs erabel
insert into SMMDB.pays(nom, programme) values('Belgique','erabel');
insert into SMMDB.pays(nom, programme) values('France','erasmus+');
insert into SMMDB.pays(nom, programme) values('Allemagne','erasmus+');
	
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, mail, role, date_naissance) values('adminTest', 'aa', 'Cadillac', 'John', 'john.c@pro.be', 'SP', TO_TIMESTAMP('1954-07-02', 'YYYY-MM-DD'));
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, mail, role, nationalite) values('etudiantTest', 'aa', 'Honyme', 'Anne', 'anne.h@student.vinci.be', 'E', 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, mail, role, nationalite) values('etudiantTest2', 'aa', 'Moranne', 'Bob', 'bob.momo@hotmail.com', 'E', 3);
	