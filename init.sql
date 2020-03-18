-- pas de CHECK, uniquement des UNIQUE, SERIAL ET NOT NULL/NULL

drop schema if exists SMMDB cascade;
create schema SMMDB;

create table SMMDB.pays(
	id_pays serial primary key,
	nom varchar(100) not null,
	code_pays char(3) not null,
	programme varchar(8) not null
);

create table SMMDB.utilisateurs(
	id_utilisateur serial primary key,
	pseudo varchar(100) not null,
	mot_de_passe varchar(100) not null,
	nom varchar(150) not null,
	prenom varchar(100) not null,
	email varchar(100) not null,
	role character(1) not null,
	date_naissance timestamp null,
	date_inscription timestamp default CURRENT_TIMESTAMP not null,
	adresse varchar(200) null,
	commune varchar(100) null,
	code_postal integer null,
	num_tel varchar(50) null,
	estHomme boolean null,
	nombre_annee_reussie integer null,
	num_compte character(19) null,
	titulaire_compte varchar(150) null,
	nom_banque varchar(150) null,
	code_bic varchar(11) null,
	nationalite integer null references SMMDB.pays(id_pays),
	version integer null default 0
);

create table SMMDB.partenaires(
	id_partenaire serial primary key,
	nom_legal varchar(100) not null,
	nom_affaire varchar(100) not null,
	nom_complet varchar(100) not null,
	departement varchar(150) null,
	type_organisation character(3) null,
	nombre_employe integer null,
	adresse varchar(100) not null,
	pays integer not null references SMMDB.pays(id_pays),
	region varchar(100) null,
	code_postal varchar(10) not null,
	ville varchar(100) not null,
	email varchar(100) not null,
	site_web varchar(100) null,
	num_tel varchar(50) not null,
	type_mobilite character(3) not null,
	version integer null default 0
);

create table SMMDB.documents_depart(
	id_documents_depart serial primary key,
	preuve_test_linguistique boolean default false not null,
	contrat_bourse boolean default false not null,
	convention_stage_etude boolean default false not null,
	charte_etudiant boolean default false not null,
	document_engagement boolean default false not null,
	envoie_document boolean default false not null,
	paiment_effectue boolean default false not null,
	version integer null default 0
);

create table SMMDB.documents_retour(
	id_documents_retour serial primary key not null,
	attestation_sejour boolean default false not null,
	stage_releve_note boolean default false not null,
	rapport_final boolean default false not null,
	preuve_passage_test boolean default false not null,
	envoie_document boolean default false not null,	
	paiement_effectue boolean default false not null,
	version integer null default 0
);

create table SMMDB.mobilites(
	id_mobilite serial primary key,
	programme varchar(8) not null,
	-- decimal(m,a) signifie qu'il peut y avoir max m chiffres au total et a chiffres apres la virgule
	-- ici le chiffre max est 99999.99
	financement decimal(7,2) null,
	periode character(2) null,
	partenaire integer null references SMMDB.partenaires(id_partenaire),
	etudiant integer not null references SMMDB.utilisateurs(id_utilisateur),
	documents_depart integer null references SMMDB.documents_depart(id_documents_depart),
	documents_retour integer null references SMMDB.documents_retour(id_documents_retour),
	niveau_preference integer not null,
	etat_mobilite varchar(15) not null,
	mobilite_annule boolean default false not null,
	pays integer null references SMMDB.pays(id_pays),
	date_introduction timestamp default CURRENT_TIMESTAMP not null,
	type_mobilite character(3) not null,
	version integer null default 0
	);
	
create table SMMDB.messages_annulation(
	id_message serial primary key,
	etudiant integer not null references SMMDB.utilisateurs(id_utilisateur),
	mobilite integer not null references SMMDB.mobilites(id_mobilite),
	est_etudiant_averti boolean default false,
	message varchar(2048) not null
);

