function modifierCompteClickHandler(donnees, etudiant){ 
    var json = form2Json($('#formModifInfo'));
    var champsNonModifies = JSON.parse(json);
    var pasDEspaces = true;
    var chiffreCorrectNbAnnee = true;
    var chiffreCorrectCodePostal = true;
    
    for(var key in champsNonModifies){
        if(key == "nbAnnee" && Math.sign(champsNonModifies[key]) == -1){
            chiffreCorrectNbAnnee = false;
        }
        else if(key == "codePostal" && Math.sign(champsNonModifies[key]) == -1){
            chiffreCorrectCodePostal = false;
        }
        if(key == "mdp" || key == "mdpBis"){
            if(champsNonModifies[key] == ""){
                delete champsNonModifies["mdp"];
                delete champsNonModifies["mdpBis"];
            }
        }
        else if(donnees[key] == 0 && champsNonModifies[key] == ""){
            delete champsNonModifies[key];
        }
        else if(champsNonModifies[key] == ""){
           delete champsNonModifies[key];
        }
        else if(donnees[key] == champsNonModifies[key]){
           delete champsNonModifies[key];
        }
        if(/^\s.*/.test(champsNonModifies[key])){
            $('[name='+key+']').focus();
            erreurChamps("Erreur, aucun champ ne peut commencer par un espace","pageProfilUtilisateur");  
            pasDEspaces = false;
            return;
        }
        
    }
    for(var key in champsNonModifies){
        donnees[key] = champsNonModifies[key];
    }
    var mdp = champsNonModifies["mdp"];
    var mdpBis = champsNonModifies["mdpBis"];
    json = JSON.stringify(champsNonModifies);

    if(chiffreCorrectNbAnnee == false){
        erreurChamps("Nombre d'années réussies ne contient pas un chiffre valide","pageProfilUtilisateur");
        return;
    }
    else if(chiffreCorrectCodePostal == false){
        erreurChamps("Code postal (CP) ne contient pas un chiffre valide","pageProfilUtilisateur"); 
        return;
    }
    else if(json.length != 2 && pasDEspaces == true){
        if(mdp !== mdpBis){
            erreurChamps("Erreur, les 2 mots de passe ne sont pas identiques","pageProfilUtilisateur");
            return;
        }
        else{
            json = json.replace('selectNationalite','nationalite');
            if(etudiant == undefined){
                $.ajax({
                    url: '/',
                    async: false,
                    type: 'POST',
                    data: { action: "updateAllInfoUser", jsonUser: json},
                    success: function(reponse){
                        alert("Vos informations ont été modifiées");
                        if(user.getRole() == 'E'){
                            setDernierePage("pageDAccueil");
                            displayPage($('#pageDAccueil'));
                        }
                        else{
                            resetChargementListeEtudiants();
                            chargementListeEtudiants();
                            setDernierePage("pageListeEtudiants");
                            displayPage($('#pageListeEtudiants'));
                        }
                    },
                    error: function (e) {
                        if(e.responseText == "adresseMailInvalide"){
                            erreurChamps("Adresse email invalide","pageProfilUtilisateur");
                        }
                        else if(e.responseText == "numBanqueInvalide"){
                            erreurChamps("Numéro de compte bancaire invalide","pageProfilUtilisateur");
                        }
                    }
                });
            }
            else{
                $.ajax({
                    url: '/',
                    async: false,
                    type: 'POST',
                    data: { action: "updateAllInfoUser", jsonUser: json, modifParProf: "oui", pseudoEtudiant: etudiant},
                    success: function(reponse){
                        alert("Les informations ont été modifiés");
                        resetChargementListeEtudiants();
                        chargementListeEtudiants();
                        setDernierePage("pageListeEtudiants");
                        displayPage($('#pageListeEtudiants'));
                    },
                    error: function(e){
                        if(e.responseText == "adresseMailInvalide"){
                            erreurChamps("Adresse email invalide","pageProfilUtilisateur");
                        }
                        else if(e.responseText == "numBanqueInvalide"){
                            erreurChamps("Numéro de compte bancaire invalide","pageProfilUtilisateur");
                        }
                    }
                });
            }
        }
    }
    else{
         erreurChamps("Aucun changement n'a été effectué","pageProfilUtilisateur");
         return;
    }
}

