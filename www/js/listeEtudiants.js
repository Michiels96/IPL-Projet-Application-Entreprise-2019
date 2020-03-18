var sensTriChampsEtudiants;
var sensTriChampsMobilites;
    

function afficherMobilitesTries(reponse, pseudoEtu){
    if($('[name=infoMob'+pseudoEtu+']').length != 0){
        $('[name=infoMob'+pseudoEtu+']').remove();
    }
    var couleurInfoMob = opaciteDeCouleur('#ebebeb');
    var mobiTrHead1 = $('<tr name="infoMob'+pseudoEtu+'">');
    mobiTrHead1.css('background-color',couleurInfoMob);
    mobiTrHead1.append($('<th colspan=6 style="font-size:110%">Mobilités de '+pseudoEtu+'</th>'));
    mobiTrHead1.append($('<td colspan=2 ><button type="button" class="btn btn-primary" name="csv'+pseudoEtu+'">Télécharger fichier CSV des demandes de mobilités de '+pseudoEtu+'</button></td>'));
    $('[name=ligneTab'+pseudoEtu+']').after(mobiTrHead1);

    var mobiTrHead2 = $('<tr name="infoMob'+pseudoEtu+'">');
    mobiTrHead2.append($('<th>').text("N° d'ordre de candidature").append(' <i class="fa fa-sort" name="triMobNrCandidature" style="cursor:pointer">'));
    mobiTrHead2.append($('<th>').text("Nom/prénom de l'étudiant").append(' <i class="fa fa-sort" name="triMobNomPrenomEtudiant" style="cursor:pointer">'));
    mobiTrHead2.append($('<th>').text("Département").append(' <i class="fa fa-sort" name="triMobDepartement" style="cursor:pointer">'));
    mobiTrHead2.append($('<th>').text("N° d'ordre de préférence").append(' <i class="fa fa-sort" name="triMobPreference" style="cursor:pointer">'));
    mobiTrHead2.append($('<th>').text("Type de mobilité").append(' <i class="fa fa-sort" name="triMobTypeMobilite" style="cursor:pointer">'));
    mobiTrHead2.append($('<th>').text("Stage/académique").append(' <i class="fa fa-sort" name="triMobSMSSMP" style="cursor:pointer">'));
    mobiTrHead2.append($('<th>').text("Quadrimèstre pendant lequel aura lieu la mobilité").append(' <i class="fa fa-sort" name="triMobPeriode" style="cursor:pointer">'));
    mobiTrHead2.append($('<th>').text("Partenaire").append(' <i class="fa fa-sort" name="triMobPartenaire" style="cursor:pointer">'));
    mobiTrHead2.css('background-color',couleurInfoMob);
    mobiTrHead1.after(mobiTrHead2); 

    var mobilites = $(JSON.parse(reponse));
    mobilites.each(function(i,el){
        //console.log(el);
        var mobiTr = $('<tr name="infoMob'+pseudoEtu+'">');
        mobiTr.append($('<td>').text(el.nrCandidature));
        mobiTr.append($('<td>').text(el.nomPrenomEtudiant));
        if(el.departement == "null"){
            mobiTr.append($('<td>').text("non précisé"));    
        }
        else{
            mobiTr.append($('<td>').text(el.departement));    
        }
        mobiTr.append($('<td>').text(el.niveauPreference));
        mobiTr.append($('<td>').text(el.typeMobilite));
        mobiTr.append($('<td>').text(el.SMSSMP));
        mobiTr.append($('<td>').text(el.periode));
        if(el.partenaire == "null"){
            mobiTr.append($('<td>').text("non précisé"));    
        }
        else{
            mobiTr.append($('<td>').text(el.partenaire));    
        }
        mobiTr.css('background-color',couleurInfoMob);
        
        // gestion click -> vue documents mobilites
        mobiTr.on('click', function(){
            var docDepart = el.idDocDepart;
            var docRetour = el.idDocRetour;
            redirectionDocumentsProf(docDepart, docRetour);
        });
        mobiTrHead2.after(mobiTr);
    });
    
    

    $('[name^=triMob]').each(function(i,el){
        $(el).on('click', function(e){
            var critereDeTri = $(this).attr('name').replace('triMob','');
            critereDeTri = critereDeTri.charAt(0).toLowerCase()+critereDeTri.substr(1, critereDeTri.length);
            $.ajax({
                url: '/',
                type: 'POST',
                data: { action: "getMobiliteByPseudo", demandeProf: "oui", pseudoEtudiant: pseudoEtu,
                      champ: critereDeTri, sens: sensTriChampsMobilites[critereDeTri]},
                beforeSend: function(){
                    waitSpinner();
                },
                success: function(rep){
                    if(sensTriChampsMobilites[critereDeTri] == "décroissant"){
                        sensTriChampsMobilites[critereDeTri] = "croissant";
                    }else{
                        sensTriChampsMobilites[critereDeTri] = "décroissant";    
                    }
                    afficherMobilitesTries(rep, pseudoEtu);
                },
                complete: function(){
                    $('html').css('cursor','default');
                    $('#waitSpinner').remove();
                }
            });

        });
    });
    
    $('[name^=csv]').each(function(i, el){
        $(el).on('click',function(e){
            var nomFichier = "mobilites-"+$(this).attr('name').replace('csv','');
            var pseudo = $(this).attr('name').replace('csv','');
            
            // java va s'occuper de generer le fichier
            $.ajax({
                url: '/',
                type: 'POST',
                data: { action: "generateCsvFileForMobilites", pseudoEtudiant: pseudo, nomFichierCSV: nomFichier},
                beforeSend: function(){
                    waitSpinner();
                },
                success: function(rep){
                    if(rep == "ok"){
                        window.open("csv/"+nomFichier+".csv","_blank", null);     
                    }
                },
                complete: function(){
                    $('html').css('cursor','default');
                    $('#waitSpinner').remove();
                }
            });
        });
    });
}