-- Rajouter les programme
insert into SMMDB.pays(nom, code_pays, programme) values
    ('Afghanistan','AFG','fame'),
    ('Afrique du Sud','ZAF','fame'),
    ('Albanie','ALB','fame'),
    ('Algerie','DZA','fame'),
    ('Allemagne','DEU','erasmus+'),
    ('Andorre','AND','fame'),
    ('Angola','AGO','fame'),
    ('Anguilla','AIA','fame'),
    ('Antarctique','ATA','fame'),
    ('Antigua et Barbuda','ATG','fame'),
    ('Antilles Néerlandaises','ANT','erasmus+'),
    ('Arabie Saoudite','SAU','fame'),
    ('Argentine','ARG','fame'),
    ('Arménie','ARM','fame'),
    ('Aruba','ABW','fame'),
    ('Australie','AUS','fame'),
    ('Autriche','AUT','erasmus+'),
    ('Azerbaidjan','AZE','fame'),
    ('Bahamas','BHS','fame'),
    ('Bahrein','BHR','fame'),
    ('Bangladesh','BGD','fame'),
    ('Barbade','BRB','fame'),
    ('Bélarus (Biélorussie)','BLR','fame'),
    ('Belgique','BEL','erabel'),
    ('Belize','BLZ','fame'),
    ('Bénin','BEN','fame'),
    ('Bermudes','BMU','fame'),
    ('Bhoutan','BTN','fame'),
    ('Bolivie','BOL','fame'),
    ('Bosnie et Herzégovine','BIH','fame'),
    ('Botswana','BWA','fame'),
    ('Bouvet Island','BVT','fame'),
    ('Brésil','BRA','fame'),
    ('Brunei','BRN','fame'),
    ('Bulgarie','BGR','erasmus+'),
    ('Burkina Faso','BFA','fame'),
    ('Burundi','BDI','fame'),
    ('Cambodge','KHM','fame'),
    ('Cameroun','CMR','fame'),
    ('Canada','CAN','fame'),
    ('Cap Vert','CPV','fame'),
    ('Chili','CHL','fame'),
    ('Chine','CHN','fame'),
    ('Chypre','CYP','erasmus+'),
    ('Cité du Vatican (Holy See)','VAT','fame'),
    ('Colombie','COL','fame'),
    ('Comores','COM','fame'),
    ('Congo, République','COG','fame'),
    ('Congo, République Démocratique du','COD','fame'),
    ('Corée du Nord','PRK','fame'),
    ('Corée du Sud','KOR','fame'),
    ('Costa Rica','CRI','fame'),
    ('Côte d''Ivoire','CIV','fame'),
    ('Croatie','HRV','erasmus+'),
    ('Cuba','CUB','fame'),
    ('Curacao','CUW','fame'),
    ('Danemark','DNK','erasmus+'),
    ('Djibouti','DJI','fame'),
    ('Dominique','DMA','fame'),
    ('Egypte','EGY','fame'),
    ('Emirats Arabes Unis','ARE','fame'),
    ('Equateur','ECU','fame'),
    ('Erythrée','ERI','fame'),
    ('Espagne','ESP','erasmus+'),
    ('Estonie','EST','erasmus+'),
    ('Etats-Unis','USA','fame'),
    ('Ethiopie','ETH','fame'),
    ('Fidji','FJI','fame'),
    ('Finlande','FIN','erasmus+'),
    ('France','FRA','erasmus+'),
    ('France, Métropolitaine','FXX','erasmus+'),
    ('Gabon','GAB','fame'),
    ('Gambie','GMB','fame'),
    ('Gaza','PSE','fame'),
    ('Géorgie','GEO','fame'),
    ('Géorgie du Sud et les îles Sandwich du Sud','SGS','erasmus+'),
    ('Ghana','GHA','fame'),
    ('Gibraltar','GIB','erasmus+'),
    ('Grèce','GRC','erasmus+'),
    ('Grenade','GRD','fame'),
    ('Greoenland','GRL','erasmus+'),
    ('Guadeloupe','GLP','erasmus+'),
    ('Guam','GUM','fame'),
    ('Guatemala','GTM','fame'),
    ('Guinée','GIN','fame'),
    ('Guinée Bissau','GNB','fame'),
    ('Guinée Equatoriale','GNQ','fame'),
    ('Guyane','GUY','fame'),
    ('Guyane Française','GUF','erasmus+'),
    ('Haïti','HTI','fame'),
    ('Honduras','HND','fame'),
    ('Hong Kong','HKG','fame'),
    ('Hongrie','HUN','erasmus+'),
    ('Ile de Man','IMN','erasmus+'),
    ('Iles Caïman','CYM','erasmus+'),
    ('Iles Christmas','CXR','fame'),
    ('Iles Cocos','CCK','fame'),
    ('Iles Cook','COK','fame'),
    ('Iles Féroé','FRO','fame'),
    ('Iles Guernesey','GGY','fame'),
    ('Iles Heardet McDonald','HMD','fame'),
    ('Iles Malouines','FLK','erasmus+'),
    ('Iles Mariannes du Nord','MNP','fame'),
    ('Iles Marshall','MHL','fame'),
    ('Iles Maurice','MUS','fame'),
    ('Iles mineures éloignées des Etats-Unis','UMI','fame'),
    ('Iles Norfolk','NFK','fame'),
    ('Iles Salomon','SLB','fame'),
    ('Iles Turques et Caïque','TCA','erasmus+'),
    ('Iles Vierges des Etats-Unis','VIR','fame'),
    ('Iles Vierges du Royaume-Uni','VGB','fame'),
    ('Inde','IND','fame'),
    ('Indonésie','IDN','fame'),
    ('Iran','IRN','fame'),
    ('Iraq','IRQ','fame'),
    ('Irlande','IRL','erasmus+'),
    ('Islande','ISL','fame'),
    ('Israël','ISR','fame'),
    ('Italie','ITA','erasmus+'),
    ('Jamaique','JAM','fame'),
    ('Japon','JPN','fame'),
    ('Jersey','JEY','fame'),
    ('Jordanie','JOR','fame'),
    ('Kazakhstan','KAZ','fame'),
    ('Kenya','KEN','fame'),
    ('Kirghizistan','KGZ','fame'),
    ('Kiribati','KIR','fame'),
    ('Kosovo','XKO','fame'),
    ('Koweit','KWT','fame'),
    ('Laos','LAO','fame'),
    ('Lesotho','LSO','fame'),
    ('Lettonie','LVA','erasmus+'),
    ('Liban','LBN','fame'),
    ('Libéria','LBR','fame'),
    ('Libye','LBY','fame'),
    ('Liechtenstein','LIE','fame'),
    ('Lituanie','LTU','erasmus+'),
    ('Luxembourg','LUX','erasmus+'),
    ('Macao','MAC','fame'),
    ('Macédoine','MKD','fame'),
    ('Madagascar','MDG','fame'),
    ('Malaisie','MYS','fame'),
    ('Malawi','MWI','fame'),
    ('Maldives','MDV','fame'),
    ('Mali','MLI','fame'),
    ('Malte','MLT','erasmus+'),
    ('Maroc','MAR','fame'),
    ('Martinique','MTQ','erasmus+'),
    ('Mauritanie','MRT','fame'),
    ('Mayotte','MYT','erasmus+'),
    ('Mexique','MEX','fame'),
    ('Micronésie','FSM','fame'),
    ('Moldavie','MDA','fame'),
    ('Monaco','MCO','fame'),
    ('Mongolie','MNG','fame'),
    ('Monténégro','MNE','fame'),
    ('Montserrat','MSR','erasmus+'),
    ('Mozambique','MOZ','fame'),
    ('Myanmar (Birmanie)','MMR','fame'),
    ('Namibie','NAM','fame'),
    ('Nauru','NRU','fame'),
    ('Népal','NPL','fame'),
    ('Nicaragua','NIC','fame'),
    ('Niger','NER','fame'),
    ('Nigeria','NGA','fame'),
    ('Niue','NIU','fame'),
    ('Norvège','NOR','fame'),
    ('Nouvelle Calédonie','NCL','erasmus+'),
    ('Nouvelle Zélande','NZL','fame'),
    ('Oman','OMN','fame'),
    ('Ouganda','UGA','fame'),
    ('Ouzbékistan','UZB','fame'),
    ('Pakistan','PAK','fame'),
    ('Palau','PLW','fame'),
    ('Panama','PAN','fame'),
    ('Papouasie Nouvelle Guinée','PNG','fame'),
    ('Paraguay','PRY','fame'),
    ('Pays-Bas','NLD','erasmus+'),
    ('Pérou','PER','fame'),
    ('Philippines','PHL','fame'),
    ('Pitcairn','PCN','erasmus+'),
    ('Pologne','POL','erasmus+'),
    ('Polynésie Française','PYF','erasmus+'),
    ('Porto Rico','PRI','fame'),
    ('Portugal','PRT','erasmus+'),
    ('Qatar','QAT','fame'),
    ('République Centraficaine','CAF','fame'),
    ('République Dominicaine','DOM','fame'),
    ('République Tchèque','CZE','erasmus+'),
    ('Réunion','REU','erasmus+'),
    ('Roumanie','ROU','erasmus+'),
    ('Royaume Uni','GBR','erasmus+'),
    ('Russie','RUS','fame'),
    ('Rwanda','RWA','fame'),
    ('Sahara Occidental','ESH','fame'),
    ('Saint Barthelemy','BLM','erasmus+'),
    ('Saint Hélène','SHN','erasmus+'),
    ('Saint Kitts et Nevis','KNA','fame'),
    ('Saint Martin','MAF','erasmus+'),
    ('Saint Martin','SXM','erasmus+'),
    ('Saint Pierre et Miquelon','SPM','erasmus+'),
    ('Saint Vincent et les Grenadines','VCT','fame'),
    ('Sainte Lucie','LCA','fame'),
    ('Salvador','SLV','fame'),
    ('Samoa Americaines','ASM','fame'),
    ('Samoa Occidentales','WSM','fame'),
    ('San Marin','SMR','fame'),
    ('Sao Tomé et Principe','STP','fame'),
    ('Sénégal','SEN','fame'),
    ('Serbie','SRB','fame'),
    ('Seychelles','SYC','fame'),
    ('Sierra Léone','SLE','fame'),
    ('Singapour','SGP','fame'),
    ('Slovaquie','SVK','erasmus+'),
    ('Slovénie','SVN','erasmus+'),
    ('Somalie','SOM','fame'),
    ('Soudan','SDN','fame'),
    ('Sri Lanka','LKA','fame'),
    ('Sud Soudan','SSD','fame'),
    ('Suède','SWE','erasmus+'),
    ('Suisse','CHE','autre'),
    ('Surinam','SUR','fame'),
    ('Svalbard et Jan Mayen','SJM','fame'),
    ('Swaziland','SWZ','fame'),
    ('Syrie','SYR','fame'),
    ('Tadjikistan','TJK','fame'),
    ('Taiwan','TWN','fame'),
    ('Tanzanie','TZA','fame'),
    ('Tchad','TCD','fame'),
    ('Terres Australes et Antarctique Françaises','ATF','erasmus+'),
    ('Térritoire Britannique de l''Océan Indien','IOT','erasmus+'),
    ('Territoires Palestiniens occupés (Gaza)','PSE','fame'),
    ('Thaïlande','THA','fame'),
    ('Timor-Leste','TLS','fame'),
    ('Togo','TGO','fame'),
    ('Tokelau','TKL','fame'),
    ('Tonga','TON','fame'),
    ('Turkmenistan','TKM','fame'),
    ('Turquie','TUR','fame'),
    ('Trinité et Tobago','TTO','fame'),
    ('Tunisie','TUN','fame'),
    ('Tuvalu','TUV','fame'),
    ('Ukraine','UKR','fame'),
    ('Uruguay','URY','fame'),
    ('Vanuatu','VUT','fame'),
    ('Venezuela','VEN','fame'),
    ('Vietnam','VNM','fame'),
    ('Wallis et Futuna','WLF','fame'),
    ('Yemen','YEM','fame'),
    ('Zambie','ZMB','fame'),
    ('Zimbabwe','ZWE','fame');