function resetChargementProfilUtilisateur(){
	$('[name=notification').empty();
    $('#modifierCompte').off();
}

function chargerProfilUtilisateur(etudiant){
    var userInfoToEdit;   
    $('[name=sectionMdp]').attr('hidden', 'true');
    if(etudiant == undefined){
        $('[name=notification').empty();
        $('[name=pseudoFormGroup]').remove();
        $('[name=afficherSectionMdp]').removeAttr('hidden');
        //$('[name=sectionMdp]').removeAttr('hidden');
        
        $('[name=afficherSectionMdpSpan]').mouseover(function(){
            $(this).css('textDecoration','underline');
            $(this).css('fontWeight','bold');
        });
        $('[name=afficherSectionMdpSpan]').mouseout(function(){
            $(this).css('textDecoration','none');
            $(this).css('fontWeight','normal');
        });

        $('[name=afficherSectionMdpSpan]').on('click', function(){
            $('[name=afficherSectionMdp]').attr('hidden','true');
            $('[name=sectionMdp]').removeAttr('hidden');
        });
        // le mdp n'est pas repris du backend
        var infoChampsUtilisateur = 
            '{"mail":"","titulaireCompte":"","nomBanque":"","numBanque":"",'+
            '"codeBic":"","dateNaissance":"","selectSexe":"","numTel":"",'+
            '"adresse":"","codePostal":"","nbAnnee":"","selectNationalite":"",'+
            '"nom":"","prenom":""}';
        infoChampsUtilisateur = JSON.parse(infoChampsUtilisateur);
        $.ajax({
            url:'/',
            type:'POST',
            async:false,
            data:{action:'allInfoUser'},
            success(resp){  
                userInfoToEdit = JSON.parse(resp.replace("nationalite","selectNationalite"));
                JSONToForm(userInfoToEdit, $('#formModifInfo'));
                if(userInfoToEdit["selectSexe"] == "H" || userInfoToEdit["selectSexe"] == "F"){
                   $('[name=optionSexeVide]').remove();
                }

                for(var key in userInfoToEdit){
                    if(infoChampsUtilisateur[key] != undefined){
                       infoChampsUtilisateur[key] = userInfoToEdit[key];
                    }
                    if(userInfoToEdit[key] == null){
                        $('[name='+key+']').attr('placeholder','non-précise');
                    }
                    else if(userInfoToEdit[key] == 0){
                        $('[name='+key+']').val("");
                        $('[name='+key+']').attr('placeholder','non-précise');
                    }
                }
            },
            error(req){
                //console.log(req);
            }
        });
        
        $.ajax({
            url:'/',
            type:'POST',
            async:false,
            data:{action:'getAllPays'},
            success(resp){
                var map = JSON.parse(resp);
                var select = $('#formModifInfo').find($('[name=selectNationalite]'));
                $(select).empty();
                for(let i =0;i<map.length;i++){
                    if(userInfoToEdit != undefined){
                        if(map[i]["idPays"] == userInfoToEdit["selectNationalite"]){
                            $(select).append('<option value='+map[i]["idPays"]+' selected> '+ map[i]["codePays"]+" ("+map[i]["nom"]+")");
                        }
                        else{
                            $(select).append('<option value='+map[i]["idPays"]+'> '+ map[i]["codePays"]+" ("+map[i]["nom"]+")");     
                        }
                    }
                }
            },
            error(req){
                console.log(req);
            }
        });
        
        $('#modifierCompte').on('click', function(){
            modifierCompteClickHandler(infoChampsUtilisateur, undefined);
        });
    }
    else{
        var infoChampsEtudiant = 
            '{"mail":"","titulaireCompte":"","nomBanque":"","numBanque":"",'+
            '"codeBic":"","dateNaissance":"","selectSexe":"","numTel":"",'+
            '"adresse":"","codePostal":"","nbAnnee":"","selectNationalite":"",'+
            '"nom":"","prenom":"","pseudo":""}';
        infoChampsEtudiant = JSON.parse(infoChampsEtudiant);
        // un professeur ne peut pas modifier le mot de passe d'un étudiant
        //$('[name=afficherSectionMdp]').css('display','none');
        $('[name=afficherSectionMdp]').attr('hidden','true');
        $('[name=sectionMdp]').attr('hidden','true');
        $('[name=pseudoFormGroup]').remove();
        $('[name=sectionMdp]')
            .after('<div class="form-group row" style="margin-left:0%" name="pseudoFormGroup">'+
                                           '<div class="col-sm-12" style="padding-left:0%">'+
                                                '<div class="input-group mb-2">'+
                                                    '<div class="input-group-prepend">'+
                                                        '<label class="input-group-text" for="inputGroupSelect01">Pseudo:</label>'+
                                                    '</div>'+
                                                    '<input type="text" class="form-control form-control-user" name="pseudo">'+
                                                '</div>'+
                                            '</div>'+
                                        '</div>');
        $.ajax({
            url:'/',
            type:'POST',
            async:false,
            data:{action:'allInfoUser', pseudoEtudiant: etudiant},
            success(resp){  
                userInfoToEdit = JSON.parse(resp.replace("nationalite","selectNationalite"));
                JSONToForm(userInfoToEdit, $('#formModifInfo'));
                if(userInfoToEdit["selectSexe"] == "H" || userInfoToEdit["selectSexe"] == "F"){
                   $('[name=optionSexeVide]').remove();
                }

                for(var key in userInfoToEdit){
                    if(infoChampsEtudiant[key] != undefined){
                       infoChampsEtudiant[key] = userInfoToEdit[key];
                    }
                    if(userInfoToEdit[key] == null){
                        $('[name='+key+']').attr('placeholder','non-précise');
                    }
                    else if(userInfoToEdit[key] == 0){
                        $('[name='+key+']').val("");
                        $('[name='+key+']').attr('placeholder','non-précise');
                    }
                }
            },
            error(req){
                //console.log(req);
            }
        });
        
        $.ajax({
            url:'/',
            type:'POST',
            async:false,
            data:{action:'getAllPays'},
            success(resp){
                var map = JSON.parse(resp);
                var select = $('#formModifInfo').find($('[name=selectNationalite]'));
                $(select).empty();
                for(let i =0;i<map.length;i++){
                    
                    if(userInfoToEdit != undefined){
                        if(map[i]["idPays"] == userInfoToEdit["selectNationalite"]){
                            $(select).append('<option value='+map[i]["idPays"]+' selected> '+ map[i]["codePays"]+" ("+map[i]["nom"]+")");
                        }
                        else{
                            $(select).append('<option value='+map[i]["idPays"]+'> '+ map[i]["codePays"]+" ("+map[i]["nom"]+")");     
                        }
                    }
                }
            },
            error(req){
                console.log(req);
            }
        });
        $('#modifierCompte').on('click', function(){
            modifierCompteClickHandler(infoChampsEtudiant, etudiant);
        });
    }
}

function chargerScriptProfil(){
    chargerProfilUtilisateur();

    $('[name=afficherSectionMdpSpan]').mouseover(function(){
        $(this).css('textDecoration','underline');
        $(this).css('fontWeight','bold');
    });
    $('[name=afficherSectionMdpSpan]').mouseout(function(){
        $(this).css('textDecoration','none');
        $(this).css('fontWeight','normal');
    });

    $('[name=afficherSectionMdpSpan]').on('click', function(){
        $('[name=afficherSectionMdp]').attr('hidden','true');
        $('[name=sectionMdp]').removeAttr('hidden');
    });
}

//------------------------------------------------------------------------------

chargerScriptProfil();