function afficherMobilites(ligneEtudiant){
    var pseudoEtu = ligneEtudiant.attr("name").replace('ligneTab','');
    $.ajax({
        url: '/',
        type: 'POST',
        data: { action: "getMobiliteByPseudo", demandeProf: "oui", pseudoEtudiant: pseudoEtu},
        beforeSend: function(){
            waitSpinner();
        },
        success: function(reponse){
            if(reponse.length != 3){
                afficherMobilitesTries(reponse, pseudoEtu);
            }
        },
        error: function(e){
            // en cas d'erreur
            //console.log(e.message);
        },
        complete: function(){
            $('html').css('cursor','default');
            $('#waitSpinner').remove();
        }
    });
}

function afficherEtudiants(reponse){
    var obj = JSON.parse(reponse);
    if($('[name^=infoMob]').length != 0){
       $('[name^=infoMob]').remove();
    }
    for(var i =0; i < obj.length; i++){
        var tr = $('<tr>');
        tr.append($('<td>').text(obj[i].pseudo).css("font-weight","bold"));
        tr.attr('name','ligneTab'+obj[i].pseudo);
        tr.append($('<td>').text(obj[i].nom));
        tr.append($('<td>').text(obj[i].prenom));
        tr.append($('<td>').text(obj[i].email));
        tr.append($('<td>').text(obj[i].nbrMobilites));
        tr.append($('<td>').text(obj[i].nbrMobilitesAnnules));
        var infoButton = $('<td><button type="button" class="btn btn-info" name="info'+obj[i].pseudo+'">Info</button></td>');
        infoButton.on('click', 'button', function(e){
            var pseudo = $(this).attr("name").replace('info','');
            resetChargementProfilUtilisateur();
            chargerProfilUtilisateur(pseudo);  
            displayPage($('#pageProfilUtilisateur'));
        });
        
        tr.append(infoButton);
        var promoButton = $('<td><button type="button" class="btn btn-success" name="promo'+obj[i].pseudo+'">Promouvoir</button></td>');
        promoButton.on('click', 'button', function(e){
            var pseudo = $(this).attr("name").replace('promo','');
            $.ajax({
                url: '/',
                type: 'POST',
                async:false,
                data: { action: "promouvoir", pseudoToUp:pseudo},
                error: function(e){
                    console.log(e.message);
                }
            });
        });
        tr.append(promoButton);
        $('[name=tableListeEtudiants] tbody').append(tr);
    }
    
    var lignesEtudiants = $('[name^=ligneTab]');
    lignesEtudiants.each(function(i, el){
        var ligneEtudiant = $(el);
        ligneEtudiant.on('click', function(){
            afficherMobilites(ligneEtudiant);
        });
    });
}

function resetChargementListeEtudiants(){
    $('[name=tableListeEtudiants]').find("*").off();
    $('[name=tableListeEtudiants] tbody').empty();
}

function chargementListeEtudiants(){
    sensTriChampsEtudiants = '{"pseudo":"croissant","nom":"croissant","prenom":"croissant","email":"croissant","nbMob":"croissant","nbMobAnu":"croissant"}';
    sensTriChampsEtudiants = JSON.parse(sensTriChampsEtudiants);
    
    sensTriChampsMobilites = '{"nrCandidature":"croissant","nomPrenomEtudiant":"croissant","departement":"croissant","preference":"croissant",'+
        '"typeMobilite":"croissant","sMSSMP":"croissant","Periode":"croissant","partenaire":"croissant"}';
    sensTriChampsMobilites = JSON.parse(sensTriChampsMobilites);
    
    $.ajax({
        url: '/',
        type: 'POST',
        data: {action: "listerEtudiants"},
        beforeSend: function(){
            waitSpinner();
        },
        success: function(reponse){
            afficherEtudiants(reponse);
        },
        error: function(e){
            // en cas d'erreur
            //console.log(e.message);
        },
        complete: function(){
            $('html').css('cursor','default');
            $('#waitSpinner').remove();
        }
    });


    $('[name^=triListe]').each(function(i, el){
        $(el).on('click', function(e){
            var critereDeTri = $(this).attr('name').replace('triListe','');
            critereDeTri = critereDeTri.charAt(0).toLowerCase()+critereDeTri.substr(1, critereDeTri.length);
            $.ajax({
                url: '/',
                type: 'POST',
                data: { action: "listerEtudiants", champ: critereDeTri, sens: sensTriChampsEtudiants[critereDeTri]},
                beforeSend: function(){
                    waitSpinner();
                },
                success: function(reponse){
                    if(sensTriChampsEtudiants[critereDeTri] == "décroissant"){
                        sensTriChampsEtudiants[critereDeTri] = "croissant";
                    }else{
                        sensTriChampsEtudiants[critereDeTri] = "décroissant";    
                    }
                    $('[name=tableListeEtudiants] tbody').empty();
                    afficherEtudiants(reponse);
                },
                complete: function(){
                    $('html').css('cursor','default');
                    $('#waitSpinner').remove();
                }
            });
        });
    });
}

function chargerScriptListeEtudiants(){
    chargementListeEtudiants();
}

//------------------------------------------------------------------------------

chargerScriptListeEtudiants();