-- scénario finale
-- mdp : Admin;10.
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('bri', '$2a$10$F74jXLAaI.Q9W5Mmp0R4he4QtcaRTtAL0ltxnu22rQOAmtYD2fAHW', 'Lehmann', 'Brigitte', 'brigitte.lehmann@vinci.be', 'A', default, 3);
-- mdp : Rouge;7?
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('lau', '$2a$10$hlomkL5SKVew172dEZnvK.HDOxfW0FBxG3OIChmKjwEWiY5t899pG', 'Leleux', 'Laurent', 'laurent.leleux@vinci.be', 'P', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('chri', '$2a$10$hlomkL5SKVew172dEZnvK.HDOxfW0FBxG3OIChmKjwEWiY5t899pG', 'Damas', 'Christophe', 'christophe.damas@vinci.be', 'E', default, 3);
-- mdp : mdpuser
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('achil', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Ile', 'Achille', 'ach.ile@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('bazz', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Ile', 'Basile', 'bas.ile@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('caro', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Line', 'Caroline', 'car.line@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('laurence', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Lasage', 'Laurence', 'lau.sage@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('pir', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Kiroule', 'Pierre', 'pir.pas@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('mousse', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Namassepas', 'Mousse', 'nam.mas@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('the', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Tatilotetatou', 'Tonthe', 'ton.the@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('albert', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Frair', 'Albert', 'al.fr@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('alph', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Albert', 'Alpha', 'al.pha@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('louis', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Albert', 'Louis', 'lo.ouis@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite)
	values('theo', '$2a$10$GB/GtOOYy04t/Qrnrsipz.eluiWTrvb/7ImJ70PY4yxprSFEPP7BO', 'Legrand', 'Théophile', 'theo.phile@student.vinci.be', 'E', default, 3);


--insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, mail, role, date_naissance) values('DTanner', '1234', 'Tanner', 'Danny', 'Danny.wanadou@gmail.cl', 'P', TO_TIMESTAMP('1954-07-02', 'YYYY-MM-DD'));
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite) values('RDupont', '$2a$10$/USAP02Ftd3ooXao2mz9XOuvtSlWQTkSAYPxUNBxFLbFboFF9ZJiy', 'Dupont', 'Robin', 'robin.dupont@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite) values('NStiers', '$2a$10$/USAP02Ftd3ooXao2mz9XOuvtSlWQTkSAYPxUNBxFLbFboFF9ZJiy', 'Stiers', 'Nathan', 'nathan.stiers@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite) values('PMichiels', '$2a$10$/USAP02Ftd3ooXao2mz9XOuvtSlWQTkSAYPxUNBxFLbFboFF9ZJiy', 'Michiels', 'Pierre', 'pierre.michiels@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite) values('JKöhler', '$2a$10$/USAP02Ftd3ooXao2mz9XOuvtSlWQTkSAYPxUNBxFLbFboFF9ZJiy', 'Köhler', 'Julien', 'julien.kohler@student.vinci.be', 'E', default, 3);
insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription, nationalite) values('LLeleux', '$2a$10$/USAP02Ftd3ooXao2mz9XOuvtSlWQTkSAYPxUNBxFLbFboFF9ZJiy', 'Leleux', 'Laurent', 'laurent.leleux@student.vinci.be', 'P', default, 4);








insert into SMMDB.partenaires(nom_legal, nom_affaire, nom_complet, departement, adresse, pays, code_postal, ville, email, site_web, num_tel, type_mobilite)
	values(
	'UC Leuven-Limburg (Leuven campus)',
	'UC Leuven-Limburg (Leuven campus)',
	'UC Leuven-Limburg (Leuven campus)',
	'Computer Sciences',
	'Abdij van Park 9',
	24,
	'3001',
	'Heverlee',
	'international@ucll.be',
	'https://www.ucll.be/',
	'+32 (0)16 375 735',
	'SMS'
	);

insert into SMMDB.partenaires(nom_legal, nom_affaire, nom_complet, departement, adresse, pays, code_postal, ville, email, site_web, num_tel, type_mobilite)
	values(
	'Technological University Dublin',
	'Technological University Dublin',
	'Technological University Dublin',
	'Computing',
	'40- 45 Mount Joy Square',
	116,
	'Dublin 1',
	'Dublin',
	'erasmus@dit.ie',
	'www.dit.ie/computing/',
	'+35314023404',
	'SMS'
	);

insert into SMMDB.partenaires(nom_legal, nom_affaire, nom_complet, departement, adresse, pays, code_postal, ville, email, site_web, num_tel, type_mobilite)
	values(
	'Université du Luxembourg',
	'Université du Luxembourg',
	'Université du Luxembourg',
	'Computing',
	'7, avenue des Hauts-Fourneaux',
	138,
	'L-4362',
	'Esch-sur-Alzette',
	'erasmus@uni.lu',
	'https://wwwfr.uni.lu/',
	'(+352) 46 66 44 4000',
	'SMS'
	);

insert into SMMDB.partenaires(nom_legal, nom_affaire, nom_complet, departement, adresse, pays, code_postal, ville, email, site_web, num_tel, type_mobilite)
	values(
	'Wölfel Engineering GmbH',
	'Wölfel Engineering GmbH',
	'Wölfel Engineering GmbH',
	'Data processing and analytics',
	'Max-Planck-Str. 15',
	5,
	'97204',
	'Höchberg',
	'tr@woelfel.de',
	'https://www.woelfel.de/home.html',
	'+49 (931) 49708-168',
	'SMP'
	);

insert into SMMDB.partenaires(nom_legal, nom_affaire, nom_complet, departement, adresse, pays, code_postal, ville, email, num_tel, type_mobilite)
	values(
	'HES-SO Haute école spécialisée de Suisse occidentale',
	'HES-SO Haute école spécialisée de Suisse occidentale (Haute école de gestion de Genève (HEG GE))',
	'HES-SO Haute école spécialisée de Suisse occidentale (Haute école de gestion de Genève (HEG GE))',
	'Business Information Systems',
	'17, Rue de la Tambourine',
	221,
	'1227',
	'Carouge – Genève',
	'international@hes-so.ch',
	'0041 22 388 17 00',
	'SMS'
	);

insert into SMMDB.partenaires(nom_legal, nom_affaire, nom_complet, departement, adresse, pays, region, code_postal, ville, email, site_web, num_tel, type_mobilite)
	values(
	'cégep Edouard Montpetit',
	'cégep Edouard Montpetit',
	'cégep Edouard Montpetit',
	'Techniques de l’informatique',
	'945 chemin de Chambly',
	40,
	'Québec',
	'J4H 3M6',
	'Longueuil',
	'mobilites@cegepmontpetit.ca',
	'http://www.cegepmontpetit.ca/',
	'450 679-2631',
	'SMS'
	);

insert into SMMDB.partenaires(nom_legal, nom_affaire, nom_complet, departement, adresse, pays, region, code_postal, ville, email, site_web, num_tel, type_mobilite)
	values(
	'Collège de Maisonneuve',
	'Collège de Maisonneuve',
	'Collège de Maisonneuve',
	'Techniques de l’informatique',
	'3 800, rue Sherbrooke Est',
	40,
	'Québec',
	'H1X 2A2',
	'Montréal',
	'international@cmaisonneuve.qc.ca',
	'https://www.cmaisonneuve.qc.ca/',
	'514 254-7131',
	'SMS'
	);

insert into SMMDB.partenaires(nom_legal, nom_affaire, nom_complet, departement, adresse, pays, region, code_postal, ville, email, site_web, num_tel, type_mobilite)
	values(
	'Cégep de Chicoutimi',
	'Cégep de Chicoutimi',
	'Cégep de Chicoutimi',
	'Techniques de l’informatique',
	'534 Jacques-Cartier Est',
	40,
	'Québec',
	'G7H 1Z6',
	'Chicoutimi',
	'mhforest@cchic.ca',
	'https://cchic.ca',
	'418 549.9520  | Poste 1144',
	'SMS'
	);




-- mobilités pour la démo finale
INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('erasmus+', 0.0, 'Q1', 3, 6, 2, 'CREE', 138, '2017-10-10 00:00:01', 'SMS');


INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('fame', 0.0, 'Q2', NULL, 4, 1, 'CREE', NULL, '2017-12-10 00:00:02', 'SMP');

INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('fame', 0.0, 'Q1', NULL, 14, 1, 'CREE', 169, '2018-12-10 00:00:03', 'SMS');

INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('autre', 0.0, 'Q1', 5, 9, 1, 'CREE', 221, '2018-12-10 00:00:04', 'SMS');

INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('erabel', 0.0, 'Q2', 1, 10, 1, 'CREE', 24, '2018-12-10 00:00:05', 'SMS');


INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('erasmus+', 0.0, 'Q2', NULL, 10, 2, 'CREE', NULL, '2018-12-10 00:00:05', 'SMP');


INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('fame', 0.0, 'Q1', 7, 13, 3, 'CREE', 40, '2018-12-10 00:00:06', 'SMS');


INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('erasmus+', 0.0, 'Q1', 2, 6, 1, 'CREE', 116, '2018-12-10 00:00:07', 'SMS');

INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('erasmus+', 0.0, 'Q1', 2, 14, 1, 'CREE', 116, '2018-12-10 00:00:08', 'SMS');


INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('erasmus+', 0.0, 'Q1', 3, 13, 1, 'CREE', 138, '2018-12-10 00:00:09', 'SMS');

INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('fame', 0.0, 'Q2', 6, 13, 2, 'CREE', 40, '2018-12-10 00:00:10', 'SMS');

INSERT INTO SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, 
niveau_preference, etat_mobilite, pays, date_introduction, type_mobilite) VALUES
('fame', 0.0, 'Q1', NULL, 6, 3, 'CREE', NULL, '2017-10-10 00:00:01', 'SMP');

-- mobilite Caroline id = 8 Achille id = 6
--insert into SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, documents_depart, documents_retour, niveau_preference, etat_mobilite, mobilite_annule, type_mobilite, pays) VALUES
--	('erasmus+', 0.0, 'Q1', 2, 8, NULL, NULL, 1, 'CREE', 'f', 'SMS', 116);
--insert into SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, documents_depart, documents_retour, niveau_preference, etat_mobilite, mobilite_annule, type_mobilite, pays) VALUES
--	('erasmus+', 0.0, 'Q1', 3, 8, NULL, NULL, 2, 'CREE', 'f', 'SMS', 138);
--insert into SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, documents_depart, documents_retour, niveau_preference, etat_mobilite, mobilite_annule, type_mobilite, pays) VALUES
--	('fame', 0.0, 'Q1', NULL, 8, NULL, NULL, 3, 'CREE', 'f', 'SMP', NULL);
--insert into SMMDB.mobilites (programme, financement, periode, partenaire, etudiant, documents_depart, documents_retour, niveau_preference, etat_mobilite, mobilite_annule, type_mobilite, pays) VALUES
--	('fame', 0.0, 'Q2', NULL, 6, NULL, NULL, 1, 'CREE', 'f', 'SMP', NULL);


select * from SMMDB.utilisateurs
